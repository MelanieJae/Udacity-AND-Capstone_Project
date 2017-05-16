package com.android.melanieh.dignitymemorialandroid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by melanieh on 4/16/17.
 */

public class Provider {

    private String providerName;
    private String address;
    private String phoneNum;
    private String providerURL;

    public Provider(String providerName, String address, String phoneNum, String providerURL) {
        this.providerName = providerName;
        this.address = address;
        this.phoneNum = phoneNum;
        this.providerURL = providerURL;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getProviderURL() {
        return providerURL;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "providerName='" + providerName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", providerURL='" + providerURL + '\'' +
                '}';
    }

    // used in user settings activity
    public String toFormattedString() {
        return "Provider Name: '" + providerName + '\'' +
                "\nAddress: '" + address + '\'' +
                "\nPhoneNum: '" + phoneNum + '\'' +
                "\nProvider URL: '" + providerURL + '\'' +
                '}';
    }
}
