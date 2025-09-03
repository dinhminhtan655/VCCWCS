package com.wcs.vcc.api.xdoc;

public class DeliveryTypeResponse {
    public String PackagedItem ;
    public String EarlyDeliveryDate;
    public String DeliveryType ;
    public String PackagedItemName;
    public String Remark ;
    @Override
    public String toString() {
        return  Remark;
    }

    public String getPackagedItem() {
        return PackagedItem;
    }

    public void setPackagedItem(String packagedItem) {
        PackagedItem = packagedItem;
    }

    public String getEarlyDeliveryDate() {
        return EarlyDeliveryDate;
    }

    public void setEarlyDeliveryDate(String earlyDeliveryDate) {
        EarlyDeliveryDate = earlyDeliveryDate;
    }

    public String getDeliveryType() {
        return DeliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        DeliveryType = deliveryType;
    }

    public String getPackagedItemName() {
        return PackagedItemName;
    }

    public void setPackagedItemName(String packagedItemName) {
        PackagedItemName = packagedItemName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
