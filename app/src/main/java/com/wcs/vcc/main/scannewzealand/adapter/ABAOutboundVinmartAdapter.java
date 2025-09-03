package com.wcs.vcc.main.scannewzealand.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.AbaItemOutboundPackingNewzealandBinding;
import com.wcs.vcc.main.scannewzealand.model.XDockVinOutboundPackingViewNewZealandABA;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ABAOutboundVinmartAdapter extends DataBoundListAdapter<XDockVinOutboundPackingViewNewZealandABA, AbaItemOutboundPackingNewzealandBinding> {


    private View root;
    private RecyclerViewItemOrderListener<XDockVinOutboundPackingViewNewZealandABA> onClick;

    private static List<XDockVinOutboundPackingViewNewZealandABA> stringList;
    private static ArrayList<XDockVinOutboundPackingViewNewZealandABA> arrayList;

    public ABAOutboundVinmartAdapter(RecyclerViewItemOrderListener<XDockVinOutboundPackingViewNewZealandABA> onClick, List<XDockVinOutboundPackingViewNewZealandABA> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
        this.arrayList = new ArrayList<XDockVinOutboundPackingViewNewZealandABA>();
        this.arrayList.addAll(stringList);
    }



    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    @Override
    protected AbaItemOutboundPackingNewzealandBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aba_item_outbound_packing_newzealand, parent, false);
    }


    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<AbaItemOutboundPackingNewzealandBinding> holder, @SuppressLint("RecyclerView") int position2, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position2, payloads);
        if (stringList.get(position2).XacNhan.equals("1")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(false);
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.waiting);
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position2).isEnalble());
            holder.itemView.setBackgroundResource(stringList.get(position2).getColorWaiting());
            holder.itemView.setEnabled(stringList.get(position2).isEnalble());
        } else if (stringList.get(position2).XacNhan.equals("0")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(true);
            stringList.get(holder.getAdapterPosition()).setColorWorking(R.color.working);
            holder.itemView.setBackgroundResource(stringList.get(position2).getColorWorking());
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position2).isEnalble());
            holder.itemView.setEnabled(stringList.get(position2).isEnalble());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onClick.onClick(getItems().get(position2), position2, 0);
                }
            }, 100);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(getItems().get(position2), position2, 0);
                }
            });
        } else if (stringList.get(position2).XacNhan.equals("2")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(false);
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.completed);
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position2).isEnalble());
            holder.itemView.setBackgroundResource(stringList.get(position2).getColorWaiting());
            holder.itemView.setEnabled(stringList.get(position2).isEnalble());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void bind(AbaItemOutboundPackingNewzealandBinding binding, XDockVinOutboundPackingViewNewZealandABA item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        binding.tvSLMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item, position, 2);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(XDockVinOutboundPackingViewNewZealandABA oldItem, XDockVinOutboundPackingViewNewZealandABA newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinOutboundPackingViewNewZealandABA oldItem, XDockVinOutboundPackingViewNewZealandABA newItem) {
        return false;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stringList.clear();
        if (charText.length() == 0) {
            stringList.addAll(arrayList);
        } else {
            for (XDockVinOutboundPackingViewNewZealandABA st : arrayList) {
                if (st.Pallet_ID.toLowerCase(Locale.getDefault()).contains(charText)) {
                    stringList.add(st);
                }
            }
        }
        notifyDataSetChanged();

    }
}
