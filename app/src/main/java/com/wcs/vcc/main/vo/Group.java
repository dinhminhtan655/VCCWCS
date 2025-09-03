package com.wcs.vcc.main.vo;

import androidx.annotation.NonNull;

public class Group {

    public static final int MANAGER = 1;
    public static final int SUPERVISOR = 2;
    public static final int PRODUCT_CHECKER = 3;
    public static final int FORKLIFT_DRIVER = 4;
    public static final int DOCUMENT = 5;
    public static final int TECHNICAL = 6;
    public static final int NO_POSITION = 7;
    public static final int LOWER_USER = 8;
    public static final int BIGC_USER = 9;
    public static final int TRANSPORTATION = 10;
    public static final int DILIVERYMAN = 11;


    public static int convertGroupStringToInt(@NonNull String group) {
        switch (group) {
            case "Manager":
                return MANAGER;
            case "Supervisor":
                return SUPERVISOR;
            case "Product Checker":
                return PRODUCT_CHECKER;
            case "Forklift Driver":
                return FORKLIFT_DRIVER;
            case "Documents":
                return DOCUMENT;
            case "Technical":
                return TECHNICAL;
            case "No position":
                return NO_POSITION;
            case "Lower User":
                return LOWER_USER;
            case "BigC User":
                return BIGC_USER;
            case "Transportation":
                return TRANSPORTATION;
            case "DeliveryMan":
                return DILIVERYMAN;
            default:
                return 0;
        }
    }

    public static boolean isEqualGroup(String group1, int group2){
        return convertGroupStringToInt(group1) == group2;
    }

}
