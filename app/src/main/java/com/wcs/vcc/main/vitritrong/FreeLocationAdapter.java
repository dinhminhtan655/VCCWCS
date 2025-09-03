package com.wcs.vcc.main.vitritrong;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wcs.wcs.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class FreeLocationAdapter extends ArrayAdapter<FreeLocationInfo> {
    private LayoutInflater inflater;

    public FreeLocationAdapter(Context context, List<FreeLocationInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_free_location, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        FreeLocationInfo info = getItem(position);
        if (info != null) {
            holder.tvFreeQty.setText(String.valueOf(info.getQtyOfFree()));
            holder.tvPalletQty.setText(String.valueOf(info.getQtyOfPallets_OnHand()));
            holder.tvH.setText(String.valueOf(info.getQtyFree_High()));
            holder.tvL.setText(String.valueOf(info.getQtyFree_Low()));
            holder.tvRoomID.setText(info.RoomNumber);
            holder.tvVH.setText(String.valueOf(info.getQtyFree_VeryHigh()));
            holder.tvVL.setText(String.valueOf(info.getQtyFree_VeryLow()));
            holder.tvFreeLocationBusy.setText(String.valueOf(info.getQtyOfPallets_OnHand()));
            holder.tvTotal.setText(String.valueOf(info.getQtyLocation()));
            holder.tvOccupancy.setText(String.format(Locale.US, "%.2f", (info.Occupancy* 100)));
            holder.tvOff.setText(String.valueOf(info.getQtyLocationOff()));
            if (position % 2 == 0)
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
            else convertView.setBackgroundColor(Color.WHITE);

            if (position == getCount() -1 ){
                convertView.setBackgroundColor(Color.YELLOW);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvFreeLocationFreeQty)
        TextView tvFreeQty;
        @BindView(R.id.tvFreeLocationH)
        TextView tvH;
        @BindView(R.id.tvFreeLocationL)
        TextView tvL;
        @BindView(R.id.tvFreeLocationVH)
        TextView tvVH;
        @BindView(R.id.tvFreeLocationVL)
        TextView tvVL;
        @BindView(R.id.tvFreeLocationBusy)
        TextView tvFreeLocationBusy;
        @BindView(R.id.tvFreeLocationPalletQty)
        TextView tvPalletQty;
        @BindView(R.id.tvFreeLocationRoomID)
        TextView tvRoomID;
        @BindView(R.id.tvFreeLocationTotal)
        TextView tvTotal;
        @BindView(R.id.tvFreeLocationOccupancy)
        TextView tvOccupancy;
        @BindView(R.id.tvFreeLocationOffQty)
        TextView tvOff;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}