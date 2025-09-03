package com.wcs.vcc.main.packingscan;

import com.wcs.vcc.api.xdoc.response.XdocOrder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

public class PickPackShipOrder {
    public UUID DispatchingOrderID;
    public String DispatchingOrderNumber;
    public String DispatchingOrderDate;
    public String DispatchingOrderRemark;
    public int TotalPackages;
    public double TotalWeight;
    public double PackingPercentage;
    public String CustomerClientCode;
    public String CustomerClientName;
    public int StoreNumber;
    public UUID CustomerClientID;
    public String StoreNumber_Barcode;

    public String getTotalWeight() {
        return NumberFormat.getInstance().format(TotalWeight);
    }

    public String getTotalPackages() {
        return NumberFormat.getInstance().format(TotalPackages);
    }

    public  static  ArrayList<PickPackShipOrder> fromXdocOrder(ArrayList<XdocOrder> listOrder)
    {
        ArrayList <PickPackShipOrder> rs = new ArrayList<>();

        if(listOrder.size()>0){
            for (XdocOrder item:
                 listOrder) {
                PickPackShipOrder pickPackShipOrder = new PickPackShipOrder();
                pickPackShipOrder.DispatchingOrderID = item.getDispatchingOrderID();
                pickPackShipOrder.DispatchingOrderNumber = item.getDispatchingOrderNumber();
                pickPackShipOrder.CustomerClientName = item.getShipTo();
                pickPackShipOrder.DispatchingOrderRemark = item.getCustomerName();
                pickPackShipOrder.StoreNumber = item.getStoreNumber();
                pickPackShipOrder.TotalPackages = item.getTotalPackages();
                pickPackShipOrder.TotalWeight = item.getTotalWeight();
                rs.add(pickPackShipOrder);
            }
        }

        return rs;
    }
}
