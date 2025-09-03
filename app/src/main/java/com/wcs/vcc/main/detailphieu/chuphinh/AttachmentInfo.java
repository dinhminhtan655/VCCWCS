package com.wcs.vcc.main.detailphieu.chuphinh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/23/2016.
 */
public class AttachmentInfo {
    @SerializedName("AttachmentID")
    public UUID attachmentID;
    @SerializedName("OrderNumber")
    public String orderNumber;
    @SerializedName("AttachmentDescription")
    public String attachmentDescription;
    @SerializedName("AttachmentFile")
    public String attachmentFile;
    @SerializedName("AttachmentDate")
    public String attachmentDate;
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

    private String fileData2;

    public String getAttachmentDate() {
        return attachmentDate;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public int getAttachmentFileSize() {
        return attachmentFileSize;
    }

    public String getAttachmentUser() {
        return attachmentUser;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public Bitmap getBitmap() {
        fileData2 = fileData;
        Bitmap bitmap = null;

        if (fileData2 != null && fileData2.length() > 0) {
            byte[] imgBytes = Base64.decode(fileData2.getBytes(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imgBytes, 0,
                    imgBytes.length);

        }
        return bitmap;
    }


}
