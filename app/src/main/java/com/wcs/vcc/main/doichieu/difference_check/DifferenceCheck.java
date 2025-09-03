package com.wcs.vcc.main.doichieu.difference_check;

import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

public class DifferenceCheck {
    public UUID ProductID;
    public String ProductNumber;
    public String ProductName;
    public int Received;
    public int Dispatched;
    public int CurrentQty;
    public int AfterDPQty;
    public int DiffAfterDP;
    public int DiffCurrent;
    public String CheckType;

    public String getReceived() {
        return Utilities.formatNumber(Received);
    }

    public String getCurrentQty() {
        return Utilities.formatNumber(CurrentQty);
    }

    public String getAfterDPQty() {
        return Utilities.formatNumber(AfterDPQty);
    }

    public String getDiffAfterDP() {
        return Utilities.formatNumber(DiffAfterDP);
    }

    public String getDiffCurrent() {
        return Utilities.formatNumber(DiffCurrent);
    }

    public String getDispatched() {
        return Utilities.formatNumber(Dispatched);
    }
}
