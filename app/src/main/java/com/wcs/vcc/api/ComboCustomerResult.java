package com.wcs.vcc.api;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class ComboCustomerResult {
        @SerializedName("CustomerID")
        private UUID CustomerID;
        @SerializedName("CustomerNumber")
        private String CustomerNumber;
        @SerializedName("CustomerName")
        private String CustomerName;

        public ComboCustomerResult() {

        }

        public ComboCustomerResult(UUID customerID, String customerNumber, String customerName) {
            CustomerID = customerID;
            CustomerNumber = customerNumber;
            CustomerName = customerName;
        }


        public UUID getCustomerID() {
            return CustomerID;
        }

        public String getCustomerNumber() {
            return CustomerNumber;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerID(UUID customerID) {
            CustomerID = customerID;
        }

        public void setCustomerNumber(String customerNumber) {
            CustomerNumber = customerNumber;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        @Override
        public String toString() {
            return this.getCustomerNumber() + " ~ " + this.getCustomerName();
        }
}