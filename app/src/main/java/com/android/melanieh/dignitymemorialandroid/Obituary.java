package com.android.melanieh.dignitymemorialandroid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by melanieh on 4/16/17.
 */

public class Obituary {

    private String personName;
    private String obitPreviewText;
    private String obitFullTextLink;

    public Obituary(String personName, String obitPreviewText, String obitFullTextLink) {
        this.personName = personName;
        this.obitPreviewText = obitPreviewText;
        this.obitFullTextLink = obitFullTextLink;
    }

    public String getPersonName() {
        return personName;
    }

    public String getObitPreviewText() {
        return obitPreviewText;
    }

    public String getObitFullTextLink() {
        return obitFullTextLink;
    }
}
