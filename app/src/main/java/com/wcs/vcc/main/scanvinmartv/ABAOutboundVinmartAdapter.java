package com.wcs.vcc.main.scanvinmartv;

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
import com.wcs.vcc.api.XDockVinOutboundPackingViewABA;
import com.wcs.wcs.databinding.AbaItemOutboundPackingBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ABAOutboundVinmartAdapter   extends DataBoundListAdapter<XDockVinOutboundPackingViewABA, AbaItemOutboundPackingBinding> {


    private View root;
    private RecyclerViewItemOrderListener<XDockVinOutboundPackingViewABA> onClick;

    private static List<XDockVinOutboundPackingViewABA> stringList;
    private static ArrayList<XDockVinOutboundPackingViewABA> arrayList;

    public ABAOutboundVinmartAdapter(RecyclerViewItemOrderListener<XDockVinOutboundPackingViewABA> onClick, List<XDockVinOutboundPackingViewABA> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
        this.arrayList = new ArrayList<XDockVinOutboundPackingViewABA>();
        this.arrayList.addAll(stringList);
    }



    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    @Override
    protected AbaItemOutboundPackingBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aba_item_outbound_packing, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<AbaItemOutboundPackingBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (stringList.get(position).XacNhan.equals("1")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(false);
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.waiting);
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
            holder.itemView.setBackgroundResource(stringList.get(position).getColorWaiting());
            holder.itemView.setEnabled(stringList.get(position).isEnalble());
        } else if (stringList.get(position).XacNhan.equals("0")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(true);
            stringList.get(holder.getAdapterPosition()).setColorWorking(R.color.working);
            holder.itemView.setBackgroundResource(stringList.get(position).getColorWorking());
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
            holder.itemView.setEnabled(stringList.get(position).isEnalble());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onClick.onClick(getItems().get(position), position, 0);
                }
            }, 100);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(getItems().get(position), position, 0);
                }
            });
        } else if (stringList.get(position).XacNhan.equals("2")) {
            stringList.get(holder.getAdapterPosition()).setEnalble(false);
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.completed);
//            holder.binding.btnMove.setEnabled(stringList.get(position).isEnalble());
            holder.binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
            holder.itemView.setBackgroundResource(stringList.get(position).getColorWaiting());
            holder.itemView.setEnabled(stringList.get(position).isEnalble());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void bind(AbaItemOutboundPackingBinding binding, XDockVinOutboundPackingViewABA item, int position) {
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
    protected boolean areItemsTheSame(XDockVinOutboundPackingViewABA oldItem, XDockVinOutboundPackingViewABA newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinOutboundPackingViewABA oldItem, XDockVinOutboundPackingViewABA newItem) {
        return false;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stringList.clear();
        if (charText.length() == 0) {
            stringList.addAll(arrayList);
        } else {
            for (XDockVinOutboundPackingViewABA st : arrayList) {
                if (st.Pallet_ID.toLowerCase(Locale.getDefault()).contains(charText)) {
                    stringList.add(st);
                }
            }
        }
        notifyDataSetChanged();

    }
}
