package com.android.melanieh.dignitymemorialandroid.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import com.android.melanieh.dignitymemorialandroid.widget.DMWidgetProvider;

import timber.log.Timber;

/**
 * Created by melanieh on 5/9/17.
 */

public class PlanSummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    String[] SELECTION_COLUMNS = {
            PlanEntry.COLUMN_ID,
            PlanEntry.COLUMN_PLAN_NAME,
        PlanEntry.COLUMN_CONTACT_EMAIL,
        PlanEntry.COLUMN_PROVIDER,
        PlanEntry.COLUMN_CEREMONY_SELECTION,
        PlanEntry.COLUMN_VISITATION_SELECTION,
        PlanEntry.COLUMN_RECEPTION_SELECTION,
        PlanEntry.COLUMN_SITE_SELECTION,
        PlanEntry.COLUMN_CONTAINER_SELECTION,
            PlanEntry.COLUMN_EST_COST};

    int[] BIND_VIEWS = {R.id.plan_name,
            R.id.contact_email,
            R.id.saved_provider,
            R.id.ceremony_selection,
            R.id.visitation_selection,
            R.id.reception_selection,
            R.id.site_selection,
            R.id.container_selection,
            R.id.est_cost};

    ListView savedPlansListView;
    SimpleCursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("fragment: onCreate");
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plan_summary, container, false);
        savedPlansListView = (ListView) rootView.findViewById(R.id.saved_plans_list_view);
        adapter = new SimpleCursorAdapter(getContext(),
                R.layout.saved_plan_list_item, null, SELECTION_COLUMNS, BIND_VIEWS,
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        savedPlansListView.setAdapter(adapter);

        getLoaderManager().initLoader(100, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader");
        return new CursorLoader(getActivity(), PlanEntry.CONTENT_URI, SELECTION_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d("onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
