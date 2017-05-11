package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.melanieh.dignitymemorialandroid.FAQ;
import com.android.melanieh.dignitymemorialandroid.PlanOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

import timber.log.Timber;

/**
 * Created by melanieh on 5/9/17.
 */

public class FAQLoader extends AsyncTaskLoader {

    // sample query string returning HTML response to grab text items
    // https://www.thedignityplanner.com/my-plans/index

    private String contentString;
    ArrayList<FAQ> faqsList;

    public FAQLoader(Context context, String contentString) {
        super(context);
        Timber.d("FAQs loader constructor: ");
        this.contentString = contentString;
    }

    @Override
    public ArrayList<FAQ> loadInBackground() {
        Timber.v("loadInBackground: ");

        // Perform HTTP request to the URL and receive an HTML response back
        Timber.v("obtainHTML: contentString= " + contentString);
        faqsList = extractFAQsListFromHTML(contentString);

        // Extract relevant fields from the HTML response and create a {@link FAQ} list.
        return faqsList;
    }

    private ArrayList<FAQ> extractFAQsListFromHTML(String contentString) {
        Timber.d("extractFAQsListFromHTML: ");
        Timber.d("contentString:" + contentString);

        String optionTitle = "";
        String heading = "";
        String imageUrlString = "";
        String detailText = "";
        String estCostString = "";
        Document doc;
        PlanOption option;
        ArrayList<FAQ> faqsList = new ArrayList<>();
        ArrayList<String> tempQuestionArray = new ArrayList<>();
        ArrayList<String> tempAnswerArray = new ArrayList<>();

        if (contentString != null) {
            doc = Jsoup.parse(contentString);

            // Jsoup library handles validation/null checks of node values
            Elements optionTitleElements = doc.select("span[class=menu-text]");
            optionTitle = optionTitleElements.first().
                    text();

            Elements headingElements = doc.select("div[class=col-md-6]>h3");
            Iterator<Element> headingsIterator = headingElements.iterator();
            while (headingsIterator.hasNext()) {
                heading = headingsIterator.next().text();
//                tempHeadingArray.add(heading);
            }

            Elements imageUrlElements = doc.select("img[src]");
            Iterator<Element> imageStringIterator = imageUrlElements.iterator();
            while (imageStringIterator.hasNext()) {
                imageUrlString = imageStringIterator.next().attr("src");
//                tempImageUrlArray.add(imageUrlString);
            }

            for (int i = 0; i < headingElements.size(); i++) {
//                option = new PlanOption(optionTitle, tempHeadingArray.get(i), tempDetailArray.get(i),
//                        tempImageUrlArray.get(i), tempCostArray.get(i));
//                optionsList.add(option);
//                Timber.d("optionsList: " + optionsList);
            }
        } else {
            Timber.e("Error: query string is null");
        }
        return faqsList;
    }
}


