package com.wcs.vcc.main.bigcqa;

import com.wcs.vcc.main.vo.QaPoProduct;

public interface QaPoProductCallback {
    void onClick(QaPoProduct product);
    void onLongClick (QaPoProduct poProduct);
}
