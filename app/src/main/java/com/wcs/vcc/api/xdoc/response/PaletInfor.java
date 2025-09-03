package com.wcs.vcc.api.xdoc.response;

public class PaletInfor {
    private Integer PalletNumber;
    private int StoreID ;
    private String StoreVietnam;
    private String LocationNumber;
    private String ProductNumber;
    private String ProductName ;
    private String CustomerRef;
    private String ProductionDate ;
    private String UseByDate;

    public Integer getPalletNumber() {
        return PalletNumber;
    }

    public void setPalletNumber(Integer palletNumber) {
        PalletNumber = palletNumber;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public String getStoreVietnam() {
        return StoreVietnam;
    }

    public void setStoreVietnam(String storeVietnam) {
        StoreVietnam = storeVietnam;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        LocationNumber = locationNumber;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String productNumber) {
        ProductNumber = productNumber;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public void setCustomerRef(String customerRef) {
        CustomerRef = customerRef;
    }

    public String getProductionDate() {
        return ProductionDate;
    }

    public void setProductionDate(String productionDate) {
        ProductionDate = productionDate;
    }

    public String getUseByDate() {
        return UseByDate;
    }

    public void setUseByDate(String useByDate) {
        UseByDate = useByDate;
    }
}
