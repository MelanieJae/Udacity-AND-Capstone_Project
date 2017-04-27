package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

/**
 * Created by melanieh on 4/10/17.
 */

public interface MenuOptionsInterface {

    // launchMenuIntent is implemented only in activities throughout this project
    void launchMenuIntent(Class activity, String extraContent);

    // launchShareIntent is implemented only in fragments throughout this project
    Intent launchShareIntent();
}
