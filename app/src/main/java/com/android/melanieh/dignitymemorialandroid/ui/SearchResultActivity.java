package com.android.melanieh.dignitymemorialandroid.ui;

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
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchResultActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Object>> {

    private static final int SEARCH_LOADER_ID = 1;
    String queryString;
    RecyclerView.LayoutManager resultRVLayoutManager;
    SearchResultRecyclerAdapter adapter;
    RecyclerView resultsRecyclerView;
    TextView test;
    String queryType;
    private static final String OBITS_QUERY_BASE_URL = BuildConfig.OBITS_QUERY_BASE_URL;
    private static final String PROVIDER_SITE_QUERY_BASE_URL = BuildConfig.PROVIDER_QUERY_BASE_URL;

    // sample provider query string
    // "http://ows.dignitymemorial.com/mapcontrol/DignityMemorialServices.svc/" +
    //        "PlotLocationNames?searchTerm=winter%20haven,%20fl&brand=DM&locale=EN&maxRecords=4" +
    //        "&startPage=1&recordsPerPage=4";

    // search obituary query string
    // http://www.legacy.com/webservices/SCI/DignityMemorial/search.svc/SearchObituaries?

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        resultsRecyclerView = (RecyclerView)findViewById(R.id.results_rv);

        // grab incoming data from menu item button selection
        queryType = getIntent().getStringExtra("EXTRA_CONTENT");
        Timber.d("queryType: " + queryType);

        // use query type info from button selection to select correct base query
        queryString = constructBaseQueryString(queryType);
        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this).forceLoad();

        collectInfoFromSearchForm();
    }

    /* Results Loader */

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

//        queryString = "http://www.legacy.com/webservices/SCI/DignityMemorial/" +
//                "search.svc/SearchObituaries?";
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

    private String constructBaseQueryString(String queryType) {
        StringBuilder baseQueryBuilder;
        if (queryType.contains("Obituaries")) {
            baseQueryBuilder = new StringBuilder(OBITS_QUERY_BASE_URL);
        } else {
            baseQueryBuilder = new StringBuilder(PROVIDER_SITE_QUERY_BASE_URL);
            baseQueryBuilder.append("searchTerm=winter%20haven,%20fl&brand=DM&locale=EN&maxRecords=4&startPage=1&recordsPerPage=4");
        }
        Timber.d("constructBaseQueryString: " + baseQueryBuilder.toString());
        return baseQueryBuilder.toString();
    }

    private void collectInfoFromSearchForm() {

    }

}
