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
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

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
        ArrayList<String> tempHeadingArray = new ArrayList<>();
        ArrayList<String> tempImageUrlArray = new ArrayList<>();
        ArrayList<String> tempDetailArray = new ArrayList<>();
        ArrayList<String> tempCostArray = new ArrayList<>();

        if (urlString != null) {
            doc = Jsoup.parse(urlString);
            // Jsoup library handles validation/null checks of node values
            Elements optionTitleElements = doc.select("span[class=menu-text]");
            Elements headingElements = doc.select("div[class=col-md-6]>h3");
            Elements imageUrlElements = doc.select("img[src]");
            Elements detailTextElements = doc.select("div[class=col-md-12]");
            Elements estCostElements = doc.select("div[class=col-md-6]>p");

            Element headingElement = doc.select("span[class=param]").first();
            Node headingNode = headingElement.nextSibling();
            heading = headingNode.toString();

//            optionTitle = optionTitleElements.first().
//                    text();
//            heading = headingElements.first().text();
//            imageUrlString = imageUrlElements.first().attr("src");
//            detailText = detailTextElements.first().text();
//            estCostString = estCostElements.first().text();

//            if (headingElements.next() != null) {
//                heading = headingElements.first().text();
//            }

//            if (imageUrlElements.next() != null) {
//                imageUrlString = imageUrlElements.next().text();
//            }
//
//            if (detailTextElements.next() != null) {
//                detailText = detailTextElements.next().text();
//            }
//
//            if (estCostElements.next() != null) {
//                estCostString = estCostElements.next().text();
//            }

            option = new PlanOption(optionTitle, heading, detailText,
                        imageUrlString, estCostString);
            optionsList.add(option);
            Timber.d("optionsList: " + optionsList);
            } else {
            Timber.e("Error: query string is null");
        }
        return optionsList;
    }

}
