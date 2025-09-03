package com.wcs.vcc.mvvm.ui.base.activity.cyclecount;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemMassCycleCountBinding;

import java.util.ArrayList;
import java.util.List;

public class CycleCountAdapter extends RecyclerView.Adapter<CycleCountAdapter.CycleCountViewHolder> {


    public interface CycleCountListener {
        void onMovieClicked(MassCycleCountRemote remote);
    }

    private List<MassCycleCountRemote> list;
    private final CycleCountListener listener;

    public CycleCountAdapter(CycleCountListener listener) {
        this.listener = listener;
        list = new ArrayList<>();
    }

    public void setList(List<MassCycleCountRemote> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public CycleCountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMassCycleCountBinding binding = ItemMassCycleCountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CycleCountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CycleCountViewHolder holder, int position) {
        holder.bind(position);
    }

    private MassCycleCountRemote getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CycleCountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMassCycleCountBinding binding;

        CycleCountViewHolder(ItemMassCycleCountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            MassCycleCountRemote remote = getItem(position);

            setClickListener(remote);
            binding.tvCustomerNumber.setText(remote.getCustomerNumber());
            binding.tvCustomerName.setText(remote.getCustomerName());
            binding.tvMassNumber.setText(remote.getStockMovementMassNumber());
            binding.tvCreatedTime.setText(Utilities.formatDate_ddMMyyHHmm(remote.getCreatedTime()));

            if (position % 2 == 0)
                binding.getRoot().setBackgroundColor(Color.WHITE);
            else
                itemView.setBackgroundColor(Color.WHITE);

            if (remote.isStockMovementMassConfirm())
                binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.text_selected));

            if (remote.isCycleCountHideQty()) {
                binding.tvTicketType.setText("M");
                binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.completed));
            } else
                binding.tvTicketType.setText("T");

            if (remote.getCustomerNumber().equals(""))
                binding.tvTicketType.setText("N");

        }

        private void setClickListener(MassCycleCountRemote remote) {
            itemView.setTag(remote);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onMovieClicked((MassCycleCountRemote) v.getTag());
        }
    }
}
