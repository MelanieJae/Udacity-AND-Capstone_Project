package com.android.melanieh.dignitymemorialandroid.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import timber.log.Timber;

/*** Created by melanieh on 4/22/17.
 * This class handles data fetching tasks for all search, find and resource loading from
 * the Dignity Memorial/SCI and Legacy web services.
 */

public class FetchIntentService extends IntentService {

    ArrayList<? extends Object> resultsList;

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION =
            "com.android.melanieh.dignitymemorialandroid.INTENT_SVC_BROADCAST";
    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS_KEY =
            "com.android.melanieh.dignitymemorialandroid.STATUS";

    public FetchIntentService() {
        super(FetchIntentService.class.getSimpleName());
    }

    /*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
//    Intent localIntent =
//            new Intent(BROADCAST_ACTION)
//                    // Puts the status into the Intent
//                    .putExtra(EXTENDED_DATA_STATUS_KEY, status);
//    // Broadcasts the Intent to receivers in this app.
//    LocalBroadcastManager iSvcBroadcastManager = LocalBroadcastManager.getInstance(this);
//    boolean isBroadcastSent = iSvcBroadcastManager.sendBroadcast(localIntent);


    // this is what is called when startActivity for the intent in the activity is called.
    // It does the work you want done (so whatever is currently in loadInBG in the loader)
    // and produces the results
    // These results are then passed back to the activity via a broadcast receiver to interact with the UI
    // since an intentservice cannot do that in the same way a loader can (through onLoadFinished)
    // or asynctask can(through onPostExecute)
    @Override
    protected void onHandleIntent(@Nullable Intent jobIntent) {
        Timber.d("onHandleIntent");

        // Gets data from the incoming Intent
        String dataString = jobIntent.getDataString();
        Timber.d("dataString: " + dataString);
        ResourcesFetchJob.getResources();
        // Do work here, based on the contents of dataString


}
