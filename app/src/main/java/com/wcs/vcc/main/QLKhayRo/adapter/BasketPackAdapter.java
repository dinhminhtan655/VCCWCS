package com.wcs.vcc.main.QLKhayRo.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.QLKhayRo.models.BasketPackResponse;

import java.util.List;

public class BasketPackAdapter extends RecyclerView.Adapter<BasketPackAdapter.MyViewHolder>{

    Context context;
    List<BasketPackResponse> responseList;

    public BasketPackAdapter(Context context, List<BasketPackResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.basket_pack_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvBasketName.setText(responseList.get(i).getItemName());
        myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()) == "null" ? "0" : String.valueOf(responseList.get(i).getQuantity()));

        myViewHolder.btnCong.setOnClickListener(view -> {
            responseList.get(i).setQuantity(Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == "null" ? "0" : myViewHolder.edtQuantity.getText().toString()) + 1);
            myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()));
        });

        myViewHolder.btnTru.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == "null" ? "0" : myViewHolder.edtQuantity.getText().toString()) > 0) {
                responseList.get(i).setQuantity(Integer.parseInt(myViewHolder.edtQuantity.getText().toString() == "null" ? "0" : myViewHolder.edtQuantity.getText().toString()) - 1);
                myViewHolder.edtQuantity.setText(String.valueOf(responseList.get(i).getQuantity()));
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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBasketName;
        EditText edtQuantity;
        Button btnCong, btnTru;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvBasketName = itemView.findViewById(R.id.tvBasketName);
            edtQuantity = itemView.findViewById(R.id.edtSLKhayRo);
            btnCong = itemView.findViewById(R.id.btnCongBasketPack);
            btnTru = itemView.findViewById(R.id.btnTruBasketPack);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
