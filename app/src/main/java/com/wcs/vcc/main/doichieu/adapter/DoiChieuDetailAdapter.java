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

import com.wcs.wcs.R;import com.wcs.vcc.main.doichieu.model.ShiftConfirmDetail;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewPassTotal;

import java.util.List;

public class DoiChieuDetailAdapter extends RecyclerView.Adapter<DoiChieuDetailAdapter.DoiChieuDetailViewHodler> {

    private LayoutInflater inflater;
    public static List<ShiftConfirmDetail> shiftConfirmDetails;
    private RecyclerViewItemOrderListener<ShiftConfirmDetail> onClick;
    private RecyclerViewPassTotal<Integer> passTotal;


    public DoiChieuDetailAdapter(Context context, List<ShiftConfirmDetail> shiftConfirmDetails, RecyclerViewItemOrderListener<ShiftConfirmDetail> onClick, RecyclerViewPassTotal<Integer> passTotal) {
        inflater = LayoutInflater.from(context);
        this.shiftConfirmDetails = shiftConfirmDetails;
        this.onClick = onClick;
        this.passTotal = passTotal;
    }

    @NonNull
    @Override
    public DoiChieuDetailViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_doi_chieu_detail, viewGroup, false);
        DoiChieuDetailViewHodler doiChieuViewHolder = new DoiChieuDetailViewHodler(view);
        return doiChieuViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DoiChieuDetailViewHodler doiChieuDetailViewHodler, int i) {
        ShiftConfirmDetail shiftConfirmDetail = shiftConfirmDetails.get(doiChieuDetailViewHodler.getAdapterPosition());
        doiChieuDetailViewHodler.itemTvItemName.setText(shiftConfirmDetail.itemName);
        doiChieuDetailViewHodler.itemTvAcctualSortQuantity.setText(String.valueOf(shiftConfirmDetail.acctualSortQuantity));
        doiChieuDetailViewHodler.itemEdtAcctualSortQuantity.setText(String.valueOf(shiftConfirmDetail.getQty_ConfirmDriver()));

        if (i % 2 == 0) {
            doiChieuDetailViewHodler.itemView.setBackgroundColor(R.color.generalGreen);
        }

    }

    @Override
    public int getItemCount() {
        return shiftConfirmDetails.size();
    }

    public List<ShiftConfirmDetail> passList() {
        return shiftConfirmDetails;
    }

    private int totalActive(int total){
        return total;
    }






    public class DoiChieuDetailViewHodler extends RecyclerView.ViewHolder {

        protected TextView itemTvItemName, itemTvAcctualSortQuantity;
        private EditText itemEdtAcctualSortQuantity;

        public DoiChieuDetailViewHodler(@NonNull View itemView) {
            super(itemView);

            itemTvItemName = itemView.findViewById(R.id.itemTvItemName);
            itemTvAcctualSortQuantity = itemView.findViewById(R.id.itemTvAcctualSortQuantity);
            itemEdtAcctualSortQuantity = itemView.findViewById(R.id.itemEdtAcctualSortQuantity);

            itemEdtAcctualSortQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    shiftConfirmDetails.get(getAdapterPosition()).setQty_ConfirmDriver(itemEdtAcctualSortQuantity.getText().toString().equals("") ? 0 : Integer.parseInt(itemEdtAcctualSortQuantity.getText().toString()));
                    int sl = 0;
                    for (ShiftConfirmDetail s : shiftConfirmDetails){
                        sl+= s.getQty_ConfirmDriver();
                    }
                    totalActive(sl);
                    passTotal.totalCount(sl);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}
