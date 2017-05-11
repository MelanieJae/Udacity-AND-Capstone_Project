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
            optionTitle = optionTitleElements.first().
                    text();

            Elements headingElements = doc.select("div[class=col-md-6]>h3");
            Iterator<Element> headingsIterator = headingElements.iterator();
            while (headingsIterator.hasNext()) {
                heading = headingsIterator.next().text();
                tempHeadingArray.add(heading);
                Timber.d("tempHeadingArray: " + tempHeadingArray);
            }

            Elements imageUrlElements = doc.select("img[src]");
            Iterator<Element> imageStringIterator = imageUrlElements.iterator();
            while (imageStringIterator.hasNext()) {
                imageUrlString = imageStringIterator.next().attr("src");
                tempImageUrlArray.add(imageUrlString);
            }

            Elements detailTextElements = doc.select("div[class=col-md-12]");
            Iterator<Element> detailIterator = detailTextElements.iterator();
            while (detailIterator.hasNext()) {
                detailText = detailIterator.next().text();
                tempDetailArray.add(detailText);
            }

            Elements estCostElements = doc.select("div[class=col-md-6]>p");
            Iterator<Element> costIterator = estCostElements.iterator();
            while (costIterator.hasNext()) {
                estCostString = costIterator.next().text();
                tempCostArray.add(estCostString);
            }

            for (int i=0; i<headingElements.size(); i++) {
                option = new PlanOption(optionTitle, tempHeadingArray.get(i), tempDetailArray.get(i),
                        tempImageUrlArray.get(i), tempCostArray.get(i));
                optionsList.add(option);
                Timber.d("optionsList: " + optionsList);
            }
        } else {
            Timber.e("Error: query string is null");
        }
        return optionsList;
    }

}
