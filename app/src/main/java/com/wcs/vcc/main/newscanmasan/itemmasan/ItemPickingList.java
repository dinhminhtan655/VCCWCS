package com.wcs.vcc.main.newscanmasan.itemmasan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class ItemPickingList implements Serializable {

    @SerializedName("OperationGroupDefinitionID")
    public int OperationGroupDefinitionID;

    @SerializedName("OperationGroupName")
    public String OperationGroupName;

    @SerializedName("LoginName")
    public String LoginName;

    @SerializedName("ProductNumber")
    public String ProductNumber;

    @SerializedName("ProductName")
    public String ProductName;

    @SerializedName("Quantity")
    public int Quantity;

    @SerializedName("NetQty")
    public int NetQty;

    @SerializedName("PalletNumber")
    public int PalletNumber;

    @SerializedName("IsWeightingRequire")
    public Boolean IsWeightingRequire;

    @SerializedName("XdocPickingListID")
    public UUID XdocPickingListID;

    public ItemPickingList(int operationGroupDefinitionID, String operationGroupName, String loginName,
                           String productNumber, String productName, int quantity, int netQty,Boolean isWeightingRequire ,UUID xdocPickingListID,Integer palletNumber) {
        OperationGroupDefinitionID = operationGroupDefinitionID;
        OperationGroupName = operationGroupName;
        LoginName = loginName;
        ProductNumber = productNumber;
        ProductName = productName;
        Quantity = quantity;
        NetQty = netQty;
        PalletNumber = palletNumber;
        IsWeightingRequire = isWeightingRequire;
        XdocPickingListID = xdocPickingListID;
    }
}
