package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.R;

import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;
import com.android.melanieh.dignitymemorialandroid.data.SearchFetchIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchResultActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Object>>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int SEARCH_LOADER_ID = 1;
    String queryString;
    RecyclerView.LayoutManager resultRVLayoutManager;
    SearchResultRecyclerAdapter adapter;
    RecyclerView resultsRecyclerView;
    TextView test;
    TextView zipCodeTV;
    TextView firstNameTV;
    TextView lastNameTV;
    EditText zipCodeET;
    EditText firstNameET;
    EditText lastNameET;
    String queryType;
    String zipCode;
    String firstName;
    String lastName;
    String searchParams[];
    LinearLayout obitsFormLayout;
    LinearLayout providerFormLayout;
    TextView locSvcsView;
    private static final String OBITS_QUERY_BASE_URL = BuildConfig.OBITS_QUERY_BASE_URL;
    private static final String PROVIDER_SITE_QUERY_BASE_URL = BuildConfig.PROVIDER_QUERY_BASE_URL;
    private TextView txtOutput;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        /* Location services Api client */
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        obitsFormLayout = (LinearLayout)findViewById(R.id.obits_search_form_layout);
        providerFormLayout = (LinearLayout)findViewById(R.id.provider_search_form_layout);
        resultsRecyclerView = (RecyclerView)findViewById(R.id.results_rv);
        locSvcsView = (TextView)findViewById(R.id.location_services_readout);

        zipCodeTV = (TextView)findViewById(R.id.zip_code);
        zipCodeET = (EditText)findViewById(R.id.search_form_zipcode_et);

        firstNameTV = (TextView)findViewById(R.id.first_name);
        firstNameET = (EditText)findViewById(R.id.search_form_first_name_et);

        lastNameTV = (TextView)findViewById(R.id.last_name);
        lastNameET = (EditText)findViewById(R.id.search_form_last_name_et);

        // grab incoming data from menu item button selection
        queryType = getIntent().getStringExtra("EXTRA_CONTENT");
        Timber.d("queryType: " + queryType);

        // use query type info from button selection to select correct base query
        executeSearch(queryType);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        Timber.d("connecting to API client...");
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        Timber.d("API client disconnected.");
        super.onStop();
    }

    /* Results Recycler View */

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp < 900) {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            return lm;
        } else {
            GridLayoutManager glm = new GridLayoutManager(this, 2);
            return glm;
        }
    }

    // onClick class for Get directions link in layout
    private void getDirections(View view) {
        // TODO: complete logic
    }

    @Override
    public Loader<ArrayList<Object>> onCreateLoader(int id, Bundle args) {
        return new SearchPageLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Object>> loader, ArrayList<Object> data) {
        Timber.d("onLoadFinished:");
        adapter = new SearchResultRecyclerAdapter(SearchResultActivity.this, data);
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.setLayoutManager(getLayoutManager());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Object>> loader) {

    }

    private void executeSearch(String queryType) {
        // build query string on base URL with search form input
        StringBuilder queryBuilder;
        String querySuffix;
        if (queryType.contains("Obituaries")) {
            queryBuilder = new StringBuilder(OBITS_QUERY_BASE_URL);
//            String firstName = firstNameET.getText().toString();
            queryBuilder.append("fn=" + firstName);
//            String lastName = lastNameET.getText().toString();
            queryBuilder.append("ln=" + lastName);
            querySuffix=getString(R.string.obits_search_query_suffix);

        } else {
            queryBuilder = new StringBuilder(PROVIDER_SITE_QUERY_BASE_URL);
//            String zipCode = zipCodeET.getText().toString();
            queryBuilder.append("zipCode=" + zipCode);
            querySuffix=getString(R.string.provider_search_query_suffix);
            // TODO: add path for "get location" input for lat & long from Google Play Location
        }

        // append appropriate query suffix
        queryBuilder.append(querySuffix);
        queryString = queryBuilder.toString();
        Timber.d("final query string: " + queryString);

        obitsFormLayout.setVisibility(View.GONE);
        providerFormLayout.setVisibility(View.GONE);

        // use loader to fetch data
        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this).forceLoad();
        // OR
        // use intent service to fetch data

        /*
        * Creates a new Intent to start the SearchFetchIntentService
        * Passes a URI in the
        * Intent's "data" field, specifically the variable queryString
        */
//        Timber.d("Launching data fetch intent service...");
//
//        Intent fetchServiceIntent = new Intent(this, SearchFetchIntentService.class);
//        fetchServiceIntent.setData(Uri.parse(queryBuilder.toString()));
//        startService(fetchServiceIntent);
    }

    // using coarse filter and low power accuracy (city level, within 10km) for obit and provider searches since it's the zip code
    // or lat/long that is being used and accuracy within 100 m is not necessary
    // if doing the navigation to plot, this would need HIGH_ACCURACY and FINE_LOCATION settings
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Google API Client is connected");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // millisecs
        try {
            PendingResult<Status> statusPendingResult =
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                            locationRequest, this);
            //TODO check permission; requires runtime permission grants that can be rejected by user and denial must be handled here
        } catch (SecurityException e) {
            Timber.wtf(e, "Security Exception: user has not granted all necessary permissions");
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Google API Client connection has been suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Google API Client connection has failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("location string: " + location.toString());
        locSvcsView.setText(location.toString());
        // TODO: parse lat/lng values from location and append to search queries
    }




}
