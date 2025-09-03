package com.wcs.vcc.main.bigc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wcs.wcs.R;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


class SuppliersTodayAdapter extends ArrayAdapter<Supplier> implements Filterable {
    private final LayoutInflater inflater;
    private final SuppliersTodayActivity.BasketMovementReturnCallback callback;
    private List<Supplier> listOrigin;
    private List<Supplier> listRelease;

    SuppliersTodayAdapter(Context context, @NonNull ArrayList<Supplier> objects, SuppliersTodayActivity.BasketMovementReturnCallback callback) {
        super(context, 0, objects);
        this.callback = callback;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listRelease = objects;
        listOrigin = objects;
    }

    @Nullable
    @Override
    public Supplier getItem(int position) {
        return listRelease.get(position);
    }

    @Override
    public int getCount() {
        return listRelease.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_suppliers_today, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_suppliers_today_name);
            holder.tvOrderWeight = (TextView) convertView.findViewById(R.id.item_suppliers_today_booking_weight);
            holder.tvActualWeight = (TextView) convertView.findViewById(R.id.item_suppliers_today_actual_weight);
            holder.tvBasket = (TextView) convertView.findViewById(R.id.item_suppliers_today_basket);
            holder.tvBasketMovement = (TextView) convertView.findViewById(R.id.item_suppliers_today_basket_movement);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        final Supplier item = getItem(position);
        if (item != null) {
            holder.tvName.setText(item.getNameShort());
            holder.tvOrderWeight.setText(String.valueOf(item.getBookingWeight()));
            float actualWeight = item.getActualWeight();
            holder.tvActualWeight.setText(String.valueOf(actualWeight));

            if (item.getBasketMovementNumber().trim().length() > 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bigcLightCyan));
            } else if (actualWeight > 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bigcLightGreen));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            holder.tvBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getBasketQuantity() > 0) {
                        Intent intent = new Intent(getContext(), BasketMovementActivity.class);
                        intent.putExtra(SuppliersTodayActivity.POSITION, position);
                        intent.putExtra(BasketMovementActivity.PURCHASING_ORDER_NUMBER, item.getPurchasingOrderNumber());
                        intent.putExtra(BasketMovementActivity.PURCHASING_ORDER_ID, item.getId());
                        intent.putExtra(BasketMovementActivity.BM_NUMBER, item.getBasketMovementNumber());
                        ((SuppliersTodayActivity) getContext()).startActivityForResult(intent, SuppliersTodayActivity.RC_BASKET_MOVEMENT);
                    }
                }
            });
            SpannableString basket = new SpannableString(String.valueOf(item.getBasketQuantity()));
            if (item.getBasketQuantity() > 0) {
                basket.setSpan(new UnderlineSpan(), 0, basket.length(), 0);
                holder.tvBasket.setTextColor(Color.BLUE);
            } else {
                holder.tvBasket.setTextColor(Color.BLACK);

            }
            holder.tvBasket.setText(basket);

            holder.tvBasketMovement.setText(String.valueOf(item.getBasketQuantityReturn()));
            holder.tvBasketMovement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getBasketMovementNumber().trim().length() > 0) {
                        callback.onClick(item.getBasketMovementNumber());
                    }
                }
            });

        }

        return convertView;
    }


    private static class ViewHolder {
        TextView tvName;
        TextView tvOrderWeight;
        TextView tvActualWeight;
        TextView tvBasket;
        TextView tvBasketMovement;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<Supplier> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < listOrigin.size(); i++) {
                        Supplier info = listOrigin.get(i);
                        String name = Normalizer.normalize(info.getNameShort().toLowerCase(), Normalizer.Form.NFD);
                        if (name.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = listOrigin.size();
                    results.values = listOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listRelease = (ArrayList<Supplier>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
