package com.wcs.vcc.main.scanvinmart;

public class ItemPoInfor {
    private String StoreID;
    private Integer PalletID;
    private Integer AcctualSortQuantity;
    private Integer Quantity;
    private Integer Weight;
    private Integer Status;

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public Integer getPalletID() {
        return PalletID;
    }

    public void setPalletID(Integer palletID) {
        PalletID = palletID;
    }

    public Integer getAcctualSortQuantity() {
        return AcctualSortQuantity;
    }

    public void setAcctualSortQuantity(Integer acctualSortQuantity) {
        AcctualSortQuantity = acctualSortQuantity;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Integer getWeight() {
        return Weight;
    }

    public void setWeight(Integer weight) {
        Weight = weight;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}
