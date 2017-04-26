package com.android.melanieh.dignitymemorialandroid.sync;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.ui.PlanOptionRecyclerViewAdapter;
import com.android.melanieh.dignitymemorialandroid.ui.PlanPageFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/25/17.
 */

public class FetchPlanResourcesAsyncTask extends AsyncTask<JobParameters, Void, ArrayList<PlanOption>> {

    private static final int ONE_OFF_ID = 2;
    public static final String ACTION_DATA_UPDATED = "com.udacity.stockhawk.ACTION_DATA_UPDATED";
    private static final int PERIOD = 300000;
    private static final int INITIAL_BACKOFF = 10000;
    private static final int PERIODIC_ID = 1;
    private static final int YEARS_OF_HISTORY = 2;
    ArrayList<PlanOption> optionsList = new ArrayList<>();

    private String urlString;
    Context context;

    public FetchPlanResourcesAsyncTask(Context context, String urlString) {
        Timber.d("asynctask constructor:");
        this.context = context;
        this.urlString= urlString;
    }

    @Override
    protected ArrayList<PlanOption> doInBackground(JobParameters...params) {
        Timber.d("doInBackground");
        optionsList = extractOptionsListFromHTML(urlString);
        return optionsList;
    }

    private ArrayList<PlanOption> extractOptionsListFromHTML(String urlString) {
        Timber.d("extractOptionsListFromHTML: ");

        String heading = "";
        String imageUrlString;
        String title = "";
        String detailText = "";
        String estCostString = "";
        Document doc;
        PlanOption option;

        try {
            doc = Jsoup.connect(urlString).get();

            // heading for each create-a-plan step screen
            Elements planStepHeadings = doc.select("span.menu-text");
            for (Element stepHeadingLink : planStepHeadings) {
                heading = stepHeadingLink.text();
                // options under each heading in each step screen
                Elements colsm7LevelLinks = doc.select("div.col-sm-7");
                for (Element rowOptionRowLink : colsm7LevelLinks) {
                    Elements rowOptionRowLinks = colsm7LevelLinks.select("div:has(option-row)");
                    // option image
                    imageUrlString = doc.select("div.modal-body.row.col-md-12.img.src").text();
                    // details text passed to dialog fragment
                    detailText = doc.select("div.col-sm-7.row.col-md-12.p.p").text();

                    for (Element col6mdLink : rowOptionRowLinks) {
                        Elements col6mdLinks = rowOptionRowLinks.select("div.col-md-6");
                        for (Element h3Link : col6mdLinks) {
                            //option title
                            title = col6mdLinks.select("h3").text();
                            estCostString = col6mdLinks.select("h3.p.strong").text();
                        }
                    }

                    Timber.d("heading= " + heading);
                    Timber.d("title= " + title);
                    Timber.d("detailText= " + detailText);
                    Timber.d("imageUrlString= " + imageUrlString);
                    Timber.d("estCostString= " + estCostString);

                    option = new PlanOption(heading, detailText,
                            imageUrlString, estCostString);
                    optionsList.add(option);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return optionsList;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onPostExecute(ArrayList<PlanOption> optionsList) {
        Timber.d("onPostExecute");
        PlanOptionRecyclerViewAdapter rvAdapter = new PlanOptionRecyclerViewAdapter(context,
                optionsList);
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(rvAdapter);

    }

}
