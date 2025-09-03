package com.wcs.vcc.main.packingscan.save_package;

public class SavePackageHN {

    public int id;
    public String ProductionDate;
    public String ProductNumber;
    public String ProductName;
    public int Quantity;
    public double NetWeight;
    public String LotNumber;
    public String BarcodeString;


    public String splitProductionDate(String ProductionDate){
        String[] arrOftr = ProductionDate.split("T");
        for (String a : arrOftr){
            a = arrOftr[0];
            return this.ProductionDate = a;
        }
        return this.ProductionDate;
    }






}
