package com.android.melanieh.dignitymemorialandroid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by melanieh on 4/16/17.
 */

public class Obituary {

    private String personName;
    private String birthDate;
    private String deathDate;
    private String obitText;

    public Obituary(String personName, String birthDate, String deathDate, String obitText) {
        this.personName = personName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.obitText = obitText;
    }

    public String getPersonName() {
        return personName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public String getObitText() {
        return obitText;
    }

}
