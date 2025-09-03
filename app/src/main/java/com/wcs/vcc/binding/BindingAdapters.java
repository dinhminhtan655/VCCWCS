package com.wcs.vcc.binding;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleInvisible")
    public static void showInvisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("isBold")
    public static void isBold(TextView v, boolean isBold) {
        if (isBold) {
            v.setTypeface(null, Typeface.BOLD);
        } else {
            v.setTypeface(null, Typeface.NORMAL);
        }
    }
}
