package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import com.android.melanieh.dignitymemorialandroid.widget.DMWidgetProvider;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanViewPagerFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<PlanOption>> {

    CollapsingToolbarLayout toolbar;
    RecyclerView recyclerView;
    View rootView;
    ArrayList<PlanOption> optionsList;
    PlanOptionRecyclerViewAdapter rvAdapter;
    TextView estCostView;
    TextView planningStepTitleView;
    TextView emptyViewText;
    RecyclerView.LayoutManager layoutManager;
    Uri planUri;
    static TextView staticContentView;
    String OPTIONS_STATIC_CONTENT;
    String planUriString;

    private int loaderId;

    public PlanViewPagerFragment() {
        //
    }

    public static PlanViewPagerFragment newInstance(String content, int loaderId
            , String planUriString) {
        Timber.d("plan page fragment new instance:");
        PlanViewPagerFragment fragment = new PlanViewPagerFragment();
        Bundle args = new Bundle();
        args.putString("static content", content);
        args.putInt("loaderId:", loaderId);
        Timber.d("loaderId" + loaderId);
        args.putString("planUriString", planUriString);
        Timber.d("planUriString: " + planUriString);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate: ");
        setHasOptionsMenu(true);
        OPTIONS_STATIC_CONTENT = getArguments().getString("static content");
        loaderId = getArguments().getInt("loaderId");
        Timber.d("loaderId", loaderId);
        planUriString = getArguments().getString("planUriString");
        Timber.d("planUriString at OnCreate: " + planUriString);

    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView: ");

        rootView = inflater.inflate(R.layout.plan_viewpager_fragment, container, false);
        staticContentView = (TextView) rootView.findViewById(R.id.static_content);
        toolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        toolbar.setTitleEnabled(false);
//        estCostView = (TextView) rootView.findViewById(R.id.toolbar_est_cost_tv);
        planningStepTitleView = (TextView) rootView.findViewById(R.id.toolbar_step_title);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.plan_option_rv);
        Timber.d("planUriString at OnCreateView before loader call: " + planUriString);

        getLoaderManager().initLoader(loaderId, null, this).forceLoad();
//        // ensures the planning step title stays visible when the toolbar is collapsed
//
//        toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
//        toolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);

        return rootView;
    }

    @Override
    public Loader<ArrayList<PlanOption>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader:");
        Timber.d("planUriString at OnCreateLoader: " + planUriString);

        return new PlanPageLoader(getContext(), OPTIONS_STATIC_CONTENT);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PlanOption>> loader, ArrayList<PlanOption> data) {
        Timber.d("onLoadFinished:");
        Timber.d("data= " + data.toString());
        Timber.d("planUri= " + planUriString);
        planningStepTitleView.setText(data.get(0).getTitle());

        if (data != null) {
            rvAdapter = new PlanOptionRecyclerViewAdapter(getContext(), data, planUriString);
            layoutManager = getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(rvAdapter);
        } else {
            updateEmptyView();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PlanOption>> loader) {

    }

    private void updateEmptyView() {
        if (!Utility.isNetworkAvailable(getContext())) {
            emptyViewText.setText(getString(R.string.emptyview_no_internet_connection));
        } else {
            emptyViewText.setText(getString(R.string.emptyview_no_results));
        }
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

}









