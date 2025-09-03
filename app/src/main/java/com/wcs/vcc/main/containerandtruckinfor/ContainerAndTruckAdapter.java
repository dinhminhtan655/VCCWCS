package com.wcs.vcc.main.containerandtruckinfor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wcs.vcc.main.containerandtruckinfor.model.ContainerAndTruckInfo;
import com.wcs.wcs.R;
import com.wcs.vcc.utilities.Utilities;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ContainerAndTruckAdapter extends ArrayAdapter<ContainerAndTruckInfo> implements Filterable {
    long currentTime = System.currentTimeMillis();
    private LayoutInflater inflater;

    private List<ContainerAndTruckInfo> list = new ArrayList<>();
    private List<ContainerAndTruckInfo> listFiltered = new ArrayList<>();


    public ContainerAndTruckAdapter(Context context, List<ContainerAndTruckInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = objects;
        listFiltered = objects;
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Nullable
    @Override
    public ContainerAndTruckInfo getItem(int position) {
        return listFiltered.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_container_truck, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final ContainerAndTruckInfo info = getItem(position);
        String phoneText = String.format(Locale.US, "0%d", info.DriverMobilePhone);
        SpannableString spanPhone = new SpannableString(phoneText);
        spanPhone.setSpan(new UnderlineSpan(), 0, phoneText.length(), 0);
        holder.tvNum.setText(info.ContainerNum);
        holder.tvCusName.setText(info.CustomerName);
        holder.tvCusNum.setText(info.CustomerNumber);
        holder.tvProgress.setText(String.format(Locale.US, "%d%%", info.TaskProgress));
        if (Utilities.notToday(info.TimeOut)) {
            holder.tvDefaultTime.setText(Utilities.formatDate_ddMMHHmm(info.TimeOut));
            holder.tvDefaultTime.setTextColor(Color.WHITE);
        } else {
            holder.tvDefaultTime.setText(Utilities.formatDate_HHmm(info.TimeOut));
            holder.tvDefaultTime.setTextColor(Color.WHITE);
        }

        if (Utilities.notToday(info.TimeIn)) {
            holder.tvTimeIn.setText(Utilities.formatDate_ddMMHHmm(info.TimeIn));
        } else {
            holder.tvTimeIn.setText(Utilities.formatDate_HHmm(info.TimeIn));
        }

        if(info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00")){
            holder.tvTimeIn.setText(Utilities.formatDate_ddMMyy(info.OrderDate));
        }

        String expect = Utilities.formatDate_HHmm(info.ExpectedProcessTime);
        if (expect.length() > 0) {
            holder.tvExpectTime.setText(expect);
            holder.tvExpectTime.setVisibility(View.VISIBLE);
        }
        holder.tvExpectTime.setVisibility(View.GONE);

        if (info.CustomerRequirement.length() > 0) {
            holder.tvRequirement.setText(String.format("Note: %s", info.CustomerRequirement));
            holder.tvRequirement.setVisibility(View.VISIBLE);
        } else
            holder.tvRequirement.setVisibility(View.GONE);
        if (info.DriverMobilePhone != 0) {
            holder.tvPhone.setText(spanPhone);
            holder.tvPhone.setVisibility(View.VISIBLE);
        } else holder.tvPhone.setVisibility(View.GONE);

        if (info.Reason.equals("N"))
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0x00, 0xC8, 0x53));
        else if (info.Reason.equals("X"))
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0xF1, 0xAD, 0x00));
        else
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0x3F, 0x51, 0xB5));
        holder.tvType.setText(String.format("%s %s", info.ContainerType, info.Reason));
        if (Utilities.getMillisecondFromDate(info.DefaultProcessTime) < currentTime)
//            convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorAlert));
            convertView.setBackgroundColor(Color.WHITE);
        else
            convertView.setBackgroundColor(Color.WHITE);

        if (!info.OrderNumber.equals(""))
            convertView.setBackgroundColor(Color.GRAY);
        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
                        , android.R.layout.simple_list_item_1
                        , new String[]{"Call", "Send Message"});
                final String phone = String.format(Locale.US, "0%d", info.DriverMobilePhone);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 1) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("smsto:" + phone));
                                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        getContext().startActivity(Intent.createChooser(intent, "Send via..."));
                                    }
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + phone));
                                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        getContext().startActivity(Intent.createChooser(intent, "Call via..."));
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                        .setTitle(phone)
                        .create();
                dialog.show();
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView tvNum;
        TextView tvCusName;
        TextView tvCusNum;
        TextView tvProgress;
        TextView tvTimeIn;
        TextView tvRequirement;
        TextView tvPhone;
        TextView tvType;
        TextView tvExpectTime;
        TextView tvDefaultTime;

        public ViewHolder(View view) {
            tvNum = (TextView) view.findViewById(R.id.tv_cont_num);
            tvCusName = (TextView) view.findViewById(R.id.tv_cont_customer_name);
            tvCusNum = (TextView) view.findViewById(R.id.tv_cont_number);
            tvProgress = (TextView) view.findViewById(R.id.tv_cont_progress);
            tvTimeIn = (TextView) view.findViewById(R.id.tv_cont_time_in);
            tvRequirement = (TextView) view.findViewById(R.id.tv_cont_requirement);
            tvPhone = (TextView) view.findViewById(R.id.tv_cont_phone);
            tvType = (TextView) view.findViewById(R.id.tv_cont_type);
            tvExpectTime = (TextView) view.findViewById(R.id.tv_cont_expect_time);
            tvDefaultTime = (TextView) view.findViewById(R.id.tv_cont_default_time);
        }
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
                    ArrayList<ContainerAndTruckInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ContainerAndTruckInfo info = list.get(i);
//                        String name = Normalizer.normalize(info.getCustomerName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String carNumber = Normalizer.normalize(info.ContainerNum.toLowerCase(), Normalizer.Form.NFD).replaceAll(" ", "").replaceAll("[^\\p{ASCII}]", "");
//                        String number = Normalizer.normalize(info.getCustomerNumber().toLowerCase(), Normalizer.Form.NFD).replaceAll("-", "");
                        if (carNumber.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = list.size();
                    results.values = list;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (List<ContainerAndTruckInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
