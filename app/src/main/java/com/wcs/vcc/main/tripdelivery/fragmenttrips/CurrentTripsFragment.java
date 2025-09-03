package com.wcs.vcc.main.tripdelivery.fragmenttrips;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.TripDeliveryParameter;
import com.wcs.vcc.main.tripdelivery.TripDelivery;
import com.wcs.vcc.main.tripdelivery.TripDeliveryAdapter;
import com.wcs.vcc.main.tripdelivery.TripItemListener;
import com.wcs.vcc.main.tripdelivery.orderlist.DeliveryOrderListActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTripsFragment extends Fragment {


    public CurrentTripsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    @BindView(R.id.rv_trip_delivery)
    RecyclerView rv;

    Unbinder unbinder;

    private String username;
    private int storeId;
    private Calendar calendar;
    private String reportDate;
    private TripDeliveryAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_current_trips, container, false);

        unbinder = ButterKnife.bind(this,view);


        username = LoginPref.getUsername(getActivity());
        storeId = LoginPref.getStoreId(getActivity());

//        setUpScan();

        adapter = new TripDeliveryAdapter(new TripItemListener() {
            @Override
            public void onClick(TripDelivery item, int position) {
                Intent intent = new Intent(getActivity(), DeliveryOrderListActivity.class);
                intent.putExtra("TRIP_ID", item.TripID.toString());
                startActivity(intent);
            }

        });
        rv.setAdapter(adapter);

        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void tripDelivery() {
        progressDialog = Utilities.getProgressDialog(getActivity(), "Loading...");
        progressDialog.show();

        TripDeliveryParameter params = new TripDeliveryParameter(username, reportDate.split("T")[0], storeId);
        MyRetrofit.initRequest(getActivity()).tripDelivery(params).enqueue(new Callback<List<TripDelivery>>() {
            @Override
            public void onResponse(Response<List<TripDelivery>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

//    @Override
//    public void onData(String data) {
//        super.onData(data);
//
//        if (data.contains("do") || data.contains("DO")) {
//
//            Intent intent = new Intent(getActivity(), DeliveryDetailActivity.class);
//            intent.putExtra("ORDER_NUMBER", data);
//            startActivity(intent);
//        }
//    }

    @OnClick(R.id.btChooceDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                tripDelivery();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();
    }

}
