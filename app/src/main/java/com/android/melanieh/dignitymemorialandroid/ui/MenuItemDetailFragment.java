package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import timber.log.Timber;

/**
 * A fragment representing a single MenuItem detail screen.
 * This fragment is either contained in a {@link MenuItemListActivity}
 * in two-pane mode (on tablets) or a {@link MenuItemDetailActivity}
 * on handsets.
 */
public class MenuItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    Activity activity;
    String id;

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
        // inflate particular fragment based on value mapped by ARG_ITEM_ID
        id = getActivity().getIntent().getStringExtra(ARG_ITEM_ID);
        Bundle bundle = this.getArguments();
        id = (String)bundle.get(ARG_ITEM_ID);
        mItem = MenuContent.ITEM_MAP.get(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // types of fragments:
        // 1. search/find
        // 2. web browser
        // 3. custom planning fragment
//        if (mItem.details.contains("http")) {
//            layoutId = R.layout.fragment_detail_search;
//        } else if (mItem.details.contains("search") || mItem.details.contains("find")) {
//            layoutId = R.layout.fragment_detail_search;
//        } else {
//            layoutId = R.layout.fragment_plan_selections;
//        }
        View rootView = inflater.inflate(R.layout.fragment_detail_search, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.textView);
        textView.setText(mItem.details);
        textView.setTextSize(32f);
        textView.setContentDescription(mItem.details);
        return rootView;
    }

}
