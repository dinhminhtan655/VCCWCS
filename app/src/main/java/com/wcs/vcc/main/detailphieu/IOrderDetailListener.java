package com.wcs.vcc.main.detailphieu;

public interface IOrderDetailListener {
    void onPalletNumberClick(Item item, int position);
    void onQuantityNumberClick(Item item, int position);
}
