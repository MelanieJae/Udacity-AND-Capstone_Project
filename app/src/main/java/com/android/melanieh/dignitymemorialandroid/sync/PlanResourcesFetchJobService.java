package com.android.melanieh.dignitymemorialandroid.sync;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import com.android.melanieh.dignitymemorialandroid.R;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.ui.PlanPageFragment;

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
        fetchPlanResourcesAsyncTask = new FetchPlanResourcesAsyncTask
                (getApplicationContext(), "<div class=\"col-sm-5\">\n" +
                        "\n" +
                        "<a class=\"\" style=\"cursor: pointer;\" data-toggle=\"modal\" data-target=\"#image-130\"><img class=\"img-responsive center-block\" " +
                        "src=\"https://s3.amazonaws.com/busites_www/tdp/media/option-media/thumb_ceremony_outdoor_1455044981_618.png\" width=\"350\" height=\"255\" " +
                        "alt=\"Outdoor Location\"></a>     \n" +
                        "</div>\n" +
                        "<div class=\"col-sm-7\">\n" +
                        "           <div class=\"row option-row\">\n" +
                        "\n" +
                        "                <div class=\"col-md-6\">\n" +
                        "                    <h3>\n" +
                        "                        Outdoor Location                    </h3>\n" +
                        "                    <p><strong>Estimated Cost:</strong><br> $0 - $500</p>\n" +
                        "                </div>\n" +
                        "\n" +
                        "\n" +
                        "                                    <div class=\"col-md-6\">\n" +
                        "\n" +
                        "                        <div class=\"row text-center\">\n" +
                        "\n" +
                        "                            <div class=\"col-xs-6\">\n" +
                        "                                \n" +
                        "                                    <button onclick=\"addTdpOption(130)\"\n" +
                        "                                            class=\"btn btn-primary btn-inverse btn-option btn-add-option-top\">\n" +
                        "                                        <span class=\"btn-text\">Add</span>\n" +
                        "                                        <i class=\"fa fa-plus btn-icon\"></i>\n" +
                        "                                        <div id=\"btn-top-spin-130\" class=\"btn-loading kv-spin-center\" style=\"display: none; margin-left: 5px; font-size: 1.25em;\"><div class=\"kv-spin kv-spin-center\">&nbsp;</div>\n" +
                        "</div>                                    </button>\n" +
                        "\n" +
                        "                                                            </div>\n" +
                        "\n" +
                        "                            <div class=\"col-xs-6\">\n" +
                        "                                <a href='#option-details-130' data-toggle=\"collapse\"\n" +
                        "                                   class=\"btn btn-secondary btn-option\">\n" +
                        "                                    <span class=\"btn-text\">Details</span>\n" +
                        "                                    <i class=\"fa fa-pencil\"></i>\n" +
                        "                                </a>\n" +
                        "                            </div>\n" +
                        "\n" +
                        "                        </div>\n" +
                        "\n" +
                        "                    </div>\n" +
                        "                \n" +
                        "            </div>\n" +
                        "\n" +
                        "            <div class=\"row\">\n" +
                        "                \n" +
                        "                    <div class=\"col-md-12\">\n" +
                        "                        <p><p>Do you feel most at peace outside? Have life's best moments occurred with the sun in your face and the wind at your back? If so, you might consider holding your funeral or cremation service outdoors. Friends, family and loved ones will find solace knowing Mother Nature is by your side.</p></p>\n" +
                        "                    </div>\n" +
                        "                \n" +
                        "            </div>\n" +
                        "\n" +
                        "        </div>\n" +
                        "\n" +
                        "    </div>\n");
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
