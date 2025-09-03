package com.wcs.vcc.main.vo;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.atalay.bluetoothhelper.Model.BluetoothCallback;
import com.atalay.bluetoothhelper.Provider.BluetoothProvider;
import com.wcs.vcc.preferences.LoginPref;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.util.Iterator;
import java.util.Set;

import static com.wcs.vcc.main.RingScanActivity.mBTService;

public class PrinterZebraSupport implements BluetoothCallback{

    private static BluetoothProvider bluetoothProvider;

    public static Connection printerConnection;
    private static ZebraPrinter printer;


    public static void initProvider(Activity activity, BluetoothCallback callback) {
        bluetoothProvider = new BluetoothProvider(activity, callback);
//        tvName.setText(bluetoothProvider.getDeviceAddress());

//        mBTService.stop();
        Set<BluetoothDevice> set = mBTService.getPairedDevices();
        if (set.size() > 0) {
            Iterator<BluetoothDevice> it = set.iterator();
            while (it.hasNext()) {
                BluetoothDevice device = it.next();
//                if(device.getName() != null && device.getName().indexOf("ABA" + LoginPref.getUsername(RingScanActivity.this).toUpperCase())>=0){
                if((device.getName() != null && device.getName().indexOf( LoginPref.getRingDeviceName(activity).toUpperCase())>= 0)
                ){
                    printerConnection = new BluetoothConnection(device.getAddress());

                    try {
                        if(printerConnection.isConnected()==false){
                            printerConnection.open();
                        }
                        printer = ZebraPrinterFactory.getInstance(printerConnection);
                    } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {

                    }
                }
            }
        }
    }
    public static Connection getConnect(Activity activity) {
        Set<BluetoothDevice> set = mBTService.getPairedDevices();
        if (set.size() > 0) {
            Iterator<BluetoothDevice> it = set.iterator();
            while (it.hasNext()) {
                BluetoothDevice device = it.next();
                if((device.getName() != null && device.getName().indexOf(LoginPref.getPrinter(activity))>= 0)
                ){
                    printerConnection = new BluetoothConnection(device.getAddress());
                }
            }
        }
        return printerConnection;
    }


    @Override
    public void onBegin() {

    }

    @Override
    public void onErrorImportant(String s) {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onSuccessful() {

    }
}
