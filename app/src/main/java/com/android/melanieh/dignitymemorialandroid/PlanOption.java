package com.android.melanieh.dignitymemorialandroid;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanOption {

    private String heading;
    private String detailText;
    private String imageUrlString;
    private String estimatedCost;

    public PlanOption(String heading, String detailText, String imageUrlString, String estimatedCost) {
        this.heading = heading;
        this.detailText = detailText;
        this.imageUrlString = imageUrlString;
        this.estimatedCost = estimatedCost;
    }

    public String getHeading() {
        return heading;
    }

    public String getDetailText() {
        return detailText;
    }

    public String getImageUrlString() {
        return imageUrlString;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

}
