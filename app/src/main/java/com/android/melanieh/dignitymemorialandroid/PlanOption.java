package com.android.melanieh.dignitymemorialandroid;

import android.graphics.Bitmap;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanOption {

    private String heading;
    private String detailText;
    private String imageUrlString;

    public PlanOption(String heading, String detailText) {
        this.heading = heading;
        this.detailText = detailText;
    }

    public PlanOption(String heading, String detailText, String imageUrlString) {
        this.heading = heading;
        this.detailText = detailText;
        this.imageUrlString = imageUrlString;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    public String getImageUrlString() {
        return imageUrlString;
    }

    public void setImageUrlString(String imageUrlString) {
        this.imageUrlString = imageUrlString;
    }
}
