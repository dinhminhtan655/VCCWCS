package com.wcs.vcc.main.bigcqa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wcs.wcs.R;import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;
import com.wcs.vcc.main.viewImage.ViewImageActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

public class QaPoProductPictureAdapter extends BaseAdapter {
    private final Picasso picasso;
    private List<AttachmentInfo> list;
    private LayoutInflater inflater;
    private Context context;

    public QaPoProductPictureAdapter(List<AttachmentInfo> list, Context context) {
        this.picasso = Utilities.getPicasso(context);
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = inflater.inflate(R.layout.item_picture_qa_po_product,null);
            viewHolder = new ViewHolder();
            viewHolder.imgQaPo = view.findViewById(R.id.imgQaPo);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        AttachmentInfo attachmentInfo = list.get(i);
        String path = Utilities.generateUrlImage(context, attachmentInfo.getAttachmentFile());
        picasso.load(path).into(viewHolder.imgQaPo);

        viewHolder.imgQaPo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putExtra("src", attachmentInfo.getAttachmentFile());
                context.startActivity(intent);
            }
        });


        return view;
    }


    static class ViewHolder{
        ImageView imgQaPo;
    }
}
