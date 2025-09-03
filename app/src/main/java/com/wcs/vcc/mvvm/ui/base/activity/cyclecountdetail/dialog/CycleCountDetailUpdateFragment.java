package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.DataManager;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseFragment;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.FragmentCycleCountDetailUpdateBinding;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class CycleCountDetailUpdateFragment extends BaseFragment<FragmentCycleCountDetailUpdateBinding, CycleCountDetailUpdateViewModel>
        implements View.OnClickListener {


    private Calendar calendar, calendar2;
    private String reportDate, reportDate2, strProductName, strLocationName;
    private static final String TAG = "CycleCountDetailUpdateFragment";
    private static final String EXTRA_MASSCYCLE_COUNT_DETAIL = "object";
    private static final String EXTRA_MASSCYCLE_COUNT_CONFIRM = "confirm";
    private MassCycleCountDetailRemote remote = null;
    private boolean isGoodBad = true;
    private OnSendData sendData;
    private Product product;
    private Locations locationDomain;

    public interface OnSendData {
        void sendInput(boolean success);
    }


    public static CycleCountDetailUpdateFragment newInstance(MassCycleCountDetailRemote remote, boolean isConfirm) {
        CycleCountDetailUpdateFragment dialog = new CycleCountDetailUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MASSCYCLE_COUNT_DETAIL, remote);
        args.putBoolean(EXTRA_MASSCYCLE_COUNT_CONFIRM, isConfirm);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected CycleCountDetailUpdateViewModel createViewModelFragment() {
        MassCycleCountDetailUpdateFramentRepository repository = DataManager.getInstance().updateMassCycleDetailRepository();
        CycleCountDetailUpdateViewModelFactory factory = new CycleCountDetailUpdateViewModelFactory(repository);
        return new ViewModelProvider(this, factory).get(CycleCountDetailUpdateViewModel.class);
    }


    @Override
    protected FragmentCycleCountDetailUpdateBinding createFragmentViewBinding(LayoutInflater inflater) {
        return FragmentCycleCountDetailUpdateBinding.inflate(inflater);
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

        viewModel.getMessText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equalsIgnoreCase("Thành công")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    Utilities.speakingSomeThingslow(s, getContext());
                    sendData.sendInput(true);
                    getDialog().dismiss();
                }
            }
        });

        viewModel.getProductList().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {

                binding.tvSpinProductID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpinnerSearchProductDialog(getActivity(), list, binding.tvSpinProductID);
                    }
                });

                //check product name to set selection
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProductName().equalsIgnoreCase(remote.getProductName())) {
                        product = list.get(i);
                        binding.tvSpinProductID.setText(list.get(i).getProductNumber() + "~~" + list.get(i).getProductName());
                        break;
                    }
                }

                for (int e = 0; e < list.size(); e++) {
                    if (list.get(e).getProductID().trim().equalsIgnoreCase(remote.getCheckingProductID().trim())) {
                        strProductName = list.get(e).getProductNumber() + "~~" + list.get(e).getProductName();
                        //set product name checked
                        binding.tvProNameChecked.setText(strProductName);
                        break;
                    }
                }
            }
        });

        viewModel.getLocationsList().observe(this, new Observer<List<Locations>>() {
            @Override
            public void onChanged(List<Locations> locations) {


                binding.tvSpinLocationID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpinnerSearchLocationDialog(getActivity(), locations, binding.tvSpinLocationID);
                    }
                });

                //check location name to set selection
                for (int i = 0; i < locations.size(); i++) {
                    if (locations.get(i).getLocationID().trim().equalsIgnoreCase(remote.getLocationID().trim())) {
                        locationDomain = locations.get(i);
                        binding.tvSpinLocationID.setText(locations.get(i).getLocationNumber());
                        break;
                    }
                }

                for (int e = 0; e < locations.size(); e++) {
                    if (locations.get(e).getLocationID().equalsIgnoreCase(remote.getCheckingLocationID().trim())) {
                        strLocationName = locations.get(e).getLocationNumber();
                        //set location name checked
                        binding.tvLocationChecked.setText(strLocationName);
                        break;
                    }
                }
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.loadLocations();
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        remote = (MassCycleCountDetailRemote) this.getArguments().get(EXTRA_MASSCYCLE_COUNT_DETAIL);

        boolean isConfirmed = this.getArguments().getBoolean(EXTRA_MASSCYCLE_COUNT_CONFIRM);

        if (remote != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("CustomerID", remote.getCustomerID());
            viewModel.loadProductList(jsonObject);

            if (isConfirmed) {
                binding.btnUpdate.setVisibility(View.GONE);
                binding.lnSL.setVisibility(View.GONE);
                binding.lnLocation.setVisibility(View.GONE);
                binding.lnProductDate.setVisibility(View.GONE);
                binding.lnProductName.setVisibility(View.GONE);
                binding.lnRef.setVisibility(View.GONE);
                binding.lnRef2.setVisibility(View.GONE);
            }

            try {
                //set NSX Date
                reportDate = formatDate(remote.getProductionDateNoFormat());
                // Set HSD Date
                reportDate2 = formatDate(remote.getUseBydateNoFormat());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.btnProductNSX.setText(reportDate);
            binding.btnProductHSD.setText(reportDate2);

            binding.tvPaOrderNumber.setText(remote.getPalletNumber() + "~" + remote.getOrderNumber() + "\n" + remote.getLocationNumber());
            binding.edtSL.setSelection(binding.edtSL.getText().length());
            binding.tvSL.setText("SL Tồn \n" + remote.getTotalOriginalQuantity());

            binding.edtSL.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    binding.edtSL.setSelection(binding.edtSL.getText().length());
                    if (s.toString().length() >= 1 && s.toString().startsWith("0")) {
                        if (s.toString().length() >= 2) {
                            binding.edtSL.setText(String.valueOf(Integer.valueOf(s.toString())));
                        }
                    }
                }
            });

            if (remote.getCheckingQuantity() > 0) {
                binding.tvPaOrderNumber.setBackground(getResources().getDrawable(R.color.text_selected));
            }

            if (remote.getCheckingQuantity() == 0) {
                binding.lnChecked.setVisibility(View.GONE);
                binding.lnCheckedBy.setVisibility(View.GONE);
                binding.lnCheckedLocation.setVisibility(View.GONE);
                binding.lnCheckedProName.setVisibility(View.GONE);
                binding.lnProductDateChecked.setVisibility(View.GONE);
                binding.lnCheckedRef.setVisibility(View.GONE);
                binding.lnCheckedRef2.setVisibility(View.GONE);
            }


            binding.tvCheckedBy.setText(remote.getCheckingScannedBy() + "\n" + remote.getCheckingScannedTime());
            binding.tvSLChecked.setText(String.valueOf(remote.getCheckingQuantity()));
            binding.edtNote.setText(remote.getCheckingReason());
            binding.edtNoteRef.setText(remote.getCustomerRef());
            binding.edtNoteRef2.setText(remote.getCustomerRef2());
            binding.tvRefChecked.setText(remote.getCheckingCustomerRef());
            binding.tvRef2Checked.setText(remote.getCheckingCustomerRef2());
//            binding.tvLocationChecked.setText(remote.getCheckingLocationID());

            try {
                binding.tvProductHSD.setText(formatDate(remote.getCheckingUseByDate()));
                binding.tvProductNSX.setText(formatDate(remote.getCheckingProductionDate()));
            } catch (ParseException e) {
                e.printStackTrace();
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
            if (remote.isCheckingStatus() && remote.getCheckingQuantity() > 0) {
                binding.radiYes.setChecked(true);
            } else if (!remote.isCheckingStatus() && remote.getCheckingQuantity() > 0) {
                binding.radiNo.setChecked(true);
            }
        }
        binding.btnUpdate.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);
        binding.btnProductNSX.setOnClickListener(this);
        binding.btnProductHSD.setOnClickListener(this);
        binding.btnRefreshLocation.setOnClickListener(this);

        observeViewModel();
    }

    private JsonObject getJsonObject(String stockMovementMassID, String palletID, String sourceLocationID,
                                     int checkingQuantity, String checkingReason, boolean checkingStatus,
                                     String userName, String deviceNumber, String productID, String dateProduct,
                                     String dateExpire, String strRef, String strRef2, String checkingLocationID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StockMovementMassID", stockMovementMassID);
        jsonObject.addProperty("PalletID", palletID);
        jsonObject.addProperty("SourceLocationID", sourceLocationID);
        jsonObject.addProperty("CheckingQuantity", checkingQuantity);
        jsonObject.addProperty("CheckingReason", checkingReason);
        jsonObject.addProperty("CheckingStatus", checkingStatus);
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("DeviceNumber", deviceNumber);
        jsonObject.addProperty("CheckingProductID", productID);
        jsonObject.addProperty("CheckingProductionDate", dateProduct);
        jsonObject.addProperty("CheckingUseByDate", dateExpire);
        jsonObject.addProperty("CheckingRemark", "");
        jsonObject.addProperty("CheckingCustomerRef", strRef);
        jsonObject.addProperty("CheckingCustomerRef2", strRef2);
        jsonObject.addProperty("CheckingLocationID", checkingLocationID);
        return jsonObject;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                String strStockMovementMassID = remote.getStockMovementMassID();
                String strPalletID = remote.getPalletID();
                String strSourceLocationID = remote.getLocationID();
                int quantityChecking = 0;
                if (binding.edtSL.getText().length() > 0) {
                    quantityChecking = Integer.valueOf(binding.edtSL.getText().toString());
                }
                String strReason = binding.edtNote.getText().toString();
                String strUserName = LoginPref.getInfoUser(getContext(), LoginPref.USERNAME);
                String strDeviceNum = Utilities.getAndroidID(getContext());
                String strProductID = product.getProductID();
                String strCheckingLocationID = locationDomain.getLocationID();
                String strNSX = Utilities.formatDate_MMddyyyy4(reportDate);
                String strHSD = Utilities.formatDate_MMddyyyy4(reportDate2);
                String strRef = binding.edtNoteRef.getText().toString();
                String strRef2 = binding.edtNoteRef2.getText().toString();
                if (binding.edtSL.length() == 0) {
                    Utilities.speakingSomeThingslow("Vui lòng nhập Số Lượng", getContext());
                    return;
                } else {
                    viewModel.updateMassCycleCountDetail(getJsonObject(strStockMovementMassID, strPalletID, strSourceLocationID,
                            quantityChecking, strReason, isGoodBad, strUserName, strDeviceNum, strProductID, strNSX, strHSD, strRef, strRef2, strCheckingLocationID));
                }
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

            case R.id.btnRefreshLocation:
                viewModel.loadLocations();
                break;
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendData = (OnSendData) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    private String formatDate(String strDate) throws ParseException {
        return Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                strDate)));
    }

    private String formatDate2(String strDate) throws ParseException {
        return Utilities.formatDate_ddMMyyyy3(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                strDate)));
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


    private void SpinnerSearchLocationDialog(Activity activity, List<Locations> list, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<Locations> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).toString());
                locationDomain = list.get(position);
                dialog.dismiss();
            }
        });
    }

    private void SpinnerSearchProductDialog(Activity activity, List<Product> list, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).toString());
                product = list.get(position);
                dialog.dismiss();
            }
        });
    }
}