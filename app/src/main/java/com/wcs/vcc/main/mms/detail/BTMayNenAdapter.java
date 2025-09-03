package com.wcs.vcc.main.mms.detail;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.api.MaintenanceJobDetailUpdateParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class BTMayNenAdapter extends RecyclerView.Adapter<BTMayNenAdapter.VH> implements MJDetailAdapter {
    private static final String TAG = BTMayNenAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<Object> objects;
    private Context context;
    private View snackBarView;
    private String[] mjJob;
    private ArrayList<Integer> jobChoose = new ArrayList<>();
    private String userName;
    private SparseBooleanArray itemSelected = new SparseBooleanArray();
    private int changePosition = -1;

    public BTMayNenAdapter(Context context, ArrayList<Object> objects, View view) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = objects;
        this.context = context;
        snackBarView = view;
        userName = LoginPref.getUsername(context);
        mjJob = context.getResources().getStringArray(R.array.mj_job);
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position) instanceof Header ? 0 : 1;
    }

    public void setSelected(int position) {
        if (!itemSelected.get(position, false)) {
            if (itemSelected.size() > 0) {
                int key = itemSelected.keyAt(0);
                itemSelected.put(key, false);
                notifyItemChanged(key);
                itemSelected.delete(key);
            }
            itemSelected.put(position, true);
            notifyItemChanged(position);

        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_mj_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_mj_detail, parent, false);
            return new DetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            Header header = (Header) objects.get(position);
            ((HeaderViewHolder) holder).titleTV.setText(header.getTitle());
        } else {
            final MaintenanceJobDetail detail = (MaintenanceJobDetail) objects.get(position);
            final DetailViewHolder detailHolder = (DetailViewHolder) holder;
            if (itemSelected.get(position))
                detailHolder.root.setSelected(true);
            else
                detailHolder.root.setSelected(false);
            detailHolder.nameTV.setText(detail.getItemName());
            detailHolder.resultCB.setChecked(detail.isCheckResult());
            detailHolder.noteET.setText(detail.getRemark());
            detailHolder.jobTV.setText(detail.getResult());
            detailHolder.jobTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    jobChoose.clear();
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMultiChoiceItems(R.array.mj_job_detail, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    Integer i = which;
                                    if (jobChoose.contains(i)) {
                                        jobChoose.remove(i);
                                    } else jobChoose.add(i);
                                    StringBuilder builder = new StringBuilder();
                                    for (Integer j : jobChoose) {
                                        builder.append(mjJob[j]).append("+");
                                    }
                                    if (jobChoose.size() > 0)
                                        builder.deleteCharAt(builder.length() - 1);
                                    String result = builder.toString();
                                    ((TextView) v).setText(result);
                                    detail.setResult(result);
                                    updateMaintenanceJobDetail(detail.getId(), detail.getResult(), detail.getRemark(), userName);

                                }
                            })
                            .setNegativeButton(context.getString(R.string.label_cancel), null)
                            .create();
                    dialog.show();
                }
            });
            detailHolder.noteET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        changePosition = holder.getAdapterPosition();
                    } else {
                        if (changePosition >= 0) {
                            detail.setRemark(detailHolder.noteET.getText().toString());
                            MaintenanceJobDetail detail = (MaintenanceJobDetail) objects.get(changePosition);
                            updateMaintenanceJobDetail(detail.getId(), detail.getResult(), detail.getRemark(), userName);
                        }
                        changePosition = -1;
                    }
                }
            });
            detailHolder.updateIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMaintenanceJobDetail(detail.getId(), detail.getResult(), detail.getRemark(), userName);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private void updateMaintenanceJobDetail(int maintenanceJobDetailID, String result, String remark, String userName) {
        if (!WifiHelper.isConnected(context)) {
            RetrofitError.errorNoAction(context, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(context)
                .updateMaintenanceJobDetail(new MaintenanceJobDetailUpdateParameter(maintenanceJobDetailID, result, remark, userName))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(context, t, TAG, snackBarView);
                    }
                });
    }

    @Override
    public void updateAll() {

    }

    class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }

    private class DetailViewHolder extends VH implements View.OnClickListener {
        TextView nameTV;
        TextView jobTV;
        CheckBox resultCB;
        EditText noteET;
        ImageView updateIV;
        LinearLayout root;

        DetailViewHolder(View view) {
            super(view);
            nameTV = (TextView) view.findViewById(R.id.item_mj_detail_name);
            jobTV = (TextView) view.findViewById(R.id.item_mj_detail_job);
            resultCB = (CheckBox) view.findViewById(R.id.item_mj_detail_result);
            noteET = (EditText) view.findViewById(R.id.item_mj_detail_note);
            updateIV = (ImageView) view.findViewById(R.id.item_mj_detail_update);
            root = (LinearLayout) view.findViewById(R.id.item_mj_detail_root);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setSelected(getAdapterPosition());
        }
    }

    private class HeaderViewHolder extends VH {
        TextView titleTV;

        public HeaderViewHolder(View view) {
            super(view);
            titleTV = (TextView) view.findViewById(R.id.item_mj_detail_header_title);
        }
    }
}