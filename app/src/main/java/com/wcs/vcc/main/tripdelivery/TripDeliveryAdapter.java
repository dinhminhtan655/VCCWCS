package com.wcs.vcc.main.tripdelivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemTripDeliveryBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

import butterknife.ButterKnife;

/**
 * Created by aang on 026/07/2018.
 */

public class TripDeliveryAdapter extends DataBoundListAdapter<TripDelivery, ItemTripDeliveryBinding> {

    private int altColor;
    private TripItemListener itemListener;
    private View viewSelected;
    private int cartonId;
    private View root;
    private Context context;



    public TripDeliveryAdapter(TripItemListener itemListener) {
        this.itemListener = itemListener;

    }

    @Override
    protected ItemTripDeliveryBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        ButterKnife.bind((Activity) context);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_trip_delivery, parent, false);
    }

    @Override
    public int getItemCount()    {
        return super.getItemCount();
    }

    @Override
    protected void bind(ItemTripDeliveryBinding binding, final TripDelivery item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.TripStatusID) {
                    case 1:
                        itemListener.onClick(item, position);
                        break;
                    case 2:
                        Toast.makeText(context, "Assigned", Toast.LENGTH_SHORT).show();
                        showDialogAssigned(item);
                        break;
                    case 3:
                        Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                        showDialogAccepted(item);
                        break;

                }
            }
        });

        if (item.TripStatusID == 1){
            root.setBackgroundColor(Color.GREEN);
        }else if(item.TripStatusID == 2){
            root.setBackgroundColor(Color.BLUE);
            binding.tvItemTripDeliveryQtyOrder.setTextColor(Color.WHITE);
            binding.tvItemTripDeliveryRouteDescription.setTextColor(Color.WHITE);
            binding.tvItemTripDeliveryTripId.setTextColor(Color.WHITE);
            binding.tvItemTripDeliveryView.setTextColor(Color.WHITE);
        }else if (item.TripStatusID == 3){
            root.setBackgroundColor(Color.YELLOW);
        }else {
            root.setBackgroundColor(Color.WHITE);
        }
    }

    private void showDialogAccepted(TripDelivery item) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alerLayout = inflater.inflate(R.layout.dialog_start_trip, null);

        TextView tvIdTrip = alerLayout.findViewById(R.id.tv_title_start_trip);
        EditText edtDistance = alerLayout.findViewById(R.id.edt_distance_start_trip);
        EditText edtFuel = alerLayout.findViewById(R.id.edt_fuel_start_trip);
        EditText edtNote = alerLayout.findViewById(R.id.edt_note_start_trip);

        tvIdTrip.setText("Mã chuyến: " + item.TripNumber);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alerLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("Bắt đầu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Đã gửi", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Đã hủy", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void showDialogAssigned(TripDelivery item) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Thông Báo");
        b.setMessage("Bạn có muốn nhận chuyến " + item.TripNumber + " ?");
        b.setCancelable(false);
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Bạn đã nhận chuyến", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        }).setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Bạn đã hủy chuyến xin cho biết lý do", Toast.LENGTH_SHORT).show();
                showDialogDenied();
                dialogInterface.cancel();
            }
        });

        AlertDialog al = b.create();
        al.show();
    }

    private void showDialogDenied() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alerLayout = inflater.inflate(R.layout.dialog_denied_trip, null);
        EditText edtContentDenied = alerLayout.findViewById(R.id.edt_dia_content_denied_trip);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Từ chối");
        alert.setView(alerLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Đã gửi", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Đã hủy", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    @Override
    protected boolean areItemsTheSame(TripDelivery oldItem, TripDelivery newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(TripDelivery oldItem, TripDelivery newItem) {
        return false;
    }

}



