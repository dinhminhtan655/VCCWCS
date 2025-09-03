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
import android.widget.EditText;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.QLKhayRo.BasketRouteDetailActivity;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteDetailResponse;

import java.util.List;

public class BasketRouteDetailAdapter extends RecyclerView.Adapter<BasketRouteDetailAdapter.MyViewHolder>{

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    Context context;
    List<BasketRouteDetailResponse> responseList;
    private static ClickListener clickListener;

    public BasketRouteDetailAdapter(Context context, List<BasketRouteDetailResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.basket_route_detail_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {
        myViewHolder.tvItemPalletID.setText(String.valueOf(responseList.get(i).getPalletID()));
        myViewHolder.tvItemHang.setText(String.valueOf(responseList.get(i).getPackTotal()));
        myViewHolder.tvItemXacNhan.setText(String.valueOf(responseList.get(i).getPackConfirmTotal()));
        myViewHolder.edtKhayRo.setText(String.valueOf(responseList.get(i).getBasketTotal()));

        myViewHolder.tvItemHang.setOnClickListener(view -> {
            if (context instanceof BasketRouteDetailActivity) {
                ((BasketRouteDetailActivity)context).showDialogUpdateXacNhan(String.valueOf(responseList.get(i).getPalletID()));
                notifyItemChanged(i);
            }
        });

        myViewHolder.btnTru.setOnClickListener(view -> {
            if (Integer.parseInt(myViewHolder.edtKhayRo.getText().toString() == null ? "0" : myViewHolder.edtKhayRo.getText().toString()) > 0) {
                responseList.get(i).setBasketTotal(Integer.parseInt(myViewHolder.edtKhayRo.getText().toString() == null ? "0" : myViewHolder.edtKhayRo.getText().toString()) - 1);
                notifyDataSetChanged();
                myViewHolder.edtKhayRo.setText(String.valueOf(responseList.get(i).getBasketTotal()));

            }
        });

        myViewHolder.btnCong.setOnClickListener(view -> {
            if (context instanceof BasketRouteDetailActivity) {
                ((BasketRouteDetailActivity)context).updateOneByIndex(responseList.get(i), i);
            }
        });

        myViewHolder.edtKhayRo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!myViewHolder.edtKhayRo.getText().toString().isEmpty()) {
                    responseList.get(i).setBasketTotal(Integer.parseInt(myViewHolder.edtKhayRo.getText().toString()));
                }
                else {
                    responseList.get(i).setBasketTotal(0);
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

    public void setOnItemClickListener(ClickListener clickListener) {
        BasketRouteDetailAdapter.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemPalletID, tvItemHang, tvItemXacNhan;
        TextView btnTru, btnCong;
        EditText edtKhayRo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemPalletID = itemView.findViewById(R.id.itemPalletId);
            tvItemHang = itemView.findViewById(R.id.itemHang);
            tvItemXacNhan = itemView.findViewById(R.id.itemXacNhan);
            edtKhayRo = itemView.findViewById(R.id.itemKhayRo);
            btnTru = itemView.findViewById(R.id.btnTruKhayRo);
            btnCong = itemView.findViewById(R.id.btnCongKhayRo);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }
}
