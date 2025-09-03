package com.wcs.vcc.main.scanhang.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "item_table")
public class ItemScan {

    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("soThung")
    private int soThung;
    @SerializedName("palletID")
    private int palletID;
    @SerializedName("itemName")
    private String itemName;
    @SerializedName("soBich")
    private double soBich;
    @SerializedName("quantityScan")
    private int quantityScan;
    @SerializedName("quantityMove")
    private int quantityMove;
    @SerializedName("quantityModify")
    private int quantityModify;
    @SerializedName("confirm")
    private int confirm;
    @SerializedName("flag")
    private String flag;

    public ItemScan(int palletID,double soBich, int quantityMove,int confirm, int soThung, String flag) {
        this.palletID = palletID;
        this.soBich = soBich;
        this.quantityMove = quantityMove;
        this.confirm = confirm;
        this.soThung = soThung;
        this.flag = flag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSoThung(int soThung) {
        this.soThung = soThung;
    }

    public void setPalletID(int palletID) {
        this.palletID = palletID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setSoBich(double soBich) {
        this.soBich = soBich;
    }

    public void setQuantityScan(int quantityScan) {
        this.quantityScan = quantityScan;
    }

    public void setQuantityMove(int quantityMove) {
        this.quantityMove = quantityMove;
    }

    public void setQuantityModify(int quantityModify) {
        this.quantityModify = quantityModify;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getId() {
        return id;
    }

    public int getSoThung() {
        return soThung;
    }

    public int getPalletID() {
        return palletID;
    }

    public String getItemName() {
        return itemName;
    }

    public double getSoBich() {
        return soBich;
    }

    public int getQuantityScan() {
        return quantityScan;
    }

    public int getQuantityMove() {
        return quantityMove;
    }

    public int getQuantityModify() {
        return quantityModify;
    }

    public int getConfirm() {
        return confirm;
    }

    public String getConfirmQuantity(){
        return String.valueOf(getQuantityScan() + getQuantityMove() + getQuantityModify());
    }

    public int getSoBichString(){
        int soBichInt = (int)soBich;
        return soBichInt;
    }

    public String getThieuDu(){
        int a = (int) soBich - Integer.parseInt(getConfirmQuantity());
        return String.valueOf(a);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
