package com.wcs.vcc.main.lichlamviec;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.MyCalendarParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.WorkingSchedulesParameter;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CalendarViewActivity extends BaseActivity {
    private final String TAG = CalendarViewActivity.class.getSimpleName();
    private ProgressDialog dialog;
    private LinkedHashMap<String, List<WorkingSchedulesEmployeePlanInfo>> employeePlan = new LinkedHashMap<>();
    private List<String> employeePlanGroup = new LinkedList<>();
    private ArrayList<WorkingSchedulesInfo> listJob = new ArrayList<>();
    private CaldroidFragment caldroidFragment;
    private String reportDate;
    private Date tDate;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        userName = LoginPref.getUsername(getApplicationContext());
        caldroidFragment = new CaldroidFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            caldroidFragment.setArguments(args);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendar, caldroidFragment).commit();
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                if (tDate != null)
                    caldroidFragment.clearSelectedDate(tDate);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(date.getTime());
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                getWorkingSchedules(findViewById(R.id.view));
                tDate = date;
            }

            @Override
            public void onChangeMonth(int month, int year) {
                getJobInMonth(findViewById(R.id.view), month, year);
            }

            @Override
            public void onCaldroidViewCreated() {

            }
        });

    }

    private void getJobInMonth(final View view, int month, int year) {
        if (!WifiHelper.isConnected(this))
            return;
        MyRetrofit.initRequest(this).getMyCalendar(new MyCalendarParameter(userName, month, year)).enqueue(new Callback<List<MyCalendarInfo>>() {
            @Override
            public void onResponse(Response<List<MyCalendarInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    setCustomResourceForDates(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(CalendarViewActivity.this, t, TAG, view);
            }
        });
    }

    private void setCustomResourceForDates(List<MyCalendarInfo> listDates) {
        if (caldroidFragment != null) {
            ColorDrawable yellow = new ColorDrawable(Color.argb(255, 0xFF, 0xBB, 0x00));
            for (MyCalendarInfo info : listDates) {
                Date date = new Date(Utilities.getMillisecondFromDate(info.getDeadline()));
                caldroidFragment.setBackgroundDrawableForDate(yellow, date);
            }
            caldroidFragment.refreshView();
        }
    }

    private void getWorkingSchedules(final View view) {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
            return;
        }
        WorkingSchedulesParameter parameter = new WorkingSchedulesParameter(reportDate, LoginPref.getUsername(this));
        MyRetrofit.initRequest(this).getWorkingSchedules(parameter).enqueue(new Callback<List<WorkingSchedulesInfo>>() {
            @Override
            public void onResponse(Response<List<WorkingSchedulesInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    listJob.clear();
                    listJob.addAll(response.body());
                    getWorkingSchedulesEmployeePlan(view);
                } else
                    dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(CalendarViewActivity.this, t, TAG, view);
            }
        });
    }

    private void getWorkingSchedulesEmployeePlan(final View view) {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
            return;
        }
        WorkingSchedulesParameter parameter = new WorkingSchedulesParameter(reportDate, LoginPref.getUsername(this));
        MyRetrofit.initRequest(this).getWorkingSchedulesEmployeePlan(parameter).enqueue(new Callback<List<WorkingSchedulesEmployeePlanInfo>>() {
            @Override
            public void onResponse(Response<List<WorkingSchedulesEmployeePlanInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    employeePlanGroup.clear();
                    employeePlan.clear();
                    for (WorkingSchedulesEmployeePlanInfo info : response.body()) {
                        String key = info.VietnamPosition;
                        if (!employeePlan.containsKey(key)) {
                            List<WorkingSchedulesEmployeePlanInfo> item = new LinkedList<>();
                            item.add(info);
                            employeePlan.put(key, item);
                        } else employeePlan.get(key).add(info);
                    }
                    employeePlanGroup.addAll(employeePlan.keySet());

                    showAlertDialog();
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(CalendarViewActivity.this, t, TAG, view);
                dismissDialog(dialog);

            }
        });
    }

    private void showAlertDialog() {
        ListView listView = new ListView(this);
        ExpandableListView workingEmployeePlan = new ExpandableListView(this);
        LinearLayout viewDialog = new LinearLayout(this);
        viewDialog.setOrientation(LinearLayout.VERTICAL);
        viewDialog.addView(listView);
        viewDialog.addView(workingEmployeePlan);
        workingEmployeePlan.setGroupIndicator(null);
        WorkingSchedulesAdapter adapter = new WorkingSchedulesAdapter(this, listJob);
        WorkingSchedulesEmployeePlanAdapter planAdapter = new WorkingSchedulesEmployeePlanAdapter(employeePlan, employeePlanGroup);
        listView.setAdapter(adapter);
        workingEmployeePlan.setAdapter(planAdapter);
        int size = employeePlanGroup.size();
        for (int i = 0; i < size; ++i) {
            workingEmployeePlan.expandGroup(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewDialog);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        else if (itemId == R.id.action_today) {
            caldroidFragment.moveToDate(Calendar.getInstance().getTime());
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}
