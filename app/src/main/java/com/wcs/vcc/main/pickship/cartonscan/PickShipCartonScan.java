package com.wcs.vcc.main.pickship.cartonscan;

import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by aang on 27/07/2018.
 */

public class PickShipCartonScan {
    public UUID TripDetailCartonUnitID;
    public String ProductNumber;
    public String ProductName;
    public double NetWeight;
    public double WeightPerPackage;
    public String ProductionDate;
    public String LotNumber;

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
    }
}
