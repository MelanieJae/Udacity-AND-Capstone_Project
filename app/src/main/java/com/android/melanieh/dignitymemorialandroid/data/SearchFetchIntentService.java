package com.android.melanieh.dignitymemorialandroid.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import timber.log.Timber;

/*** Created by melanieh on 4/22/17.
 * This class handles data fetching tasks for all search, find and resource loading from
 * the Dignity Memorial/SCI and Legacy web services.
 */

public class SearchFetchIntentService extends IntentService {

    public SearchFetchIntentService(String name) {
        super(SearchFetchIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent jobIntent) {
    // Gets data from the incoming Intent
        String dataString = jobIntent.getDataString();
        // Do work here, based on the contents of dataString
        Timber.d("Intent handled");
//        DataFetchJob.getQuotes(getApplicationContext());
    }
}
