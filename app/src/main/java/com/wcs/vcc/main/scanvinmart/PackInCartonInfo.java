package com.wcs.vcc.main.scanvinmart;

import com.wcs.vcc.main.packingscan.pack.Pack;

import java.util.ArrayList;

public class PackInCartonInfo
{
    private String ItemID;
    private Integer Qty;
    private double Weight;
    private String CreatedBy;
    private String CreatedTime;
    private Integer DispatchingCartonID;
    private String ItemName;

    public static Pack toPackModel(PackInCartonInfo packInCartonInfo){
        Pack pack = new Pack();
        pack.ProductNumber = packInCartonInfo.getItemID();
        pack.ProductName = packInCartonInfo.getItemName();
        pack.Quantity = packInCartonInfo.getQty();
        pack.NetWeight =packInCartonInfo.getWeight();
        pack.OrderNetWeight = packInCartonInfo.getWeight();
        pack.OrderQuantity = packInCartonInfo.getQty();
        return  pack;
    }

    public  static ArrayList<Pack> toListPackModel(ArrayList<PackInCartonInfo> packInCartonInfos){
        ArrayList<Pack> packs = new ArrayList<>();
        if(packInCartonInfos!= null && packInCartonInfos.size()>0){
            for (PackInCartonInfo packInCartonInfo:
                    packInCartonInfos) {
                packs.add(toPackModel(packInCartonInfo));
            }
        }
        return packs;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
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

    public Integer getDispatchingCartonID() {
        return DispatchingCartonID;
    }

    public void setDispatchingCartonID(Integer dispatchingCartonID) {
        DispatchingCartonID = dispatchingCartonID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
