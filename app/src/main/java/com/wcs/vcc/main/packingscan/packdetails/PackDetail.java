package com.wcs.vcc.main.packingscan.packdetails;

import com.wcs.vcc.utilities.Utilities;

import java.text.NumberFormat;

public class PackDetail {
    public int DispatchingProductPackID;
    public int PackNumber;
    public String ProductionDate;
    public double NetWeight;
    public String LotNumber;

    public String getNetWeight() {
        return NumberFormat.getInstance().format(NetWeight);
    }
    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
    }
}
