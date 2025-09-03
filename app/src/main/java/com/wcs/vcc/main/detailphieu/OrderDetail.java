package com.wcs.vcc.main.detailphieu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.wcs.wcs.R;
import com.wcs.vcc.main.palletcartonweighting.PalletCartonWeightingActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by buu on 1/3/2016.
 */
public class OrderDetail implements Item {
    @SerializedName("BarcodeScanDetailID")
    public UUID BarcodeScanDetailID;
    public String ProductNumber;
    public String PalletNumber;
    public String BarcodeString;

    private String customerNumber;
    @SerializedName("DO")
    private String DO;
    @SerializedName("SpecialRequirement")
    private String SpecialRequirement;
    @SerializedName("PalletID")
    private UUID PalletID;
    @SerializedName("ProductID")
    private UUID ProductID;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("Result")
    private String Result;
    @SerializedName("QuantityOfPackages")
    private String QuantityOfPackages;
    @SerializedName("IsRecordNew")
    private byte IsRecordNew;
    @SerializedName("DispatchingOrderDetailID")
    private UUID dispatchingOrderDetailID;
    @SerializedName("DispatchingLocationRemark")
    private String dispatchingLocationRemark;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("UseByDate")
    private String useByDate;
    @SerializedName("ProductionDate")
    private String productionDate;
    @SerializedName("Label")
    private String Label;
    @SerializedName("ScannedType")
    private String ScannedType;
    @SerializedName("RemainByProductAtLocation")
    private int RemainByProductAtLocation;
    @SerializedName("ActualQuantity")
    private String ActualQuantity;
    @SerializedName("ScanQty")
    private String ScanQty;
    @SerializedName("ScanKg")
    private float ScanKg;
    @SerializedName("Status")
    private int Status;
    @SerializedName("ActualProductionDate")
    private String ActualProductionDate;
    @SerializedName("ActualUseByDate")
    private String ActualUseByDate;
    @SerializedName("ActualCustomerRef")
    private String ActualCustomerRef;
    @SerializedName("ActualCustomerRef2")
    private String ActualCustomerRef2;
    @SerializedName("VehicleNumber")
    private String VehicleNumber;
    @SerializedName("ContainerNum")
    private String ContainerNum;

    @SerializedName("ReceivingOrderDate")
    private String ReceivingOrderDate;
    @SerializedName("GrossWeight")
    private double GrossWeight;
    @SerializedName("PalletID_Barcode")
    private String PalletID_Barcode;
    @SerializedName("SumQuantity")
    private int SumQuantity;
    @SerializedName("TemperatureRequire")
    private double TemperatureRequire;
    @SerializedName("CustomerName")
    private String CustomerName;

    @SerializedName("ReceivingOrderRemark")
    private String ReceivingOrderRemark;
    @SerializedName("WeightPerPackage")
    private double WeightPerPackage;
    @SerializedName("UnitQuantity")
    private int UnitQuantity;
    @SerializedName("ProductCategoryDescription")
    private String ProductCategoryDescription;

    public String getSpecialRequirement() {
        return SpecialRequirement;
    }

    public String getScannedType() {
        return ScannedType;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyyyy3(productionDate).equals("01/01/1900") ? "" : Utilities.formatDate_ddMMyyyy3(productionDate);
    }

    public String getLabel() {
        return Label;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyyyy3(useByDate).equals("01/01/1900") ? "" : Utilities.formatDate_ddMMyyyy3(useByDate);
    }

    public String getDispatchingLocationRemark() {
        return dispatchingLocationRemark;
    }

    public String getRemark() {
        return remark;
    }

    public UUID getDispatchingOrderDetailID() {
        return dispatchingOrderDetailID;
    }

    public UUID getPalletID() {
        return PalletID;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getResult() {
        return Result;
    }

    public String getQuantityOfPackages() {
        return QuantityOfPackages;
    }

    public String getActualQuantity() {
        return ActualQuantity;
    }

    public byte getIsRecordNew() {
        return IsRecordNew;
    }

    public String getDO() {
        return DO;
    }

    public UUID getProductID() {
        return ProductID;
    }

    public int getRemainByProductAtLocation() {
        return RemainByProductAtLocation;
    }

    public String getScanQty() {
        return ScanQty;
    }

    public float getScanKg() {
        return ScanKg;
    }

    public int getStatus() {
        return Status;
    }

    public String getActualProductionDate() {
        return Utilities.formatDate_ddMMyyyy3(ActualProductionDate).equals("01/01/1900") ? "" : Utilities.formatDate_ddMMyyyy3(ActualProductionDate);
    }

    public String getActualUseByDate() {
        return Utilities.formatDate_ddMMyyyy3(ActualUseByDate).equals("01/01/1900") ? "" : Utilities.formatDate_ddMMyyyy3(ActualUseByDate);
    }

    public String getActualCustomerRef() {
        return ActualCustomerRef;
    }

    public String getActualCustomerRef2() {
        return ActualCustomerRef2;
    }

    public String getContainerNum() {
        return ContainerNum;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public String getReceivingOrderDate() {
        return ReceivingOrderDate;
    }

    public double getGrossWeight() {
        return GrossWeight;
    }

    public String getPalletID_Barcode() {
        return PalletID_Barcode;
    }

    public int getSumQuantity() {
        return SumQuantity;
    }

    public double getTemperatureRequire() {
        return TemperatureRequire;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getReceivingOrderRemark() {
        return ReceivingOrderRemark;
    }

    public double getWeightPerPackage() {
        return WeightPerPackage;
    }

    public int getUnitQuantity() {
        return UnitQuantity;
    }

    public String getProductCategoryDescription() {
        return ProductCategoryDescription;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "PalletID: %s\nResult: %s\nSố lượng: %s\n\nRemark: %s\nNSX: %s\nHSD: %s\nTồn: %d", getPalletID(), getResult(), getQuantityOfPackages(), getRemark(), getProductionDate(), getUseByDate(), getRemainByProductAtLocation());
    }

    @Override
    public View getItem(Context context, LayoutInflater inflater, View convertView, final IOrderDetailListener listener) {
        ChildViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ChildViewHolder)) {
            convertView = inflater.inflate(R.layout.item_detail_phieu, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ChildViewHolder) convertView.getTag();
        final Item info = this;
        Utilities.setUnderLine(holder.palletId, PalletNumber);
        holder.palletId.setTag(PalletNumber);
        holder.remark.setText(getRemark());
        holder.location.setText(getDispatchingLocationRemark());
        holder.quantity.setText(getQuantityOfPackages());
        holder.actualQuantity.setText(getActualQuantity());
        holder.tvRemainByProductAtLocation.setText(String.valueOf(RemainByProductAtLocation));
//        holder.tvRemainByProductAtLocation.setVisibility(RemainByProductAtLocation != 0 ? View.VISIBLE : View.GONE);
        holder.tvnsx.setText(String.format(Utilities.formatDate_ddMMyy5(getProductionDate())));
        holder.tvhsd.setText(String.format(Utilities.formatDate_ddMMyy5(getUseByDate())));
        holder.tvLabel.setText(getLabel().trim());
        holder.tvScanKg.setText(String.format(Locale.US, "%.3f", getScanKg()));
        holder.tvScanQuantity.setText(getScanQty());
        holder.tvQuantityCheck.setText(getActualQuantity());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PalletCartonWeightingActivity.class);
                intent.putExtra("PALLET_NUMBER", PalletNumber);
                intent.putExtra("CUSTOMER_NUMBER", customerNumber);
                intent.putExtra("PALLET_ID", getPalletID().toString());
                intent.putExtra("DO", "");
                intent.putExtra("IS_EDITABLE", getDO().contains("RO"));
                intent.putExtra("DO", getDO());
                intent.putExtra("DO_DETAIL", getDispatchingOrderDetailID().toString());
                context.startActivity(intent);
            }
        });

        holder.container2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PalletCartonWeightingActivity.class);
                intent.putExtra("PALLET_NUMBER", PalletNumber);
                intent.putExtra("CUSTOMER_NUMBER", customerNumber);
                intent.putExtra("PALLET_ID", getPalletID().toString());
                intent.putExtra("DO", "");
                intent.putExtra("IS_EDITABLE", getDO().contains("RO"));
                intent.putExtra("DO", getDO());
                intent.putExtra("DO_DETAIL", getDispatchingOrderDetailID().toString());
                context.startActivity(intent);
            }
        });
        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuantityNumberClick(info, 0);
            }
        });

        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuantityNumberClick(info, 0);
            }
        });
        holder.tvQuantityCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuantityNumberClick(info, 0);
            }
        });

        if (RemainByProductAtLocation > 0) {
            holder.tvRemainByProductAtLocation.setTextColor(Color.BLACK);
        } else
            holder.tvRemainByProductAtLocation.setTextColor(Color.GRAY);
        if (getResult().equalsIgnoreCase("OK")) {
            convertView.setBackgroundColor(Color.argb(255, 76, 175, 80));
            if (getIsRecordNew() == 1)
                convertView.setBackgroundColor(Color.argb(255, 255, 238, 88));
        } else if (getResult().equalsIgnoreCase("NO"))
            convertView.setBackgroundColor(Color.argb(255, 183, 28, 28));
        else if (getResult().equalsIgnoreCase("XX"))
            convertView.setBackgroundColor(Color.argb(255, 144, 202, 249));
        else convertView.setBackgroundColor(Color.argb(0, 76, 175, 80));

        if (Integer.parseInt(getActualQuantity()) < Integer.parseInt(getQuantityOfPackages())){
            holder.tvQuantityCheck.setBackground(convertView.getResources().getDrawable(R.drawable.red_border));
        }else
            holder.tvQuantityCheck.setBackground(convertView.getResources().getDrawable(R.drawable.outline_right_bottom_dark));

        if (Integer.parseInt(getScanQty()) < Integer.parseInt(getQuantityOfPackages())){
            holder.tvScanQuantity.setBackground(convertView.getResources().getDrawable(R.drawable.red_border));
        }else
            holder.tvScanQuantity.setBackground(convertView.getResources().getDrawable(R.drawable.outline_right_bottom_dark));

        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.tv_PalletID)
        TextView palletId;
        @BindView(R.id.tv_remark)
        TextView remark;
        @BindView(R.id.tv_location)
        TextView location;
        @BindView(R.id.tv_Quantity)
        TextView quantity;
        @BindView(R.id.tv_ActualQuantity)
        TextView actualQuantity;
        @BindView(R.id.tv_nsx)
        TextView tvnsx;
        @BindView(R.id.tv_hsd)
        TextView tvhsd;
        @BindView(R.id.tv_label)
        TextView tvLabel;
        @BindView(R.id.tv_RemainByProductAtLocation)
        TextView tvRemainByProductAtLocation;
        @BindView(R.id.ll_pallet_container)
        View container;
        @BindView(R.id.ll_pallet_container2)
        View container2;
        @BindView(R.id.tv_ScanKg)
        TextView tvScanKg;
        @BindView(R.id.tv_ScanQuantity)
        TextView tvScanQuantity;
        @BindView(R.id.tv_QuantityCheck)
        TextView tvQuantityCheck;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
