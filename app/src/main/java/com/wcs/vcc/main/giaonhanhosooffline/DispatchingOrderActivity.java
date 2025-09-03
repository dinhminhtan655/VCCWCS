package com.wcs.vcc.main.giaonhanhosooffline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.wcs.wcs.R;
import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.api.DSOrderDetailParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.NotificationParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.SendMailParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.database.MyDbHelper;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrderDetailsInfo;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrdersInfo;
import com.wcs.vcc.main.giaonhanhoso.GiaoHoSoAdapter;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DispatchingOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GiaoHoSoAdapter adapter;
    private String username;
    private ProgressDialog dialog;
    private MyDbHelper dbHelper;
    private Iterator<DSDispatchingOrdersInfo> dispatchingOrdersInfoIterator;
    private Iterator<DSDispatchingOrderDetailsInfo> orderChangeIterator;
    private HashMap<String, String> signatures;
    private Date dateCreate;
    private String originalFileName;
    private String md5FileName;
    private int fileSize;
    private Iterator<String> orderNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching_order);
        Utilities.showBackIcon(getSupportActionBar());

        ListView listView = (ListView) findViewById(R.id.lv_dispatching_order);
        listView.setOnItemClickListener(this);

        dbHelper = MyDbHelper.getInstance(this);

        adapter = new GiaoHoSoAdapter(this, new ArrayList<DSDispatchingOrdersInfo>());

        updateListView();

        listView.setAdapter(adapter);
        snackBarView = listView;

        username = LoginPref.getUsername(this);
    }

    private void updateListView() {
        List<DSDispatchingOrdersInfo> dispatchingOrders = dbHelper.getDispatchingOrders();
        adapter.clear();
        adapter.addAll(dispatchingOrders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.giao_ho_so_offline, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                updateDataOffline();
                break;
            case R.id.action_sync:
                syncData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDataOffline() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Cập nhật dữ liệu mới sẽ xóa toàn bộ thông tin hồ sơ đang lưu trên máy, bao gồm các phiếu đã scan và thông tin chữ ký. Vì vậy hãy chắc chắn bạn đã đồng bộ dữ liệu trước khi thực hiện thao tác này.")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbHelper.clearAllData();

                        updateListView();

                        getDSDispatchingOrders();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void getDSDispatchingOrders() {

        dialog = Utilities.getProgressDialog(this, "Đang tải và lưu dữ liệu mới vào máy...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this).getDSDispatchingOrders(new NotificationParameter(username)).enqueue(new Callback<List<DSDispatchingOrdersInfo>>() {
            @Override
            public void onResponse(Response<List<DSDispatchingOrdersInfo>> response, Retrofit retrofit) {
                List<DSDispatchingOrdersInfo> body = response.body();


                if (response.isSuccess() && body != null) {

                    List<DSDispatchingOrdersInfo> notDone = new ArrayList<>();

                    for (DSDispatchingOrdersInfo order : body) {
                        if (order.getStatus() == 0)
                            notDone.add(order);
                    }
                    if (notDone.size() > 0) {

                        dbHelper.saveDispatchingOrders(notDone);

                        dispatchingOrdersInfoIterator = notDone.iterator();

                        if (dispatchingOrdersInfoIterator.hasNext()) {

                            getDSDispatchingOrdersDetail();
                        } else {

                            updateListView();
                            dismissDialog(dialog);
                        }
                    } else {

                        updateListView();
                        dismissDialog(dialog);
                    }

                } else {

                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });
    }

    private void getDSDispatchingOrdersDetail() {

        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        DSDispatchingOrdersInfo orderInfo = dispatchingOrdersInfoIterator.next();
        MyRetrofit.initRequest(this).getDSDispatchingOrderDetails(new DSOrderDetailParameter(orderInfo.getDispatchingOrderNumber(), "", username)).enqueue(new Callback<List<DSDispatchingOrderDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<DSDispatchingOrderDetailsInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    dbHelper.saveDispatchingOrdersDetail(response.body());

                    if (dispatchingOrdersInfoIterator.hasNext()) {

                        getDSDispatchingOrdersDetail();
                    } else {

                        updateListView();
                        dismissDialog(dialog);
                    }
                } else {
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });
    }

    private void syncData() {
        signatures = new HashMap<>();

        dialog = Utilities.getProgressDialog(this, "Đang đồng bộ dữ liệu lên máy chủ...");
        dialog.show();

        List<DSDispatchingOrderDetailsInfo> orders = dbHelper.getDispatchingOrderDetailsHaveChange();

        orderChangeIterator = orders.iterator();
        if (orderChangeIterator.hasNext()) {

            syncProgress();

        } else {
            Toast.makeText(this, "Dữ liệu không có thay đổi", Toast.LENGTH_LONG).show();
            dismissDialog(dialog);
        }
    }

    private void syncProgress() {

        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        final DSDispatchingOrderDetailsInfo order = orderChangeIterator.next();

        final String orderNumber = order.getOrderNumber();
        if (order.getAttachmentFile().length() > 0) {

            if (signatures.get(orderNumber) == null) {

                signatures.put(orderNumber, order.getAttachmentFile());
            }
        }

        MyRetrofit.initRequest(this).getDSDispatchingOrderDetails(new DSOrderDetailParameter(orderNumber, order.getBarcode(), order.getUsername())).enqueue(new Callback<List<DSDispatchingOrderDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<DSDispatchingOrderDetailsInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {

                    dbHelper.updateOrderDone(order.getCartonNewID());

                    if (orderChangeIterator.hasNext()) {

                        syncProgress();
                    } else if (signatures.size() > 0) {

                        orderNumbers = signatures.keySet().iterator();
                        if (orderNumbers.hasNext()) {
                            uploadImage();
                        }
                    } else {
                        dismissDialog(dialog);
                        Snackbar.make(snackBarView, "Đồng bộ thành công", Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });
    }

    public void uploadImage() {
        final String orderNumber = orderNumbers.next();

        File fileUpload = new File(signatures.get(orderNumber));
        dateCreate = new Date(fileUpload.lastModified());
        originalFileName = fileUpload.getName();
        md5FileName = Utilities.md5(originalFileName) + ".jpg";
        fileSize = (int) fileUpload.length() / 1024;

        RequestBody requestBodyFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), fileUpload);
        RequestBody requestBodyFileName =
                RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), "");

        Call<String> call = MyRetrofit.initRequest(this)
                .uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    updateAttachment(orderNumber);
                } else {
                    dismissDialog(dialog);
                    Snackbar.make(snackBarView, "Đã xảy ra lỗi trong quá trình lưu hình ảnh chữ ký", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });

    }

    private void updateAttachment(final String orderNumber) {
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(dateCreate.getTime()),
                "",
                md5FileName,
                fileSize,
                Utilities.getDefaultUUID(),
                username,
                0,
                3,
                orderNumber,
                originalFileName);
        MyRetrofit.initRequest(this)
                .setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    sendMail(new SendMailParameter(orderNumber, username));
                } else {
                    dismissDialog(dialog);
                    Snackbar.make(snackBarView, "Đã xảy ra lỗi trong quá trình lưu thông tin chữ ký", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });
    }

    private void sendMail(SendMailParameter parameter) {

        MyRetrofit.initRequest(this).sendMail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    if (orderNumbers.hasNext()) {
                        uploadImage();
                    } else {
                        dismissDialog(dialog);
                        Snackbar.make(snackBarView, "Đồng bộ thành công", Snackbar.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(DispatchingOrderActivity.this, t, TAG, snackBarView);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DispatchingOrderDetailActivity.class);
        intent.putExtra(ORDER_NUMBER, adapter.getItem(position).getDispatchingOrderNumber());
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {

            dbHelper.close();
            dbHelper = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing())
            return;
        super.onBackPressed();
    }
}
