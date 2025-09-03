package com.wcs.vcc.mvvm.data.model.MassCycleCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MassCycleCountResponse {

    @Expose
    @SerializedName("masscyclecount")
    private List<MassCycleCountRemote> massCycleCount;

    public List<MassCycleCountRemote> getMassCycleCount() {
        return massCycleCount;
    }

    public void setMassCycleCount(List<MassCycleCountRemote> massCycleCount) {
        this.massCycleCount = massCycleCount;
    }
}
