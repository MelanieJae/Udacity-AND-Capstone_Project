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
        Timber.d("urlString:" + urlString);

        String optionTitle = "";
        String heading = "";
        String imageUrlString = "";
        String detailText = "";
        String estCostString = "";
        Document doc;
        PlanOption option;
        ArrayList<PlanOption> optionsList = new ArrayList<>();

        if (urlString != null) {
            doc = Jsoup.parse(urlString);
            // Jsoup library handles validation/null checks of node values
            Elements optionTitleElements = doc.select("span[class=menu-text]");
            Elements headingElements = doc.select("div[class=col-md-6]>h3");
            Elements imageUrlElements = doc.select("img[src]");
            Elements detailTextElements = doc.select("div[class=col-md-12]");
            Elements estCostElements = doc.select("div[class=col-md-6]>p");

//            while (optionTitleElements.first() != null || optionTitleElements.next() != null) {
                optionTitle = optionTitleElements.first().text();
                Timber.d("optionTitle: " + optionTitle);
                heading = headingElements.first().text();
                imageUrlString = imageUrlElements.attr("src");  // exact content value of the attribute.
                Timber.d("imageUrlString: " + imageUrlString);
                detailText = detailTextElements.first().text();
                Timber.d("detailText: " + detailText);
                estCostString = estCostElements.first().text();
                Timber.d("estCostString: " + estCostString);

                option = new PlanOption(optionTitle, heading, detailText,
                        imageUrlString, estCostString);
                Timber.d("option: " + option.toString());
                optionsList.add(option);
                Timber.d("optionsList: " + optionsList);

//                while (headingElements.next() != null) {
//                    heading = optionTitleElements.next().text();
//                    imageUrlString = imageUrlElements.next().text();
//                    detailText = optionTitleElements.next().text();
//                    estCostString = estCostElements.next().text();
//
//                }



//            }

        } else {
            Timber.e("Error: query string is null");
        }
//        Timber.d("optionsList: " + optionsList.toString());
        return optionsList;
    }

}
