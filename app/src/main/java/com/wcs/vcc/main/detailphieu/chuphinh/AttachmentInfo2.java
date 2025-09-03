package com.wcs.vcc.main.detailphieu.chuphinh;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AttachmentInfo2 {

    @SerializedName("AttachmentID")
    public String attachmentID;
    @SerializedName("OrderNumber")
    public String orderNumber;
    @SerializedName("AttachmentDescription")
    public String attachmentDescription;
    @SerializedName("AttachmentFile")
    public String attachmentFile;
    @SerializedName("AttachmentDate")
    public Date attachmentDate;
    @SerializedName("AttachmentUser")
    public String attachmentUser;
    @SerializedName("IsDeleted")
    public boolean isDeleted;
    @SerializedName("AttachmentFileSize")
    public int attachmentFileSize;
    @SerializedName("ConfidentialLevel")
    public int confidentialLevel;
    @SerializedName("OriginalFileName")
    public String originalFileName;
    @SerializedName("FileData")
    public String fileData;

    public String getAttachmentID() {
        return attachmentID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public Date getAttachmentDate() {
        return attachmentDate;
    }

    public String getAttachmentUser() {
        return attachmentUser;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public int getAttachmentFileSize() {
        return attachmentFileSize;
    }

    public int getConfidentialLevel() {
        return confidentialLevel;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getFileData() {
        return fileData;
    }
}
