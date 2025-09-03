package com.wcs.vcc.main.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.wcs.wcs.BuildConfig;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NotificationParameter;
import com.wcs.vcc.main.MainActivity;
import com.wcs.vcc.main.VersionInfo;
import com.wcs.vcc.main.capnhatphienban.CapNhatUngDungActivity;
import com.wcs.vcc.main.containerandtruckinfor.ContainerAndTruckInfoActivity;
import com.wcs.vcc.main.crm.add.AddCRMActivity;
import com.wcs.vcc.main.phieucuatoi.PhieuCuaToiActivity;
import com.wcs.vcc.main.technical.assign.AssignWorkActivity;
import com.wcs.vcc.main.vesinhantoan.QHSEActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;

import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tranxuanloc on 3/3/2016.
 */
public class NotificationServices extends IntentService {
    public static final long INTERVAL_MILLIS = 10 * 60 * 1000;
    private final String TAG = NotificationServices.class.getSimpleName();

    public NotificationServices() {
        super(NotificationServices.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        boolean twelve = calendar.get(Calendar.HOUR_OF_DAY) - Const.timeSchedule >= 12;
        if (twelve) {
            getVersionNo(this);
            Const.timeSchedule = calendar.get(Calendar.HOUR_OF_DAY);
        }
        final String userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        Intent intentSelf = new Intent(this, this.getClass());
        PendingIntent pendingIntentSelf = PendingIntent.getService(this, 1, intentSelf, PendingIntent.FLAG_MUTABLE);
        if (Const.alarmManager == null)
            Const.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (!userName.equalsIgnoreCase("-1")) {
            getUserWork(NotificationServices.this, userName);
        } else {
            Const.alarmManager.cancel(pendingIntentSelf);
            return;
        }
        Const.alarmManager.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + INTERVAL_MILLIS, INTERVAL_MILLIS, pendingIntentSelf);
        Log.e(TAG, "onHandleIntent: Start service");

    }

    private void getUserWork(final Context context, String userName) {
        MyRetrofit.initRequest(this).getNotification(new NotificationParameter(userName)).enqueue(new Callback<List<NotificationInfo>>() {
            @Override
            public void onResponse(Response<List<NotificationInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    int idNotify = 0;
                    for (NotificationInfo info : response.body()) {
                        Intent notificationIntent;
                        if (info.getType().equalsIgnoreCase("QH")) {
                            idNotify = 10;
                            notificationIntent = new Intent(context, AssignWorkActivity.class);
                        } else if (info.getType().equalsIgnoreCase("QHSE")) {
                            idNotify = 11;
                            notificationIntent = new Intent(context, QHSEActivity.class);
                        } else if (info.getType().equalsIgnoreCase("DO")
                                || info.getType().equalsIgnoreCase("RO")
                                || info.getType().equalsIgnoreCase("DD")
                                || info.getType().equalsIgnoreCase("RD")) {
                            idNotify = 12;
                            notificationIntent = new Intent(context, PhieuCuaToiActivity.class);
                        } else if (info.getType().equalsIgnoreCase("CO")) {
                            idNotify = 13;
                            notificationIntent = new Intent(context, ContainerAndTruckInfoActivity.class);
                        } else if (info.getType().equalsIgnoreCase("TR")) {
                            idNotify = 14;
                            notificationIntent = new Intent(context, ContainerAndTruckInfoActivity.class);
                        } else if (info.getType().equalsIgnoreCase("MT")) {
                            idNotify = 15;
                            notificationIntent = new Intent(context, AddCRMActivity.class);
                            int meetingId = -1;
                            try {
                                meetingId = Integer.parseInt(info.getOrderNumber().split("-")[1]);
                            } catch (NumberFormatException ignored) {

                            }
                            notificationIntent.putExtra("ID", meetingId);
                            notificationIntent.putExtra("CONFIRM", true);
                            notificationIntent.putExtra(CalendarContract.Calendars._ID, -1);
                        } else {
                            idNotify = 16;
                            notificationIntent = new Intent(context, MainActivity.class);
                        }
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, idNotify, notificationIntent, PendingIntent.FLAG_MUTABLE);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notification.Builder builder = new Notification.Builder(NotificationServices.this)
                                .setContentTitle(info.getSubject())
                                .setContentText(info.getContent())
                                .setSubText(info.getDate())
                                .setSmallIcon(getSmallIcon())
                                .setContentIntent(contentIntent)
                                .setSound(defaultSoundUri)
                                .setAutoCancel(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        Notification notification = builder.build();
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(idNotify, notification);
                        Const.arrayListIDNotify.add(idNotify);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getVersionNo(final Context context) {
        MyRetrofit.initRequest(this).getWHCVersion().enqueue(new Callback<List<VersionInfo>>() {
            @Override
            public void onResponse(Response<List<VersionInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && !BuildConfig.VERSION_NAME.equalsIgnoreCase(response.body().get(0).getVersionNo())) {
                    VersionInfo info = response.body().get(0);
                    Intent notificationIntent = new Intent(NotificationServices.this, CapNhatUngDungActivity.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent contentIntent = PendingIntent.getActivity(NotificationServices.this, 9999, notificationIntent, PendingIntent.FLAG_MUTABLE);
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification.Builder builder = new Notification.Builder(NotificationServices.this)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("Đã có phiên bản mới " + info.getVersionNo())
                            .setSubText(info.getVersionDate())
                            .setSmallIcon(getSmallIcon())
                            .setContentIntent(contentIntent)
                            .setSound(defaultSoundUri)
                            .setAutoCancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder.setColor(ContextCompat.getColor(NotificationServices.this, R.color.colorPrimary));
                    Notification notification = builder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(9999, notification);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private int getSmallIcon() {
        return R.mipmap.ic_launcher;

    }

}
