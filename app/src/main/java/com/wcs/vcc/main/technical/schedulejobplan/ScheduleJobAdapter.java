package com.wcs.vcc.main.technical.schedulejobplan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.mms.MaintenanceActivity;
import com.wcs.vcc.main.mms.MaintenanceJob;
import com.wcs.vcc.main.mms.add.CreateMaintenanceActivity;
import com.wcs.vcc.utilities.Utilities;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class ScheduleJobAdapter extends ArrayAdapter<ScheduleJobPlanInfo> implements Filterable {
    public static final String ID = "ID";
    public static final String WORKING_HOUR = "WORKING_HOUR";
    public static final String DATE = "DATE";
    public static final String SJ_ID = "SJ_ID";
    private static final String TAG = ScheduleJobAdapter.class.getSimpleName();
    private Calendar calendar = Calendar.getInstance();
    private int week = calendar.get(Calendar.WEEK_OF_YEAR);
    private View.OnClickListener intentToMJ = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ScheduleJobPlanInfo info = (ScheduleJobPlanInfo) v.getTag();
            Intent intent = new Intent(getContext(), CreateMaintenanceActivity.class);
            try {
                int sjID = Integer.parseInt(info.getId().replaceAll("\\D*", ""));
                intent.putExtra(SJ_ID, sjID);
            } catch (NumberFormatException ex){

            }
            intent.putExtra("TYPE", MaintenanceActivity.TYPE_CREATE);
            intent.putExtra(ID, info.getEquipmentID());
            intent.putExtra(MaintenanceJob.MAINTENANCE_JOB_ID, info.getMaintenanceJobID());
            intent.putExtra(WORKING_HOUR, info.getPlanHour());
            intent.putExtra(DATE, Utilities.getMillisecondFromDate(info.getDueDate()));
            ((AppCompatActivity) getContext()).startActivityForResult(intent, ScheduleJobActivity.REQUEST_CODE);
        }
    };

    private ArrayList<ScheduleJobPlanInfo> dataRelease;
    private ArrayList<ScheduleJobPlanInfo> dataOrigin;

    public ScheduleJobAdapter(Context context, ArrayList<ScheduleJobPlanInfo> objects) {
        super(context, R.layout.item_schedule_job, objects);
        dataOrigin = objects;
        dataRelease = objects;

    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public ScheduleJobPlanInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_job, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        ScheduleJobPlanInfo info = getItem(position);
        if (info.getEquipmentID().length() + info.getEquipmentName().length() > 40) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.tvEquipmentID);
            holder.tvEquipmentName.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.tvEquipmentID);
            holder.tvEquipmentName.setLayoutParams(params);
        }
        holder.tvEquipmentID.setText(info.getEquipmentID());
        holder.tvEquipmentName.setText(info.getEquipmentName());
        holder.tvFrequence.setText(info.getFrequence());
        holder.by.setText(info.getAssignedBy());
        holder.to.setText(String.format("To: %s", info.getAssignedTo()));
        holder.mjId.setText(String.format(Locale.getDefault(), "%d", info.getMaintenanceJobID()));
        holder.tvHours.setText(String.format("Hour: %s", NumberFormat.getInstance().format(info.getPlanHour())));
        String dueDate = info.getDueDate();
        holder.tvDate.setText(Utilities.formatDate_ddMMyyyy(dueDate));
        holder.mjBt.setTag(info);
        holder.mjBt.setOnClickListener(intentToMJ);
        calendar.setTimeInMillis(Utilities.getMillisecondFromDate(dueDate));
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (info.isScheduled_JobDone())
            convertView.setBackgroundColor(Color.WHITE);
        else {
            if (this.week > week)
                convertView.setBackgroundColor(Color.parseColor("#FF9900"));
            else if (this.week == week)
                convertView.setBackgroundColor(Color.parseColor("#FFFF99"));
            else if (this.week == week - 1)
                convertView.setBackgroundColor(Color.parseColor("#99CCFF"));
            else
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<ScheduleJobPlanInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        ScheduleJobPlanInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getEquipmentName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        if (info.getEquipmentID().toLowerCase().contains(constraint) || name.contains(constraint)
                                || Utilities.formatDate_ddMMyy(info.getDueDate()).contains(constraint)
                                || Integer.toString(info.getMaintenanceJobID()).contains(constraint))
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
                dataRelease = (ArrayList<ScheduleJobPlanInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @BindView(R.id.tvEquipmentID)
        TextView tvEquipmentID;
        @BindView(R.id.tvEquipmentName)
        TextView tvEquipmentName;
        @BindView(R.id.tvFrequence)
        TextView tvFrequence;
        @BindView(R.id.tvHours)
        TextView tvHours;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvEquipmentBy)
        TextView by;
        @BindView(R.id.tvEquipmentTo)
        TextView to;
        @BindView(R.id.tvMJId)
        TextView mjId;
        @BindView(R.id.item_schedule_job_mj)
        Button mjBt;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
