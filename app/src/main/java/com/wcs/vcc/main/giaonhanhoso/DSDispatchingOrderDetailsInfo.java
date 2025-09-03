package com.wcs.vcc.main.giaonhanhoso;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;


public class DSDispatchingOrderDetailsInfo implements BaseColumns {
    public static final String TABLE_NAME = "DSDispatchingOrderDetailsInfo";
    public static final String CARTON_NEW_ID = "CartonNewID";
    public static final String CARTON_DESCRIPTION = "CartonDescription";
    public static final String CUSTOMER_REF = "CustomerRef";
    public static final String CARTON_SIZE = "CartonSize";
    public static final String ORDER_NUMBER = "OrderNumber";
    public static final String ATTACHMENT_FILE = "AttachmentFile";
    public static final String RESULT = "Result";
    public static final String REMARK = "Remark";
    public static final String SCANNED_TYPE = "ScannedType";
    public static final String IS_RECORD_NEW = "IsRecordNew";
    public static final String DS_RO_CARTON_ID = "DSROCartonID";
    public static final String BARCODE = "Barcode";
    public static final String USER_NAME = "UserName";
    public static final String DONE = "Done";
    @SerializedName(CARTON_NEW_ID)
    private int CartonNewID;
    @SerializedName(CARTON_DESCRIPTION)
    private String CartonDescription;
    @SerializedName(CUSTOMER_REF)
    private String CustomerRef;
    @SerializedName(CARTON_SIZE)
    private float CartonSize;
    @SerializedName(ORDER_NUMBER)
    private String OrderNumber;
    @SerializedName(ATTACHMENT_FILE)
    private String AttachmentFile;
    @SerializedName(RESULT)
    private String Result;
    @SerializedName(REMARK)
    private String Remark;
    @SerializedName(SCANNED_TYPE)
    private int ScannedType;
    @SerializedName(IS_RECORD_NEW)
    private int IsRecordNew;
    @SerializedName(DS_RO_CARTON_ID)
    private int DSROCartonID;
    private String username;
    private String barcode;

    public String getRemark() {
        return Remark;
    }

    public int getDSROCartonID() {
        return DSROCartonID;
    }

    public int isRecordNew() {
        return IsRecordNew;
    }

    public int getScannedType() {
        return ScannedType;
    }

    public String getCartonDescription() {
        return CartonDescription;
    }

    public int getCartonNewID() {
        return CartonNewID;
    }

    public float getCartonSize() {
        return CartonSize;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public String getAttachmentFile() {
        return AttachmentFile;
    }

    public String getResult() {
        return Result;
    }

    public void setCartonNewID(int cartonNewID) {
        CartonNewID = cartonNewID;
    }

    public void setCartonDescription(String cartonDescription) {
        CartonDescription = cartonDescription;
    }

    public void setCustomerRef(String customerRef) {
        CustomerRef = customerRef;
    }

    public void setCartonSize(float cartonSize) {
        CartonSize = cartonSize;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public void setAttachmentFile(String attachmentFile) {
        AttachmentFile = attachmentFile;
    }

    public void setResult(String result) {
        Result = result;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setScannedType(int scannedType) {
        ScannedType = scannedType;
    }

    public void setIsRecordNew(int isRecordNew) {
        IsRecordNew = isRecordNew;
    }

    public void setDSROCartonID(int DSROCartonID) {
        this.DSROCartonID = DSROCartonID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
