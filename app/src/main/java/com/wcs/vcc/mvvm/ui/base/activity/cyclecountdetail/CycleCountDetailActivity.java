package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.mvvm.data.DataManager;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseActivity;
import com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail.dialog.CycleCountDetailUpdateFragment;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ActivityCycleCountDetailBinding;

import java.util.List;

public class CycleCountDetailActivity extends BaseActivity<ActivityCycleCountDetailBinding, CycleCountDetailViewModel>
        implements CycleCountDetailAdapter.CycleCountDetailListener, CycleCountDetailUpdateFragment.OnSendData, View.OnClickListener {

    private static final String EXTRA_MASSCYCLE_COUNT = "EXTRA_MASSCYCLE_COUNT";
    private static final String EXTRA_MASSCYCLE_COUNT_MASSID = "EXTRA_MASSCYCLE_COUNT_MASSID";
    private static final String EXTRA_MASSCYCLE_COUNT_AUTO = "EXTRA_MASSCYCLE_COUNT_AUTO";
    private MassCycleCountRemote remoteMass;
    private String massID;
    private boolean bAuto;
    private CycleCountDetailAdapter adapter;
    private FragmentManager fm;
    private boolean iSuccess = false;

    @NonNull
    @Override
    protected CycleCountDetailViewModel createViewModel() {
        MassCycleCountDetailRepository repository = DataManager.getInstance().getMassCycleCountDetailRepository();
        CycleCountDetailViewModelFactory factory = new CycleCountDetailViewModelFactory(repository);
        return ViewModelProviders.of(this, factory).get(CycleCountDetailViewModel.class);
    }

    @NonNull
    @Override
    protected ActivityCycleCountDetailBinding createViewBinding(LayoutInflater inflater) {
        return ActivityCycleCountDetailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        adapter = new CycleCountDetailAdapter(this);
        binding.rcCycleCountDetail.setAdapter(adapter);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        binding.toolbar.setTitle(R.string.cycle_count_detail);
        showHideWhenScroll();
        remoteMass = getIntent().getParcelableExtra(EXTRA_MASSCYCLE_COUNT);
        if (remoteMass != null) {
            if (remoteMass.isStockMovementMassConfirm())
                binding.floatBtnDone.setVisibility(View.GONE);
            loadMassCycleCountDetail(remoteMass.getStockMovementMassID());
        }
        bAuto = getIntent().getBooleanExtra(EXTRA_MASSCYCLE_COUNT_AUTO, false);
        if (bAuto) {
            massID = getIntent().getStringExtra(EXTRA_MASSCYCLE_COUNT_MASSID);
            if (massID != null) {
                loadMassCycleCountDetail(massID);
            }
        }

        fm = getSupportFragmentManager();
        binding.floatBtnDone.setOnClickListener(this);
        binding.ivCameraScan.setOnClickListener(this);

        observeViewModel();
        etScanListener();
        swipeRefresh();
    }

    private void loadMassCycleCountDetail(String massID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StockMovementMassID", massID);
        viewModel.loadMassCycleCountDetail(jsonObject);
    }

    private void observeViewModel() {
        viewModel.getListMassCycleCountDetailRemote().observe(this, new Observer<List<MassCycleCountDetailRemote>>() {
            @Override
            public void onChanged(List<MassCycleCountDetailRemote> remoteList) {
                adapter.setList(remoteList);
                binding.swipeRefresh.setRefreshing(false);
                if (remoteList.size() > 0) {
                    if (bAuto && !iSuccess) {
                        CycleCountDetailUpdateFragment.newInstance(viewModel.sendRemoteObjectTo(remoteList.get(0).getPalletNumber()), false).show(fm, "FMUPDATE");
                    }
                }
            }
        });

        viewModel.getErrorText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(CycleCountDetailActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getShowProgressbar().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideProgressbar().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getScanText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvPrevBarcode.setText(s);
                binding.etTargetScan.getText().clear();
            }
        });

        viewModel.getIsExitingLocation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Utilities.speakingSomeThingslow("Vui lòng scan tiếp pallet", CycleCountDetailActivity.this);
                } else
                    Utilities.speakingSomeThingslow("Vị trí không tồn tại.", CycleCountDetailActivity.this);
            }
        });

        viewModel.getIsScanLocationFirst().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (!aBoolean)
                        Utilities.speakingSomeThingslow("Vui lòng Scan Vị trí", CycleCountDetailActivity.this);
                    else
                        Utilities.speakingSomeThingslow("Vui lòng Scan Pallet", CycleCountDetailActivity.this);
                }

            }
        });

        viewModel.getIsExistingPallet().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean)
                    Utilities.speakingSomeThingslow("Pallet không tồn tại", CycleCountDetailActivity.this);
            }
        });

        viewModel.getRemoteMutableLiveData().observe(this, new Observer<MassCycleCountDetailRemote>() {
            @Override
            public void onChanged(MassCycleCountDetailRemote remote) {
                if (remote != null) {
                    if (bAuto) {
                        CycleCountDetailUpdateFragment.newInstance(remote, false).show(fm, "FMUPDATE");
                    } else {
                        CycleCountDetailUpdateFragment.newInstance(remote, remoteMass.isStockMovementMassConfirm()).show(fm, "FMUPDATE");
                    }
                }

            }
        });

        viewModel.getMessTextConfirm().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Utilities.speakingSomeThingslow(s, CycleCountDetailActivity.this);
                if (s.equalsIgnoreCase("Đóng phiếu thành công"))
                    finish();
            }
        });
    }


    public static void start(Context context, MassCycleCountRemote cycleCountRemote) {
        Intent starter = new Intent(context, CycleCountDetailActivity.class);
        starter.putExtra(EXTRA_MASSCYCLE_COUNT, cycleCountRemote);
        context.startActivity(starter);
    }

    public static void autoStart(Context context, String massID) {
        Intent starter = new Intent(context, CycleCountDetailActivity.class);
        starter.putExtra(EXTRA_MASSCYCLE_COUNT_MASSID, massID);
        starter.putExtra(EXTRA_MASSCYCLE_COUNT_AUTO, true);
        context.startActivity(starter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onClicked(MassCycleCountDetailRemote remote) {

    }

    @Override
    public void sendInput(boolean success) {
        iSuccess = success;
        if (iSuccess) {
            if (bAuto) {
                loadMassCycleCountDetail(massID);
            } else {
                loadMassCycleCountDetail(remoteMass.getStockMovementMassID());
            }
        }
    }

    private void etScanListener() {
        binding.etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stopLogoutService();
                String contents = s.toString();
                if (contents.contains("\n")) {
                    viewModel.onData(contents.replaceAll("\t\t\n", ""));
                }
            }
        });
    }

    private void swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (bAuto)
                    loadMassCycleCountDetail(massID);
                else
                    loadMassCycleCountDetail(remoteMass.getStockMovementMassID());
            }
        });
    }

    private void showHideWhenScroll() {
        binding.rcCycleCountDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scroll Down
                    if (binding.floatBtnDone.isShown()) {
                        binding.floatBtnDone.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!binding.floatBtnDone.isShown()) {
                        binding.floatBtnDone.show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatBtnDone:

                Utilities.basicDialog(this, "Bạn có muốn Xác Nhận phiếu này", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strUserName = LoginPref.getInfoUser(CycleCountDetailActivity.this, LoginPref.USERNAME);
                        String strDeviceNum = Utilities.getAndroidID(CycleCountDetailActivity.this);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("StockMovementMassID", bAuto == true ? massID : remoteMass.getStockMovementMassID());
                        jsonObject.addProperty("UserName", strUserName);
                        jsonObject.addProperty("DeviceNumber", strDeviceNum);
                        confirmCycleCountTicket(jsonObject);
                    }
                });
                break;
            case R.id.ivCameraScan:
                Utilities.ScanByCamera(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String dataScan = result.getContents();
                viewModel.onData(dataScan.trim());
            }
        }
    }

    private void confirmCycleCountTicket(JsonObject jsonObject) {
        viewModel.confirmMassCycleCountDetail(jsonObject);
    }
}