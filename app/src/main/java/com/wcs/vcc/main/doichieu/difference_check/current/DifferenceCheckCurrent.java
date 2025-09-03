package com.wcs.vcc.main.doichieu.difference_check.current;

import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

public class DifferenceCheckCurrent {
    public UUID PalletID;
    public int PalletNumber;
    public String Label;
    public int OriginalQuantity;
    public int AfterDPQuantity;
    public int CurrentQuantity;
    public int DispatchingQuantity;
    public int DispatchingPlanQty;
    public int CurrentActual;
    public int AfterDPActual;

    public String getOriginalQuantity() {
        return Utilities.formatNumber(OriginalQuantity);
    }

    public String getPalletNumber() {
        return String.valueOf(PalletNumber);
    }

    public String getAfterDPActual() {
        return Utilities.formatNumber(AfterDPActual);
    }

    public String getCurrentActual() {
        return Utilities.formatNumber(CurrentActual);
    }

    public String getDispatchingPlanQty() {
        return Utilities.formatNumber(DispatchingPlanQty);
    }

    public String getDispatchingQuantity() {
        return Utilities.formatNumber(DispatchingQuantity);
    }

    public String getCurrentQuantity() {
        return Utilities.formatNumber(CurrentQuantity);
    }

    public String getAfterDPQuantity() {
        return Utilities.formatNumber(AfterDPQuantity);
    }

}
