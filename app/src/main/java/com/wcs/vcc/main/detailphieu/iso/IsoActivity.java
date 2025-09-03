package com.wcs.vcc.main.detailphieu.iso;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.EmployeeWorkingISOUpdateParams;
import com.wcs.vcc.api.EmployeeWorkingParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.wcs.databinding.ActivityIsoBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.detailphieu.worker.WorkerInfo;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class IsoActivity extends ShowHomeButtonActivity {

    @BindView(R.id.tv_start_date)
    TextView tvStartDate;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;

    @BindView(R.id.et_iso_nhiet_do)
    EditText vNhietDo;

    @BindView(R.id.et_iso_temp_may_lanh)
    EditText etTempMayLanh;

    @BindView(R.id.et_iso_trucker)
    EditText vSoXe;

    @BindView(R.id.et_iso_seal)
    EditText vSeal;

    @BindView(R.id.et_iso_cua)
    EditText vCuaNhap;

    @BindView(R.id.et_iso_thermometer)
    EditText etTemperatureThermometer;

    @BindView(R.id.cb_iso_co_khoa)
    CheckBox cbKhoa;

    @BindView(R.id.cb_iso_floor_dirty)
    CheckBox cbDirty;

    @BindView(R.id.cb_iso_smell)
    CheckBox cbSmell;

    @BindView(R.id.cb_iso_co_nhiet_ke)
    CheckBox cbCoNhietKe;

    @BindView(R.id.cb_iso_xe_hu_mop)
    CheckBox cbXeHuMop;

    @BindView(R.id.cb_iso_electricity)
    CheckBox cbElectricity;

    private ActivityIsoBinding binding;
    private View root;
    private String username;
    private String androidID;
    private String date_format_sql = "%d-%02d-%02d";
    private String time_format_sql = "%02d:%02d:00";
    private String startDate = "1900-01-01", startTime = "00:00:00";
    private String date_time_format_sql = "yyyy-MM-dd'T'HH:mm:ss";

    private Calendar currentCalendar;
    private String orderNumber;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iso);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_iso);
        root = binding.getRoot();
        ButterKnife.bind(this);

        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);
        androidID = Utilities.getAndroidID(getApplicationContext());
        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        currentCalendar = Calendar.getInstance();

        getEmployeeWorking();
    }

    @OnCheckedChanged(R.id.cb_iso_co_nhiet_ke)
    public void onCbThermometerChange(boolean checked) {
        binding.etIsoThermometer.setFocusable(checked);
        binding.etIsoThermometer.setFocusableInTouchMode(checked);
    }


    @OnClick(R.id.tv_start_date)
    public void startDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                startTime = String.format(Locale.getDefault(), time_format_sql, hourOfDay, minute);
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            String truckNo = vSoXe.getText().toString().trim();
            if (truckNo.length() <= 0) {
                Snackbar.make(root, "Bạn phải nhập Số xe", Snackbar.LENGTH_SHORT).show();
                vSoXe.requestFocus();
                return true;
            }
            String strTemperatureItem = vNhietDo.getText().toString();
            if (strTemperatureItem.length() <= 0 || Integer.parseInt(strTemperatureItem) < -30 || Integer.parseInt(strTemperatureItem) > 50) {
                Snackbar.make(root, "Bạn phải nhập Nhiệt độ trong khoảng từ -30℃ đến +50℃", Snackbar.LENGTH_SHORT).show();
                vNhietDo.requestFocus();
                return true;
            }
            String dockNo = vCuaNhap.getText().toString().trim();
            if (dockNo.length() <= 0) {
                Snackbar.make(root, "Bạn phải nhập Cửa nhập", Snackbar.LENGTH_SHORT).show();
                vCuaNhap.requestFocus();
                return true;
            }


            String temperatureAirCondition = etTempMayLanh.getText().toString().trim();
            String seal = vSeal.getText().toString().trim();


            EmployeeWorkingISOUpdateParams params = new EmployeeWorkingISOUpdateParams(username, androidID);
            params.OrderNumber = orderNumber;
            params.StartTime = startDate + "T" + startTime;
            params.TruckNo = truckNo;
            params.SealNo = seal;
            params.Temperature = strTemperatureItem;
            params.SetupTemperature = temperatureAirCondition;
            params.DockNumber = dockNo;
            params.HasLocker = cbKhoa.isChecked();
            params.Electricity = cbElectricity.isChecked();
            params.HasThremometer = cbCoNhietKe.isChecked();
            params.HasThremometerTemp = etTemperatureThermometer.getText().toString().trim();
            params.TruckContAfterDamaged = cbXeHuMop.isChecked();
            params.TruckContDirty = cbDirty.isChecked();
            params.TruckContSmell = cbSmell.isChecked();
            params.StoreID = storeId;
            employeeWorkingISOUpdate(params);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void employeeWorkingISOUpdate(EmployeeWorkingISOUpdateParams params) {

        MyRetrofit.initRequest(this).employeeWorkingISOUpdate(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final String body = response.body();
                    showMessage(body, new IShowMessage() {
                        @Override
                        public void callback(boolean isShow) {
                            if (!isShow) {
                                finish();
                                Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getEmployeeWorking() {
        EmployeeWorkingParameter params = new EmployeeWorkingParameter(orderNumber, username);
        MyRetrofit.initRequest(this).getEmployeeWorking(params).enqueue(new Callback<List<WorkerInfo>>() {
            @Override
            public void onResponse(Response<List<WorkerInfo>> response, Retrofit retrofit) {
                List<WorkerInfo> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    updateUI(body.get(0));
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void updateUI(WorkerInfo info) {

        vNhietDo.setText(info.getTemperature().equals("") ? "0" : info.getTemperature());

        etTempMayLanh.setText(info.getSetupTemperature().equals("") ? "0" : info.getSetupTemperature());

        vSoXe.setText(info.getTruckNo().equals("") ? "0" : info.getTruckNo());

        vSeal.setText(info.getSealNo().equals("") ? "0" : info.getSealNo());

        vCuaNhap.setText(info.getDockNumber().equals("") ? "0" : info.getDockNumber());

        etTemperatureThermometer.setText(info.HasThremometerTemp.equals("") ? "0" : info.getDockNumber());

        cbKhoa.setChecked(info.isHasLocker());

        cbCoNhietKe.setChecked(info.isHasThremometer());

        cbXeHuMop.setChecked(info.isTruckContAfterDamaged());

        cbElectricity.setChecked(info.Electricity);

        cbSmell.setChecked(info.TruckContSmell);
        cbDirty.setChecked(info.TruckContDirty);

        try {
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

            startTime = tvStartTime.getText().toString() + ":00";
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
}
