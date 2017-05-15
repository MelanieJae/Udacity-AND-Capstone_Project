package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.FAQ;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.menucontent.MenuContent;

import java.util.ArrayList;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * A fragment representing a single MenuItem detail screen.
 * This fragment is either contained in a {@link MenuItemListActivity}
 * in two-pane mode (on tablets) or a {@link MenuItemDetailActivity}
 * on handsets.
 */
public class MenuItemDetailFragment extends Fragment
        implements MenuOptionsInterface, LoaderManager.LoaderCallbacks<ArrayList<FAQ>>{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    String id;
    View rootView;

    WebView webView;
    String detailContent;
    RecyclerView faqsRV;
    LinearLayoutManager layoutManager;
    private static final int FAQ_LOADER_ID = 100;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        detailContent = getActivity().getIntent().getStringExtra("button_extra_content");

//        if (savedInstanceState != null) {
//            detailContent = savedInstanceState.getString("detailContent");
//        } else {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // types of fragments handled by this class:

        // 1. web browser
        // 2. FAQ page
//        detailContent = getArguments().getString("menuButtonExtra");

        Timber.d("detailContent: " + detailContent);
        // picks up content linked to menu button that was pressed and detects whether it is
        // supposed to be a browser fragment or the FAQ pg.
        boolean isWebContent = Pattern.compile(Pattern.quote("http"),
                Pattern.CASE_INSENSITIVE).matcher(detailContent).find();

        if (isWebContent) {
            rootView = inflater.inflate(R.layout.fragment_browser, container, false);
            webView = (WebView) rootView.findViewById(R.id.webview);
            webView = new WebView(getActivity());
            webView.getSettings().setJavaScriptEnabled(true);
//            webView.setInitialScale(1);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    getActivity().setProgress(progress * 1000);
                }
            });

            webView.loadUrl(detailContent);
            getActivity().setContentView(webView);

        } else {
            rootView = inflater.inflate(R.layout.fragment_faqs_list, container, false);
            faqsRV = (RecyclerView) rootView.findViewById(R.id.faqs_rv);
            getLoaderManager().initLoader(FAQ_LOADER_ID, null, this).forceLoad();
        }

        return rootView;

    }

    @Override
    public Loader<ArrayList<FAQ>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader");
        return new FAQLoader(getContext(), getString(R.string.FAQsHtmlString));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FAQ>> loader, ArrayList<FAQ> data) {
        Timber.d("onLoadFinished");
        if (data != null && !data.isEmpty()) {
            Timber.d("data:" + data);
            FAQRecyclerViewAdapter faqAdapter = new FAQRecyclerViewAdapter(data);
            layoutManager = new LinearLayoutManager(getContext());
            faqsRV.setLayoutManager(layoutManager);
            faqsRV.setAdapter(faqAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FAQ>> loader) {
        Timber.d("onLoaderReset");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("detailContentType", detailContent);
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Timber.d("launchMenuIntent:");
        Timber.d("destinationClass=" + destinationClass.toString());
        Timber.d("intentExtraContent: " + extraContent);
        Intent intent = new Intent(getContext(), destinationClass);
        intent.putExtra("button_extra_content", extraContent);
        startActivity(intent);
    }

    public Intent launchShareIntent() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }

    /**
     * FAQ recyclerview adapter
     **/
    public class FAQRecyclerViewAdapter
            extends RecyclerView.Adapter<FAQRecyclerViewAdapter.FAQViewHolder> {

        private ArrayList<FAQ> mFAQSList = new ArrayList<>();



        public FAQRecyclerViewAdapter(ArrayList<FAQ> faqsList) {
            mFAQSList = faqsList;
            Timber.d("mFAQSList:" + mFAQSList);
        }

        @Override
        public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Timber.d("onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.faqs_list_item, parent, false);
            FAQViewHolder viewHolder =
                    new FAQViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FAQViewHolder holder, int position) {
            Timber.d("onBindViewHolder");

            holder.faqItem = mFAQSList.get(position);

            // destination class for intent; varies according to which button is selected
            final String question = holder.faqItem.getQuestion();
            final String answer = holder.faqItem.getAnswer();
//            holder.questionView.setContentDescription(question);
//            holder.answerView.setContentDescription(answer);
            holder.questionView.setText(question);
            holder.answerView.setText(answer);

        }

        @Override
        public int getItemCount() {
            Timber.d("getItemCount");
            return mFAQSList.size();
        }

        public class FAQViewHolder extends RecyclerView.ViewHolder {
            TextView questionView;
            TextView answerView;
            FAQ faqItem;

            public FAQViewHolder(View view) {
                super(view);
                questionView = (TextView) view.findViewById(R.id.question);
                answerView = (TextView) view.findViewById(R.id.answer);
            }

        }
    }
}



