package com.wcs.vcc.main;

import com.wcs.vcc.api.AttachmentParameter;

public interface UploadImageCallback {
    void uploadDone(AttachmentParameter params);
}
