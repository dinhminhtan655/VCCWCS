package com.wcs.vcc.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.wcs.R;
import com.wcs.vcc.preferences.SettingPref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities extends WifiHelper {

    public static final String CHANNEL_NAME = "VHLAPP";
    public static final String CHANNEL_DESCRIPTION = "Thông báo VHL";
    private static TextToSpeech textToSpeech;


    public static final String folderName = "vhl";

    //THÔNG TIN BARCODE
    public static String PALET_ID_FROM_LABEL_CODE = "PA";
    public static String ID_CARTON_FROM_LABEL_CODE = "ID";
    public static String CUSTOMER_FROM_LABEL_CODE = "CUS";
    public static String PRODUCT_GROUP_FROM_LABEL_CODE = "PRG";

    public static String PATH_SAVE_PICTURE = "/Android/data/com.wcs.vcc/files/Pictures";

    //barcodeInput=PA1ID45CUSBHXPGR1
    //PA1=>PalletID: 1
    //ID45=>ID:45
    //CUSBHX=> CUS:BHX
    //PRG1=>PRG: 1
    public static Map<String, String> GET_INFOR_BARCODE_PALLET(String barcodeInput) {
        Map<String, String> result = new HashMap<>();
        result.put(PALET_ID_FROM_LABEL_CODE, barcodeInput.substring(barcodeInput.indexOf(PALET_ID_FROM_LABEL_CODE), barcodeInput.indexOf(PALET_ID_FROM_LABEL_CODE)));
        result.put(ID_CARTON_FROM_LABEL_CODE, barcodeInput.substring(barcodeInput.indexOf(ID_CARTON_FROM_LABEL_CODE), barcodeInput.indexOf(CUSTOMER_FROM_LABEL_CODE)));
        result.put(CUSTOMER_FROM_LABEL_CODE, barcodeInput.substring(barcodeInput.indexOf(CUSTOMER_FROM_LABEL_CODE), barcodeInput.indexOf(PRODUCT_GROUP_FROM_LABEL_CODE)));
        result.put(PRODUCT_GROUP_FROM_LABEL_CODE, barcodeInput.substring(barcodeInput.indexOf(PRODUCT_GROUP_FROM_LABEL_CODE), barcodeInput.length()));
        return result;
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        return dialog;
    }

    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void showBackIcon(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public static void hideKeyboard(AppCompatActivity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void thongBaoDialog(Context context, String content) {
        android.app.AlertDialog.Builder ba = new android.app.AlertDialog.Builder(context);
        ba.setTitle("Thông báo").setMessage(content);

        Dialog dialog = ba.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }


    /**
     * From "MM/dd/yyyy" to "dd/MM"
     */
    public static String formatDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    /**
     * From "yyyy-MM-dd'T'HH:mm:ss" to "dd/MM/yyyy"
     */
    public static String formatDate_ddMMyyyy(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyy5(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyyy4(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyyy3(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }


    public static String formatDate_yyyyMMdd(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_yyyyMMdd2(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyyy2(String sDate) {
        String dateStr = sDate.replace('-', '/');
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_MMddyyyy4(String sDate) {
        String dateStr = sDate.replace('-', '/');
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    /**
     * From <code>Date</code> to "dd/MM/yyyy"
     */
    public static String formatDate_ddMMyyyy(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return newFormat.format(date);
    }

    /**
     * From <code>long</code> to "dd/MM/yyyy"
     */
    public static String formatDate_ddMMyyyy(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return newFormat.format(calendar.getTime());
    }

    public static String formatDate_yyyyMMdd(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        return newFormat.format(calendar.getTime());
    }

    /**
     * From "yyyy-MM-dd'T'HH:mm:ss" to "HH:mm"
     */
    public static String formatDate_HHmm(String sDate) {
        if (sDate.substring(0, 4).equals("1900"))
            return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyy2(String sDate) {
        if (sDate.substring(0, 4).equals("1900"))
            return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }


    public static String formatDate_ddMMyy(String sDate) {
        SimpleDateFormat format;
        if (sDate.length() > 10)
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        else
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01/00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            return sDate;
        }
    }


    public static String formatDate_ddMM(String sDate) {
        SimpleDateFormat format;
        if (sDate.length() > 10)
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        else
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01/00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            return sDate;
        }
    }

    public static String formatDate_ddMMyyHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01/00 00:00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01 00:00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyyyHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static long getMillisecondFromDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String formatDateTime_yyyyMMddHHmmssFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String formatDateTime_MMddyyyyHHmmssFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String formatDateTime_ddMMyyyyFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public static long formatDateTimeToMilisecond(String strDateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = sdf.parse(strDateTime);
        long timeInMillis = date.getTime();
        return timeInMillis;

    }


    public static String formatDateTime_ddMMyyHHmmFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String formatDateTime_yyyyMMddFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String format_ddMMHHmm(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM HH:mm", Locale.US);
        return format.format(calendar.getTime());
    }


    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String getCurrentDateTime1() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(calendar.getTime());
    }

    public static long getMinute(String date) {
        long timeWork;
        Calendar calendar = Calendar.getInstance();
        long timeCurrent = calendar.getTimeInMillis() > 0 ? calendar.getTimeInMillis() : 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            timeWork = format.parse(date).getTime();
            return (timeCurrent - timeWork) / 1000 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean notToday(String date) {
        Calendar ca = Calendar.getInstance();
        int dayToday = ca.get(Calendar.DAY_OF_MONTH);
        ca.setTimeInMillis(getMillisecondFromDate(date));
        return ca.get(Calendar.DAY_OF_MONTH) != dayToday;
    }

    public static String convertMinute(long minute) {
        String result = String.format(Locale.US, "%d phút", minute);
        if (minute > (23 * 60 + 59))
            result = String.format(Locale.US, "%d ngày %d giờ %d phút", minute / 60 / 24, (minute % (60 * 24)) / 60, (minute % (60 * 24)) % 60);
        else if (minute > 59)
            result = String.format(Locale.US, "%d giờ %d phút", minute / 60, minute % 60);
        return result;
    }

    public static String getAndroidID(Context context) {

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void basicDialog(Context context, String message, DialogInterface.OnClickListener pos) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", pos)
                .setNegativeButton("Cancel", null).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEmpty(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            Snackbar.make(view, R.string.this_field_not_empty, Snackbar.LENGTH_LONG).show();
            view.requestFocus();
            return true;
        }
        return false;
    }

    public static String generateUrlImage(Context context, String imageName) {
        String BASE_URL = String.format("http://%s", SettingPref.getInfoNetwork(context)[0]);
        return String.format("%s/File_Attachments/%s", BASE_URL, imageName);
    }

    public static Picasso getPicasso(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
    }

    public static String formatNumber(float number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static void setUnderLine(TextView view, String content) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        view.setText(spannableString);
    }

    public static void showKeyboard(Context context, View editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static String getWiFiMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {

                    String str = Integer.toHexString(b & 0xFF);
                    if (str.length() == 0) {
                        res1.append("00");
                    } else if (str.length() == 1) {
                        res1.append("0");
                    }
                    res1.append(str).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toUpperCase();
            }
        } catch (Exception ignored) {
        }
        return "02:00:00:00:00:00";
    }

    public static HashMap<String, String> barcodeInfo(Context context, String barcode) {
        HashMap<String, String> data = new HashMap<>();
        if (!barcode.contains(".")){
            String type = "";
            boolean isValid = true;
            int barcodeNumber = 0;
            try {
                type = barcode.substring(0, 2);
                barcodeNumber = Integer.parseInt(barcode.substring(2));
            }
//        catch (NumberFormatException ex) {
//            type = barcode.substring(0, 3);
//            barcodeNumber = Integer.parseInt(barcode.substring(3));
//            Log.d("NumberFormatError", ex.toString());
//        }
            catch (IndexOutOfBoundsException ex) {
                Toast.makeText(context, "Chuỗi barcode không hợp lệ.", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (!isValid) {
                return null;
            }

            data.put("type", type);
            data.put("number", "0" + barcodeNumber);
        }
        return data;
    }

    public static UUID getDefaultUUID() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public static UUID getUUIDFromTag(View view) {
        if (view != null) {
            try {
                return UUID.fromString(view.getTag().toString());
            } catch (Exception e) {
                return getDefaultUUID();
            }
        }
        return getDefaultUUID();
    }

    public static int getIntFromEditText(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static double getDoubleFromEditText(EditText view) {
        try {
            return Double.parseDouble(view.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static String normalizeString(String string) {
        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static void speakingSomeThing(String lyric, Context context) {
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(new Locale("vi_VN"));
                    textToSpeech.setSpeechRate(3f);
                    textToSpeech.speak(lyric, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }


    public static void speakingSomeThingslow(String lyric, Context context) {
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(new Locale("vi_VN"));
                    textToSpeech.setSpeechRate(1f);
                    textToSpeech.speak(lyric, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }


    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static String addChar(String str) {
        int i = 9;
        int length = str.length();
        String s = "MS";
        for (int o = 0; o < (i - length); o++) {
            s = s.concat("0");
        }
        return s;
    }


    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    public static boolean isValidDate(String d) {
//        String regex = "^(1[0-2]|0[1-9])/(3[01]"
//                + "|[12][0-9]|0[1-9])/[0-9]{4}$";

        String regex2 = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher((CharSequence) d);
        return matcher.matches();
    }


    public static boolean isValidSoKg(String d) {
//        String regex = "^(1[0-2]|0[1-9])/(3[01]"
//                + "|[12][0-9]|0[1-9])/[0-9]{4}$";

        String regex2 = "^(99|\\d)(\\.\\d{3})?$";
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher((CharSequence) d);
        return matcher.matches();
    }

    public static byte[] convertUsingIOUtils(File file) {
        byte[] fileBytes = null;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            fileBytes = IOUtils.toByteArray(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileBytes;
    }

    public static byte[] asBytes(String s) {
        String tmp;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            tmp = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte) (Integer.parseInt(tmp, 16) & 0xff);
        }
        return b;                                            //return bytes
    }


    public static void createNotificationChannel(String CHANNEL_ID, Context context, int iImportance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = iImportance;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static NotificationCompat.Builder createNotification(Context contextFrom, Class<?> contextTo, String CHANNEL_ID, String strTitle, String strContent, int strUniqueID, int iImportance) {
        createNotificationChannel(CHANNEL_ID, contextFrom,iImportance);
        Intent intent = new Intent(contextFrom, contextTo);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(contextFrom, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contextFrom, CHANNEL_ID)
                .setSmallIcon(R.drawable.vcclogo)
                .setContentTitle(strTitle)
                .setContentText(strContent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(contextFrom);
        notificationManager.notify(strUniqueID, builder.build());

        return builder;
    }


    public static void downloadPictureBitmap(File file, @NonNull Context context, Bitmap bm) throws IOException {
//        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        Integer counter = 0;
//        File file = new File(path, "FitnessGirl" + counter + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        fOut = new FileOutputStream(file);

        Bitmap pictureBitmap = bm; // obtaining the Bitmap
        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut.flush(); // Not really required
        fOut.close(); // do not forget to close the stream

        MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
    }


    public static void saveBitmapToMedia(Context context, Bitmap bitmap) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderName);

        String fileName = "Image-" + UUID.randomUUID().toString() + ".jpg";

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, fileName);

        try {
            Utilities.downloadPictureBitmap(file, context, bitmap);
            Toast.makeText(context, "Đã thêm ảnh vào thư viện", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showSettingsDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static boolean validateSoXe(String strSoXe) {
        Pattern pattern = Pattern.compile("^([1-9]|[1-9][0-9]|100{2})([A-Z0-9]{1,2})([0-9]{4,5})$");
        Matcher matcher = pattern.matcher(strSoXe);
        return matcher.find();
    }

    public static boolean validateSDT(String strSDT) {
        Pattern pattern = Pattern.compile("^[0][78359][0-9]{8}");
        Matcher matcher = pattern.matcher(strSDT);
        return matcher.find();
    }

    public static boolean validateCMNDCCCD(String strCMND) {
        Pattern pattern = Pattern.compile("^\\d{9}(\\d{3})?$");
        Matcher matcher = pattern.matcher(strCMND);
        return matcher.find();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void ScanByCamera(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    public static Dialog DialogFullscreen(DialogFragment fragment, boolean bVisible) {
        Dialog dialog = fragment.getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setCancelable(bVisible);
        }
        return dialog;
    }

    public static String getAppPatch(Context context) {
        File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        if (!dir.exists())
            dir.mkdirs();

        return dir.getPath() + File.separator;
    }

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }


}
