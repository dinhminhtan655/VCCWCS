package com.wcs.vcc.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.wcs.vcc.api.ChangeStoreParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.wcs.R;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DialogSpinnerChangeWH extends Dialog implements View.OnClickListener {

    private String userName;
    private Context context;

    public DialogSpinnerChangeWH(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_wh);

        userName = LoginPref.getUsername(context);
        Spinner spinnerPlace = findViewById(R.id.spinWH);

        spinnerPlace.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"Van Ho Valley 1 - Kho lạnh", "Van Ho Valley 2 - Kho khô", "Van Ho Valley 3", "Kho Test"}));
        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.storeId = position + 1;
                updateStore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPlace.setSelection(MainActivity.storeId - 1);

    }

    @Override
    public void onClick(View v) {

    }

    private void updateStore() {
        MyRetrofit.initRequest(context).updateStore(new ChangeStoreParameter(userName, MainActivity.storeId)).enqueue(new Callback<List<UpdateStoreResult>>() {
            @Override
            public void onResponse(Response<List<UpdateStoreResult>> response, Retrofit retrofit) {
                List<UpdateStoreResult> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    LoginPref.putStoreId(context, MainActivity.storeId);
                    UpdateStoreResult info = body.get(0);
                    LoginPref.putWarehouseId(context, info.getWarehousId());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

}
