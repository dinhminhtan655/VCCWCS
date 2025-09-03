package com.wcs.vcc.mvvm.ui.base.activity.cyclecount.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.DataManager;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseFragment;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.FragmentRandomCycleCountBinding;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class RandomCycleCountFragment extends BaseFragment<FragmentRandomCycleCountBinding, RandomCycleCountViewModel> implements View.OnClickListener {
    private static final String TAG = "RandomCycleCountFragment";
    private static final String EXTRA_RANDOM_CYCLE_COUNT = "object";
    private PalletRemote palletRemote;
    private Calendar calendar, calendar2;
    private String reportDate, reportDate2;
    private boolean isGoodBad;
    private OnSendDataRandom onSendDataRandom;
    private Product product;

    public interface OnSendDataRandom {
        void sendInput(boolean success, String massID);
    }


    public static RandomCycleCountFragment newInstance(PalletRemote remote) {
        RandomCycleCountFragment fragment = new RandomCycleCountFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RANDOM_CYCLE_COUNT, remote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RandomCycleCountViewModel createViewModelFragment() {
        RandomCycleCountInsertFragmentRepository repository = DataManager.getInstance().insertRandomCycleCountRepository();
        RandomCycleCountViewModelFactory factory = new RandomCycleCountViewModelFactory(repository);
        return new ViewModelProvider(this, factory).get(RandomCycleCountViewModel.class);
    }

    @Override
    protected FragmentRandomCycleCountBinding createFragmentViewBinding(LayoutInflater inflater) {
        return FragmentRandomCycleCountBinding.inflate(inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        if (getArguments() != null) {
            palletRemote = (PalletRemote) this.getArguments().getSerializable(EXTRA_RANDOM_CYCLE_COUNT);
        }

        if (palletRemote != null) {
            //Load Product list
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("CustomerID", palletRemote.getCustomerID());
//            viewModel.loadProductList(jsonObject);

            try {
                //set NSX Date
                reportDate = formatDate(palletRemote.getProductionDate());
                // Set HSD Date
                reportDate2 = formatDate(palletRemote.getUseByDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.btnProductNSX.setText(reportDate);
            binding.btnProductHSD.setText(reportDate2);

            binding.tvPaOrderNumber.setText(palletRemote.getPalletNumber() + "~" + palletRemote.getReceivingOrderNumber() + "\n" + palletRemote.getLocationNumber());
            binding.edtSL.setText(String.valueOf(palletRemote.getCurrentQuantity()));
            binding.edtLocationChecking.setText(palletRemote.getLocationNumber());
            binding.tvProductName.setText(palletRemote.getProductNumber() + "~~" +palletRemote.getProductName());
        }

        binding.radiGroupYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiYes:
                        viewModel.checkingGoodBad(true);
                        break;
                    case R.id.radiNo:
                        viewModel.checkingGoodBad(false);
                        break;
                }
            }
        });
        binding.btnUpdate.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);
        binding.btnProductHSD.setOnClickListener(this);
        binding.btnProductNSX.setOnClickListener(this);
        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getShowLoading().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressLoading.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHideLoading().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.progressLoading.setVisibility(View.GONE);
            }
        });

        viewModel.getCheckingGoodBad().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isGoodBad = aBoolean;
            }
        });

        viewModel.getProductList().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {
                binding.spinProductID.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list));
                int p = 0;
                //check product name to set selection
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProductName().equalsIgnoreCase(palletRemote.getProductName())) {
                        p = i;
                        binding.spinProductID.setSelection(p);
                        break;
                    }
                }

                binding.spinProductID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        product = list.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        viewModel.getMessInsert().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.equalsIgnoreCase("NO") || !s.equalsIgnoreCase("")) {
                    onSendDataRandom.sendInput(true, s);
                    Utilities.speakingSomeThingslow("Tạo Thành công phiếu ",getContext());
                    getDialog().dismiss();
                } else
                    Utilities.speakingSomeThingslow("Thất bại", getContext());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                String strPalletID = palletRemote.getPalletNumber();
                String strSourceLocationID = palletRemote.getLocationID();
                int quantityChecking = Integer.valueOf(binding.edtSL.getText().toString());
                String strReason = binding.edtNote.getText().toString();
                String strUserName = LoginPref.getInfoUser(getContext(), LoginPref.USERNAME);
                String strDeviceNum = Utilities.getAndroidID(getContext());
                String strProductID = palletRemote.getProductID();
                String strNSX = Utilities.formatDate_MMddyyyy4(reportDate);
                String strHSD = Utilities.formatDate_MMddyyyy4(reportDate2);
                int iStoreID = LoginPref.getStoreId(getContext());

                viewModel.insertRandomCycleCount(getJsonObject(strPalletID, strSourceLocationID, quantityChecking, strReason, strDeviceNum, isGoodBad,
                        strUserName, strProductID, strNSX, strHSD, iStoreID));

                break;

            case R.id.btnClose:
                getDialog().dismiss();
                break;

            case R.id.btnProductNSX:
                DatePickerDialog dialogNSX = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                        binding.btnProductNSX.setText(reportDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialogNSX.setCanceledOnTouchOutside(false);
                dialogNSX.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialogNSX.show();
                break;

            case R.id.btnProductHSD:
                DatePickerDialog dialogHSD = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
                        binding.btnProductHSD.setText(reportDate2);
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                dialogHSD.setCanceledOnTouchOutside(false);
                dialogHSD.show();
                break;
        }
    }

    private JsonObject getJsonObject(String strPalletID, String strLocationID, int iCheckingQty, String strCheckingReason, String strDeviceNum, boolean bCheckingStatus,
                                     String strUserName, String strProductID, String strProductDate, String strUseByDate, int iStoreID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ScanResult", strPalletID);
        jsonObject.addProperty("SourceLocationID", strLocationID);
        jsonObject.addProperty("CheckingQuantity", iCheckingQty);
        jsonObject.addProperty("CheckingReason", strCheckingReason);
        jsonObject.addProperty("CheckingStatus", bCheckingStatus);
        jsonObject.addProperty("UserName", strUserName);
        jsonObject.addProperty("DeviceNumber", strDeviceNum);
        jsonObject.addProperty("CheckingProductID", strProductID);
        jsonObject.addProperty("CheckingProductionDate", strProductDate);
        jsonObject.addProperty("CheckingUseByDate", strUseByDate);
        jsonObject.addProperty("CheckingRemark", "");
        jsonObject.addProperty("CheckingCustomerRef", "");
        jsonObject.addProperty("CheckingCustomerRef2", "");
        jsonObject.addProperty("StoreID", iStoreID);
        return jsonObject;
    }

    private String formatDate(String strDate) throws ParseException {
        return Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                strDate)));
    }

    private String formatDate2(String strDate) throws ParseException {
        return Utilities.formatDate_ddMMyyyy3(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                strDate)));
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSendDataRandom = (OnSendDataRandom) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}