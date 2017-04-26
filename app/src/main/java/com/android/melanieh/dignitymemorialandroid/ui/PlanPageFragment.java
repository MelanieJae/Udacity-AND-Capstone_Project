package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.sync.PlanResourcesFetchJobService;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanPageFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<PlanOption>>{

    android.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    View rootView;
    ArrayList<PlanOption> phOptionsList;
    PlanOptionRecyclerViewAdapter rvAdapter;
    TextView estCostView;
    TextView planningStepTitleView;

    // job scheduler job id
    private static final int JOB_ID = 1;


    public PlanPageFragment() {
        //
    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // obtain views for resources to be loaded in to via jobscheduler kicked off below
        Timber.d("onCreateView: ");
        phOptionsList = new ArrayList<>();
//        phOptionsList.add(new PlanOption("Hotels", "hotels", BuildConfig.APP_BAR_IMAGE_URL,
//                "Est. cost=$0-$500"));
//        phOptionsList.add(new PlanOption("Ceremony", "ceremony", BuildConfig.APP_BAR_IMAGE_URL,
//                "Est. cost=$0-$500"));
        rootView = inflater.inflate(R.layout.plan_viewpager_item, container, false);
        toolbar = (android.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        estCostView = (TextView)rootView.findViewById(R.id.toolbar_est_cost_tv);
        planningStepTitleView = (TextView)rootView.findViewById(R.id.toolbar_step_title);
//        planningStepTitleView.setText(fragmentTagTitleString);

//        recyclerView = (RecyclerView)rootView.findViewById(R.id.plan_option_rv);
//        rvAdapter = new PlanOptionRecyclerViewAdapter(getContext(),
//                phOptionsList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(rvAdapter);
//        getActivity().getSupportLoaderManager().initLoader(3, null, this).forceLoad();

        /** job scheduler for fetching resources for page in order to sync
         * what is present on the website with what is present in the app */
        PersistableBundle jobExtrasBundle = new PersistableBundle();
        String[] resourcesQueryStringArray = {BuildConfig.CEREMONY_PLAN_URL};

//        String[] resourcesQueryStrings = {BuildConfig.CEREMONY_PLAN_URL, BuildConfig.VISITATION_PLAN_URL,
//                BuildConfig.RECEPTION_PLAN_URL, BuildConfig.SITE_SELECTION_PLAN_URL, BuildConfig.CONTAINER_PLAN_URL};

//        jobExtrasBundle.putStringArray("urlStringsArray", resourcesQueryStringArray);
        ComponentName fetchResourcesJobSvc = new ComponentName(getActivity(), PlanResourcesFetchJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, fetchResourcesJobSvc)
                .setExtras(jobExtrasBundle)
                // saves on data charges and battery life by running only on private wi-fi by default
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                // the next two save battery life by running the job only when the device is charging
                // and idle
                // uncomment for running on device
//                .setRequiresDeviceIdle(true)
//                .setRequiresCharging(true)
//                .setOverrideDeadline(86400000) //the job will be executed anyway after one hour
                .build();
        JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS) {
            Timber.d("Job scheduled successfully!");
        }

        return rootView;
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp < 900) {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            return lm;
        } else {
            RecyclerView.LayoutManager glm = new GridLayoutManager(getContext(), 2);
            return glm;
        }
    }

    // query first for existing plan URI, then insert new plan entry if not present
//        if (planUri != null) {
//            values.put(PlanEntry.COLUMN_ ? ??,heading);
//            getContext().getContentResolver().update(planUri, values, null, null);
//        } else {
//            values.put(UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME, planName);
//            // etc...
//            Uri planUri = getContext().getContentResolver().insert(UserSelectionContract.PlanEntry.CONTENT_URI, values);
//        }

    private ArrayList<Integer> loadFragmentResources(String fragmentTag) {
        ArrayList<Integer> resourcesList = new ArrayList<>();


        return resourcesList;
    }

    private void addOptionToPlan(String selection) {
        ContentValues values = new ContentValues();
        switch (selection) {
//            case ceremony:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_CEREMONY_SELECTION, selection);
//                break;
//            case reception:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_RECEPTION_SELECTION, selection);
//                break;
//            case visitation:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_VISITATION_SELECTION, selection);
//                break;
//            case siteFragment:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_SITE_SELECTION, selection);
//                break;
//            case container:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_CONTAINER_SELECTION, selection);
//                break;
            default:
                Timber.e("Invalid Fragment tag");
        }
    }

    /** plan option loader */
    @Override
    public Loader<ArrayList<PlanOption>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader:");
        String queryString = "https://www.thedignityplanner.com/create-plan/funeralceremony";
        return new PlanPageLoader(getContext(), queryString);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PlanOption>> loader, ArrayList<PlanOption> data) {
        Timber.d("onLoadFinished:");
        Timber.d("data=" + data.get(0).getHeading());
//        PlanOptionRecyclerViewAdapter rvAdapter = new PlanOptionRecyclerViewAdapter(getActivity(), );
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PlanOption>> loader) {

    }

}


