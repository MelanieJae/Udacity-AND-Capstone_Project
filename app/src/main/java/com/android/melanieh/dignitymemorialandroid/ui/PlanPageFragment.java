package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<PlanOption>> {

    android.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    View rootView;
    ArrayList<PlanOption> optionsList;
    PlanOptionRecyclerViewAdapter rvAdapter;
    TextView estCostView;
    TextView planningStepTitleView;
    TextView emptyViewText;
    RecyclerView.LayoutManager layoutManager;
    Uri planUri;
    TextView whichFragment;

    private String staticContent;
    private int loaderId;

    public PlanPageFragment() {
        //
    }

    public static PlanPageFragment newInstance(String content, int loaderId) {
        PlanPageFragment fragment = new PlanPageFragment();
        Bundle args = new Bundle();
        args.putString("static content", content);
        args.putInt("loaderId", loaderId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        staticContent = getArguments().getString("static content");
        loaderId = getArguments().getInt("loaderId");
    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView: ");

        rootView = inflater.inflate(R.layout.plan_viewpager_item, container, false);
        whichFragment = (TextView) rootView.findViewById(R.id.which_fragment);
        toolbar = (android.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        estCostView = (TextView) rootView.findViewById(R.id.toolbar_est_cost_tv);
        planningStepTitleView = (TextView) rootView.findViewById(R.id.toolbar_step_title);
//        planningStepTitleView.setText(fragmentTagTitleString);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.plan_option_rv);

        // get new plan Uri from plan creation form
        planUri = getActivity().getIntent().getData();

//        whichFragment.setText(staticContent);
        getActivity().getSupportLoaderManager().initLoader(loaderId, null, this).forceLoad();
        return rootView;
    }

    @Override
    public Loader<ArrayList<PlanOption>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader:");
        return new PlanPageLoader(getContext(), staticContent);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PlanOption>> loader, ArrayList<PlanOption> data) {
        Timber.d("onLoadFinished:");
        Timber.d("data= " + data.toString());
        Timber.d("planUri= " + planUri);
        planningStepTitleView.setText(data.get(0).getTitle());

        if (data != null && !data.isEmpty()) {
            rvAdapter = new PlanOptionRecyclerViewAdapter(getContext(), data, planUri);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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



}









