package com.wcs.vcc.api;

public class PickPackShipPackageTypeUpdateParameter extends BaseParam {
    private int DispatchingProductCartonID;
    private String PackageType;
    private double WeightOfPackage;

    public PickPackShipPackageTypeUpdateParameter(String username, String deviceNumber, int dispatchingProductCartonID, String packageType, double weightOfPackage) {
        super(username, deviceNumber);
        DispatchingProductCartonID = dispatchingProductCartonID;
        PackageType = packageType;
        WeightOfPackage = weightOfPackage;
    }
}
