package com.wcs.vcc.main.doichieu.adapter;

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

import com.wcs.wcs.R;import com.wcs.vcc.main.doichieu.model.ShiftConfirm;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewPassTotal;

import java.util.List;

public class DoiChieuAdapter extends RecyclerView.Adapter<DoiChieuAdapter.DoiChieuViewHolder> {

    private LayoutInflater inflater;
    public static List<ShiftConfirm> shiftConfirms;
    private RecyclerViewItemOrderListener<ShiftConfirm> onClick;
    private RecyclerViewPassTotal<Integer> passTotal;

    public DoiChieuAdapter(Context context, List<ShiftConfirm> shiftConfirms, RecyclerViewItemOrderListener<ShiftConfirm> onClick, RecyclerViewPassTotal<Integer> passTotal) {
        inflater = LayoutInflater.from(context);
        this.shiftConfirms = shiftConfirms;
        this.onClick = onClick;
        this.passTotal = passTotal;
    }

    @NonNull
    @Override
    public DoiChieuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_doi_chieu, viewGroup, false);
        DoiChieuViewHolder doiChieuViewHolder = new DoiChieuViewHolder(view);
        return doiChieuViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DoiChieuViewHolder doiChieuViewHolder, int i) {
        ShiftConfirm shiftConfirm = shiftConfirms.get(doiChieuViewHolder.getAdapterPosition());
        doiChieuViewHolder.itemTVShipToName.setText(shiftConfirms.get(doiChieuViewHolder.getAdapterPosition()).shipToName);
        doiChieuViewHolder.itemTvPallet.setText(String.valueOf(shiftConfirms.get(doiChieuViewHolder.getAdapterPosition()).palletID));
        doiChieuViewHolder.itemEdtKhay.setText(String.valueOf(shiftConfirms.get(doiChieuViewHolder.getAdapterPosition()).box_ConfirmDriver));
        doiChieuViewHolder.itemEdtVi.setText(String.valueOf(shiftConfirms.get(doiChieuViewHolder.getAdapterPosition()).qty_ConfirmDriver));

        doiChieuViewHolder.itemTVShipToName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(shiftConfirm,doiChieuViewHolder.getAdapterPosition(),0);
            }
        });

        if (i %2 ==0){
            doiChieuViewHolder.itemView.setBackgroundColor(R.color.generalGreen);
        }
    }

    @Override
    public int getItemCount() {
        return shiftConfirms.size();
    }

    public List<ShiftConfirm> passList(){
        return shiftConfirms;
    }

    private int totalActive(int total){
        return total;
    }

    private int totalActive2(int total){
        return total;
    }



    public class DoiChieuViewHolder extends RecyclerView.ViewHolder {

        protected TextView itemTVShipToName, itemTvPallet;
        private EditText itemEdtKhay, itemEdtVi;

        public DoiChieuViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTVShipToName = itemView.findViewById(R.id.itemTVShipToName);
            itemTvPallet = itemView.findViewById(R.id.itemTvPallet);
            itemEdtKhay = itemView.findViewById(R.id.itemEdtKhay);
            itemEdtVi = itemView.findViewById(R.id.itemEdtVi);

            itemEdtKhay.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    shiftConfirms.get(getAdapterPosition()).setBox_ConfirmDriver(itemEdtKhay.getText().toString().equals("") ? 0 : Integer.parseInt(itemEdtKhay.getText().toString()));
                    int sl = 0;
                    for (ShiftConfirm s : shiftConfirms){
                        sl+= s.getBox_ConfirmDriver();
                    }
                    totalActive(sl);
                    passTotal.totalCount(sl);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            itemEdtVi.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    shiftConfirms.get(getAdapterPosition()).setTongBich(itemEdtVi.getText().toString().equals("") ? 0 :  Integer.parseInt(itemEdtVi.getText().toString()));
                    int sl = 0;
                    for (ShiftConfirm s : shiftConfirms){
                        sl+= s.getTongBich();
                    }
                    totalActive2(sl);
                    passTotal.totalCount2(sl);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }
    }
}
