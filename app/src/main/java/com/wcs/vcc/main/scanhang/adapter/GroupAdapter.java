package com.wcs.vcc.main.scanhang.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<String> numberGroupList;
    private RecyclerViewItemListener<String> onClick;

    private Context context;

    public GroupAdapter(RecyclerViewItemListener<String> onClick) {
        this.onClick = onClick;
    }

    public GroupAdapter() {
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_scan, viewGroup, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {
        String number = numberGroupList.get(i);
        groupViewHolder.tvNumberGroup.setText(number);
        groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(number,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (numberGroupList != null) {
            return numberGroupList.size();
        } else {
            return 0;
        }
    }

    public void setCustomerScan(Context context, List<String> numberGroupList) {
        this.context = context;
        this.numberGroupList = numberGroupList;
        notifyDataSetChanged();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNumberGroup)
        TextView tvNumberGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
