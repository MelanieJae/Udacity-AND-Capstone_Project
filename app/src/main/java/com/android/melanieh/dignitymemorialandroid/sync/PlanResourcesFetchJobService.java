package com.android.melanieh.dignitymemorialandroid.sync;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;

import java.io.Serializable;

import timber.log.Timber;

/**
 * Created by melanieh on 4/25/17.
 * This is the job service responsible for scheduling the job of obtaining UI resources for the
 * create-a-plan
 * stepped fragments
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PlanResourcesFetchJobService extends JobService {

    private FetchPlanResourcesAsyncTask fetchPlanResourcesAsyncTask;

    @Override
    public boolean onStartJob(JobParameters params) {
        // Note: this is preformed on the main thread.
        Timber.d("JobSvc: onStartJob:");
        fetchPlanResourcesAsyncTask = new FetchPlanResourcesAsyncTask(this, BuildConfig.CEREMONY_PLAN_URL);
        fetchPlanResourcesAsyncTask.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // job will be not rescheduled, stops when asynctask is null(i.e. it has executed)
        Timber.d("JobSvc: onStopJob:");
        if (fetchPlanResourcesAsyncTask == null) {
            fetchPlanResourcesAsyncTask.cancel(true);
            jobFinished(params, false);
        }
        return true;
    }

}
