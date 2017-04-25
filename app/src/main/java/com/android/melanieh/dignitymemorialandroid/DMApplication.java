package com.android.melanieh.dignitymemorialandroid;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by melanieh on 4/22/17.
 */

public class DMApplication extends Application {

    Tracker tracker;
    public void startTracking() {

        /** Google Analytics tracker */
        if (tracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            // Get the config data for the tracker
            tracker = ga.newTracker(R.xml.analytics_tracker);

            // enable auto-tracking
            ga.enableAutoActivityReports(this);

            // enable auto-logging
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    /** called by activities */
    public Tracker getTracker() {
        // Make sure the tracker exists
        startTracking();

        // Then return the tracker
        return tracker;
    }

}
