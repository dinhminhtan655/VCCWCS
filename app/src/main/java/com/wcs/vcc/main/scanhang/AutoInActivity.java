package com.wcs.vcc.main.scanhang;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wcs.wcs.R;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.utilities.UIHelper;
import com.wcs.vcc.utilities.Utilities;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoInActivity extends RingScanActivity {


    String strMac, strCustomer = "", reportDate2, region, nhom, userName;
    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String bluetoothName = "ZEBRA_DEMO_BLUETOOTH_NAME";

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private UIHelper helper = new UIHelper(this);

    @BindView(R.id.imgBarcode)
    ImageView imgBarcode;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_in);

        ButterKnife.bind(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        setUpScan();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }


        Bundle b = getIntent().getExtras();

        if (b != null) {
            strCustomer = b.getString("customer");
            reportDate2 = b.getString("time");
            region = b.getString("region");
            nhom = b.getString("nhom");
            userName = b.getString("username");
        }

        strMac = settings.getString(bluetoothAddressKey, "");

    }

    private Connection getZebraPrinterConn() {
        return new BluetoothConnection(strMac);
    }

    private void printPhotoFromExternal(final Bitmap bitmap, List<ScanPalletCode> l) {

        /*
        MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().equals('"' + "Thành công" + '"')) {
                        l.clear();
                        Utilities.thongBaoDialog(AutoInActivity.this, "Thêm khay thành công");
                        if (strMac != null && !strMac.equals("")) {
                            new Thread(new Runnable() {
                                public void run() {

                                    try {

                                        Looper.prepare();
                                        Toast.makeText(AutoInActivity.this, "Sending image to printer", Toast.LENGTH_SHORT).show();
                                        Connection connection = getZebraPrinterConn();
                                        connection.open();
                                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                                        printer.printImage(ZebraImageFactory.getImage(bitmap), 0, 0, (int) (bitmap.getWidth() / 1.2f), bitmap.getHeight(), false);
                                        connection.close();



                                    } catch (ConnectionException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (ZebraPrinterLanguageUnknownException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        bitmap.recycle();
                                        helper.dismissLoadingDialog();
                                        Looper.myLooper().quit();
                                    }

                                }
                            }).start();
                        }

                    } else {
                        Utilities.thongBaoDialog(AutoInActivity.this, "Hệ thống chưa lưu được khay này.Vui lòng in lại");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utilities.thongBaoDialog(AutoInActivity.this, "Kiểm tra kết nối Internet");
            }
        });
*/

    }


    @Override
    public void onData(String data) {
        super.onData(data);
        if (data.substring(0, 2).equals("PA")) {

            String totalPaBoxWhitoutCode = null, totalPaBox = null;
            int iSoThung = 0;

            if (strCustomer.equals("BHX")) {
                Pattern pattern = Pattern.compile("[P][A]\\d{1,4}[-][0-9]{1,3}[-]\\d{1}");
                if (pattern.matcher(data).matches()) {

                    String[] arrayPaBox = data.split("-");
//                    String strPaBox = arrayPaBox[1];
                    iSoThung = Integer.parseInt(arrayPaBox[1]) + 1;

                    if (arrayPaBox[2].equals("1")) {
                        totalPaBoxWhitoutCode = arrayPaBox[0].replace("PA", "") + "-" + iSoThung + "\n(Thịt)" + "\n"+ reportDate2;
                        totalPaBox = arrayPaBox[0] + "-" + iSoThung + "-" + arrayPaBox[2];
                    } else if (arrayPaBox[2].equals("2")) {
                        totalPaBoxWhitoutCode = arrayPaBox[0].replace("PA", "") + "-" + iSoThung + "\n(Mát)" + "\n"+ reportDate2;
                        totalPaBox = arrayPaBox[0] + "-" + iSoThung + "-" + arrayPaBox[2];
                    } else if (arrayPaBox[2].equals("3")) {
                        totalPaBoxWhitoutCode = arrayPaBox[0].replace("PA", "") + "-" + iSoThung + "\n(Đông)" + "\n"+ reportDate2;
                        totalPaBox = arrayPaBox[0] + "-" + iSoThung + "-" + arrayPaBox[2];
                    }


                } else {
                    Utilities.speakingSomeThingslow("Không đúng định dạng", AutoInActivity.this);
                }


                Bitmap bm = Bitmap.createBitmap(540, 380, Bitmap.Config.ARGB_8888);
                /*create a canvas object*/
                Canvas cv = new Canvas(bm);
                /*create a paint object*/
                Paint paint = new Paint();


                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                Writer codeWriter;
                codeWriter = new Code39Writer();
                BitMatrix byteMatrix = null;

                try {
                    byteMatrix = codeWriter.encode(totalPaBox, BarcodeFormat.CODE_39, 540, 150, hintMap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                int width = byteMatrix.getWidth();
                int height = byteMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }

                cv.drawBitmap(bitmap, 0f, 180f, paint);

                Bitmap bitmap1 = Bitmap.createBitmap(540, 230, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap1);
                canvas.drawBitmap(bm, 0, 0, null);
                Paint paint1 = new Paint();

                Paint.FontMetrics fm = new Paint.FontMetrics();
                paint1.setColor(Color.WHITE);
                paint1.getFontMetrics(fm);
                paint1.setTextSize(80);
                paint1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                paint1.setTextAlign(Paint.Align.CENTER);
                int margin = 14;

                int xPos = (cv.getWidth() / 2);
                int yPos = (int) (((cv.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
//                    float w = paint.measureText(totalPaBox);
                //        cv.drawRect(0, (int) (yPos / 0.50f), margin, 0, paint1);
                cv.drawRect(0, 0, 540, 200, paint1);
                paint1.setColor(Color.BLACK);
                int y = yPos + (margin * 3);
                int count = 0;
                if (strCustomer.equals("BHX")) {
                    for (String line : totalPaBoxWhitoutCode.split("\n")) {
                        if (count == 2){
                            paint1.setTextSize(20);
                            y += paint1.descent() - paint1.ascent();
                            cv.drawText(line, xPos, 20, paint1);
                        }else {
                            cv.drawText(line, xPos, y, paint1);
                            y += paint1.descent() - paint1.ascent();
                        }

                        count++;
                    }
                } else {
                    cv.drawText(totalPaBoxWhitoutCode, xPos, y, paint1);
                }

                List<ScanPalletCode> l = new ArrayList<>();
                l.add(new ScanPalletCode(Integer.parseInt(totalPaBox.split("-")[0].replace("PA", "")), totalPaBox.trim(), reportDate2, userName, true, Integer.parseInt(nhom), strCustomer, region, totalPaBox.split("-")[2]));


                imgBarcode.setImageBitmap(bm);

                printPhotoFromExternal(bm, l);

            }
        }else {
            Utilities.speakingSomeThingslow("Không đúng định dạng", AutoInActivity.this);
        }
    }
}