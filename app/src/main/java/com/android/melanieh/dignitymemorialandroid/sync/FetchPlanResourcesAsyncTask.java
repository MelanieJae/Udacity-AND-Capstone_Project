package com.android.melanieh.dignitymemorialandroid.sync;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.ui.PlanOptionRecyclerViewAdapter;
import com.android.melanieh.dignitymemorialandroid.ui.PlanPageFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/26/17.
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

        doc = Jsoup.parse(urlString);
        Elements headingElements = doc.select("div[class=col-md-6]>h3");
        Elements imageUrlElements = doc.select("img[class=img-responsive center-block]");
        Elements detailTextElements = doc.select("div[class=col-md-12]");
        Elements estCostElements = doc.select("div[class=col-md-6]>p");

        heading = headingElements.first().text();
        imageUrlString = imageUrlElements.attr("src");  // exact content value of the attribute.
        detailText = detailTextElements.first().text();
        estCostString = estCostElements.first().text();

        option = new PlanOption(heading, detailText,
                        imageUrlString, estCostString);
        Timber.d("option: " + option.toString());
        optionsList.add(option);
        Timber.d("optionsList: " + optionsList);

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