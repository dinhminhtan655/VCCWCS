package com.wcs.vcc.api.production_order;

import java.util.ArrayList;

public class ProductionOrderResponse {
    private ArrayList<ProductionOrder> ProductionOrderList;

    public ArrayList<ProductionOrder> getProductionOrderList() {
        return ProductionOrderList;
    }

    public void setProductionOrderList(ArrayList<ProductionOrder> productionOrderList) {
        ProductionOrderList = productionOrderList;
    }
}
