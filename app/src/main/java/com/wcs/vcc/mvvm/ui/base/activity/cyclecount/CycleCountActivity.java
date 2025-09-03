package com.wcs.vcc.mvvm.ui.base.activity.cyclecount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.mvvm.data.DataManager;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseActivity;
import com.wcs.vcc.mvvm.ui.base.activity.cyclecount.dialog.RandomCycleCountFragment;
import com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail.CycleCountDetailActivity;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ActivityCycleCountBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CycleCountActivity extends BaseActivity<ActivityCycleCountBinding, CycleCountViewModel> implements View.OnClickListener,
        CycleCountAdapter.CycleCountListener, RandomCycleCountFragment.OnSendDataRandom {

    private Calendar calendar, calendar2, calendar3;
    private String reportDate, reportDate2, repotDate3;
    private CycleCountAdapter adapter;
    private FragmentManager fm;
    private MenuItem item_kho;
    private List<MassCycleCountRemote> massCycleCountNoFilterRemotes, massCycleCountNormalRemotes, massCycleCountBlindRemotes, massCycleCountRandomRemotes;

    @NonNull
    @Override
    protected CycleCountViewModel createViewModel() {
        MassCycleCountRepository repository = DataManager.getInstance().getMassCycleCountRepository();
        CycleCountViewModelFactory factory = new CycleCountViewModelFactory(repository);
        return ViewModelProviders.of(this, factory).get(CycleCountViewModel.class);
    }

    @NonNull
    @Override
    protected ActivityCycleCountBinding createViewBinding(LayoutInflater inflater) {
        return ActivityCycleCountBinding.inflate(inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(binding.toolbar);
        fm = getSupportFragmentManager();
        adapter = new CycleCountAdapter(this);
        binding.rcMassCycleCount.setAdapter(adapter);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        binding.toolbar.setTitle(R.string.cycle_count);
        massCycleCountNoFilterRemotes = new ArrayList<>();
        massCycleCountNormalRemotes = new ArrayList<>();
        massCycleCountBlindRemotes = new ArrayList<>();
        massCycleCountRandomRemotes = new ArrayList<>();
        calendar3 = Calendar.getInstance();
        repotDate3 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar3.getTimeInMillis()));

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
        reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));

        viewModel.onDatePreNextFromButton(reportDate);
        viewModel.onDatePreNextToButton(reportDate2);


        binding.btChooseDateFrom.setOnClickListener(this);
        binding.ivArrowLeftFrom.setOnClickListener(this);
        binding.ivArrowRightFrom.setOnClickListener(this);
        binding.btChooseDateTo.setOnClickListener(this);
        binding.ivArrowLeftTo.setOnClickListener(this);
        binding.ivArrowRightTo.setOnClickListener(this);
        binding.ivCameraScan.setOnClickListener(this);

        compareDateToShowHideFrom(repotDate3, reportDate);
        compareDateToShowHideTo(repotDate3, reportDate2);
        swipeRefresh();
        etScanListener();
        observeViewModel();

    }

    private void observeViewModel() {
        viewModel.getShowLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoadingLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getShowTextFromDateOnButton().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btChooseDateFrom.setText(s);
            }
        });

        viewModel.getShowTextToDateOnButton().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btChooseDateTo.setText(s);
            }
        });


        viewModel.getMassCycleCountLiveData().observe(this, new Observer<List<MassCycleCountRemote>>() {
            @Override
            public void onChanged(List<MassCycleCountRemote> massCycleCountRemotes) {
                massCycleCountNoFilterRemotes.clear();
                massCycleCountNoFilterRemotes.addAll(massCycleCountRemotes);
                binding.swipeRefreshCycleCount.setRefreshing(false);
                adapter.setList(massCycleCountNoFilterRemotes);
            }
        });

        viewModel.getMassCycleCountNormalLiveData().observe(this, new Observer<List<MassCycleCountRemote>>() {
            @Override
            public void onChanged(List<MassCycleCountRemote> massCycleCountRemotes) {
                massCycleCountNormalRemotes.clear();
                massCycleCountNormalRemotes.addAll(massCycleCountRemotes);
            }
        });

        viewModel.getMassCycleCountBlindLiveData().observe(this, new Observer<List<MassCycleCountRemote>>() {
            @Override
            public void onChanged(List<MassCycleCountRemote> massCycleCountRemotes) {
                massCycleCountBlindRemotes.clear();
                massCycleCountBlindRemotes.addAll(massCycleCountRemotes);
            }
        });

        viewModel.getMassCycleCountRandomLiveData().observe(this, new Observer<List<MassCycleCountRemote>>() {
            @Override
            public void onChanged(List<MassCycleCountRemote> massCycleCountRemotes) {
                massCycleCountRandomRemotes.clear();
                massCycleCountRandomRemotes.addAll(massCycleCountRemotes);
            }
        });

        viewModel.getShowErrorMessageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(CycleCountActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getNavigateToMassID().observe(this, new Observer<MassCycleCountRemote>() {
            @Override
            public void onChanged(MassCycleCountRemote remote) {
                CycleCountDetailActivity.start(CycleCountActivity.this, remote);
            }
        });

        viewModel.getSendScanData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvPrevBarcode.setText(s);
                binding.etTargetScan.getText().clear();
                findPalletID(s);
            }
        });

        viewModel.getPalletRemote().observe(this, new Observer<List<PalletRemote>>() {
            @Override
            public void onChanged(List<PalletRemote> palletRemotes) {
                if (!palletRemotes.isEmpty()) {
                    PalletRemote palletRemote = palletRemotes.get(0);
                    RandomCycleCountFragment.newInstance(palletRemote).show(fm, null);
                } else {
                    Utilities.speakingSomeThingslow("Pallet không tồn tại", CycleCountActivity.this);
                }
            }
        });
    }

    private void findPalletID(String palletID) {
        JsonObject jsonObject = new JsonObject();
        String idString = "";
        if (!palletID.contains("pi"))
            palletID = "PI" + palletID;
        try {
            idString = palletID.replaceAll("\\D*", "");
        } catch (NumberFormatException ignored) {

        }
        jsonObject.addProperty("PalletNumber", idString);
        viewModel.loadPalletID(jsonObject);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btChooseDateFrom:
                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                        viewModel.onDatePreNextFromButton(reportDate);
                        compareDateToShowHideFrom(repotDate3, reportDate);
                        viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setCanceledOnTouchOutside(false);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                break;

            case R.id.ivArrowLeftFrom:
                calendar.add(Calendar.DATE, -1);
                reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                viewModel.onDatePreNextFromButton(reportDate);
                compareDateToShowHideFrom(repotDate3, reportDate);
                viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                break;

            case R.id.ivArrowRightFrom:

                calendar.add(Calendar.DATE, 1);
                reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                viewModel.onDatePreNextFromButton(reportDate);
                compareDateToShowHideFrom(repotDate3, reportDate);
                viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                break;

            case R.id.btChooseDateTo:
                DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
                        viewModel.onDatePreNextToButton(reportDate2);
                        compareDateToShowHideTo(repotDate3, reportDate2);
                        viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog2.show();
                break;

            case R.id.ivArrowLeftTo:
                calendar2.add(Calendar.DATE, -1);
                reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
                viewModel.onDatePreNextToButton(reportDate2);
                compareDateToShowHideTo(repotDate3, reportDate2);
                viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                break;

            case R.id.ivArrowRightTo:

                calendar2.add(Calendar.DATE, 1);
                reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
                viewModel.onDatePreNextToButton(reportDate2);
                compareDateToShowHideTo(repotDate3, reportDate2);
                viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
                break;

            case R.id.ivCameraScan:
                Utilities.ScanByCamera(this);
                break;
        }
    }

    private void swipeRefresh() {
        binding.swipeRefreshCycleCount.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
            }
        });
    }

    private void compareDateToShowHideFrom(String rp1, String rp2) {
        if (rp1.equals(rp2)) {
            binding.ivArrowRightFrom.setVisibility(View.GONE);
        } else
            binding.ivArrowRightFrom.setVisibility(View.VISIBLE);
    }

    private void compareDateToShowHideTo(String rp1, String rp2) {
        if (rp1.equals(rp2)) {
            binding.ivArrowRightTo.setVisibility(View.GONE);
        } else
            binding.ivArrowRightTo.setVisibility(View.VISIBLE);
    }

    private JsonObject getJsonObject(String dateFrom, String dateTo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("FromDate", Utilities.formatDate_yyyyMMdd2(dateFrom));
        jsonObject.addProperty("ToDate", Utilities.formatDate_yyyyMMdd2(dateTo));
        return jsonObject;
    }

    @Override
    public void onMovieClicked(MassCycleCountRemote remote) {
        viewModel.onMassCycleRemoteClicked(remote);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
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

    @Override
    public void sendInput(boolean success, String massID) {
        if (success) {
            viewModel.loadMassCycleCount(getJsonObject(reportDate, reportDate2));
            CycleCountDetailActivity.autoStart(CycleCountActivity.this, massID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cycle_count_menu, menu);
        item_kho = menu.findItem(R.id.action_cycle_count_menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle(getResources().getString(R.string.tat_ca));
            }
            adapter.setList(massCycleCountNoFilterRemotes);
        } else if (itemId == R.id.action_normal) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle(R.string.normal);
            }
            adapter.setList(massCycleCountNormalRemotes);
        } else if (itemId == R.id.action_blind) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle(R.string.blind);
            }
            adapter.setList(massCycleCountBlindRemotes);
        } else if (itemId == R.id.action_random) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle(getResources().getString(R.string.random));
            }
            adapter.setList(massCycleCountRandomRemotes);
        }
        return true;
    }


}