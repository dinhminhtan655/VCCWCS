package com.wcs.vcc.main.scanvinmart;

import com.wcs.vcc.main.packingscan.carton.Carton;

import java.util.ArrayList;

public class CartonInfor {
    private Integer DispatchingCartonID;
    private String CartonType;
    private  String CreatedBy;
    private String CreatedTime;
    private Integer Qty;
    private Boolean Confirm;
    private Integer StoreID;
    private String DeliveryDate;
    private Integer PalletID;
    private Integer Qty1;

    public  static Carton toCartonModel(CartonInfor cartonInfor){
        Carton carton = new Carton();
        carton.DispatchingProductCartonID = cartonInfor.getDispatchingCartonID();
        carton.CartonNumber = cartonInfor.getDispatchingCartonID();
        carton.Completed = cartonInfor.getConfirm();
        carton.PackageType = cartonInfor.getCartonType();
        carton.Quantity = cartonInfor.getQty();
        return  carton;
    }
    public static  ArrayList<Carton> toListCartonModel( ArrayList<CartonInfor>  cartonInfors){
        ArrayList<Carton> rs = new ArrayList<>();
        if(cartonInfors!=null &&cartonInfors.size()>0){
            for (CartonInfor cartonInfor:
                 cartonInfors) {
                rs.add(toCartonModel(cartonInfor));
            }
        }
        return rs;
    }
    public Integer getDispatchingCartonID() {
        return DispatchingCartonID;
    }

    public void setDispatchingCartonID(Integer dispatchingCartonID) {
        DispatchingCartonID = dispatchingCartonID;
    }

    public String getCartonType() {
        return CartonType;
    }

    public void setCartonType(String cartonType) {
        CartonType = cartonType;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public Boolean getConfirm() {
        return Confirm;
    }

    public void setConfirm(Boolean confirm) {
        Confirm = confirm;
    }

    public Integer getStoreID() {
        return StoreID;
    }

    public void setStoreID(Integer storeID) {
        StoreID = storeID;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public Integer getPalletID() {
        return PalletID;
    }

    public void setPalletID(Integer palletID) {
        PalletID = palletID;
    }

    public Integer getQty1() {
        return Qty1;
    }

    public void setQty1(Integer qty1) {
        Qty1 = qty1;
    }
}
