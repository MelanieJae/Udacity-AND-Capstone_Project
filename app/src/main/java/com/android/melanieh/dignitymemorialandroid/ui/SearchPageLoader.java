package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.melanieh.dignitymemorialandroid.Obituary;
import com.android.melanieh.dignitymemorialandroid.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by melanieh on 4/16/17.
 */

public class SearchPageLoader extends AsyncTaskLoader {

    ArrayList<Obituary> obituariesList;
    ArrayList<Provider> providersList;
    String urlString;
    String kind;
    String totalItems;
    String itemsKind;
    String id;
    String eTag;
    String selfLink;
    CharSequence title;
    String author;
    String category;
    String authors;
    String categories;
    CharSequence desc;
    Double averageRating;
    ArrayList<CharSequence> categoriesList;
    ArrayList<CharSequence> authorsList;

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
    /** Query the Google Books dataset and return a list of {@link Object} search result objects.
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
////            Thread.sleep(2000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        Timber.v("obtainData: requestUrlString= " + requestUrlString);
        try {
            URL url = new URL(requestUrlString);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Timber.wtf(e, "Error closing input stream");
        }

        // Extract relevant fields from the JSON response and create an {@link Object} object
        // depending on the type of object for which the search is conducted

        if (requestUrlString.contains("SearchObituaries?")) {
            resultsList = extractObituaryDataFromJSON(jsonResponse);
        } else {
            resultsList = extractProviderDataFromJSON(jsonResponse);
        }

        // Return a list of {@link Object} events. The object will be either:
        // 1. an Obituary object or 2. a Provider object
        return resultsList;

    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
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

    private ArrayList<Obituary> extractObituaryDataFromJSON(String jsonResponse) {
        /** fields needed for obituary:
         * 1. "SortName"
         * 2. "DateOfDeath"
         * 3. "NoticeText"
         *
         */

        Timber.d("jsonResponse: " + jsonResponse);
        String personName = "";
        String dateOfDeath = "";
        String noticeText = "";

        try {
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray resultsJSONArray = baseJsonResponse.getJSONArray("Results");
            for (int i = 0; i < resultsJSONArray.length(); i++) {
                JSONObject sortNameObject = resultsJSONArray.getJSONObject(i);

                // validation/null checks
                if (sortNameObject.has("SortName") == false) {
                    Timber.i("SortName is null");
                } else {
                    personName = sortNameObject.getString("SortName");
                }

                if (sortNameObject.has("DateOfDeath") == false) {
                    Timber.i("DateOfDeath is null");
                } else {
                    dateOfDeath = sortNameObject.getString("DateOfDeath")
                            .substring(1);
                }

                if (sortNameObject.has("NoticeText") == false) {
                    Timber.i("DateOfDeath is null");
                } else {
                    noticeText = sortNameObject.getString("NoticeText");
                }

                Timber.i("extractData", "SortName= " + personName
                        + "; DateOfDeath= " + dateOfDeath
                        + "; NoticeText= " + noticeText);
                obituariesList = new ArrayList<>();
                Obituary currentObituary = new Obituary(personName, null, dateOfDeath, noticeText);
                obituariesList.add(currentObituary);
            }

        } catch (JSONException e) {
            Timber.wtf(e, "extractObit: ");
        }
        return obituariesList;
    }

    private ArrayList<Provider> extractProviderDataFromJSON(String jsonResponse) {
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


