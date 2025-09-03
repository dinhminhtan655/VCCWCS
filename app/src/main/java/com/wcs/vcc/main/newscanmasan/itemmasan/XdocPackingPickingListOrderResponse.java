package com.wcs.vcc.main.newscanmasan.itemmasan;

import java.util.List;

public class XdocPackingPickingListOrderResponse {
    public List<XdocPackingPickingListOrder> PickingListOrder;
    public String Message;
    public XdocPackingPickingListOrderResponse(List<XdocPackingPickingListOrder> pickingList) {
        PickingListOrder = pickingList;
    }

    public List<XdocPackingPickingListOrder> getPickingList() {
        return PickingListOrder;
    }

    public void setPickingList(List<XdocPackingPickingListOrder> pickingList) {
        PickingListOrder = pickingList;
    }

    public XdocPackingPickingListOrderResponse(List<XdocPackingPickingListOrder> pickingListOrder,String message) {
        PickingListOrder = pickingListOrder;
        Message = message;
    }

    public List<XdocPackingPickingListOrder> getPickingListOrder() {
        return PickingListOrder;
    }

    public void setPickingListOrder(List<XdocPackingPickingListOrder> pickingListOrder) {
        PickingListOrder = pickingListOrder;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
