package com.fao.fmd.fmdappbeta;

import android.animation.TimeInterpolator;

public class ItemModel {
    public final String header;
    public final String description;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public ItemModel(String header, String description, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.header = header;
        this.description = description;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
