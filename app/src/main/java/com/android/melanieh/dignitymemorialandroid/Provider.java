package com.android.melanieh.dignitymemorialandroid;

/**
 * Created by melanieh on 4/16/17.
 */

public class Provider {

    private String providerName;
    private String address1;
    private String address2;
    private String cityStateZip;
    private String phoneNum;
    private String providerURL;

    public Provider(String providerName, String address1, String address2, String cityStateZip,
                    String phoneNum, String providerURL) {
        this.providerName = providerName;
        this.address1 = address1;
        this.address2 = address2;
        this.cityStateZip = cityStateZip;
        this.phoneNum = phoneNum;
        this.providerURL = providerURL;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getProviderURL() {
        return providerURL;
    }
}
