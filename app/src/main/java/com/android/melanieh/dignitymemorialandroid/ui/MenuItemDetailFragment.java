package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;

import timber.log.Timber;

/**
 * A fragment representing a single MenuItem detail screen.
 * This fragment is either contained in a {@link MenuItemListActivity}
 * in two-pane mode (on tablets) or a {@link MenuItemDetailActivity}
 * on handsets.
 */
public class MenuItemDetailFragment extends Fragment implements MenuOptionsInterface {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    Activity activity;
    String id;
    String menuButtonExtra;
    View rootView;
    TextView textView;
    WebView webView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // types of fragments:
        // 1. search/find
        // 2. web browser
        // 3. custom planning fragment

        rootView = inflater.inflate(R.layout.fragment_menu_item_detail, container, false);
        webView = new WebView(getContext());
        getActivity().setContentView(webView);
        // inflate particular fragment based on value mapped by ARG_ITEM_ID
        id = getActivity().getIntent().getStringExtra(ARG_ITEM_ID);
        menuButtonExtra = getActivity().getIntent().getStringExtra("EXTRA_CONTENT");
        Timber.d("menuButtonExtra: " + menuButtonExtra);

        // Let's display the progress in the activity title bar, like the
        // browser app does.

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                getActivity().setProgress(progress * 1000);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), getString(R.string.webview_error_desc), Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(menuButtonExtra);
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(launchShareIntent());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void launchMenuIntent(Class activity, String extraContent) {

    }

    @Override
    public Intent launchShareIntent() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }
}
