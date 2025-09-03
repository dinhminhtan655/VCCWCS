package com.wcs.vcc.main.palletcartonchecking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcs.wcs.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class MovementHistoryAdapter extends ArrayAdapter<MovementHistoryInfo> {
    private LayoutInflater inflater;

    public MovementHistoryAdapter(Context context, List<MovementHistoryInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_movement_history_pallet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        MovementHistoryInfo info = getItem(position);
        holder.tvDateMovement.setText(info.getDateMovement());
        holder.tvAuthor.setText(info.getAuthorisedBy());
        holder.tvFrom.setText(info.getLocationNumber());
        holder.tvTo.setText(info.getLocationTo());
        String reasonMovement = info.getReasonMovement();
        holder.tvReason.setText(reasonMovement);
        holder.tvRef.setText(String.format("Qty: %.2f   Ref: %s", info.getQuantity(), info.getRemark()));
        if (reasonMovement.equalsIgnoreCase("Moved")) {
            holder.iv.setImageResource(R.drawable.left_arrow24x24);
        } else if (reasonMovement.equalsIgnoreCase("Reversed")) {
            holder.iv.setImageResource(R.drawable.adept_update);
        } else if (reasonMovement.equalsIgnoreCase("Joined")) {
            holder.iv.setImageResource(R.drawable.left_24x24);
        } else {
            holder.iv.setImageResource(R.drawable.actionbar_back_indicator);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvMovementHistoryDateMovement)
        TextView tvDateMovement;
        @BindView(R.id.tvMovementHistoryAuthorisedBy)
        TextView tvAuthor;
        @BindView(R.id.tvMovementHistoryRef)
        TextView tvRef;
        @BindView(R.id.tvMovementHistoryFromLocation)
        TextView tvFrom;
        @BindView(R.id.tvMovementHistoryToLocation)
        TextView tvTo;
        @BindView(R.id.tvMovementHistoryReasonMovement)
        TextView tvReason;
        @BindView(R.id.ivMovementHistory)
        ImageView iv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}