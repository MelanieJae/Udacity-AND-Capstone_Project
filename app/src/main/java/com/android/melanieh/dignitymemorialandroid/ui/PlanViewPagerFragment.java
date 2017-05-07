package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanViewPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<PlanOption>> {

    CollapsingToolbarLayout toolbar;
    RecyclerView recyclerView;
    View rootView;
    ArrayList<PlanOption> optionsList;
    PlanOptionRecyclerViewAdapter rvAdapter;
    TextView estCostView;
    TextView planningStepTitleView;
    TextView emptyViewText;
    RecyclerView.LayoutManager layoutManager;
    Uri planUri;
    static TextView staticContentView;
    String STATIC_CONTENT;
    String planUriString;

    private int loaderId;

    /** projection for DB query to display plan selection summary */
    public static String[] SELECTION_COLUMNS = {
                                    PlanEntry.COLUMN_PLAN_NAME,
                                    PlanEntry.COLUMN_PLAN_TYPE,
                                    PlanEntry.COLUMN_CONTACT_EMAIL,
                                    PlanEntry.COLUMN_CEREMONY_SELECTION,
                                    PlanEntry.COLUMN_VISITATION_SELECTION,
                                    PlanEntry.COLUMN_RECEPTION_SELECTION,
                                    PlanEntry.COLUMN_SITE_SELECTION,
                                    PlanEntry.COLUMN_CONTAINER_SELECTION,
                                    PlanEntry.COLUMN_EST_COST
                                    };

    private static final int INDEX_PLAN_NAME = 1;
    private static final int INDEX_PLAN_TYPE = 2;
    private static final int INDEX_CONTACT_EMAIL = 3;
    private static final int INDEX_CEREMONY_SELECTION = 4;
    private static final int INDEX_VISITATION_SELECTION = 5;
    private static final int INDEX_RECEPTION_SELECTION = 6;
    private static final int INDEX_SITE_SELECTION = 7;
    private static final int INDEX_CONTAINER_SELECTION = 8;
    private static final int INDEX_EST_COST = 9;

    public PlanViewPagerFragment() {
        //
    }

    public static PlanViewPagerFragment newInstance(String content, int loaderId, String planUriString) {
        PlanViewPagerFragment fragment = new PlanViewPagerFragment();
        Bundle args = new Bundle();
        args.putString("static content", content);
        args.putInt("loaderId", loaderId);
        args.putString("planUriString", planUriString);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        STATIC_CONTENT = getArguments().getString("static content");
        loaderId = getArguments().getInt("loaderId");
        planUriString = getArguments().getString("planUriString");

    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView: ");

        rootView = inflater.inflate(R.layout.plan_viewpager_fragment, container, false);
        staticContentView = (TextView) rootView.findViewById(R.id.static_content);
        toolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
//        estCostView = (TextView) rootView.findViewById(R.id.toolbar_est_cost_tv);
        planningStepTitleView = (TextView) rootView.findViewById(R.id.toolbar_step_title);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.plan_option_rv);

//        // ensures the planning step title stays visible when the toolbar is collapsed
//
//        toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
//        toolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);

        // This AsyncTask is to be used for the final screen only to display the plan details
        // since two loaders returning different objects can't exist and db queries are very infrequent
        // (ideally only once or twice) an async task is used for the cursor creation
        // and a loader is used for the recycler view.

        if (loaderId > 50) {
            Timber.d("planUriString: " + planUriString);
            ReadCursorAsyncTask asyncTask = new ReadCursorAsyncTask(getContext());
            asyncTask.execute(Uri.parse(planUriString));
        } else {
            getActivity().getSupportLoaderManager().initLoader(loaderId, null, this).forceLoad();
        }

        return rootView;
    }

    @Override
    public Loader<ArrayList<PlanOption>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader:");
        return new PlanPageLoader(getContext(), STATIC_CONTENT);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PlanOption>> loader, ArrayList<PlanOption> data) {
        Timber.d("onLoadFinished:");
        Timber.d("data= " + data.toString());
        Timber.d("planUri= " + planUri);
        planningStepTitleView.setText(data.get(0).getTitle());

        if (data != null && !data.isEmpty()) {
            rvAdapter = new PlanOptionRecyclerViewAdapter(getContext(), data, planUriString);
            layoutManager = getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(rvAdapter);
        } else {
            updateEmptyView();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PlanOption>> loader) {

    }

    private void updateEmptyView() {
        if (!Utility.isNetworkAvailable(getContext())) {
            emptyViewText.setText(getString(R.string.emptyview_no_internet_connection));
        } else {
            emptyViewText.setText(getString(R.string.emptyview_no_results));
        }
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


    /**
     * Created by melanieh on 4/30/17
     * It is intended that the saved plan database only be read a few times at most,
     * ideally no more than once or twice to produce the plan summary info and then pass that
     * info along to the widget, notification and share functions
     */

    public static class ReadCursorAsyncTask extends AsyncTask<Uri, Void, Cursor> {

        Context context;
        public ReadCursorAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(Uri... params) {
            Cursor cursor =
                    context.getContentResolver().query(params[0],
                            PlanViewPagerFragment.SELECTION_COLUMNS, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            String planName = cursor.getString(PlanViewPagerFragment.INDEX_PLAN_NAME);
            String planType = cursor.getString(PlanViewPagerFragment.INDEX_PLAN_TYPE);
            String contactEmail = cursor.getString(PlanViewPagerFragment.INDEX_CONTACT_EMAIL);
            String ceremonySelection = cursor.getString(PlanViewPagerFragment.INDEX_CEREMONY_SELECTION);
            String visitationSelection = cursor.getString(PlanViewPagerFragment.INDEX_VISITATION_SELECTION);
            String receptionSelection = cursor.getString(PlanViewPagerFragment.INDEX_RECEPTION_SELECTION);
            String siteSelection = cursor.getString(PlanViewPagerFragment.INDEX_SITE_SELECTION);
            String containerSelection = cursor.getString(PlanViewPagerFragment.INDEX_CONTAINER_SELECTION);
            String estCostString = cursor.getString(PlanViewPagerFragment.INDEX_EST_COST);

            // append staticContent string with these values
            StringBuilder planSummaryContent = new StringBuilder();
            String planSummary = String.format(context.getString(R.string.plan_summary_intro), planName)
                    + String.format(context.getString(R.string.ceremony_selection), ceremonySelection)
                    + String.format(context.getString(R.string.visitation_selection), visitationSelection)
                    + String.format(context.getString(R.string.reception_selection), receptionSelection)
                    + String.format(context.getString(R.string.site_selection), siteSelection)
                    + String.format(context.getString(R.string.container_selection), containerSelection)
                    + String.format(context.getString(R.string.est_cost), Double.valueOf(estCostString));
            planSummaryContent.append(planSummary);
            staticContentView.setText(planSummaryContent);

        }
    }
}









