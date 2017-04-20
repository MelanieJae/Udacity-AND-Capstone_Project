package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

/**
 * Created by melanieh on 4/10/17.
 */

public interface ToolbarOptionsInterface {

    void launchMenuIntent(Class activity, String extraContent);
    void launchShareAction();
}
