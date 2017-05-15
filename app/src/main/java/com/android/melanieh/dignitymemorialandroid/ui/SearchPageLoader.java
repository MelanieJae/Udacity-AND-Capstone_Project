package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

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
import java.net.MalformedURLException;
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

//        String htmlResponse = "";
        String jsonResponse = "";

//        if (isObitQuery) {
//            resultsList = extractObituaryData(jsonResponse);
//        } else {
            // initialize JSON Response and url variables
            URL url = createURL(requestUrlString);
            Timber.d("requestURLString= " + requestUrlString);
            // now that the id has been extracted the second query string (idQuery) to receive details,
            // video and review data can be constructed

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Timber.wtf(e, "");
            }
        if (isObitQuery) {
            resultsList = extractObituaryData(jsonResponse);
        } else {
            resultsList = extractProviderData(jsonResponse);
        }

        // Return a list of {@link Object} events. The object will be either:
        // 1. an Obituary object or 2. a Provider object
        return resultsList;

    }

    private URL createURL(String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Timber.wtf(e, "");
        }

        return url;
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

    private ArrayList<Obituary> extractObituaryData(String jsonResponse) {
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
        String obitText = "";
        String obitURL = "";
        String obitName = "";

        try {
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray resultsJsonArray = baseJsonObject.getJSONArray("Results");
            Timber.d("resultsJsonArray" + resultsJsonArray);
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject obitJsonObject = resultsJsonArray.getJSONObject(i);

                // validation/null checks
                if (obitJsonObject.has("NoticeText") == false) {
                    Timber.d("NoticeText is null");
                } else {
                    obitText = obitJsonObject.getString("NoticeText");
                }

                if (obitJsonObject.has("ObituaryLink") == false) {
                    Timber.d("ObituaryLink is null");
                } else {
                    obitURL = obitJsonObject.getString("ObituaryLink");
                }

                if (obitJsonObject.has("SortName") == false) {
                    Timber.d("SortName is null");
                } else {
                    obitName = obitJsonObject.getString("SortName");
                }

                obituariesList = new ArrayList<>();
                Obituary currentObituary = new Obituary(obitName, obitText, obitURL);
                obituariesList.add(currentObituary);
            }

        } catch (JSONException e) {
            Timber.wtf(e, "extractObituary");
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
        String locationAddress = "";
        String locationURL = "";
        String locationPhone = "";

        try {
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONArray baseJsonArray = new JSONArray(jsonResponse);
            Timber.d("baseJsonArray" + baseJsonArray);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject descriptionObject = baseJsonArray.getJSONObject(i);
                JSONObject sciLocationObject = descriptionObject.getJSONObject("SCILocation");

                // validation/null checks
                if (sciLocationObject.has("LocationName") == false) {
                    Timber.i("LocationName is null");
                } else {
                    locationName = sciLocationObject.getString("LocationName");
                }

                if (sciLocationObject.has("FormatAddress") == false) {
                    Timber.d("FormatAddress");
                } else {
                    locationAddress = sciLocationObject.getString("FormatAddress");
                }

                if (sciLocationObject.has("LocationPhone") == false) {
                    Timber.d("LocationPhone is null");
                } else {
                    locationPhone = sciLocationObject.getString("LocationPhone");
                }

                if (sciLocationObject.has("LocationURL") == false) {
                    Timber.d("LocationURL is null");
                } else {
                    locationURL = sciLocationObject.getString("LocationURL");
                }

                providersList = new ArrayList<>();
                Provider currentProvider = new Provider(locationName, locationAddress,
                        locationPhone, locationURL);
                providersList.add(currentProvider);
            }

        } catch (JSONException e) {
            Timber.wtf(e, "extractProvider");
        }
        return providersList;
    }

}


