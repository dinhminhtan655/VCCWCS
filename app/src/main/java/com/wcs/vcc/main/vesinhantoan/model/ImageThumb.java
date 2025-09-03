package com.wcs.vcc.main.vesinhantoan.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImageThumb implements Serializable {

    private  transient  Bitmap bImageThumb;
    private boolean isUp;

    public ImageThumb(Bitmap bImageThumb) {
        this.bImageThumb = bImageThumb;
    }

    public ImageThumb(Bitmap bImageThumb, boolean isUp) {
        this.bImageThumb = bImageThumb;
        this.isUp = isUp;
    }



    public Bitmap getbImageThumb() {
        return bImageThumb;
    }

    public void setbImageThumb(Bitmap bImageThumb) {
        this.bImageThumb = bImageThumb;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }
}
