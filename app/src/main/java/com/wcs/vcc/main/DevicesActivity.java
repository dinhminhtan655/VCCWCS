package com.wcs.vcc.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.scanhang.model.PrinterDevices;
import com.wcs.vcc.main.vo.DevicesAdapter;
import com.wcs.vcc.preferences.SaveMacHelper;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesActivity extends EmdkActivity {

    @BindView(R.id.btnRing)
    Button btnRing;

    @BindView(R.id.btnPrinter)
    Button btnPrinter;

    @BindView(R.id.tvNameChosen)
    TextView tvNameChosen;

    @BindView(R.id.lvDevices)
    ListView lvDevices;

    private View.OnClickListener tryAgain;

    String strTypeDevices;

    List<PrinterDevices> list;

    DevicesAdapter adapter;

    private ProgressDialog dialog;

    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String bluetoothName = "ZEBRA_DEMO_BLUETOOTH_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        ButterKnife.bind(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        String name = settings.getString(bluetoothName,"");
        tvNameChosen.setText(name);


        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strTypeDevices.equals("Ring")) {
                    getDevice(strTypeDevices);
                } else {
                    getDevice(strTypeDevices);
                }
            }
        };

        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DevicesActivity.this, list.get(i).mac, Toast.LENGTH_SHORT).show();
                getAndSaveSettings(list.get(i).mac,list.get(i).name);
                tvNameChosen.setText(list.get(i).name);
            }
        });

    }


    public void getDevice(String strTypeDevices) {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(DevicesActivity.this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Type", strTypeDevices);

        MyRetrofit2.initRequest2().getPrinterDevices(jsonObject).enqueue(new Callback<List<PrinterDevices>>() {
            @Override
            public void onResponse(Call<List<PrinterDevices>> call, Response<List<PrinterDevices>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list = response.body();
                    adapter = new DevicesAdapter(DevicesActivity.this, R.layout.item_devices, list);
                    lvDevices.setAdapter(adapter);
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Call<List<PrinterDevices>> call, Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(DevicesActivity.this, t, TAG, snackBarView, tryAgain);
            }
        });
    }

    @OnClick(R.id.btnRing)
    public void ringList() {
        strTypeDevices = "Ring";
        getDevice(strTypeDevices);
    }

    @OnClick(R.id.btnPrinter)
    public void printerList() {
        strTypeDevices = "Printer";
        getDevice(strTypeDevices);
    }

//    private Connection getZebraPrinterConn() {
//        return new BluetoothConnection(getMacAddressFieldText());
//    }

    private void getAndSaveSettings(String strMac,String strName) {
        SaveMacHelper.saveBluetoothAddress(DevicesActivity.this, strMac);
        SaveMacHelper.saveBluetoothName(DevicesActivity.this, strName);
    }

}