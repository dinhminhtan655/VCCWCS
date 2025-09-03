package com.wcs.vcc.main.nhapphi;

import android.os.Bundle;

import com.wcs.vcc.main.EmdkActivity;

public class DanhSachPhiActivity extends EmdkActivity {

//    private ActivityDanhSachPhiBindingImpl binding;
//    DanhSachPhiAdapter adapter;

//    @BindView(R.id.rcPhi)
//    RecyclerView rc;
//
//    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_danh_sach_phi);
//        ButterKnife.bind(this);
//
//        adapter = new DanhSachPhiAdapter(new RecyclerViewItemListener<ChiPhi>() {
//            @Override
//            public void onClick(ChiPhi item, int position) {
//
//            }
//
//            @Override
//            public void onLongClick(ChiPhi item, int position) {
//
//            }
//        });
//        rc.setLayoutManager(new LinearLayoutManager(this));
//        rc.setAdapter(adapter);
//        getListNamePhi();


    }

//    private void getListNamePhi() {
//        progressDialog = Utilities.getProgressDialog(this, "Loading...");
//        progressDialog.show();
//        MyRetrofit.initRequest(this).getListNameChiPhi().enqueue(new Callback<List<ChiPhi>>() {
//            @Override
//            public void onResponse(Response<List<ChiPhi>> response, Retrofit retrofit) {
//                if (response.isSuccess()){
//                    List<ChiPhi> list = response.body();
//                    adapter.replace(list);
//                    progressDialog.dismiss();
//                }else {
//                    Toast.makeText(DanhSachPhiActivity.this, "Fail rồi cha", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Toast.makeText(DanhSachPhiActivity.this, "Kiểm tra mạng đi!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
