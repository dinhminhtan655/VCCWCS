package com.wcs.vcc.main.QLKhayRo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.QLKhayRo.BasketRouteActivity;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteResponse;

import java.util.List;

public class BasketRouteAdapter extends RecyclerView.Adapter<BasketRouteAdapter.MyViewHolder> {

    Context context;
    List<BasketRouteResponse> basketRouteResponseList;

    public BasketRouteAdapter(Context context, List<BasketRouteResponse> basketRouteResponseList) {
        this.context = context;
        this.basketRouteResponseList = basketRouteResponseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.basket_route_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {
        myViewHolder.tvRouteNo.setText(basketRouteResponseList.get(i).getRouteNo());
        myViewHolder.tvTotalPack.setText(String.valueOf(basketRouteResponseList.get(i).getPackTotal()));
        myViewHolder.tvTotalBasket.setText(String.valueOf(basketRouteResponseList.get(i).getBasketTotal()));
        myViewHolder.tvTotalBasketNhan.setText(String.valueOf(basketRouteResponseList.get(i).getBasketTotalReceived()));
        myViewHolder.tvCCDC.setText(String.valueOf(basketRouteResponseList.get(i).getTotalCCDC()));
        myViewHolder.tvCCDCNhan.setText(String.valueOf(basketRouteResponseList.get(i).getCcdcTotalReceived()));

        if (basketRouteResponseList.get(i).getStatus() == 1) {
            myViewHolder.tvRouteNo.setBackgroundColor(Color.parseColor("#f0f0f0"));
            myViewHolder.tvTotalPack.setBackgroundColor(Color.parseColor("#f0f0f0"));
            myViewHolder.tvTotalBasket.setBackgroundColor(Color.parseColor("#f0f0f0"));
            myViewHolder.tvTotalBasketNhan.setBackgroundColor(Color.parseColor("#f0f0f0"));
            myViewHolder.tvCCDC.setBackgroundColor(Color.parseColor("#f0f0f0"));
            myViewHolder.tvCCDCNhan.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }

        if (basketRouteResponseList.get(i).getStatus() == 2) {
            myViewHolder.tvRouteNo.setBackgroundColor(Color.parseColor("#ffb300"));
            myViewHolder.tvTotalPack.setBackgroundColor(Color.parseColor("#ffb300"));
            myViewHolder.tvTotalBasket.setBackgroundColor(Color.parseColor("#ffb300"));
            myViewHolder.tvTotalBasketNhan.setBackgroundColor(Color.parseColor("#ffb300"));
            myViewHolder.tvCCDC.setBackgroundColor(Color.parseColor("#ffb300"));
            myViewHolder.tvCCDCNhan.setBackgroundColor(Color.parseColor("#ffb300"));
        }

        if (basketRouteResponseList.get(i).getStatus() == 4) {
            myViewHolder.tvRouteNo.setBackgroundColor(Color.parseColor("#00ff5e"));
            myViewHolder.tvTotalPack.setBackgroundColor(Color.parseColor("#00ff5e"));
            myViewHolder.tvTotalBasket.setBackgroundColor(Color.parseColor("#00ff5e"));
            myViewHolder.tvTotalBasketNhan.setBackgroundColor(Color.parseColor("#00ff5e"));
            myViewHolder.tvCCDC.setBackgroundColor(Color.parseColor("#00ff5e"));
            myViewHolder.tvCCDCNhan.setBackgroundColor(Color.parseColor("#00ff5e"));
        }

        myViewHolder.tvCCDC.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).getBasketCCDCDetail(basketRouteResponseList.get(i).getRouteNo());
            }
        });

        myViewHolder.tvCCDCNhan.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).getBasketCCDCDetail(basketRouteResponseList.get(i).getRouteNo());
            }
        });

        myViewHolder.tvRouteNo.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).routeNoItemClick(basketRouteResponseList.get(i).getRouteNo());
            }
        });

        myViewHolder.tvTotalBasketNhan.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).getBasketReceivedDetail(basketRouteResponseList.get(i).getRouteNo());
            }
        });

        myViewHolder.tvTotalPack.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).gotoDetail(basketRouteResponseList.get(i).getRouteNo());
            }
        });

        myViewHolder.tvTotalBasket.setOnClickListener(view -> {
            if (context instanceof BasketRouteActivity) {
                ((BasketRouteActivity)context).gotoDetail(basketRouteResponseList.get(i).getRouteNo());
            }
        });
    }


    @Override
    public int getItemCount() {
        return basketRouteResponseList.size();
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
        TextView tvRouteNo, tvTotalPack, tvTotalBasket, tvTotalBasketNhan, tvCCDC, tvCCDCNhan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteNo = itemView.findViewById(R.id.tvRouteNo);
            tvTotalPack = itemView.findViewById(R.id.tvtotalPack);
            tvTotalBasket = itemView.findViewById(R.id.tvtotalBasket);
            tvTotalBasketNhan = itemView.findViewById(R.id.tvtotalBasketNhan);
            tvCCDC = itemView.findViewById(R.id.tvtotalCCDC);
            tvCCDCNhan = itemView.findViewById(R.id.tvtotalCCDCNhan);
        }
    }


}
