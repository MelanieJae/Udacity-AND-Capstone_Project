package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.FAQ;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * A fragment representing a single MenuItem detail screen.
 * This fragment is either contained in a {@link MenuItemListActivity}
 * in two-pane mode (on tablets) or a {@link MenuItemDetailActivity}
 * on handsets.
 */
public class MenuItemDetailFragment extends Fragment {
//        implements MenuOptionsInterface, LoaderManager.LoaderCallbacks<ArrayList<FAQ>>{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    String id;
    View rootView;

    WebView webView;
    String detailContent;
    RecyclerView faqsRV;
    RecyclerView.LayoutManager layoutManager;

    /**
     * The dummy content this fragment is presenting.
     */
    private MenuContent.MenuItem mItem;

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

        boolean isWebContent = Pattern.compile(Pattern.quote("http"),
                Pattern.CASE_INSENSITIVE).matcher(detailContent).find();

        if (isWebContent) {
            rootView = inflater.inflate(R.layout.fragment_browser, container, false);
            webView = (WebView) rootView.findViewById(R.id.webview);
            webView = new WebView(getContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(1);
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


        } else {
            rootView = inflater.inflate(R.layout.fragment_faqs_list, container, false);
//            getLoaderManager().initLoader(200, null, this).forceLoad();
        }

        return rootView;

    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp > 600) {
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
            return glm;
        } else {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            return lm;
        }
    }
//
//    @Override
//    public Loader<ArrayList<FAQ>> onCreateLoader(int id, Bundle args) {
//        return new FAQLoader(getContext(), detailContent);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<FAQ>> loader, ArrayList<FAQ> data) {
//        if (data != null && !data.isEmpty()) {
//            SimpleItemRecyclerViewAdapter faqAdapter = new SimpleItemRecyclerViewAdapter(data);
//            layoutManager = getLayoutManager();
//            faqsRV.setLayoutManager(layoutManager);
//            faqsRV.setAdapter(faqAdapter);
//        }
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<ArrayList<FAQ>> loader) {
//
//    }

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

    public Intent launchShareIntent() {Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }

//    /** FAQ recyclerview adapter **/
//    public class SimpleItemRecyclerViewAdapter
//            extends RecyclerView.Adapter<MenuItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private ArrayList<FAQ> mFAQSList = new ArrayList<>();
//
//
//        public SimpleItemRecyclerViewAdapter(ArrayList<FAQ> faqsList) {
//            mFAQSList = faqsList;
//        }
//
//        @Override
////        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            Timber.d("onCreateViewHolder");
////            View view = LayoutInflater.from(parent.getContext())
////                    .inflate(R.layout.menuitem_list_content, parent, false);
////            ViewHolder viewHolder =
////                    new SimpleItemRecyclerViewAdapter.ViewHolder(view);
////            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(final MenuItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder
//                                                     holder, int position) {
//            Timber.d("onBindViewHolder");
//
////            holder.faqItem = mFAQSList.get(position);
////
////            // destination class for intent; varies according to which button is selected
////            final String buttonLabel = holder.mItem.content;
////            holder.questionView.setContentDescription(buttonLabel);
////            holder.answerView.setContentDescription(buttonLabel);
////            holder.questionView.setText(mFAQSList.get(position).getQuestion());
////            holder.answerView.setText(mFAQSList.get(position).getAnswer());
//
//        }
//
//        @Override
//        public int getItemCount() {
//            Timber.d("getItemCount");
//            return mFAQSList.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            TextView questionView;
//            TextView answerView;
//
//            public ViewHolder(View view) {
//                super(view);
//                questionView = (TextView) view.findViewById(R.id.question);
//                answerView = (TextView) view.findViewById(R.id.answer);
//            }
//
//        }
    }



