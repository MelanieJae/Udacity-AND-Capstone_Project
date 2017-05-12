package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.Provider;

import com.android.melanieh.dignitymemorialandroid.R;

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
import java.util.Iterator;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by melanieh on 4/16/17.
 */

public class SearchPageLoader extends AsyncTaskLoader {

    ArrayList<Obituary> obituariesList;
    ArrayList<Provider> providersList;
    String urlString;

    public SearchPageLoader(Context context, String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Override
    public ArrayList<? extends Object> loadInBackground() {
        Timber.v("loadInBackground: ");

        // Perform the HTTP request for book listings data and process the response.
        ArrayList<? extends Object> resultsList = obtainSearchResults(urlString);
        return resultsList;
    }
    /** Query the Legacy or DM /SCI web services and return a list of {@link Object} search result objects.
     * The Object can be one of the following:
     * 1. Obituary
     * 2. Provider
     * */

    public ArrayList<? extends Object> obtainSearchResults(String requestUrlString) {
        Timber.d("obtainSearchResults: ");
        Timber.d("requestUrlString: ");
        ArrayList<? extends Object> resultsList;
//
////         uncomment for testing purposes only to simulate slow network
///         response to test the progress indicator
////        try {
////            Thread.sleep(5000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }

        boolean isObitQuery = Pattern.compile(Pattern.quote(BuildConfig.OBITS_QUERY_BASE_URL),
                Pattern.CASE_INSENSITIVE).matcher(urlString).find();
        boolean isProviderQuery = Pattern.compile(Pattern.quote(BuildConfig.PROVIDER_QUERY_BASE_URL),
                Pattern.CASE_INSENSITIVE).matcher(urlString).find();
        String htmlResponse = "";
        String jsonResponse = "";

        if (isObitQuery) {
            resultsList = extractObituaryData(htmlResponse);
        } else {
            resultsList = extractProviderData(jsonResponse);
        }

        // Return a list of {@link Object} events. The object will be either:
        // 1. an Obituary object or 2. a Provider object
        return resultsList;

    }

    /**
     * Make an HTTP request to the given URL and return a String as the response (used for JSON
     * and non-JSoup HTML parsing as JSoup connect method called in the extractData methods below
     * handles the Http connection.)
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Timber.e("Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Timber.wtf(e, "Problem retrieving the search JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Object} object by parsing out information
     * about the first book from the inputstring. The object wil be either:
     * 1. an Obituary object; or
     * 2. a Provider object
     */

    private ArrayList<Obituary> extractObituaryData(String htmlResponse) {
        /** tags needed for obituary:
         * 1. "ObitName"
         * 2. "ObitText"
         *
         */
        Timber.d("htmlResponse: " + htmlResponse);
        String obitName = "";
        String obitPreviewText = "";

        String obitFullTextLinkHeading = "";
        String obitFullTextLink = "";
        String detailText = "";
        String estCostString = "";
        Document doc;
        Obituary obituary;
        obituariesList = new ArrayList<>();
        ArrayList<String> tempNameArray = new ArrayList<>();
        ArrayList<String> tempObitPreviewTextArray = new ArrayList<>();
        ArrayList<String> tempObitFullTextArray = new ArrayList<>();

        try {
            if (urlString != null) {
                doc = Jsoup.connect(urlString).get();

                // Jsoup library handles validation/null checks of node values
                Elements obitNameElements = doc.getElementsByClass("obitName");
                Elements obitPreviewTextElements = doc.getElementsByClass("obitText");
                Elements obitFullTextElements = doc.select("a.RightLink[href]");

                Iterator<Element> namesIterator = obitNameElements.iterator();
                while (namesIterator.hasNext()) {
                    obitName = namesIterator.next().text();
                    tempNameArray.add(obitName);
                }

                Iterator<Element> obitPreviewTextIterator = obitPreviewTextElements.iterator();
                while (obitPreviewTextIterator.hasNext()) {
                    obitPreviewText = obitPreviewTextIterator.next().text();
                    tempObitPreviewTextArray.add(obitPreviewText);
                }

                Iterator<Element> obitFullTextIterator = obitFullTextElements.iterator();
                while (obitFullTextIterator.hasNext()) {
                    obitFullTextLink = obitFullTextIterator.next().attr("abs:href");
                    tempObitFullTextArray.add(obitFullTextLink);
                }

                for (int i = 0; i < obitNameElements.size(); i++) {
                    obituary = new Obituary(tempNameArray.get(i), tempObitPreviewTextArray.get(i),
                            tempObitFullTextArray.get(i));
                    obituariesList.add(obituary);
                    Timber.d("obituariesList: " + obituariesList);
                }
            } else {
                Timber.e("Error: query string is null");
            }
        } catch (IOException e) {
            Timber.wtf(e, "Error: IO Exception");
        }
        return obituariesList;

    }

    private ArrayList<Provider> extractProviderData(String jsonResponse) {
        /** fields needed for Provider:
         * 1. "LocationName"
         * 2. "LocationAddress1"
         * 3. "LocationAddress2"
         * 4. "LocationCity"
         * 5. "LocationState"
         * 6. "LocationPostalCode"
         * 7. "LocationURL"
         *
         */
        Timber.d("jsonResponse: " + jsonResponse);
        String locationName = "";
        String locationAddress1 = "";
        String locationAddress2 = "";
        String locationCity = "";
        String locationState = "";
        String locationPostalCode = "";
        String locationURL = "";
        String locationPhone = "";

        try {
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONArray baseJsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject descriptionObject = baseJsonArray.getJSONObject(i);
                JSONObject sciLocationObject = descriptionObject.getJSONObject("SCILocation");

                // validation/null checks
                if (sciLocationObject.has("LocationName") == false) {
                    Timber.i("LocationName is null");
                } else {
                    locationName = sciLocationObject.getString("LocationName");
                }

                if (sciLocationObject.has("LocationAddress1") == false) {
                    Timber.i("LocationAddress1 is null");
                } else {
                    locationAddress1 = sciLocationObject.getString("LocationAddress1");
                }

                if (sciLocationObject.has("LocationAddress2") == false) {
                    Timber.i("LocationAddress2 is null");
                } else {
                    locationAddress2 = sciLocationObject.getString("LocationAddress2");
                }

                if (sciLocationObject.has("LocationCity") == false) {
                    Timber.i("LocationCity is null");
                } else {
                    locationCity = sciLocationObject.getString("LocationCity");
                }

                if (sciLocationObject.has("LocationState") == false) {
                    Timber.i("LocationState is null");
                } else {
                    locationState = sciLocationObject.getString("LocationState");
                }

                if (sciLocationObject.has("LocationPostalCode") == false) {
                    Timber.i("LocationPostalCode is null");
                } else {
                    locationPostalCode = sciLocationObject.getString("LocationPostalCode");
                }

                if (sciLocationObject.has("LocationPhone") == false) {
                    Timber.i("LocationPhone is null");
                } else {
                    locationPhone = sciLocationObject.getString("LocationPhone");
                }

                if (sciLocationObject.has("LocationURL") == false) {
                    Timber.i("LocationURL is null");
                } else {
                    locationURL = sciLocationObject.getString("LocationURL");
                }

                providersList = new ArrayList<>();
                Provider currentProvider = new Provider(locationName, locationAddress1, locationAddress2,
                        locationCity + locationState + "," + locationPostalCode, locationPhone, locationURL);
                providersList.add(currentProvider);
            }

        } catch (JSONException e) {
            Timber.wtf(e, "extractProvider");
        }
        return providersList;
    }

}


