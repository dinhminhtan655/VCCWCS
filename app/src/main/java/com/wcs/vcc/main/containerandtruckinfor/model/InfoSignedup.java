package com.wcs.vcc.main.containerandtruckinfor.model;

import java.io.Serializable;
import java.util.Objects;

public class InfoSignedup implements Serializable {

    private String carNumber;
    private String driverMobilePhone;
    private String driverName;
    private String iDNumber;


    public InfoSignedup(String carNumber, String driverMobilePhone, String driverName, String iDNumber) {
        this.carNumber = carNumber;
        this.driverMobilePhone = driverMobilePhone;
        this.driverName = driverName;
        this.iDNumber = iDNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getDriverMobilePhone() {
        return driverMobilePhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getIDNumber() {
        return iDNumber;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InfoSignedup other = (InfoSignedup) obj;
        return Objects.equals(carNumber, other.carNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carNumber, driverMobilePhone, driverName, iDNumber);
    }
}
