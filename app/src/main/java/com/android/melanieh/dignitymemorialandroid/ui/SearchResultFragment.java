package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 5/1/17.
 */

public class SearchResultFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<? extends Object>>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    /* Location services Api client for current location and Maps */
//        googleApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

        queryType = getActivity().getIntent().getStringExtra("query type");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        resultsRecyclerView = (RecyclerView)rootView.findViewById(R.id.results_rv);

        // grab incoming data from menu item button selection and any sharedprefs info
        // grab any saved zip codes
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (sharedPrefs.contains("zipCode") || sharedPrefs.contains("provider")) {
            zipCode = sharedPrefs.getString("zipCode", "");
            provider = sharedPrefs.getString("provider", "");
        }

        queryType="obituaries";
//        queryType = getActivity().getIntent().getStringExtra("query type");
        zipCode = getActivity().getIntent().getStringExtra("zipCode");
        firstName = getActivity().getIntent().getStringExtra("first name");
        lastName = getActivity().getIntent().getStringExtra("last name");

        locationLat = getActivity().getIntent().getDoubleExtra("current location lat", 0);
        locationLong = getActivity().getIntent().getDoubleExtra("current location long", 0);
        Timber.d("queryType: " + queryType);
        Timber.d("firstName= " + firstName);
        Timber.d("lastName= " + lastName);
        Timber.d("zipCode= " + zipCode);
        Timber.d("provider= " + provider);
        Timber.d("locationLat= " + locationLat);
        Timber.d("locationLong= " + locationLong);

        executeSearch(queryType);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
//        googleApiClient.connect();
        Timber.d("connecting to API client...");
    }

    @Override
    public void onStop() {
        // disconnect Google Play services API client
//        googleApiClient.disconnect();
        Timber.d("API client disconnected.");
        super.onStop();
    }

    /* Results Recycler View */

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp > 600) {
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
            return glm;
        } else {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            return lm;
        }
    }

    @Override
    public Loader<ArrayList<? extends Object>> onCreateLoader(int id, Bundle args) {
        Timber.d("loader: queryString: " + queryString);
        return new SearchPageLoader(getContext(), queryString);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<? extends Object>> loader, ArrayList<? extends Object> data) {
        Timber.d("onLoadFinished:");
        adapter = new SearchResultRecyclerAdapter(getContext(), data);
        resultsRecyclerView.setLayoutManager(getLayoutManager());
        resultsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<? extends Object>> loader) {

    }

    // using coarse filter and low power accuracy (city level, within 10km) for obit and provider searches since it's the zip code
    // or lat/long that is being used and accuracy within 100 m is not necessary
    // if doing the navigation to plot, this would need HIGH_ACCURACY and FINE_LOCATION settings

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Google API Client is connected");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
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

    }

    private void executeSearch(String queryType) {

        // build query string on base URL with search form input
        StringBuilder queryBuilder;
        String querySuffix;
        if (queryType.contains("obituaries")) {
            queryBuilder = new StringBuilder(OBITS_QUERY_BASE_URL);
            queryBuilder.append("&firstname=" + firstName);
            queryBuilder.append("&lastname=" + lastName);
            querySuffix = getString(R.string.obits_search_query_suffix);

        } else {
            queryBuilder = new StringBuilder(PROVIDER_SITE_QUERY_BASE_URL);
            queryBuilder.append("&latitude=" + locationLat);
            queryBuilder.append("&longitude=" + locationLong);
            if (zipCode != null) {
                queryBuilder.append("&zipCode=" + zipCode);
            } else {
                queryBuilder.append("&zipCode=undefined");
            }
            querySuffix = getString(R.string.provider_search_query_suffix);
            // TODO: add path for "get location" input for lat & long from Google Play Location
        }

        // append appropriate query suffix
        queryBuilder.append(querySuffix);
        queryString = queryBuilder.toString();
        Timber.d("final query string: " + queryString);


        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this).forceLoad();
    }

    private String getPrefKey(Preference preference) {
        String prefDefaultKey = getResources().getString(R.string.pref_zip_code_key);
        return prefDefaultKey;
    }

}

