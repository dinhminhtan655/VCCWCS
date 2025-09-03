package com.wcs.vcc.api.xdoc.response;

public class XdocCarton {
    private Integer DispatchingProductCartonID;
    private String CartonDescription ;
    private int Quantity ;
    private Double NetWeight ;
    private int StoreNumber ;
    private String DispatchingOrderDate ;
    private String CustomerClientName ;
    private String DispatchingOrderNumber ;
    private String CUST_PO_NUMBER ;
    private String PackageType;
    private Double WeightOfPackage ;
    private Boolean Completed;
    private String Title;
    public  Integer CartonNumber;

    public Integer getCartonNumber() {
        return CartonNumber;
    }

    public void setCartonNumber(Integer cartonNumber) {
        CartonNumber = cartonNumber;
    }

    public Integer getDispatchingProductCartonID() {
        return DispatchingProductCartonID;
    }

    public void setDispatchingProductCartonID(Integer dispatchingProductCartonID) {
        DispatchingProductCartonID = dispatchingProductCartonID;
    }

    public String getCartonDescription() {
        return CartonDescription;
    }

    public void setCartonDescription(String cartonDescription) {
        CartonDescription = cartonDescription;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Double getNetWeight() {
        return NetWeight;
    }

    public void setNetWeight(Double netWeight) {
        NetWeight = netWeight;
    }

    public int getStoreNumber() {
        return StoreNumber;
    }

    public void setStoreNumber(int storeNumber) {
        StoreNumber = storeNumber;
    }

    public String getDispatchingOrderDate() {
        return DispatchingOrderDate;
    }

    public void setDispatchingOrderDate(String dispatchingOrderDate) {
        DispatchingOrderDate = dispatchingOrderDate;
    }

    public String getCustomerClientName() {
        return CustomerClientName;
    }

    public void setCustomerClientName(String customerClientName) {
        CustomerClientName = customerClientName;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }

    public String getCUST_PO_NUMBER() {
        return CUST_PO_NUMBER;
    }

    public void setCUST_PO_NUMBER(String CUST_PO_NUMBER) {
        this.CUST_PO_NUMBER = CUST_PO_NUMBER;
    }

    public String getPackageType() {
        return PackageType;
    }

    public void setPackageType(String packageType) {
        PackageType = packageType;
    }

    public Double getWeightOfPackage() {
        return WeightOfPackage;
    }

    public void setWeightOfPackage(Double weightOfPackage) {
        WeightOfPackage = weightOfPackage;
    }

    public Boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(Boolean completed) {
        Completed = completed;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
