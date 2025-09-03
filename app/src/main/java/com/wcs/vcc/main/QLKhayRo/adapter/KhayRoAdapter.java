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
import com.wcs.vcc.main.QLKhayRo.models.KhayRoResponse;

import java.util.List;

public class KhayRoAdapter extends RecyclerView.Adapter<KhayRoAdapter.MyViewHolder> {

    Context context;
    List<KhayRoResponse> responseList;
    int key;

    public KhayRoAdapter(Context context, List<KhayRoResponse> responseList, int key) {
        this.context = context;
        this.responseList = responseList;
        this.key = key;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_khayro_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {
        myViewHolder.tvBasketName.setText(responseList.get(i).getBasketName());
        myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()) == "null" ? "0" : String.valueOf(responseList.get(i).getQuantity()));
        myViewHolder.edtQuantityReceived.setText(String.valueOf(responseList.get(i).getQuantityConfirmReceived()) == "null" ? "0" : String.valueOf(responseList.get(i).getQuantityConfirmReceived()));
        if (key == 1) {
            myViewHolder.tv1.setVisibility(View.GONE);
            myViewHolder.edtQuantity.setVisibility(View.GONE);
            myViewHolder.btnTru.setVisibility(View.GONE);
            myViewHolder.btnCong.setVisibility(View.GONE);
        } else {
            myViewHolder.tv2.setVisibility(View.GONE);
            myViewHolder.edtQuantityReceived.setVisibility(View.GONE);
            myViewHolder.btnTru1.setVisibility(View.GONE);
            myViewHolder.btnCong1.setVisibility(View.GONE);
        }

        myViewHolder.btnCong.setOnClickListener(view -> {
            responseList.get(i).setQuantity(String.valueOf((Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == null ? "0" : myViewHolder.edtQuantity.getText().toString()) + 1)));
            myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()));
        });

        myViewHolder.btnTru.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == null ? "0" : myViewHolder.edtQuantity.getText().toString()) > 0) {
                responseList.get(i).setQuantity(String.valueOf((Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == null ? "0" : myViewHolder.edtQuantity.getText().toString()) - 1)));
                myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()));
            }

        });

        myViewHolder.btnCong1.setOnClickListener(view -> {
            responseList.get(i).setQuantityConfirmReceived(String.valueOf((Integer.parseInt(myViewHolder.edtQuantityReceived.getText().toString() == null ? "0" : myViewHolder.edtQuantityReceived.getText().toString()) + 1)));
            myViewHolder.edtQuantityReceived.setText(String.valueOf(responseList.get(i).getQuantityConfirmReceived()));
        });

        myViewHolder.btnTru1.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtQuantityReceived.getText().toString() == null ? "0" : myViewHolder.edtQuantityReceived.getText().toString()) > 0) {
                responseList.get(i).setQuantityConfirmReceived(String.valueOf((Integer.parseInt(myViewHolder.edtQuantityReceived.getText().toString() == null ? "0" : myViewHolder.edtQuantityReceived.getText().toString()) - 1)));
                myViewHolder.edtQuantityReceived.setText(String.valueOf(responseList.get(i).getQuantityConfirmReceived()));
            }
        });

        myViewHolder.edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!myViewHolder.edtQuantity.getText().toString().isEmpty()) {
                    responseList.get(i).setQuantity(myViewHolder.edtQuantity.getText().toString());
                }
                else {
                    responseList.get(i).setQuantity("0");
                }
            }
        });

        myViewHolder.edtQuantityReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!myViewHolder.edtQuantityReceived.getText().toString().isEmpty()) {
                    responseList.get(i).setQuantityConfirmReceived(myViewHolder.edtQuantityReceived.getText().toString());
                }
                else {
                    responseList.get(i).setQuantityConfirmReceived("0");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBasketName, tv1, tv2;
        EditText edtQuantity, edtQuantityReceived;
        Button btnTru, btnTru1, btnCong, btnCong1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBasketName = itemView.findViewById(R.id.tvBasketName);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            edtQuantity = itemView.findViewById(R.id.edtSLKhayRo);
            edtQuantityReceived = itemView.findViewById(R.id.edtSLKhayRoReceived);
            btnTru = itemView.findViewById(R.id.btnTru);
            btnTru1 = itemView.findViewById(R.id.btnTru1);
            btnCong = itemView.findViewById(R.id.btnCong);
            btnCong1 = itemView.findViewById(R.id.btnCong1);
        }
    }
}
