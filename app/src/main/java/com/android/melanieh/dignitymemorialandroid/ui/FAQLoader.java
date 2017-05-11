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

        String question = "";
        String answer = "";
        Document doc;
        FAQ faq;
        ArrayList<FAQ> faqsList = new ArrayList<>();
        ArrayList<String> tempQuestionArray = new ArrayList<>();
        ArrayList<String> tempAnswerArray = new ArrayList<>();

        if (contentString != null) {
            doc = Jsoup.parse(contentString);

            Elements questionElements = doc.getElementsByClass("faq-header");
            Iterator<Element> questionIterator = questionElements.iterator();
            while (questionIterator.hasNext()) {
                question = questionIterator.next().text();
                tempQuestionArray.add(question);
                Timber.d("tempQuestionArray:" + tempQuestionArray);
            }

            Elements answerElements = doc.getElementsByClass("faq-body");
            Iterator<Element> answerIterator = answerElements.iterator();
            while (answerIterator.hasNext()) {
                answer = answerIterator.next().text();
                tempAnswerArray.add(answer);
                Timber.d("tempAnswerArray:" + tempAnswerArray);

            }

            for (int i = 0; i < tempQuestionArray.size(); i++) {
                faq = new FAQ(tempQuestionArray.get(i), tempAnswerArray.get(i));
                faqsList.add(faq);
                Timber.d("faqsList: " + faqsList);
            }
        } else {
            Timber.e("Error: query string is null");
        }
        return faqsList;
    }
}


