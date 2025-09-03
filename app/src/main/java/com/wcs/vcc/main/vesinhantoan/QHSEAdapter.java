package com.wcs.vcc.main.vesinhantoan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wcs.wcs.R;
import com.wcs.vcc.api.InsertQHSEParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.viewImage.ViewImageActivity;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class QHSEAdapter extends ArrayAdapter<QHSEInfo> implements View.OnClickListener, Filterable {
    private static final String TAG = QHSEAdapter.class.getSimpleName();
    private final Picasso picasso;
    private final int storeId;
    private LayoutInflater inflater;
    private String userName, positionGroup;
    private ArrayList<QHSEInfo> dataOrigin;
    private ArrayList<QHSEInfo> dataRelease;

    public QHSEAdapter(Context context, ArrayList<QHSEInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userName = LoginPref.getInfoUser(context, LoginPref.USERNAME);
        positionGroup = LoginPref.getInfoUser(context, LoginPref.POSITION_GROUP);
        picasso = Utilities.getPicasso(context);
        dataOrigin = objects;
        dataRelease = objects;
        storeId = LoginPref.getStoreId(context);
    }

    @Override
    public QHSEInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_qhse, parent, false);
            holder = new ViewHolder(convertView);
            holder.ivEditor.setOnClickListener(this);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final QHSEInfo info = getItem(position);
        holder.tvSubject.setText(info.getSubject());
        holder.tvComment.setText(info.getComment());
        holder.tvCreateTime.setText(String.format("%s bởi %s", info.getCreatedTime(), info.getCreatedBy()));
        holder.tvArea.setText(String.format("Khu: %s", info.getLocation()));
        holder.tvCategory.setText(String.format("Loại: %s", info.getCategory()));
        holder.tvCommentQty.setText(String.format(Locale.US, "%d comment", info.getCommentQty()));
        holder.tvLastCommentBy.setText(info.getLatestCommentBy());

        final String comment = info.getLatestComment();

        if (info.getLatestCommentBy().length() > 0) {
            holder.tvLastCommentBy.setVisibility(View.VISIBLE);
            if (comment.length() == 36 && comment.endsWith(".jpg")) {
                holder.ivLastComment.setVisibility(View.VISIBLE);
                holder.tvLastComment.setVisibility(View.GONE);
                Utilities.getPicasso(getContext()).load(Utilities.generateUrlImage(getContext(), comment)).into(holder.ivLastComment);
            } else {
                holder.tvLastComment.setVisibility(View.VISIBLE);
                holder.ivLastComment.setVisibility(View.GONE);
                holder.tvLastComment.setText(comment);
            }
        } else {
            holder.tvLastCommentBy.setVisibility(View.GONE);
            holder.tvLastComment.setVisibility(View.GONE);
            holder.ivLastComment.setVisibility(View.GONE);
        }

        int length = holder.tvCategory.getText().length() + holder.tvArea.getText().length();
        if (length > 35)
            holder.llAreaCategory.setOrientation(LinearLayout.VERTICAL);
        else
            holder.llAreaCategory.setOrientation(LinearLayout.HORIZONTAL);


        if (!userName.equalsIgnoreCase(info.getCreatedBy().split(" - ")[0]) && !Group.isEqualGroup(positionGroup, Group.MANAGER) && !Group.isEqualGroup(positionGroup, Group.SUPERVISOR)) {
            holder.ivEditor.setVisibility(View.GONE);
        } else {
            holder.ivEditor.setVisibility(View.VISIBLE);
            holder.ivEditor.setTag(info);
        }

        LinearLayout.LayoutParams params = getLayoutParamsImage();
        holder.ivImage.setLayoutParams(params);
        if (info.getPhotoAttachment().equalsIgnoreCase("")) {
            holder.ivImage.setVisibility(View.GONE);
        } else {
            String path = Utilities.generateUrlImage(getContext(), info.getPhotoAttachment());
            picasso.load(path).into(holder.ivImage);
            holder.ivImage.setVisibility(View.VISIBLE);
        }
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewImageActivity.class);
                intent.putExtra("src", info.getPhotoAttachment());
                getContext().startActivity(intent);
            }
        });
        holder.tvCommentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra(CommentActivity.QHSE_ID, info.getQHSEID());
                intent.putExtra(CommentActivity.QHSE_SUBJECT, info.getSubject());
                getContext().startActivity(intent);

            }
        });
        holder.tvCommentQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });
        holder.tvLastComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });
        holder.ivLastComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewImageActivity.class);
                intent.putExtra("src", info.getLatestComment());
                getContext().startActivity(intent);
            }
        });
        holder.tvLastCommentBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });

        return convertView;
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParamsImage() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width / 2);
        params.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics));
        return params;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<QHSEInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        QHSEInfo info = dataOrigin.get(i);
                        String type = info.getCategory();
                        if (type.equalsIgnoreCase(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (ArrayList<QHSEInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onClick(final View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.qhse_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_qhse_edit) {
                    QHSEInfo info = (QHSEInfo) v.getTag();
                    Intent intent = new Intent(getContext(), UpdateQHSEActivity.class);
                    intent.putExtra(UpdateQHSEActivity.QHSEID, info.getQHSEID());
                    intent.putExtra(UpdateQHSEActivity.QHSENUMBER, info.getQHSENumber());
                    intent.putExtra(UpdateQHSEActivity.CreatedTime, info.getCreatedTime());
                    intent.putExtra(UpdateQHSEActivity.Subject, info.getSubject());
                    intent.putExtra(UpdateQHSEActivity.Category, info.getCategory());
                    intent.putExtra(UpdateQHSEActivity.Comment, info.getComment());
                    intent.putExtra(UpdateQHSEActivity.Location, info.getLocation());
                    intent.putExtra(UpdateQHSEActivity.PhotoAttachment, info.getPhotoAttachment());
                    getContext().startActivity(intent);
                } else if (item.getItemId() == R.id.action_qhse_delete) {
                    alertDelete(v);
                }
                return true;
            }
        });
        popupMenu.show();

    }

    private void alertDelete(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn xóa bài viết này?").setPositiveButton("Không", null).setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InsertQHSEParameter parameter = new InsertQHSEParameter(userName, ((QHSEInfo) v.getTag()).getQHSEID(), 3, storeId);
                deleteQHSE(v, parameter);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteQHSE(final View view, InsertQHSEParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), getContext().getString(R.string.deleting));
        dialog.show();

        if (!WifiHelper.isConnected(getContext())) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertQHSE(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    remove((QHSEInfo) view.getTag());
                    Log.e(TAG, "onResponse: xóa success");
                    Snackbar.make(view, "Đã xóa", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "onResponse: xóa failed");
                    Snackbar.make(view, getContext().getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
                dismissDialog(dialog);
            }
        });
    }

    private void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_qhse_subject)
        TextView tvSubject;
        @BindView(R.id.tv_qhse_comment)
        TextView tvComment;
        @BindView(R.id.tv_qhse_create_time)
        TextView tvCreateTime;
        @BindView(R.id.iv_qhse_editor)
        ImageView ivEditor;
        @BindView(R.id.iv_qhse_image)
        ImageView ivImage;
        @BindView(R.id.tv_qhse_comment_click)
        TextView tvCommentClick;
        @BindView(R.id.tv_qhse_area)
        TextView tvArea;
        @BindView(R.id.tv_qhse_category)
        TextView tvCategory;
        @BindView(R.id.ll_qhse_area_category)
        LinearLayout llAreaCategory;
        @BindView(R.id.tv_qhse_comment_qty)
        TextView tvCommentQty;
        @BindView(R.id.item_tv_qhse_LastCommentBy)
        TextView tvLastCommentBy;
        @BindView(R.id.item_tv_qhse_LastComment)
        TextView tvLastComment;
        @BindView(R.id.item_iv_qhse_LastComment)
        ImageView ivLastComment;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}