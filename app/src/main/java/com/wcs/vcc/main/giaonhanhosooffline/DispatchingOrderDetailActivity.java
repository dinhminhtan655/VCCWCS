package com.wcs.vcc.main.giaonhanhosooffline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.database.MyDbHelper;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrderDetailsInfo;
import com.wcs.vcc.main.giaonhanhoso.GiaoHoSoDetailAdapter;
import com.wcs.vcc.main.giaonhanhoso.SignActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DispatchingOrderDetailActivity extends EmdkActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    public static final int RC_SIGN = 9001;
    public static final String ORDER_NUMBER = "order_number";
    public static final String IS_OFFLINE = "is_offline";
    private EditText etBarcode;
    private ImageView ivSignature;
    private Button btSign;
    private EditText etTargetScan;
    private TextView tvNumberScanned;
    private String userName;
    private GiaoHoSoDetailAdapter adapter;
    private String orderNumber;
    private MyDbHelper dbHelper;
    private boolean isSignatureShowed;
    private int eventKeycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching_order_detail);
        Utilities.showBackIcon(getSupportActionBar());

        dbHelper = MyDbHelper.getInstance(this);

        ListView listView = (ListView) findViewById(R.id.lv_dispatching_order_detail);
        snackBarView = listView;

        etBarcode = (EditText) findViewById(R.id.et_all_barcode);
        ivSignature = (ImageView) findViewById(R.id.iv_signature);
        btSign = (Button) findViewById(R.id.bt_sign);
        etTargetScan = (EditText) findViewById(R.id.et_all_target_scan);
        tvNumberScanned = (TextView) findViewById(R.id.tv_number_scanned);
        Button btScan = (Button) findViewById(R.id.bt_scan_camera);

        btSign.setOnClickListener(this);
        btScan.setOnClickListener(this);

        orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);

        adapter = new GiaoHoSoDetailAdapter(this, new ArrayList<DSDispatchingOrderDetailsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contents = s.toString();
                if (contents.contains("\n"))
                    new AsyncUiControlUpdate().execute(contents.replace("\n", ""));
            }
        });

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(String.format(getString(R.string.ho_so_x), orderNumber));

        updateUI();

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    private void updateUI() {
        List<DSDispatchingOrderDetailsInfo> dispatchingOrderDetails = dbHelper.getDispatchingOrderDetails(orderNumber);

        adapter.clear();
        adapter.addAll(dispatchingOrderDetails);

        int totalOK = 0;
        int totalOrder = dispatchingOrderDetails.size();

        if (totalOrder > 0) {
            for (DSDispatchingOrderDetailsInfo info : dispatchingOrderDetails)
                if ((info.getScannedType() == 3 || info.getScannedType() == 6) && (info.getResult().equalsIgnoreCase("OK") || info.getResult().equalsIgnoreCase("XX")))
                    totalOK++;
            tvNumberScanned.setText(String.format(Locale.US, "%d / %d", totalOK, totalOrder));

            String attachmentFile = dispatchingOrderDetails.get(0).getAttachmentFile();
            if (attachmentFile.length() > 0) {
                if (!isSignatureShowed) {
                    isSignatureShowed = true;
                    btSign.setEnabled(false);

                    try {
                        attachmentFile = ResizeImage.resizeImageFromFile(attachmentFile, Const.IMAGE_UPLOAD_WIDTH);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = Const.SAMPLE_SIZE;

                        Bitmap bitmap = BitmapFactory.decodeFile(attachmentFile, options);

                        ivSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                        ivSignature.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        Log.w(TAG, "updateUI: ", e);
                    }
                }
            } else if (totalOK == totalOrder)
                btSign.setEnabled(true);
            else
                btSign.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String contents = result.getContents();
            new AsyncUiControlUpdate().execute(contents);

        } else if (requestCode == RC_SIGN && resultCode == RESULT_OK) {

            String filePath = intent.getStringExtra("filePath");
            if (filePath != null) {
                dbHelper.updateSignaturePath(orderNumber, filePath);

                try {
                    filePath = ResizeImage.resizeImageFromFile(filePath, Const.IMAGE_UPLOAD_WIDTH);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = Const.SAMPLE_SIZE;

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

                    ivSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                    ivSignature.setImageBitmap(bitmap);

                    btSign.setEnabled(false);

                } catch (FileNotFoundException e) {
                    Log.w(TAG, "onActivityResult: ", e);
                }

            }
        }
    }

    @Override
    public void onData(String data) {
        new AsyncUiControlUpdate().execute(data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Utilities.hideKeyboard(this);
        if (eventKeycode == 0 && (keyCode == 245 || keyCode == 242)) {
            eventKeycode++;
            etTargetScan.requestFocus();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 245 || keyCode == 242)
            eventKeycode = 0;

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_sign:
                signClick();

                break;
            case R.id.bt_scan_camera:
                scanCamera();

                break;
        }
    }


    public void signClick() {
        Intent intent = new Intent(this, SignActivity.class);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        intent.putExtra(IS_OFFLINE, true);
        startActivityForResult(intent, RC_SIGN);
    }

    public void scanCamera() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    public class AsyncUiControlUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {

            etTargetScan.setText("");
            etBarcode.setText(result);

            try {
                int cartonId = Integer.parseInt(result.substring(2, result.length()));

                List<DSDispatchingOrderDetailsInfo> query = dbHelper.getOrderDetail(orderNumber, cartonId);
                if (query.size() == 1) {

                    DSDispatchingOrderDetailsInfo order = query.get(0);
                    if (order.getResult().equals("")) {
                        dbHelper.updateOrderDetail(orderNumber, order.getCartonNewID(), userName, result, 0);
                    } else {

                        dbHelper.updateOrderDetail(orderNumber, order.getCartonNewID(), userName, result, 1);
                    }
                } else if (query.size() == 0) {
                    dbHelper.insertOrderError(orderNumber, userName, result, cartonId);
                } else {
                    Log.w(TAG, "onPostExecute: have more than one result");
                }

                updateUI();
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                Toast.makeText(DispatchingOrderDetailActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {

            dbHelper.close();
            dbHelper = null;
        }
    }
}
