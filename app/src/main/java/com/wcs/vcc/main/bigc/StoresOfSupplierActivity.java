package com.wcs.vcc.main.bigc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.PostImage;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.StoresOfSupplierParameter;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StoresOfSupplierActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String SALES_ORDER_ID = "SalesOrderId";
    private View.OnClickListener action;

    private StoresOfSupplierAdapter adapter;
    private ListView lv;
    private int purchasingOrderId;
    private TextView tvHeader;
    private String header;
    private String purchasingOrderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_of_supplier);
        Utilities.showBackIcon(getSupportActionBar());

        mapViewAndSetListener();

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStoresOfSupplier();
            }
        };

        adapter = new StoresOfSupplierAdapter(this, new ArrayList<Store>());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        header = getIntent().getStringExtra(SuppliersTodayActivity.HEADER);
        tvHeader.setText(header);
        purchasingOrderId = getIntent().getIntExtra(SuppliersTodayActivity.PURCHASING_ORDER_ID, 0);
        purchasingOrderNumber = getIntent().getStringExtra(SuppliersTodayActivity.PURCHASING_ORDER_NUMBER);

        getStoresOfSupplier();
    }

    private void mapViewAndSetListener() {
        lv = (ListView) findViewById(R.id.lv_stores_of_supplier);
        tvHeader = (TextView) findViewById(R.id.tv_stores_of_supplier_header);
        snackBarView = lv;
    }

    public void getStoresOfSupplier() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        MyRetrofit.initRequest(this).getStoresOfSupplier(new StoresOfSupplierParameter(purchasingOrderId)).enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Response<List<Store>> response, Retrofit retrofit) {
                List<Store> body = response.body();
                if (response.isSuccess() && body != null) {
                    Collections.sort(body);
                    adapter.clear();
                    adapter.addAll(body);
                }

                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(StoresOfSupplierActivity.this, t, TAG, snackBarView, action);
            }
        });
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Store store = adapter.getItem(position);
        if (store != null) {
            Intent intent = new Intent(this, ProductsOfStoreActivity.class);
            intent.putExtra(SuppliersTodayActivity.HEADER,
                    String.format(Locale.getDefault(), getString(R.string.bigc_title_products_store),
                            header,
                            store.getName(),
                            store.getBookingWeight(),
                            store.getActualWeight()));
            intent.putExtra(SuppliersTodayActivity.NEXT_ACTIVITY_HEADER, String.format("%s > %s", header, store.getName()));
            intent.putExtra(SuppliersTodayActivity.PURCHASING_ORDER_ID, store.getPurchasingOrderID());
            intent.putExtra(SALES_ORDER_ID, store.getSalesOrderID());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_of_supplier, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_capture_image) {
            checkCaptureImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCaptureImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                files = GridImage.updateGridImage(outputMediaFile.getPath(), null);
            } else {
                files = GridImage.updateGridImage(imageCapturedUri.getPath(), null);
            }
            uploadImage();
        }
    }

    private void uploadImage() {
        ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.upload));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        PostImage postImage = new PostImage(this, dialog, snackBarView, "", purchasingOrderNumber, new UploadImageCallback() {
            @Override
            public void uploadDone(AttachmentParameter params) {

            }
        });
        postImage.uploadImage(files, files.size() - 1);
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }
}
