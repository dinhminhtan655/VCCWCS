package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemCycleCountDetailBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CycleCountDetailAdapter extends RecyclerView.Adapter<CycleCountDetailAdapter.CycleCountDetailViewHolder> {


    public interface CycleCountDetailListener {
        void onClicked(MassCycleCountDetailRemote remote);
    }

    private List<MassCycleCountDetailRemote> list;
    private final CycleCountDetailListener listener;

    public CycleCountDetailAdapter(CycleCountDetailListener listener) {
        this.listener = listener;
        list = new ArrayList<>();
    }

    public void setList(List<MassCycleCountDetailRemote> remoteList) {
        this.list = remoteList;
        notifyDataSetChanged();
    }

    @Override
    public CycleCountDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCycleCountDetailBinding binding = ItemCycleCountDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CycleCountDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CycleCountDetailViewHolder holder, int position) {
        holder.bind(holder.getAbsoluteAdapterPosition());

        MassCycleCountDetailRemote remote = getItem(position);

        if (position % 2 == 0)
            holder.binding.getRoot().setBackgroundColor(holder.binding.getRoot().getResources().getColor(R.color.colorPrimary));
        else
            holder.binding.getRoot().setBackgroundColor(Color.WHITE);

        if (position > 0) {
            int i = position - 1;
            if (i < list.size() && remote.getOrderNumber().equalsIgnoreCase(list.get(i).getOrderNumber())) {
                remote.setGone(true);
            }
        }


        if (remote.isCheckingStatus()) {
            holder.binding.getRoot().setBackgroundColor(holder.binding.getRoot().getResources().getColor(R.color.text_selected));
            if (remote.getCheckingQuantity() != Integer.parseInt(remote.getTotalAfterDPQuantity() != null ? remote.getTotalAfterDPQuantity() : "0")) {
                remote.setRed(true);
            }


            try {
                if (!Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                        remote.getCheckingProductionDate()))).equals(remote.getProductionDate()) || !Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(Utilities.formatDateTimeToMilisecond(
                                remote.getCheckingUseByDate())))
                        .equals(remote.getUseByDate())) {
                    remote.setDateRed(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!remote.getCheckingLocationID().equals(remote.getLocationID())) {
                remote.setLocationRed(true);
            }
        }


        holder.binding.itemHeaderCycleCountDetail.setVisibility(remote.isGone() ? View.GONE : View.VISIBLE);

        holder.binding.itemTvCheckQty.setBackground(remote.isRed() ? holder.binding.getRoot().getResources().getDrawable(R.drawable.red_border)
                : holder.binding.getRoot().getResources().getDrawable(R.drawable.outline_right_bottom_dark));

        holder.binding.itemTvUseByDate.setBackground(remote.isDateRed() ? holder.binding.getRoot().getResources().getDrawable(R.drawable.red_border)
                : holder.binding.getRoot().getResources().getDrawable(R.drawable.outline_right_bottom_dark));

        holder.binding.itemLocationNum.setBackground(remote.isLocationRed() ? holder.binding.getRoot().getResources().getDrawable(R.drawable.red_border)
                : holder.binding.getRoot().getResources().getDrawable(R.drawable.outline_right_bottom_dark));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private MassCycleCountDetailRemote getItem(int position) {
        return list.get(position);
    }

    public class CycleCountDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemCycleCountDetailBinding binding;

        CycleCountDetailViewHolder(ItemCycleCountDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            MassCycleCountDetailRemote remote = getItem(position);

            setClickListener(remote);
            binding.itemTvPalletNumber.setText(remote.getPalletNumber());
            binding.itemTvAfterQty.setText(remote.getTotalAfterDPQuantity());
            binding.itemTvTotalOriginalQty.setText(String.valueOf(remote.getTotalOriginalQuantity()));
            binding.itemTvCheckQty.setText(String.valueOf(remote.getCheckingQuantity()));
            binding.itemTvUseByDate.setText(remote.getProductionDate() + "\n" + remote.getUseByDate());
            binding.itemLocationNum.setText(remote.getLocationNumber());
            binding.itemHeaderCycleCountDetail.setText(String.format("%s ~ %s ~ %s", remote.getCustomerNumber(), remote.getCustomerName(), remote.getOrderNumber()));


        }

        private void setClickListener(MassCycleCountDetailRemote remote) {
            itemView.setTag(remote);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClicked((MassCycleCountDetailRemote) v.getTag());
        }
    }
}
