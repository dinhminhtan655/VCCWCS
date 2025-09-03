package com.wcs.vcc.main.detailphieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wcs.wcs.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Product implements Item {
    private String title;
    private int total;
    private String palletNumber;
    private String productNumber;

    public Product(String title, int total, String palletNumber, String productNumber) {
        this.title = title;
        this.total = total;
        this.palletNumber = palletNumber;
        this.productNumber = productNumber;
    }

    public String getTitle() {
        return title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProductNumber() {
        return productNumber;
    }

    @Override
    public View getItem(Context context, LayoutInflater inflater, View convertView, IOrderDetailListener listener) {
        final GroupViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof GroupViewHolder)) {
            convertView = inflater.inflate(R.layout.items_detail_phieu_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (GroupViewHolder) convertView.getTag();

        holder.prodName.setText(getTitle());
        holder.prodTotal.setText(String.valueOf(total));
        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.tv_prod_name)
        TextView prodName;
        @BindView(R.id.tv_prod_total)
        TextView prodTotal;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Product && ((Product) obj).palletNumber.equals(palletNumber);
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(palletNumber);
    }
}
