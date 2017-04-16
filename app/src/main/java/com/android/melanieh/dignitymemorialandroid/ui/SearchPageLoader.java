package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.android.melanieh.dignitymemorialandroid.Obituary;

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

    ArrayList<Object> searchResultsList;
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
    public ArrayList<Object> loadInBackground() {
        Timber.v("loadInBackground: ");

        // Perform the HTTP request for book listings data and process the response.
        searchResultsList = obtainSearchResults(urlString);
        return searchResultsList;
    }
    /** Query the Google Books dataset and return a list of {@link Object} search result objects.
     * The Object can be one of the following:
     * 1. Obituary
     * 2. Provider
     * */

    public ArrayList<Object> obtainSearchResults(String requestUrlString) {
        Timber.v("obtainSearchResults: ");
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
        Timber.v("obtainBookData: requestUrlString= " + requestUrlString);
        try {
            URL url = new URL(requestUrlString);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Timber.e("Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Object} object
        // depending on the type of object for which the search is conducted

        searchResultsList = extractDataFromJSONResponse(jsonResponse);

        // Return a list of {@link Object} events. The object will be either:
        // 1. an Obituary object or 2. a Provider object
        return searchResultsList;

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
            Timber.e("Problem retrieving the search JSON results.", e);
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

    private ArrayList<Object> extractDataFromJSONResponse(String jSONResponse) {
        Log.d("log", "jsonResponse= " + jSONResponse);
//        authorsList = new ArrayList<>();
//        categoriesList = new ArrayList<>();
//        ArrayList<BookListing> bookListings = new ArrayList<>();
//
//        try {
//            kind = "";
//            totalItems = "";
//
//            ObjectMapper rootMapper = new ObjectMapper();
//
//            JsonNode root = rootMapper.readTree(jSONResponse);
//            kind = root.path("kind").asText();
//            totalItems = root.path("totalItems").asText();
//            JsonNode itemsNode = root.path("items");
//
//            for (JsonNode node : itemsNode) {
//
//                itemsKind = node.path("kind").asText();
//                id = node.path("id").asText();
//                eTag = node.path("etag").asText();
//                selfLink = node.path("selfLink").asText();
//                JsonNode volumeInfoNode = node.path("volumeInfo");
//
//                title = volumeInfoNode.path("title").asText();
//                Iterator<JsonNode> authorNodeIterator
//                        = node.findValue("authors").elements();
//
//                authors = authorNodeIterator.next().toString();
////                authorsList.add(authors);
//
//                Iterator<JsonNode> categoriesNodeIterator
//                        = volumeInfoNode.path("categories").elements();
//
//                while (categoriesNodeIterator.hasNext()) {
//                    categories = categoriesNodeIterator.next().toString();
//                    categoriesList.add(categories);
//                }
//
//                desc = volumeInfoNode.path("description").asText();
//                averageRating = volumeInfoNode.path("averageRating").doubleValue();
//                ;               BookListing currentListing = new BookListing(averageRating,
//                        categoriesList, title, authorsList, desc);
//                bookListings.add(currentListing);
//            }
//
//        } catch (JsonGenerationException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bookListings;
//    }
        try {
            Double rating = 0.0;
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONObject baseJsonResponse = new JSONObject(jSONResponse);
//            JSONArray itemsJSONArray = baseJsonResponse.getJSONArray("items");
//            for (int itemIndex = 0; itemIndex < itemsJSONArray.length(); itemIndex++) {
//                ArrayList<CharSequence> categoryArray = new ArrayList<CharSequence>();
//                ArrayList<CharSequence> authorArray = new ArrayList<CharSequence>();
//                JSONObject itemsObject = itemsJSONArray.getJSONObject(itemIndex);
//                JSONObject volumeInfoValue = itemsObject.getJSONObject("volumeInfo");
//
//                JSONArray categoriesJSONArray = volumeInfoValue.getJSONArray("categories");
//                JSONArray authorsJSONArray = volumeInfoValue.getJSONArray("authors");
//
//                // check for empty authors array
//                for (int authIndex = 0; authIndex < authorsJSONArray.length(); authIndex++) {
//                    if (authorsJSONArray.length() == 0) {
//                        Log.i("extractFeature", "No author information is available");
//                    } else {
//                        // Extract the formatted contents from the objects in the authors array
//                        author = authorsJSONArray.getString(authIndex);
//                        Log.i("extractFeature", "authorArray: " + authorArray);
//                        authorArray.add(author);
//                    }
//                }

                // validation/null checks
//                if (volumeInfoValue.has("averageRating") == false) {
//                    Log.i(LOG_TAG, "average rating is null");
//                } else {
//                    rating = volumeInfoValue.getDouble("averageRating");
//                }
//
//                // check for empty categories array
//                if (categoriesJSONArray.length() == 0) {
//                    Log.i("extractFeature", "No category information available");
//                } else {
//                    for (int catIndex = 0; catIndex < categoriesJSONArray.length(); catIndex++) {
//                        // Extract the formatted contents from the objects in the categories array
//                        category = categoriesJSONArray.getString(catIndex);
//                        Log.i("extractFeature", "categoryArray: " + categoryArray);
//                        categoryArray.add(category);
//                    }
//                }
//
//                bookTitle = volumeInfoValue.getString("title");
//                bookDesc = volumeInfoValue.getString("description");
//                Log.i("extractFeature", "ItemIndex= " + itemIndex +
//                        "; categoriesArray= {" + categoryArray + "}"
//                        + "; rating= " + rating
//                        + "; title= " + bookTitle
//                        + "; authorsArray= {" + authorArray + "}"
//                        + "; bookDesc= " + bookDesc);

                Obituary currentObituary = new Obituary(jSONResponse, null, null, null);
                searchResultsList.add(currentObituary);

        } catch (JSONException e) {
            Log.e("extractFeature", "" + e);
        }
        return searchResultsList;
    }


}
