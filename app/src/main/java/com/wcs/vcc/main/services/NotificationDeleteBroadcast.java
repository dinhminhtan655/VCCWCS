package com.wcs.vcc.main.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wcs.vcc.utilities.Const;

/**
 * Created by tranxuanloc on 3/5/2016.
 */
public class NotificationDeleteBroadcast extends BroadcastReceiver {

    public static final String TAG = NotificationDeleteBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        if (id != -1 && Const.arrayListIDNotify.contains(id)) {
            Const.arrayListIDNotify.remove(Const.arrayListIDNotify.indexOf(id));
        }
        Log.e(TAG, "onReceive: " + id);
    }
}
