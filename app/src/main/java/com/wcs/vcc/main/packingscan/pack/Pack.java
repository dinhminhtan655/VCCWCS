package com.wcs.vcc.main.packingscan.pack;

import java.util.UUID;

public class Pack {
    public int DispatchingProductPackID;
    public UUID ProductID;
    public String ProductionDate;
    public String ProductNumber;
    public String ProductName;
    public int OrderQuantity;
    public double OrderNetWeight;
    public int Quantity;
    public double NetWeight;
    public String LotNumber;
    public String CustomerClientName;
    public  double GrossWeight;
    public  String RouteCode;

    public String getProductNumber() {
        if (ProductNumber != null) {
            String[] strings = ProductNumber.split("-");
            if (strings.length == 2)
                return strings[1];
        }
        return ProductNumber;
    }
}
