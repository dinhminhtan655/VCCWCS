package com.wcs.vcc.main.postiamge;

import android.graphics.Bitmap;

/**
 * Created by xuanloc on 11/7/2016.
 */
public class Thumb {
    private Bitmap bitmap;
    private String path;

    public Thumb(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Thumb(Bitmap bitmap, String path) {
        this.bitmap = bitmap;
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }
}
