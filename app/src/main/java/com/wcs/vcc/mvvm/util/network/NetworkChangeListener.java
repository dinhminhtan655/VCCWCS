package com.wcs.vcc.mvvm.util.network;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.wcs.wcs.R;

public class NetworkChangeListener extends BroadcastReceiver {

    private AlertDialog.Builder connectivityDialogBuilder;
    private AlertDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Common.isConnectedToInternet(context)) {
            connectivityDialogBuilder = new AlertDialog.Builder(context);
            View layoutConnectivityDialog = LayoutInflater.from(context).inflate(R.layout.dialog_connectivity_state, null);
            dialog = connectivityDialogBuilder.setView(layoutConnectivityDialog).create();

            AppCompatButton btnRetry = layoutConnectivityDialog.findViewById(R.id.btnDialogConnectivity);

            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        } else {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }


    }


}
