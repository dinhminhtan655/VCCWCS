package com.wcs.vcc.main.detailphieu.worker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wcs.wcs.R;
import com.wcs.vcc.api.EmployeePresentParameter;
import com.wcs.vcc.api.EmployeeWorkingParameter;
import com.wcs.vcc.api.InsertWorkerParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class WorkerFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {
    public static final String ORDER_NUMBER = "ORDER_NUMBER";
    public static final String TAG = "WorkerFragment";
    @BindView(R.id.tv_worker_check_by)
    TextView tvCheckBy;
    @BindView(R.id.tv_worker_approve_by)
    TextView tvApproveBy;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.et_worker_tong_cnts)
    EditText vTongSoThung;
    @BindView(R.id.et_worker_qty_pallet)
    EditText etQtyPallet;
    @BindView(R.id.cb_worker_co_hang_pallet)
    CheckBox cbHangPallet;
    @BindView(R.id.cb_worker_xe_binh_thuong)
    CheckBox cbXeBinhThuong;
    @BindView(R.id.et_worker_sai_lech)
    EditText etSaiLech;
    @BindView(R.id.sp_worker_boc_xep_total_percent)
    AppCompatAutoCompleteTextView spTotalPercentGH;
    @BindView(R.id.actv_worker_boc_xep_1)
    AppCompatAutoCompleteTextView vBocXepID1;
    @BindView(R.id.actv_worker_boc_xep_2)
    AppCompatAutoCompleteTextView vBocXepID2;
    @BindView(R.id.et_worker_boc_xep_percent_1)
    EditText vPercentBocXep1;
    @BindView(R.id.et_worker_boc_xep_percent_2)
    EditText vPercentBocXep2;
    @BindView(R.id.actv_worker_walkie_1)
    AppCompatAutoCompleteTextView vWalkieID1;
    @BindView(R.id.actv_worker_walkie_2)
    AppCompatAutoCompleteTextView vWalkieID2;
    @BindView(R.id.et_worker_walkie_percent_1)
    EditText vPercentWalkie1;
    @BindView(R.id.et_worker_walkie_percent_2)
    EditText vPercentWalkie2;
    @BindView(R.id.actv_worker_tai_xe_1)
    AppCompatAutoCompleteTextView actvTaiXeID1;
    @BindView(R.id.et_worker_tai_xe_percent_1)
    EditText etPercentTaiXe1;
    @BindView(R.id.cb_worker_smell)
    CheckBox vSmell;
    @BindView(R.id.cb_worker_dirty)
    CheckBox vDirty;
    @BindView(R.id.cb_worker_carton_torn)
    CheckBox vCartonTorn;
    @BindView(R.id.cb_worker_wet)
    CheckBox vWet;
    @BindView(R.id.cb_worker_missing)
    CheckBox vMissing;
    @BindView(R.id.cb_worker_lid_opening)
    CheckBox vLidOpen;
    @BindView(R.id.cb_worker_denting)
    CheckBox vDent;
    @BindView(R.id.cb_worker_clean)
    CheckBox vClean;
    @BindView(R.id.cb_worker_damages)
    CheckBox vDamages;
    @BindView(R.id.cb_worker_fall_break)
    CheckBox vFallBreak;
    @BindView(R.id.cb_worker_musty)
    CheckBox vMusty;
    @BindView(R.id.cb_worker_soft)
    CheckBox vSoft;
    @BindView(R.id.cb_worker_insects_risk)
    CheckBox vInsects;
    @BindView(R.id.cb_worker_leaks)
    CheckBox vLeaks;
    @BindView(R.id.cb_worker_glass_wood_fragments)
    CheckBox vGlassFragments;
    @BindView(R.id.cb_worker_other)
    CheckBox vOther;
    @BindView(R.id.et_worker_description)
    EditText vDescription;

    private String orderNumber;
    private Calendar currentCalendar;
    private ProgressDialog dialog;
    private List<Integer> employeeIDArray = new LinkedList<>();
    private String date_format_sql = "%d-%02d-%02d";
    private String date_time_format_sql = "yyyy-MM-dd'T'HH:mm:ss";
    private String time_format_sql = "%02d:%02d:00";
    private String startDate = "1900-01-01", startTime = "00:00:00", endDate = "1900-01-01", endTime = "00:00:00";
    private TonPerHourAdapter adapter;
    private List<EmployeeWorkingTonPerHourInfo> listTonPerHour = new ArrayList<>();
    private WorkerAdditionalFragment workerAdditionalFragment;
    private OnDataListener dataListener;
    private String userName;
    private int storeId;
    private EmployeePresentAdapter employeePresentAdapter;

    public static WorkerFragment newInstance(String orderNumber) {
        WorkerFragment workerFragment = new WorkerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, orderNumber);
        workerFragment.setArguments(bundle);
        return workerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataListener)
            dataListener = (OnDataListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            orderNumber = arguments.getString(ORDER_NUMBER);
        }
        workerAdditionalFragment = ((WorkerActivity) getActivity()).getWorkerAdditionalFragment();
        userName = LoginPref.getInfoUser(getContext(), LoginPref.USERNAME);
        storeId = LoginPref.getStoreId(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutID = orderNumber.contains("DO") ? R.layout.fragment_worker_do : R.layout.fragment_worker_ro;
        View view = inflater.inflate(layoutID, container, false);
        ButterKnife.bind(this, view);
        if (Group.convertGroupStringToInt(LoginPref.getPositionGroup(getContext())) == Group.SUPERVISOR ||
                Group.convertGroupStringToInt(LoginPref.getPositionGroup(getContext())) == Group.MANAGER)
            spTotalPercentGH.setEnabled(true);

        currentCalendar = Calendar.getInstance();
        cbHangPallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etQtyPallet.setEnabled(isChecked);
            }
        });

        adapter = new TonPerHourAdapter(getContext(), new ArrayList<EmployeeWorkingTonPerHourInfo>());
        spTotalPercentGH.setAdapter(adapter);
        spTotalPercentGH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !spTotalPercentGH.isPopupShowing())
                    spTotalPercentGH.showDropDown();
            }
        });
        spTotalPercentGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spTotalPercentGH.showDropDown();
            }
        });
        vBocXepID1.setOnFocusChangeListener(this);
        vBocXepID2.setOnFocusChangeListener(this);
        vWalkieID1.setOnFocusChangeListener(this);
        vWalkieID2.setOnFocusChangeListener(this);
        actvTaiXeID1.setOnFocusChangeListener(this);

        vBocXepID1.setOnItemClickListener(onItemClickListener(vBocXepID1));
        vBocXepID2.setOnItemClickListener(onItemClickListener(vBocXepID2));
        vWalkieID1.setOnItemClickListener(onItemClickListener(vWalkieID1));
        vWalkieID2.setOnItemClickListener(onItemClickListener(vWalkieID2));
        actvTaiXeID1.setOnItemClickListener(onItemClickListener(actvTaiXeID1));

        vBocXepID1.setOnClickListener(this);
        vBocXepID2.setOnClickListener(this);
        vWalkieID1.setOnClickListener(this);
        vWalkieID2.setOnClickListener(this);
        actvTaiXeID1.setOnClickListener(this);
        getEmployeeID();
        getEmployeeWorkingTonPerHour();
        getEmployeeWorking(getView());
        return view;
    }

    private AdapterView.OnItemClickListener onItemClickListener(final View view) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                EmployeeInfo item = employeePresentAdapter.getItem(i);
                if (item != null) {
                    String id = item.getEmployeeID().toString();
                    view.setTag(id);
                }
            }
        };
    }

    public void getEmployeeID() {
        dialog = Utilities.getProgressDialog(getContext(), getString(R.string.loading_data));
        dialog.show();
        MyRetrofit.initRequest(getContext()).getEmployeeID(new EmployeePresentParameter(1, Const.EMPLOYEE_ID, storeId)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    employeeIDArray.clear();
                    for (EmployeeInfo info : response.body()) {
                        employeeIDArray.add(info.EmployeeCode);
                    }
                    setAdapterForACTV(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setAdapterForACTV(List<EmployeeInfo> employees) {
        if (employees != null) {
            employeePresentAdapter = new EmployeePresentAdapter(getContext(), employees);
            vBocXepID1.setAdapter(employeePresentAdapter);
            vBocXepID2.setAdapter(employeePresentAdapter);
            vWalkieID1.setAdapter(employeePresentAdapter);
            vWalkieID2.setAdapter(employeePresentAdapter);
            actvTaiXeID1.setAdapter(employeePresentAdapter);
            workerAdditionalFragment.setEmployeePresentAdapter(employeePresentAdapter);
        }
    }

    public void getEmployeeWorkingTonPerHour() {
        MyRetrofit.initRequest(getContext()).getEmployeeWorkingTonPerHour(orderNumber).enqueue(new Callback<List<EmployeeWorkingTonPerHourInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeWorkingTonPerHourInfo>> response, Retrofit retrofit) {
                List<EmployeeWorkingTonPerHourInfo> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    listTonPerHour.clear();
                    listTonPerHour.addAll(body);

                    adapter.clear();
                    adapter.addAll(listTonPerHour);
                }

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void getEmployeeWorking(final View view) {
        if (!WifiHelper.isConnected(getContext())) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).getEmployeeWorking(new EmployeeWorkingParameter(orderNumber, userName)).enqueue(new Callback<List<WorkerInfo>>() {
            @Override
            public void onResponse(Response<List<WorkerInfo>> response, Retrofit retrofit) {
                List<WorkerInfo> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    new AsyncUpdateData().execute(body.get(0));
                } else {
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }


    @OnClick(R.id.tv_start_date)
    public void startDate() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvStartDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                startDate = String.format(Locale.getDefault(), date_format_sql, year, monthOfYear + 1, dayOfMonth);
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_start_time)
    public void startTime() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                startTime = String.format(Locale.getDefault(), time_format_sql, hourOfDay, minute);
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @OnClick(R.id.tv_end_date)
    public void endDate() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format(Locale.getDefault(), date_format_sql, year, monthOfYear + 1, dayOfMonth);
                tvEndDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                endDate = date;
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_end_time)
    public void endTime() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(Locale.getDefault(), time_format_sql, hourOfDay, minute);
                tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                endTime = time;
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    public void done(View view) {
        if (!orderNumber.contains("DO"))
            if (vTongSoThung.getText().toString().length() <= 0 || Integer.parseInt(vTongSoThung.getText().toString()) <= 0) {
                Snackbar.make(view, "Bạn phải nhập số lượng Thùng lớn hơn 0", Snackbar.LENGTH_SHORT).show();
                vTongSoThung.requestFocus();
                return;
            }
        if (!checkID(vBocXepID1))
            return;
        else if (!checkPercentage(vPercentBocXep1))
            return;
        if (!checkID(vBocXepID2))
            return;
        else if (!checkPercentage(vPercentBocXep2))
            return;

        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;

        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;

        setParameterAndExecute(view, 1);

    }

    public void save(View view) {
        setParameterAndExecute(view, 0);
    }

    public void approve(View view) {
        if (!orderNumber.contains("DO"))
            if (vTongSoThung.getText().toString().length() <= 0 || Integer.parseInt(vTongSoThung.getText().toString()) <= 0) {
                Snackbar.make(view, "Bạn phải nhập số lượng Thùng lớn hơn 0", Snackbar.LENGTH_SHORT).show();
                vTongSoThung.requestFocus();
                return;
            }
        if (!checkID(vBocXepID1))
            return;
        else if (!checkPercentage(vPercentBocXep1))
            return;
        if (!checkID(vBocXepID2))
            return;
        else if (!checkPercentage(vPercentBocXep2))
            return;
        if (!checkID(workerAdditionalFragment.getvBocXepID3()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep3()))
            return;
        if (!checkID(workerAdditionalFragment.getvBocXepID4()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep4()))
            return;
        if (!checkID(workerAdditionalFragment.getvBocXepID5()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep5()))
            return;

        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;
        if (!checkID(workerAdditionalFragment.getvWalkieID3()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentWalkie3()))
            return;
        if (!checkID(workerAdditionalFragment.getvWalkieID4()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentWalkie4()))
            return;

        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;
        if (!checkID(workerAdditionalFragment.getActvTaiXeID2()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getEtPercentTaiXe2()))
            return;
        setParameterAndExecute(view, 2);
    }

    private void setParameterAndExecute(View view, int flag) {
        int totalPackage = 0;
        if (vTongSoThung.getText().toString().length() > 0)
            totalPackage = Integer.parseInt(vTongSoThung.getText().toString());
        int totalPercentGH = 0;
        if (vBocXepID1.getText().length() > 0)
            totalPercentGH += stringToInt(vPercentBocXep1);
        if (vBocXepID2.getText().length() > 0)
            totalPercentGH += stringToInt(vPercentBocXep2);
        if (workerAdditionalFragment.getvBocXepID3().getText().length() > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep3());
        if (workerAdditionalFragment.getvBocXepID4().getText().length() > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep4());
        if (workerAdditionalFragment.getvBocXepID5().getText().length() > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep5());
        if (totalPercentGH != 100 && totalPercentGH != 0) {
            Snackbar.make(view, "Tổng tỉ lệ phần trăm của nhân viên bốc xếp phải bằng 100", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (etSaiLech.getText().toString().length() == 0)
            etSaiLech.setText("0");
        if (etQtyPallet.getText().toString().length() == 0)
            etQtyPallet.setText("0");
        InsertWorkerParameter parameter = new InsertWorkerParameter(
                vClean.isChecked(), vDamages.isChecked(),
                vDent.isChecked(), vDirty.isChecked(),
                vFallBreak.isChecked(), vGlassFragments.isChecked(),
                vInsects.isChecked(), vLeaks.isChecked(), vLidOpen.isChecked(),
                vMusty.isChecked(), vOther.isChecked(), vSmell.isChecked(),
                vSoft.isChecked(), vCartonTorn.isChecked(), vWet.isChecked(),
                vMissing.isChecked());
        int percentage = spTotalPercentGH.getText().length() > 0 ? Integer.parseInt(spTotalPercentGH.getText().toString()) : 0;

        parameter.setTotalPercentGH(percentage);
        parameter.setStartTime(startDate + "T" + startTime);
        parameter.setOrderStatus(flag);
        parameter.setTruckContAfterNormal(cbXeBinhThuong.isChecked());
        parameter.setGoodsOnPallet(cbHangPallet.isChecked());
        parameter.setDifferentQty(Integer.parseInt(etSaiLech.getText().toString()));
        parameter.setPalletQty(Integer.parseInt(etQtyPallet.getText().toString()));
        parameter.setEndTime(endDate + "T" + endTime);
        parameter.setTotalPackages(totalPackage);
        parameter.setGeneralHandID1(Utilities.getUUIDFromTag(vBocXepID1));
        parameter.setGeneralHandID2(Utilities.getUUIDFromTag(vBocXepID2));
        parameter.setPercentGH1(stringToInt(vPercentBocXep1));
        parameter.setPercentGH2(stringToInt(vPercentBocXep2));
        parameter.setWalkieID1(Utilities.getUUIDFromTag(vWalkieID1));
        parameter.setWalkieID2(Utilities.getUUIDFromTag(vWalkieID2));
        parameter.setPercentWalkieID1(stringToInt(vPercentWalkie1));
        parameter.setPercentWalkieID2(stringToInt(vPercentWalkie2));
        parameter.setForkliftDriverID1(Utilities.getUUIDFromTag(actvTaiXeID1));
        parameter.setPercentFD1(stringToInt(etPercentTaiXe1));
        parameter.setRemark(vDescription.getText().toString());
        parameter.setUserName(LoginPref.getInfoUser(getContext(), LoginPref.USERNAME));
        parameter.setOrderNumber(orderNumber);
        parameter.setGeneralHandID3(Utilities.getUUIDFromTag(workerAdditionalFragment.getvBocXepID3()));
        parameter.setGeneralHandID4(Utilities.getUUIDFromTag(workerAdditionalFragment.getvBocXepID4()));
        parameter.setGeneralHandID5(Utilities.getUUIDFromTag(workerAdditionalFragment.getvBocXepID5()));
        parameter.setPercentGH3(stringToInt(workerAdditionalFragment.getvPercentBocXep3()));
        parameter.setPercentGH4(stringToInt(workerAdditionalFragment.getvPercentBocXep4()));
        parameter.setPercentGH5(stringToInt(workerAdditionalFragment.getvPercentBocXep5()));
        parameter.setWalkieID3(Utilities.getUUIDFromTag(workerAdditionalFragment.getvWalkieID3()));
        parameter.setWalkieID4(Utilities.getUUIDFromTag(workerAdditionalFragment.getvWalkieID4()));
        parameter.setForkliftDriverID2(Utilities.getUUIDFromTag(workerAdditionalFragment.getActvTaiXeID2()));
        parameter.setPercentFD2(stringToInt(workerAdditionalFragment.getEtPercentTaiXe2()));
        insertEmployeeWorking(view, parameter);
    }

    private boolean checkID(AppCompatAutoCompleteTextView view) {
        if (view.getText().toString().length() == 0) {
            view.setText("0");
            return true;
        }
        if (Integer.parseInt(view.getText().toString()) == 0)
            return true;
        if (employeeIDArray.contains(Integer.parseInt(view.getText().toString())))
            return true;
        view.requestFocus();
        Snackbar.make(view, "Bạn phải chọn mã Nhân viên có trong danh sách", Snackbar.LENGTH_LONG).show();
        return false;

    }

    private boolean checkPercentage(EditText view) {
        if (view.getText().toString().length() == 0) {
            view.setText("0");
            return true;
        }

        if (view.getText().toString().length() != 0)
            if (Integer.parseInt(view.getText().toString()) >= 0 && Integer.parseInt(view.getText().toString()) <= 100)
                return true;
        view.requestFocus();
        Snackbar.make(view, "Năng suất được phép là từ 0% đến 100%", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private int stringToInt(AppCompatAutoCompleteTextView view) {
        String data = view.getText().toString();
        if (data.length() > 0) {
            return Integer.parseInt(data);
        }
        return 0;
    }

    private int stringToInt(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void insertEmployeeWorking(final View view, InsertWorkerParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang lưu dữ liệu...");
        dialog.show();
        if (!WifiHelper.isConnected(getContext())) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertEmployeeWorking(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(view, response.body(), Snackbar.LENGTH_LONG).show();
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }


    private boolean isUpdate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql, Locale.getDefault());
        long time = 0;
        try {
            Date startDate = simpleDateFormat.parse("2000-01-01T00:00:00");
            time = startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis() >= time;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ((AppCompatAutoCompleteTextView) v).showDropDown();
        }
    }

    @Override
    public void onClick(View v) {
        ((AppCompatAutoCompleteTextView) v).showDropDown();
    }

    interface OnDataListener {
        void onUpdateButton(int orderStatus);
    }

    private class AsyncUpdateData extends AsyncTask<WorkerInfo, Void, WorkerInfo> {

        @Override
        protected WorkerInfo doInBackground(WorkerInfo... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(WorkerInfo info) {
            workerAdditionalFragment.setWorkerInfo(info);
            spTotalPercentGH.setText(String.format(Locale.US, "%d", info.getTotalPercentGH()));
            cbXeBinhThuong.setChecked(info.isTruckContAfterNormal());
            cbHangPallet.setChecked(info.isGoodsOnPallet());
            etSaiLech.setText(NumberFormat.getInstance().format(info.getDifferentQty()));
            etQtyPallet.setText(NumberFormat.getInstance().format(info.getPalletQty()));

            tvCheckBy.setText(String.format(Locale.US, "Check by: %s", info.getCreatedBy()));
            tvApproveBy.setText(String.format(Locale.US, "Approve by: %s", info.getApprovedBy()));
            vSmell.setChecked(info.isSmell());
            vCartonTorn.setChecked(info.isTorn());
            vWet.setChecked(info.isWet());
            vMissing.setChecked(info.isMissing());
            vFallBreak.setChecked(info.isFall_Break());
            vLidOpen.setChecked(info.isLidOpening());
            vDent.setChecked(info.isDenting());
            vClean.setChecked(info.isClean());
            vDamages.setChecked(info.isDamages());
            vMusty.setChecked(info.isMusty());
            vSoft.setChecked(info.isSoft());
            vInsects.setChecked(info.isInsectsRisk());
            vLeaks.setChecked(info.isLeak());
            vGlassFragments.setChecked(info.isGlass_WoodFragments());
            vDirty.setChecked(info.isDirty());
            vOther.setChecked(info.isOthers());
            /*--*/
            vTongSoThung.setText(String.format(Locale.US, "%d", info.getTotalPackages()));
            vBocXepID1.setText(String.format(Locale.US, "%d", info.GHCode1));
            vBocXepID2.setText(String.format(Locale.US, "%d", info.GHCode2));

            vPercentBocXep1.setText(String.format(Locale.US, "%d", info.getPercentGH1()));
            vPercentBocXep2.setText(String.format(Locale.US, "%d", info.getPercentGH2()));

            vWalkieID1.setText(String.format(Locale.US, "%d", info.WalkieCode1));
            vWalkieID2.setText(String.format(Locale.US, "%d", info.WalkieCode2));

            vPercentWalkie1.setText(String.format(Locale.US, "%d", info.getPercentWalkieID1() == 0 ? 100 : info.getPercentWalkieID1()));
            vPercentWalkie2.setText(String.format(Locale.US, "%d", info.getPercentWalkieID2() == 0 ? 100 : info.getPercentWalkieID2()));
            actvTaiXeID1.setText(String.format(Locale.US, "%d", info.ForkliftCode1));
            etPercentTaiXe1.setText(String.format(Locale.US, "%d", info.getPercentFD1() == 0 ? 100 : info.getPercentFD1()));
            vDescription.setText(info.getRemark());

            dataListener.onUpdateButton(info.getOrderStatus());

            /*time*/

            try {
//                1900-01-01T00:00:00
                String vn_date_format = "dd-MM-yyyy";
                SimpleDateFormat newDateFormat = new SimpleDateFormat(vn_date_format, Locale.getDefault());
                String vn_time_format = "HH:mm";
                SimpleDateFormat newTimeFormat = new SimpleDateFormat(vn_time_format, Locale.getDefault());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql, Locale.getDefault());
                Date date = simpleDateFormat.parse(info.getStartTime());
                Calendar calendar = Calendar.getInstance();
                currentCalendar = Calendar.getInstance();
                calendar.setTime(date);

                if (isUpdate(calendar)) {
                    tvStartDate.setText(newDateFormat.format(calendar.getTime()));
                    tvStartTime.setText(newTimeFormat.format(calendar.getTime()));
                    startDate = String.format(Locale.getDefault(), date_format_sql, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    tvStartDate.setText(newDateFormat.format(currentCalendar.getTime()));
                    tvStartTime.setText(newTimeFormat.format(currentCalendar.getTime()));
                    startDate = String.format(Locale.getDefault(), date_format_sql, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));
                }

                date = simpleDateFormat.parse(info.getEndTime());
                calendar.setTime(date);

                if (isUpdate(calendar)) {
                    tvEndDate.setText(newDateFormat.format(calendar.getTime()));
                    tvEndTime.setText(newTimeFormat.format(calendar.getTime()));
                    endDate = String.format(Locale.getDefault(), date_format_sql, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    tvEndDate.setText(newDateFormat.format(currentCalendar.getTime()));
                    tvEndTime.setText(newTimeFormat.format(currentCalendar.getTime()));
                    endDate = String.format(Locale.getDefault(), date_format_sql, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));
                }
                startTime = tvStartTime.getText().toString() + ":00";
                endTime = tvEndTime.getText().toString() + ":00";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dismissDialog(dialog);
        }
    }

    private void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
