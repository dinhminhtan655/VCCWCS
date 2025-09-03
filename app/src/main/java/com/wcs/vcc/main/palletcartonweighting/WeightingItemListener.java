package com.wcs.vcc.main.palletcartonweighting;

/**
 * Created by aang on 02/06/2018.
 */

public interface WeightingItemListener {
    void onClick(PalletCartonWeighting item, int position);
    void onLongClick(PalletCartonWeighting item, int position);
    void onChecked(int sumChecked);
}
