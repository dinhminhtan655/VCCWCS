package com.wcs.vcc.main.packingscan.carton;


import com.wcs.vcc.api.xdoc.response.XdocCarton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Carton {
    public Integer DispatchingProductCartonID;
    public int CartonNumber;
    public String PackageType;
    public String CartonDescription;
    public int Quantity;
    public int StoreNumber;
    public String CustomerClientName;
    public String DispatchingOrderNumber;
    public String CUST_PO_NUMBER;
    public double WeightOfPackage;
    public double NetWeight;
    public boolean Completed;
    public UUID DispatchingOrderID;


    public Carton(Integer dispatchingProductCartonID, int cartonNumber, String packageType, String cartonDescription, int quantity, int storeNumber, String customerClientName, String dispatchingOrderNumber, String CUST_PO_NUMBER, double weightOfPackage, double netWeight, boolean completed, UUID dispatchingOrderID) {
        DispatchingProductCartonID = dispatchingProductCartonID;
        CartonNumber = cartonNumber;
        PackageType = packageType;
        CartonDescription = cartonDescription;
        Quantity = quantity;
        StoreNumber = storeNumber;
        CustomerClientName = customerClientName;
        DispatchingOrderNumber = dispatchingOrderNumber;
        this.CUST_PO_NUMBER = CUST_PO_NUMBER;
        WeightOfPackage = weightOfPackage;
        NetWeight = netWeight;
        Completed = completed;
        DispatchingOrderID = dispatchingOrderID;
    }

    public Carton() {
    }

    public static ArrayList<Carton> fromXdocCarton(List<XdocCarton> xdocCartons){
        if(xdocCartons.size()>0){
            ArrayList<Carton> rs = new ArrayList<>();
            for (XdocCarton item :
                    xdocCartons) {
                Carton carton = new Carton();

                carton.DispatchingOrderNumber = item.getDispatchingOrderNumber();
                carton.CartonNumber = item.getCartonNumber();
                carton.PackageType = item.getPackageType();
                carton.CartonDescription = item.getCartonDescription();
                carton.Quantity = item.getQuantity();
                carton.StoreNumber = item.getStoreNumber();
                carton.CustomerClientName = item.getCustomerClientName();
                carton.DispatchingOrderNumber = item.getDispatchingOrderNumber();
                carton.CUST_PO_NUMBER = item.getCUST_PO_NUMBER();
                carton.WeightOfPackage = item.getWeightOfPackage();
                carton.NetWeight = item.getNetWeight();
                carton.Completed = item.getCompleted();
                carton.DispatchingProductCartonID = item.getDispatchingProductCartonID();
                rs.add(carton);
            }
            return  rs;
        }
        else {
            return  null;
        }

    }
}
