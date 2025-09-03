package com.wcs.vcc.api.production_order;

public class ProductionOrder {
    private String ProductionOrderID;
    private String ProductionOrderNumber;
    private String ProductionOrderDate;
    private String Status;
    private String StatusName;
    private String ProductID;
    private String ProductNumber;
    private String ProductName;
    private float Planned_Unit;
    private String DispatchingOrderNumber;
    private String ProductionOrderRemark;


    // Getter Methods

    public String getProductionOrderID() {
        return ProductionOrderID;
    }

    public String getProductionOrderNumber() {
        return ProductionOrderNumber;
    }

    public String getProductionOrderDate() {
        return ProductionOrderDate;
    }

    public String getStatus() {
        return Status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public String getProductID() {
        return ProductID;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public String getProductName() {
        return ProductName;
    }

    public float getPlanned_Unit() {
        return Planned_Unit;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public String getProductionOrderRemark() {
        return ProductionOrderRemark;
    }

    // Setter Methods

    public void setProductionOrderID(String ProductionOrderID) {
        this.ProductionOrderID = ProductionOrderID;
    }

    public void setProductionOrderNumber(String ProductionOrderNumber) {
        this.ProductionOrderNumber = ProductionOrderNumber;
    }

    public void setProductionOrderDate(String ProductionOrderDate) {
        this.ProductionOrderDate = ProductionOrderDate;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public void setProductNumber(String ProductNumber) {
        this.ProductNumber = ProductNumber;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setPlanned_Unit(float Planned_Unit) {
        this.Planned_Unit = Planned_Unit;
    }

    public void setDispatchingOrderNumber(String DispatchingOrderNumber) {
        this.DispatchingOrderNumber = DispatchingOrderNumber;
    }

    public void setProductionOrderRemark(String ProductionOrderRemark) {
        this.ProductionOrderRemark = ProductionOrderRemark;
    }
}