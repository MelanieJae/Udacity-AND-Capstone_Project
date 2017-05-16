package com.android.melanieh.dignitymemorialandroid.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.regex.Pattern;

import timber.log.Timber;

import static com.android.melanieh.dignitymemorialandroid.R.id.textView;

/**
 * Created by melanieh on 5/1/17.
 */

public class SearchResultFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<? extends Object>> {

    private static final int SEARCH_LOADER_ID = 1;

    String queryString;
    SearchResultRecyclerAdapter adapter;
    RecyclerView resultsRecyclerView;
    String queryType;
    String[] locationArray;
    String zipCode;
    String provider;
    String firstName;
    String lastName;
    String locationLat;
    String locationLong;
    private static final String OBITS_QUERY_BASE_URL = BuildConfig.OBITS_QUERY_BASE_URL;
    private static final String PROVIDER_SITE_QUERY_BASE_URL = BuildConfig.PROVIDER_QUERY_BASE_URL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        resultsRecyclerView = (RecyclerView)rootView.findViewById(R.id.results_rv);

        queryType = getActivity().getIntent().getStringExtra("query type");
        zipCode = getActivity().getIntent().getStringExtra("zipCode");
        firstName = getActivity().getIntent().getStringExtra("first name");
        lastName = getActivity().getIntent().getStringExtra("last name");

        executeSearch(queryType);
        return rootView;
    }

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
        return new SearchPageLoader(getContext(), queryString);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<? extends Object>> loader, ArrayList<? extends Object> data) {

        adapter = new SearchResultRecyclerAdapter(getContext(), data);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<? extends Object>> loader) {

    }

    private void executeSearch(String queryType) {

        // build query string on base URL with search form input
        StringBuilder queryBuilder;
        String querySuffix;

        boolean isObitQuery = Pattern.compile(Pattern.quote("obituaries"),
                Pattern.CASE_INSENSITIVE).matcher(queryType).find();

        if (isObitQuery) {
            queryBuilder = new StringBuilder(OBITS_QUERY_BASE_URL);
            if (firstName != null) {
                queryBuilder.append("&fn=" + firstName);
            } else {
                // if you leave the firstName variable in here you will get obituary results for people
                // with the last name "Null"; queries with no names to the web service yield the most
                // recent obituaries from the last 14 days.
                //
                // are formatted as "&fn=&ln=&..."
                queryBuilder.append("&fn=");
            }
            if (lastName != null) {
                queryBuilder.append("&ln=" + lastName);
            } else {
                // if you leave the firstName variable in here you will get obituary results for people
                // with the last name "Null"; queries with no names to the web service yield the most
                // recent obituaries from the last 14 days.
                //
                // are formatted as "&firstname=&ln=&..."
                queryBuilder.append("&ln=");
            }
            querySuffix = getString(R.string.obits_search_query_suffix);

        } else {
            queryBuilder = new StringBuilder(PROVIDER_SITE_QUERY_BASE_URL);
            locationArray = getActivity().getIntent().getStringArrayExtra("current location");
            locationLat = locationArray[0];
            locationLong = locationArray[1];
            if (locationLat != null) {
                queryBuilder.append("latitude=" + locationLat);
            } else {
                queryBuilder.append("latitude=undefined");
            }
            if (locationLong != null) {
                queryBuilder.append("&longitude=" + locationLong);
            } else {
                queryBuilder.append("&longitude=undefined");
            }
            if (zipCode != null) {
                queryBuilder.append("&zipCode=" + zipCode);
            } else {
                queryBuilder.append("&zipCode=undefined");
            }
            querySuffix = getString(R.string.provider_search_query_suffix);
        }

        // append appropriate query suffix
        queryBuilder.append(querySuffix);
        queryString = queryBuilder.toString();

        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this).forceLoad();
    }

}

