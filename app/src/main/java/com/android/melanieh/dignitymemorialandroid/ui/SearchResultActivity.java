package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.R;

import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchResultActivity extends AppCompatActivity {

    private static final int SEARCH_LOADER_ID = 1;

    String queryString;
    RecyclerView.LayoutManager resultRVLayoutManager;
    SearchResultRecyclerAdapter adapter;
    RecyclerView resultsRecyclerView;
    String queryType;
    String zipCode;
    String provider;
    String firstName;
    String lastName;
    LinearLayout obitsFormLayout;
    LinearLayout providerFormLayout;
    TextView locSvcsView;
    Double locationLat;
    Double locationLong;
    private static final String OBITS_QUERY_BASE_URL = BuildConfig.OBITS_QUERY_BASE_URL;
    private static final String PROVIDER_SITE_QUERY_BASE_URL = BuildConfig.PROVIDER_QUERY_BASE_URL;
    private TextView txtOutput;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Timber.d("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected");
        Class destClass;
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                navigateUpTo(new Intent(this, MenuItemListActivity.class));
                return true;
            case R.id.action_access_preferences:
                destClass = SettingsActivity.class;
                launchMenuIntent(destClass, null);
                break;
            case R.id.action_view_plan_selections:
                destClass = PlanSummaryActivity.class;
                launchMenuIntent(destClass, null);
                break;
            case R.id.action_share:
                startActivity(Intent.createChooser(launchShareIntent(), getString(R.string.share_app_chooser_dialog_title)));
        }
        return true;
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Timber.d("launchMenuIntent:");
        Timber.d("destinationClass=" + destinationClass.toString());
        Timber.d("extraContent: " + extraContent);
        Intent intent = new Intent(this, destinationClass);
        intent.putExtra("button_extra_content", extraContent);
        startActivity(intent);
    }

    public Intent launchShareIntent() {Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }



}
