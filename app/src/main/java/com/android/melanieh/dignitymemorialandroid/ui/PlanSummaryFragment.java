package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanSummaryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    TextView planSummaryView;
    TextView estCostView;
    CursorAdapter adapter;

    String[] PLAN_COLUMNS = {
            UserSelectionContract.PlanEntry.COLUMN_ID,
            UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME,
            UserSelectionContract.PlanEntry.COLUMN_CEREMONY_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_VISITATION_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_RECEPTION_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_SITE_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_CONTAINER_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_EST_COST
    };

    private static final int INDEX_ID = 1;
    private static final int INDEX_PLAN_NAME = 2;
    private static final int INDEX_CEREMONY_SELECTION = 3;
    private static final int INDEX_VISITATION_SELECTION = 4;
    private static final int INDEX_RECEPTION_SELECTION = 5;
    private static final int INDEX_SITE_SELCTION = 6;
    private static final int INDEX_CONTAINER_SELECTION = 7;
    private static final int INDEX_EST_COST = 8;

    public PlanSummaryFragment() {
        //
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_search, container, false);
        planSummaryView = (TextView)rootView.findViewById(R.id.plan_summary_intro);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new
                CursorLoader(getContext(), UserSelectionContract.PlanEntry.CONTENT_URI,
                PLAN_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        while (data.moveToNext()) {
            String planName = data.getString(INDEX_PLAN_NAME);
            String ceremony = data.getString(INDEX_CEREMONY_SELECTION);
            String visitation = data.getString(INDEX_VISITATION_SELECTION);
            String reception = data.getString(INDEX_RECEPTION_SELECTION);
            String siteOrNiche = data.getString(INDEX_SITE_SELCTION);
            String container = data.getString(INDEX_CONTAINER_SELECTION);
            double estCost = data.getDouble(INDEX_EST_COST);

            StringBuilder builder = new StringBuilder();
            builder.append(String.format(getString(R.string.plan_summary_intro), planName));
            builder.append(getString(R.string.ceremony_selection_label)
                    + String.format(getString(R.string.ceremony_selection), ceremony));
            builder.append(getString(R.string.visitation_selection_label)
                    + String.format(getString(R.string.visitation_selection), visitation));
            builder.append(getString(R.string.reception_selection_label)
                    + String.format(getString(R.string.reception_selection), reception));
            builder.append(getString(R.string.site_cremation_label)
                    + String.format(getString(R.string.site_selection), siteOrNiche));
            builder.append(getString(R.string.container_selection_label)
                    + String.format(getString(R.string.ceremony_selection), container));
            builder.append(String.format(getString(R.string.est_cost), estCost));

            planSummaryView.setText(builder.toString());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        adapter.swapCursor(null);
    }

}
