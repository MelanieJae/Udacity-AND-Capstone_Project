package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/17/17.
 * Loader for Create-a-plan UI flow screens; extracts text and image strings from HTML response to
 * populate fragments in the create-a-plan viewpager
 */

public class PlanPageLoader extends AsyncTaskLoader{

    // sample query string returning HTML response to grab text items
    // https://www.thedignityplanner.com/my-plans/index

    private String urlString;
    ArrayList<PlanOption> fragmentPlanOptionsList;

    public PlanPageLoader(Context context, String urlString) {
        super(context);
        Timber.d("PlanPage loader constructor: ");
        this.urlString = urlString;
    }

    @Override
    public ArrayList<PlanOption> loadInBackground() {
        Timber.v("loadInBackground: ");

        // Perform HTTP request to the URL and receive an HTML response back
        Timber.v("obtainHTML: urlString= " + urlString);
        fragmentPlanOptionsList = extractOptionsListFromHTML(urlString);

        // Extract relevant fields from the HTML response and create a {@link PlanOption} list.
        return fragmentPlanOptionsList;
    }

    private ArrayList<PlanOption> extractOptionsListFromHTML(String urlString) {
        Timber.d("extractOptionsListFromHTML: ");

        String heading = "Home";
        String imageUrlString = "https://s3.amazonaws.com/busites_www/tdp/1/1/media/option-media" +
                "/thumb_cooking03_1457631930_1349.png";
        String title = "";
        String detailText = "Testing";
        Document doc;
        ArrayList<PlanOption> optionsList = new ArrayList<>();
        PlanOption option;

    try {
        doc = Jsoup.connect(urlString).get();

        // get title of the page
        title = doc.title();
        System.out.println("Title: " + title);

        // get all links
        Elements links = doc.select("a[href]");
        for (Element link : links) {

            // get the value from href attribute
            System.out.println("\nLink : " + link.attr("href"));
            System.out.println("Text : " + link.text());
//            if (link.text().contains("Home")) {
//                heading = link.text();
//            } else {
//                heading = "Heading not available";
//            }
//
//            if (link.text().contains("Create a Plan")) {
//                detailText = link.text();
//
//            } else {
//                detailText = "Detail Text not available";
//            }
//
//            if (link.text().contains("http:")) {
//                imageUrlString = link.text();
//            } else {
//                imageUrlString = "Image source not Available";
//            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
        Timber.d("heading= " + heading);
        Timber.d("detailText= " + detailText);
        Timber.d("imageUrlString= " + imageUrlString);
        option = new PlanOption("heading", "detailText",
                BuildConfig.APP_BAR_IMAGE_URL, "0");
        optionsList.add(option);
        return optionsList;

    }

}
