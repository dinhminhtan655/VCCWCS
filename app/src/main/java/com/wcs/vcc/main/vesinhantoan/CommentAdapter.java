package com.wcs.vcc.main.vesinhantoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class CommentAdapter extends ArrayAdapter<CommentInfo> {
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<CommentInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_qhse_comment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final CommentInfo info = getItem(position);
        holder.tvBy.setText(info.getCommentBy());

        final String comment = info.getComment();
        if (comment.length() == 36 && comment.endsWith(".jpg")) {
            holder.ivPhoto.setVisibility(View.VISIBLE);
            holder.tvComment.setVisibility(View.GONE);
            Utilities.getPicasso(getContext()).load(Utilities.generateUrlImage(getContext(), comment)).into(holder.ivPhoto);
        } else {
            holder.tvComment.setVisibility(View.VISIBLE);
            holder.ivPhoto.setVisibility(View.GONE);
            holder.tvComment.setText(comment);
        }

        holder.tvTime.setText(info.getCommentTime());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_tv_qhse_comment_by)
        TextView tvBy;
        @BindView(R.id.item_tv_qhse_comment_comment)
        TextView tvComment;
        @BindView(R.id.item_tv_qhse_comment_time)
        TextView tvTime;
        @BindView(R.id.item_iv)
        ImageView ivPhoto;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}