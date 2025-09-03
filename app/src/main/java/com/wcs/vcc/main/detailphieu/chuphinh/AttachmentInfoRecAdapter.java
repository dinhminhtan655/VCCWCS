package com.wcs.vcc.main.detailphieu.chuphinh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemsAttachmentInfoBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class AttachmentInfoRecAdapter extends DataBoundListAdapter<AttachmentInfo, ItemsAttachmentInfoBinding> {

    private View root;
    private RecyclerViewItemOrderListener<AttachmentInfo> onClick;
    private Context context;

    private static List<AttachmentInfo> stringList;
    private static ArrayList<AttachmentInfo> arrayList;

    public AttachmentInfoRecAdapter(Context context, RecyclerViewItemOrderListener<AttachmentInfo> onClick, List<AttachmentInfo> stringList) {
        this.context = context;
        this.onClick = onClick;
        this.stringList = stringList;
        this.arrayList = new ArrayList<AttachmentInfo>();
        this.arrayList.addAll(stringList);
    }

    @Override
    protected ItemsAttachmentInfoBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.items_attachment_info, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemsAttachmentInfoBinding> holder, @SuppressLint("RecyclerView") int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.binding.tvFileName.setText(stringList.get(holder.getAdapterPosition()).originalFileName == null ? "File Name: " + "" : "File Name: " + stringList.get(holder.getAdapterPosition()).originalFileName);
        holder.binding.tvFileSize.setText("Size: " + String.valueOf(stringList.get(holder.getAdapterPosition()).attachmentFileSize));
        holder.binding.tvFileLastModifier.setText(Utilities.formatDate_ddMMyyyyHHmm(stringList.get(holder.getAdapterPosition()).getAttachmentDate()));
        holder.binding.tvFileUser.setText("User: " + stringList.get(holder.getAdapterPosition()).attachmentUser);
        holder.binding.tvFileDescription.setText(stringList.get(holder.getAdapterPosition()).attachmentDescription);
        holder.binding.imgFile.setImageBitmap(stringList.get(holder.getAdapterPosition()).getBitmap());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(getItems().get(position), position, 0);
            }
        });
    }

    @Override
    protected void bind(ItemsAttachmentInfoBinding binding, AttachmentInfo item, int position) {
        binding.setItem(item);
        root = binding.getRoot();
    }

    @Override
    protected boolean areItemsTheSame(AttachmentInfo oldItem, AttachmentInfo newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(AttachmentInfo oldItem, AttachmentInfo newItem) {
        return false;
    }
}
