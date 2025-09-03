package com.wcs.vcc.main.scanvinmartv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinOutboundPackingViewSupplierABA;

import java.util.List;

public class ABASpinnerSupplierAdapter extends BaseAdapter {

    Context context;
    List<XDockVinOutboundPackingViewSupplierABA> list;
    LayoutInflater inflter;
    private int mSelectedIndex = -1;


    public ABASpinnerSupplierAdapter(Context context, List<XDockVinOutboundPackingViewSupplierABA> list) {
        this.context = context;
        this.list = list;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_spinner_supplier, null);
        TextView tvSpinSupplier = view.findViewById(R.id.tvSpinSupplier);
        TextView tvSpinStatus = view.findViewById(R.id.tvSpinStatus);

        if (list.get(i).totalMove == 0) {
            tvSpinStatus.setText("Chưa");
        }else if (list.get(i).totalMove > 0 && list.get(i).totalMove < list.get(i).totalBich){
            tvSpinStatus.setText("Đang");
        }else if (list.get(i).totalMove == list.get(i).totalBich){
            tvSpinStatus.setText("Xong");
        }
        tvSpinSupplier.setText(list.get(i).Supplier_Name);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

}
