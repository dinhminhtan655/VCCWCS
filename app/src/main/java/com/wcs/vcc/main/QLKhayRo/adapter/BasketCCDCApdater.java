package com.wcs.vcc.main.QLKhayRo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.QLKhayRo.models.BasketCCDCResponse;

import java.util.List;

public class BasketCCDCApdater extends RecyclerView.Adapter<BasketCCDCApdater.MyViewHolder> {

    Context context;
    List<BasketCCDCResponse> basketCCDCResponseList;

    public BasketCCDCApdater(Context context, List<BasketCCDCResponse> basketCCDCResponseList) {
        this.context = context;
        this.basketCCDCResponseList = basketCCDCResponseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.basket_ccdc_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {
        myViewHolder.tvBasketName.setText(basketCCDCResponseList.get(i).getBasketName());
        myViewHolder.edtCCDC.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantity()) == "null" ? "" : String.valueOf(basketCCDCResponseList.get(i).getQuantity()));
        myViewHolder.edtCCDCReceived.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantityConfirmReceived()) == "null" ? "" : String.valueOf(basketCCDCResponseList.get(i).getQuantityConfirmReceived()));

        myViewHolder.btnCong.setOnClickListener(view -> {
            basketCCDCResponseList.get(i).setQuantity(Integer.parseInt(myViewHolder.edtCCDC.getText().toString() == null ? "0" : myViewHolder.edtCCDC.getText().toString()) + 1);
            myViewHolder.edtCCDC.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantity()));
        });

        myViewHolder.btnTru.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtCCDC.getText().toString() == null ? "0" : myViewHolder.edtCCDC.getText().toString()) > 0) {
                basketCCDCResponseList.get(i).setQuantity(Integer.parseInt(myViewHolder.edtCCDC.getText().toString() == null ? "0" : myViewHolder.edtCCDC.getText().toString()) - 1);
                myViewHolder.edtCCDC.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantity()));
            }
        });

        myViewHolder.btnCong1.setOnClickListener(view -> {
            basketCCDCResponseList.get(i).setQuantityConfirmReceived(Integer.parseInt(myViewHolder.edtCCDCReceived.getText().toString() == null ? "0" : myViewHolder.edtCCDCReceived.getText().toString()) + 1);
            myViewHolder.edtCCDCReceived.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantityConfirmReceived()));
        });

        myViewHolder.btnTru1.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtCCDCReceived.getText().toString() == null ? "0" : myViewHolder.edtCCDCReceived.getText().toString()) > 0) {
                basketCCDCResponseList.get(i).setQuantityConfirmReceived(Integer.parseInt(myViewHolder.edtCCDCReceived.getText().toString() == null ? "0" : myViewHolder.edtCCDCReceived.getText().toString()) - 1);
                myViewHolder.edtCCDCReceived.setText(String.valueOf(basketCCDCResponseList.get(i).getQuantityConfirmReceived()));
            }
        });

        myViewHolder.edtCCDC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!myViewHolder.edtCCDC.getText().toString().isEmpty()) {
                    basketCCDCResponseList.get(i).setQuantity(Integer.parseInt(myViewHolder.edtCCDC.getText().toString()));
                }
                else {
                    basketCCDCResponseList.get(i).setQuantity(0);
                }
            }
        });

        myViewHolder.edtCCDCReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!myViewHolder.edtCCDCReceived.getText().toString().isEmpty()) {
                    basketCCDCResponseList.get(i).setQuantityConfirmReceived(Integer.parseInt(myViewHolder.edtCCDCReceived.getText().toString()));
                }
                else {
                    basketCCDCResponseList.get(i).setQuantityConfirmReceived(0);
                }
            }
        });




//        myViewHolder.btnUpate.setOnClickListener(view -> {
//            UpdateKhayRoRequest request = new UpdateKhayRoRequest();
//            request.setDate(basketCCDCResponseList.get(i).getDate().substring(0, 10));
//            request.setCustomerCode(basketCCDCResponseList.get(i).getCustomerCode());
//            request.setRouteNo(basketCCDCResponseList.get(i).getRouteNo());
//            request.setPalletId(null);
//            request.setBasketId(basketCCDCResponseList.get(i).getBasketId());
//            request.setQuantity(Integer.parseInt(myViewHolder.edtCCDC.getText().toString()));
//            request.setQuantityConfirmReceived(Integer.parseInt(myViewHolder.edtCCDCReceived.getText().toString()));
//
//            MyRetrofit.initRequest(context).updateKhayRo(request).enqueue(new Callback<UpdateKhayRoResponse>() {
//                @Override
//                public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
//                    if (response.isSuccess() && response.body() != null) {
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
    }

    @Override
    public int getItemCount() {
        return basketCCDCResponseList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBasketName;
        EditText edtCCDC, edtCCDCReceived;
        Button btnCong, btnCong1, btnTru, btnTru1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBasketName = itemView.findViewById(R.id.tvBasketName);
            edtCCDC = itemView.findViewById(R.id.edtUpdateCcdc);
            edtCCDCReceived = itemView.findViewById(R.id.edtUpdateCcdcReceived);
            btnCong = itemView.findViewById(R.id.btnCongCCDC);
            btnCong1 = itemView.findViewById(R.id.btnCongCCDC1);
            btnTru = itemView.findViewById(R.id.btnTruCCDC);
            btnTru1 = itemView.findViewById(R.id.btnTruCCDC1);
        }

    }
}
