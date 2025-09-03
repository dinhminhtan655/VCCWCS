package com.wcs.vcc.main.soki;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wcs.wcs.R;import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.DeleteSavePackageParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickinglistCreateEDISavePackageHNParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.OrderDetailActivity;
import com.wcs.vcc.main.soki.adapter.ProductKgAdapter;
import com.wcs.vcc.main.soki.model.ProductId;
import com.wcs.vcc.main.soki.model.ProductKg;
import com.wcs.vcc.main.soki.model.RO;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SizeContentSoKiPref;
import com.wcs.vcc.preferences.SpinCusPrefVin;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.UIHelper;
import com.wcs.vcc.utilities.Utilities;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GhiSoKiActivity extends EmdkActivity {
    public static final String ORDER_NUMBER = "order_number";
    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.spinCustomerID)
    SearchableSpinner spinCustomerID;
    @BindView(R.id.spinProductID)
    SearchableSpinner spinProductID;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.edtNgayThangNam)
    EditText edtNgayThangNam;
    @BindView(R.id.edtHSD)
    EditText edtHSD;
    @BindView(R.id.edtSoKg)
    EditText edtSoKg;
    @BindView(R.id.edtSoKg2)
    EditText edtSoKg2;
    @BindView(R.id.edtLotRef)
    EditText edtLotRef;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.btnInTem)
    Button btnInTem;
    @BindView(R.id.btnTaoRO)
    Button btnTaoRO;
    @BindView(R.id.rvGhiSoKi)
    RecyclerView rvGhiSoKi;
    @BindView(R.id.tvTong)
    TextView tvTong;
    @BindView(R.id.tvTongSoKg)
    TextView tvTongSoKg;
    @BindView(R.id.edtLocation)
    EditText edtLocation;
    @BindView(R.id.spinRO)
    SearchableSpinner spinRO;

    List<ComboCustomerResult> customers = new ArrayList<>();
    List<ProductId> products = new ArrayList<>();
    List<RO> ros = new ArrayList<>();
    private ComboCustomerResult cusSelected;
    private ProductId proSelected;
    private RO roSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter;
    ArrayAdapter<ProductId> spinnerProductIdAdapter;
    ArrayAdapter<RO> spinnerROAdapter;
    ProductKgAdapter adapter;
    byte flag;
    int sizeW, sizeH;

    private UIHelper helper = new UIHelper(this);

    Calendar calendar;

    String customerID, productId, cartonWeight, proDate, lotNumber, userName, deviceNumber, shelfDate, productIDUpdate, strKg1, strKg2, strId, strMac, strProductIdOnClick, ReceivingOrderNumber;
    private Calendar calendar2;
    private String reportDate, reportDate2;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_so_ki);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(reportDate);

        calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        edtNgayThangNam.setText(formatter.format(date));
        edtHSD.setText(formatter.format(date));
        userName = LoginPref.getUsername(GhiSoKiActivity.this);
        deviceNumber = Utilities.getAndroidID(getApplicationContext());
//        CustomerID = valueOf(SpinCusIdPref.LoadCusID(GhiSoKiActivity.this));
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        strMac = settings.getString(bluetoothAddressKey, "");
        sizeW = SizeContentSoKiPref.loadSizeWidth(GhiSoKiActivity.this);
        sizeH = SizeContentSoKiPref.loadSizeHeight(GhiSoKiActivity.this);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String strDate = Utilities.formatDate_ddMMyyyy(calendar.getTime());
                edtNgayThangNam.setText(strDate);
                getProductKg();
                if (Utilities.isValidDate(strDate)) {
                    long timemiliHSD = (long) 1000 * 60 * 60 * 24 * Integer.parseInt(shelfDate);
                    if (Integer.parseInt(shelfDate) > 0) {
                        String myDate = strDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long mills = date.getTime();
                        long timemiliHSD2 = timemiliHSD + mills;


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis((long) timemiliHSD2);

                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                        if (mDay < 10) {
                            edtHSD.setText("0" + mDay + "/" + (mMonth + 1) + "/" + mYear);
                            if (mMonth < 10)
                                edtHSD.setText("0" + mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                        } else {
                            if (mMonth < 10)
                                edtHSD.setText(mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                        }
                    }
                }

            }
        };

        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String strDate = Utilities.formatDate_ddMMyyyy(calendar.getTime());
                edtHSD.setText(strDate);
            }
        };


        edtNgayThangNam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                edtNgayThangNam.setText("");
                new DatePickerDialog(GhiSoKiActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });

        edtHSD.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                edtHSD.setText("");
                new DatePickerDialog(GhiSoKiActivity.this, dateSetListener2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });


        edtNgayThangNam.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Utilities.isValidDate(textView.getText().toString())) {
                        String strDate = textView.getText().toString();
                        long timemiliHSD = (long) 1000 * 60 * 60 * 24 * Integer.parseInt(shelfDate);
                        if (Integer.parseInt(shelfDate) > 0) {
                            String myDate = strDate;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = null;
                            try {
                                date = sdf.parse(myDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long mills = date.getTime();
                            long timemiliHSD2 = timemiliHSD + mills;


                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis((long) timemiliHSD2);

                            int mYear = calendar.get(Calendar.YEAR);
                            int mMonth = calendar.get(Calendar.MONTH);
                            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                            if (mDay < 10) {
                                edtHSD.setText("0" + mDay + "/" + (mMonth + 1) + "/" + mYear);
                                if (mMonth < 10)
                                    edtHSD.setText("0" + mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                            } else {
                                if (mMonth < 10)
                                    edtHSD.setText(mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                            }
                            getProductKg();
                        }
                    } else {
                        Toast.makeText(GhiSoKiActivity.this, "Không đúng định dạng", Toast.LENGTH_SHORT).show();
                    }
//                    return true;
                }

                return false;
            }
        });

        edtHSD.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (Utilities.isValidDate(textView.getText().toString())) {


//                        String[] strChar = strDate.split("/");
//                        int iYear = Integer.parseInt(strChar[2]) + 2;
//                        edtHSD.setText(strChar[0]+"/"+strChar[1]+"/"+iYear);
//                        Toast.makeText(GhiSoKiActivity.this, strChar[0]+"/"+strChar[1]+"/"+String.valueOf(iYear), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GhiSoKiActivity.this, "Không đúng định dạng", Toast.LENGTH_SHORT).show();
                    }
//                    return true;
                }

                return false;
            }
        });

        edtSoKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSoKg.getText().toString().length() == 2) {
                    edtSoKg2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getCustomer();


    }


    private void getCustomer() {
        //khoi tao doi tuong jsonObject
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(this));
        //goi API
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    customers = response.body();
                    spineradapter = new ArrayAdapter<ComboCustomerResult>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    spinCustomerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            cusSelected = customers.get(position);
                            customerID = String.valueOf(cusSelected.getCustomerID());
                            SpinCusPrefVin.SaveIntVin(GhiSoKiActivity.this, position);
                            getProductID(cusSelected.getCustomerID());
                            getProductKg();
                            loadRO();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinCustomerID.setAdapter(spineradapter);

                    if (SpinCusPrefVin.LoadInt(GhiSoKiActivity.this) >= customers.size()) {
                        spinCustomerID.setSelection(0);
                    } else {
                        spinCustomerID.setSelection(SpinCusPrefVin.LoadInt(GhiSoKiActivity.this));
                    }
                }
            }


            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GhiSoKiActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }


    private void getProductID(UUID strCustomerID) {
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("CustomerID", String.valueOf(strCustomerID));

        MyRetrofit.initRequest(GhiSoKiActivity.this).loadProductByCustomer(jsonObject).enqueue(new Callback<List<ProductId>>() {
            @Override
            public void onResponse(Response<List<ProductId>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    products = response.body();

                    spinnerProductIdAdapter = new ArrayAdapter<ProductId>(GhiSoKiActivity.this, android.R.layout.simple_spinner_dropdown_item, products);

                    spinProductID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            proSelected = products.get(i);
                            productId = proSelected.ProductID;
                            shelfDate = proSelected.Shelfdate;
                            SpinCusPrefVin.SaveIntProduct(GhiSoKiActivity.this, i);
                            tvProductName.setText(proSelected.ProductName);
                            if (Utilities.isValidDate(edtNgayThangNam.getText().toString())) {
                                String strDate = edtNgayThangNam.getText().toString();
                                long timemiliHSD = (long) 1000 * 60 * 60 * 24 * Integer.parseInt(shelfDate);
                                if (Integer.parseInt(shelfDate) > 0) {
                                    String myDate = strDate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = null;
                                    try {
                                        date = sdf.parse(myDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long mills = date.getTime();
                                    long timemiliHSD2 = timemiliHSD + mills;


                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis((long) timemiliHSD2);

                                    int mYear = calendar.get(Calendar.YEAR);
                                    int mMonth = calendar.get(Calendar.MONTH);
                                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                                    if (mDay < 10) {
                                        edtHSD.setText("0" + mDay + "/" + (mMonth + 1) + "/" + mYear);
                                        if (mMonth < 10)
                                            edtHSD.setText("0" + mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                                    } else {
                                        if (mMonth < 10)
                                            edtHSD.setText(mDay + "/" + "0" + (mMonth + 1) + "/" + mYear);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    spinProductID.setAdapter(spinnerProductIdAdapter);

                    if (SpinCusPrefVin.LoadIntProduct(GhiSoKiActivity.this) >= products.size()) {
                        spinProductID.setSelection(0);
                    } else {
                        spinProductID.setSelection(SpinCusPrefVin.LoadIntProduct(GhiSoKiActivity.this));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @OnClick(R.id.btnAdd)
    public void AddSoKi() {
        if (edtSoKg.getText().toString().equals("") || Integer.parseInt(edtSoKg.getText().toString()) == 0) {
            Toast.makeText(this, "Số kg không được bằng 0", Toast.LENGTH_SHORT).show();
        } else if (Utilities.isValidDate(edtNgayThangNam.getText().toString()) && Utilities.isValidDate(edtHSD.getText().toString())) {

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("CustomerID", customerID);
            jsonObject.addProperty("ProductID", productId);
            jsonObject.addProperty("CartonWeight", edtSoKg.getText().toString() + "." + (edtSoKg2.getText().toString().equals("") ? "000" : edtSoKg2.getText().toString()));
            jsonObject.addProperty("ProDate", Utilities.formatDate_yyyyMMdd2(edtNgayThangNam.getText().toString()));
            jsonObject.addProperty("LotNumber", edtLotRef.getText().toString());
            jsonObject.addProperty("UserName", userName);
            jsonObject.addProperty("DeviceNumber", deviceNumber);
            jsonObject.addProperty("ExpDate", Utilities.formatDate_yyyyMMdd2(edtHSD.getText().toString()));
            jsonObject.addProperty("LocationNumber", edtLocation.getText().toString());
            jsonObject.addProperty("ReceivingOrderNumber", ReceivingOrderNumber);

            MyRetrofit.initRequest(GhiSoKiActivity.this).addPickinglistManual(jsonObject).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body() != null) {
                        if (response.body().equals("OK")) {
                            Toast.makeText(GhiSoKiActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                            getProductKg();
                            if (proSelected.ProductNumber.length() >= 5) {
                                String iProductNumber = proSelected.ProductNumber.substring(proSelected.ProductNumber.length() - 5);
                                String soKi1 = null, soki2 = null;
                                if (edtSoKg.length() == 1) {
                                    soKi1 = "0" + edtSoKg.getText().toString();
                                } else if (edtSoKg.length() > 1) {
                                    soKi1 = edtSoKg.getText().toString();
                                }
                                if (edtSoKg2.length() == 0) {
                                    soki2 = "000";
                                } else if (edtSoKg2.length() == 1) {
                                    soki2 = edtSoKg2.getText().toString() + "00";
                                } else if (edtSoKg2.length() == 2) {
                                    soki2 = edtSoKg2.getText().toString() + "0";
                                } else {
                                    soki2 = edtSoKg2.getText().toString();
                                }
                                String[] splitDate = edtNgayThangNam.getText().toString().split("/");
                                String splitYear = splitDate[2].substring(2);
                                String barcode = "2" + iProductNumber + soKi1 + soki2 + splitDate[0] + splitDate[1] + splitYear + "000";
//                            printTemSoKi(cusSelected.getCustomerName(), proSelected.ProductName, barcode, edtNgayThangNam.getText().toString(), edtHSD.getText().toString(), edtSoKg.getText().toString() + "." + (edtSoKg2.getText().toString().equals("") ? "000" : edtSoKg2.getText().toString()));

                                String totalString = cusSelected.getCustomerName() + "\n" + proSelected.ProductName + "\n" + "Khối Lượng: " + edtSoKg.getText().toString() + "." + (edtSoKg2.getText().toString().equals("") ? "000" : edtSoKg2.getText().toString()) + " Kg" + "\n" + "NSX: " + edtNgayThangNam.getText().toString() + "\n" + "HSD: " + edtHSD.getText().toString();

                                Bitmap bm = Bitmap.createBitmap(sizeW, sizeH, Bitmap.Config.ARGB_8888);

                                /*create a canvas object*/
                                Canvas cv = new Canvas(bm);
                                /*create a paint object*/
                                Paint paint = new Paint();


                                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                                Writer codeWriter;
                                codeWriter = new Code128Writer();
                                BitMatrix byteMatrix = null;

                                try {
                                    byteMatrix = codeWriter.encode(barcode, BarcodeFormat.CODE_128, sizeW, 40 * sizeH / 100, hintMap);
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

                                cv.drawBitmap(bitmap, 0f, 200f, paint);

                                Bitmap bitmap1 = Bitmap.createBitmap(sizeW, 60 * sizeH / 100, Bitmap.Config.ARGB_8888);

                                Canvas canvas = new Canvas(bitmap1);
                                canvas.drawBitmap(bm, 0, 0, null);
                                Paint paint1 = new Paint();

                                Paint.FontMetrics fm = new Paint.FontMetrics();
                                paint1.setColor(Color.WHITE);
                                paint1.getFontMetrics(fm);
                                paint1.setTextSize(18);
                                paint1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                paint1.setTextAlign(Paint.Align.CENTER);
                                int margin = 5;

                                int xPos = (cv.getWidth() / 2);
                                int yPos = (int) (((cv.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
//                    float w = paint.measureText(totalPaBox);
                                //        cv.drawRect(0, (int) (yPos / 0.50f), margin, 0, paint1);
                                cv.drawRect(0, 0, sizeW, 52 * sizeH / 100, paint1);
                                paint1.setColor(Color.BLACK);
                                int y = yPos + (margin * 3);
//                            int y = 0;
                                for (String line : totalString.split("\n")) {
                                    cv.drawText(line, xPos, y, paint1);
                                    y += paint1.descent() - paint1.ascent();
                                }


                                printTemSoKi(bm);


                                edtSoKg2.setText("");
                                edtSoKg2.requestFocus();
                            } else {
                                Toast.makeText(GhiSoKiActivity.this, "Mã sản phẩm quá ngắn không thể in", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(GhiSoKiActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(GhiSoKiActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("Lỗi đường truyền. Kiểm tra lại !");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
            });
        } else {
            Toast.makeText(this, "huhu", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btnUpdate)
    public void UpdateSoKi() {
        if (edtSoKg.getText().toString().equals("") || Integer.parseInt(edtSoKg.getText().toString()) == 0) {
            Toast.makeText(GhiSoKiActivity.this, "Số kg không được bằng 0", Toast.LENGTH_SHORT).show();
        } else if (Utilities.isValidDate(edtNgayThangNam.getText().toString()) && Utilities.isValidDate(edtHSD.getText().toString())) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("id", strId);
            jsonObject1.addProperty("CustomerID", customerID);
            jsonObject1.addProperty("ProductID", productId);
            jsonObject1.addProperty("UserName", userName);
            jsonObject1.addProperty("DeviceNumber", deviceNumber);
            jsonObject1.addProperty("ProductionDate", Utilities.formatDate_yyyyMMdd2(edtNgayThangNam.getText().toString()));
            jsonObject1.addProperty("ExpiryDate", Utilities.formatDate_yyyyMMdd2(edtHSD.getText().toString()));
            jsonObject1.addProperty("Weights", edtSoKg.getText().toString() + "." + edtSoKg2.getText().toString());
            jsonObject1.addProperty("CustomerRef", edtLotRef.getText().toString());

            MyRetrofit.initRequest(GhiSoKiActivity.this).updatePickinglistScan(jsonObject1).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body() != null) {
                        Toast.makeText(GhiSoKiActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        getProductKg();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(GhiSoKiActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btnInTem)
    public void print() {
//        strProductIdOnClick = proSelected.ProductNumber.substring(proSelected.ProductNumber.length() - 5);
        String soKi1 = null, soki2 = null;
        if (edtSoKg.length() == 1) {
            soKi1 = "0" + edtSoKg.getText().toString();
        } else if (edtSoKg.length() > 1) {
            soKi1 = edtSoKg.getText().toString();
        }
        if (edtSoKg2.length() == 0) {
            soki2 = "000";
        } else if (edtSoKg2.length() == 1) {
            soki2 = edtSoKg2.getText().toString() + "00";
        } else if (edtSoKg2.length() == 2) {
            soki2 = edtSoKg2.getText().toString() + "0";
        } else {
            soki2 = edtSoKg2.getText().toString();
        }
        String[] splitDate = edtNgayThangNam.getText().toString().split("/");
        String splitYear = splitDate[2].substring(2);
        String barcode = "2" + strProductIdOnClick.substring(strProductIdOnClick.length() - 5) + soKi1 + soki2 + splitDate[0] + splitDate[1] + splitYear + "000";

        String totalString = cusSelected.getCustomerName() + "\n" + tvProductName.getText().toString() + "\n" + "Khối Lượng: " + edtSoKg.getText().toString() + "." + (edtSoKg2.getText().toString().equals("") ? "000" : edtSoKg2.getText().toString()) + " Kg" + "\n" + "NSX: " + edtNgayThangNam.getText().toString() + "\n" + "HSD: " + edtHSD.getText().toString() + "\n" + barcode;

        Bitmap bm = Bitmap.createBitmap(540, 380, Bitmap.Config.ARGB_8888);

        /*create a canvas object*/
        Canvas cv = new Canvas(bm);
        /*create a paint object*/
        Paint paint = new Paint();


        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        Writer codeWriter;
        codeWriter = new Code128Writer();
        BitMatrix byteMatrix = null;

        try {
            byteMatrix = codeWriter.encode(barcode, BarcodeFormat.CODE_128, 540, 150, hintMap);
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

        cv.drawBitmap(bitmap, 0f, 200f, paint);

        Bitmap bitmap1 = Bitmap.createBitmap(540, 230, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap1);
        canvas.drawBitmap(bm, 0, 0, null);
        Paint paint1 = new Paint();

        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint1.setColor(Color.WHITE);
        paint1.getFontMetrics(fm);
        paint1.setTextSize(18);
        paint1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint1.setTextAlign(Paint.Align.CENTER);
        int margin = 5;

        int xPos = (cv.getWidth() / 2);
        int yPos = (int) (((cv.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
//                    float w = paint.measureText(totalPaBox);
        //        cv.drawRect(0, (int) (yPos / 0.50f), margin, 0, paint1);
        cv.drawRect(0, 0, 540, 200, paint1);
        paint1.setColor(Color.BLACK);
        int y = yPos + (margin * 3);
//                            int y = 0;
        for (String line : totalString.split("\n")) {
            cv.drawText(line, xPos, y, paint1);
            y += paint1.descent() - paint1.ascent();
        }


        printTemSoKi(bm);
    }

    @OnClick(R.id.btnTaoRO)
    public void TaoRO() {
        if (edtLocation.getText().toString().length() == 6) {
            MyRetrofit.initRequest(GhiSoKiActivity.this).PickinglistCreateEDISavePackageHN(new PickinglistCreateEDISavePackageHNParameter(userName, deviceNumber, cusSelected.getCustomerID(), Utilities.formatDate_yyyyMMdd(System.currentTimeMillis()), edtLocation.getText().toString())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(GhiSoKiActivity.this);
                        alert.setTitle("Thông Báo");
                        alert.setMessage("Thành công !");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                        Intent i = new Intent(GhiSoKiActivity.this, OrderDetailActivity.class);
                        i.putExtra(ORDER_NUMBER, response.body());
                        i.putExtra("SCAN_TYPE", 1);
                        i.putExtra("CUSTOMER_TYPE", "ByCartonID");
                        startActivity(i);

                    } else {
                        //Toast.makeText(SavePackageHNActivity.this, response.body() + " Thất bại", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alert = new AlertDialog.Builder(GhiSoKiActivity.this);
                        alert.setTitle("Thông Báo");
                        alert.setMessage("Thất bại !");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    //Toast.makeText(SavePackageHNActivity.this, "Lỗi rồi! Kiểm tra lại đường truyền nha!", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(GhiSoKiActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("Lỗi đường truyền. Kiểm tra lại !");
                    alert.setPositiveButton("OK", null);
                    alert.show();


                }
            });
        } else {
            Toast.makeText(this, "Vị trí phải có độ dài bằng 6", Toast.LENGTH_SHORT).show();
        }

    }

    public void getProductKg() {
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("CustomerID", String.valueOf(customerID));
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("Pick_date", Utilities.formatDate_yyyyMMdd2(edtNgayThangNam.getText().toString()));
        jsonObject.addProperty("DeviceNumber", String.valueOf(deviceNumber));


        MyRetrofit.initRequest(GhiSoKiActivity.this).loadProductKg(jsonObject).enqueue(new Callback<List<ProductKg>>() {
            @Override
            public void onResponse(Response<List<ProductKg>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter = new ProductKgAdapter(new RecyclerViewItemOrderListener<ProductKg>() {
                        @Override
                        public void onClick(ProductKg item, int position, int order) {
                            switch (order) {
                                case 0:
                                    strProductIdOnClick = item.ProductNumber;
                                    Toast.makeText(GhiSoKiActivity.this, item.id + "", Toast.LENGTH_SHORT).show();
                                    edtNgayThangNam.setText(Utilities.formatDate_ddMMyyyy(item.ProductionDate));
                                    edtHSD.setText(Utilities.formatDate_ddMMyyyy(item.ExpiryDate));
                                    String[] strKg = item.NetWeight.split("\\.");
                                    strId = item.id;
                                    strKg1 = strKg[0];
                                    strKg2 = strKg[1];
                                    edtSoKg.setText(strKg1);
                                    edtSoKg2.setText(strKg2);
                                    edtLotRef.setText(item.LotNumber);
                                    tvProductName.setText(item.ProductName);


                                    break;
                            }
                        }

                        @Override
                        public void onLongClick(ProductKg item, int position, int order) {
                            switch (order) {
                                case 0:
                                    AlertDialog dialog = new AlertDialog.Builder(GhiSoKiActivity.this)
                                            .setMessage("Bạn có muốn xóa thùng này không?")
                                            .setPositiveButton("Xóa một thùng", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    flag = 0;
                                                    deleteSavePackageHN(Integer.parseInt(item.id), flag);
                                                }
                                            })
                                            .setNegativeButton("Hủy", null)
                                            .setNeutralButton("Xóa tất cả", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    flag = 1;
                                                    deleteSavePackageHN(Integer.parseInt(item.id), flag);
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                    break;
                            }
                        }
                    });
                    if (response.body().size() > 0) {
                        adapter.replace(response.body());
                    } else {
                        Toast.makeText(GhiSoKiActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        adapter.replace(response.body());
                    }
                    int totalTong = 0;
                    double totalKg = 0.0;
                    for (ProductKg p : response.body()) {
                        totalTong++;
                        totalKg += Double.valueOf(p.NetWeight);
                    }

                    tvTong.setText(String.valueOf(totalTong));
                    NumberFormat formatter = new DecimalFormat("#0.000");
                    tvTongSoKg.setText(String.valueOf(formatter.format(totalKg)));

                    rvGhiSoKi.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    private void deleteSavePackageHN(int item, byte flag) {
        MyRetrofit.initRequest(this).deleteSavePackageHN(new DeleteSavePackageParameter(userName, deviceNumber, cusSelected.getCustomerID(), item, flag)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Toast.makeText(GhiSoKiActivity.this, response.body() + " Xóa Thành công", Toast.LENGTH_SHORT).show();
                    getProductKg();
                } else {
                    Toast.makeText(GhiSoKiActivity.this, response.body() + " Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(GhiSoKiActivity.this, "Kiểm tra lại mạng đi nha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Connection getZebraPrinterConn() {
        return new BluetoothConnection(strMac);
    }

    private void printTemSoKi(final Bitmap bitmap) {
        if (strMac != null && !strMac.equals("")) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Looper.prepare();
                        Toast.makeText(GhiSoKiActivity.this, "Sending data to printer", Toast.LENGTH_SHORT).show();
                        Connection connection = getZebraPrinterConn();
                        connection.open();
                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

//                        printer.printImage(ZebraImageFactory.getImage(bitmap), (int) (bitmap.getWidth() / 4), bitmap.getHeight()/2,0, bitmap.getHeight(), false);
//                        printer.printImage(ZebraImageFactory.getImage(bitmap), (int) (bitmap.getWidth()), bitmap.getHeight() / 8, 0, bitmap.getHeight(), false);
//                        printer.printImage(ZebraImageFactory.getImage(bitmap), 0, 0, (int) (bitmap.getWidth() / 1.4), (int) (bitmap.getHeight() / 1.0), false);
                        printer.printImage(ZebraImageFactory.getImage(bitmap), 0, 0, (int) (bitmap.getWidth() / 1.2f), bitmap.getHeight(), false);
                        connection.close();
                    } catch (ConnectionException e) {
                        helper.showErrorDialogOnGuiThread(e.getMessage());
                        e.printStackTrace();
                    } catch (ZebraPrinterLanguageUnknownException e) {
                        helper.showErrorDialogOnGuiThread(e.getMessage());
                        e.printStackTrace();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_size_content_so_ki, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_size_content_soki:
                final Dialog dialog = new Dialog(GhiSoKiActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_size_content_soki);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                EditText edtWidth = dialog.findViewById(R.id.edtWidth);
                EditText edtHeight = dialog.findViewById(R.id.edtHeight);
                EditText edtTextSize = dialog.findViewById(R.id.edtTextSize);
                Button btnHuy = dialog.findViewById(R.id.btnHuy);
                Button btnDongY = dialog.findViewById(R.id.btnDongY);

                edtWidth.setText(String.valueOf(SizeContentSoKiPref.loadSizeWidth(GhiSoKiActivity.this)));
                edtHeight.setText(String.valueOf(SizeContentSoKiPref.loadSizeHeight(GhiSoKiActivity.this)));
                edtTextSize.setText(String.valueOf(SizeContentSoKiPref.loadSizeText(GhiSoKiActivity.this)));

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnDongY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SizeContentSoKiPref.saveSizeWidth(GhiSoKiActivity.this, Integer.parseInt(edtWidth.getText().toString()));
                        SizeContentSoKiPref.saveSizeHeight(GhiSoKiActivity.this, Integer.parseInt(edtHeight.getText().toString()));
                        SizeContentSoKiPref.saveSizeText(GhiSoKiActivity.this, Integer.parseInt(edtTextSize.getText().toString()));
                        dialog.dismiss();
                    }
                });


                dialog.show();
                dialog.getWindow().setAttributes(lp);


                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getProductKg();
        edtHSD.setText("");
        edtSoKg.setText("");
        edtSoKg2.setText("");
        edtLotRef.setText("");
        edtLocation.setText("");

    }


    @OnClick(R.id.btChooseDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                loadRO();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadRO();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadRO();
    }

    private void loadRO() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("OrderDate", reportDate2);
        jsonObject.addProperty("CustomerID", customerID);

        MyRetrofit.initRequest(GhiSoKiActivity.this).loadRO(jsonObject).enqueue(new Callback<List<RO>>() {
            @Override
            public void onResponse(Response<List<RO>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    ros = response.body();
                    spinnerROAdapter = new ArrayAdapter<RO>(GhiSoKiActivity.this, android.R.layout.simple_spinner_dropdown_item, ros);
                    spinRO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            roSelected = ros.get(position);
                            ReceivingOrderNumber = roSelected.receivingOrderNumber;
                            SpinCusPrefVin.SaveIntRO(GhiSoKiActivity.this, position);
//                            getProductKg();
//                            loadRO();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinRO.setAdapter(spinnerROAdapter);

                    if (SpinCusPrefVin.LoadIntRO(GhiSoKiActivity.this) >= ros.size()) {
                        spinRO.setSelection(0);
                    } else {
                        spinRO.setSelection(SpinCusPrefVin.LoadIntRO(GhiSoKiActivity.this));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(GhiSoKiActivity.this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}