package com.wcs.vcc.main.detailphieu.chuphinh;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class AttachmentInfoAdapter extends ArrayAdapter<AttachmentInfo> {
    private LayoutInflater inflater;

//    int positions;

    public AttachmentInfoAdapter(Context context, List<AttachmentInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder[] holder = new ViewHolder[1];

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_attachment_info, parent, false);
            holder[0] = new ViewHolder(convertView);
            convertView.setTag(holder[0]);
        } else holder[0] = (ViewHolder) convertView.getTag();
        AttachmentInfo info = getItem(position);
        holder[0].tvFileName.setText(info.getOriginalFileName());
        holder[0].tvFileSize.setText(String.format(Locale.getDefault(), "Size: %dkb", info.getAttachmentFileSize()));
        holder[0].tvFileCreateTime.setText(Utilities.formatDate_ddMMyyyyHHmm(info.getAttachmentDate()));
        holder[0].tvFileDescription.setText(info.getAttachmentDescription());
        holder[0].tvFileCreateUser.setText(String.format("User: %s", info.getAttachmentUser()));





        new AsyncTask<ViewHolder,Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(ViewHolder... viewHolders) {
                holder[0] = viewHolders[0];
                return info.getBitmap();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
//                if (positions == position)
                holder[0].img_file.setImageBitmap(info.getBitmap());
            }
        }.execute(holder);

        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.tv_file_name)
        TextView tvFileName;
        @BindView(R.id.tv_file_size)
        TextView tvFileSize;
        @BindView(R.id.tv_file_last_modifier)
        TextView tvFileCreateTime;
        @BindView(R.id.tv_file_description)
        TextView tvFileDescription;
        @BindView(R.id.tv_file_user)
        TextView tvFileCreateUser;
        @BindView(R.id.img_file)
        ImageView img_file;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
