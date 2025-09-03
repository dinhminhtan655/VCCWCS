package com.wcs.vcc.main.newscanmasan;

public class APIGetNewLablePalletResponse {
    //[{"Label":"PA10ID50CUSBHXPGR10","PalletID":"PA10","ID":"ID50","CustomerCode":"CUSBHX","ProductGroup":"PGR10"}]
    public String Label;
    public String PalletID;
    public String ID;
    public String CustomerCode;
    public String ProductGroup;

    public APIGetNewLablePalletResponse(String label, String palletID, String ID, String customerCode, String productGroup) {
        Label = label;
        PalletID = palletID;
        this.ID = ID;
        CustomerCode = customerCode;
        ProductGroup = productGroup;
    }
}
