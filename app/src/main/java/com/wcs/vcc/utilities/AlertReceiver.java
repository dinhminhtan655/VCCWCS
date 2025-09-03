package com.wcs.vcc.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("huhuhu"," sdjklfsalkd");
//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());


//        LoginPref.resetInfoUserByUser(context);
//        Const.isActivating = false;
//        Const.timePauseActive = 0;

            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage("com.wcs.vhlapp");
            launchIntent.putExtra("some_data", "value");
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(launchIntent);

//        Intent intentReceiver = new Intent(context, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intentReceiver, 0);
//        Const.alarmManager.cancel(pendingIntent);
    }
}
