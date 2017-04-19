package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.data.*;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionDBHelper;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanPageFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<PlanOption>>{

    android.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    View rootView;
    ArrayList<PlanOption> phOptionsList;

    public PlanPageFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
        phOptionsList = new ArrayList<>();
        phOptionsList.add(new PlanOption("Hotels", "hotels"));
        phOptionsList.add(new PlanOption("Ceremony", "ceremony"));
        rootView = inflater.inflate(R.layout.plan_viewpager_item, container, false);
        toolbar = (android.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.plan_option_rv);
        PlanOptionRecyclerViewAdapter rvAdapter = new PlanOptionRecyclerViewAdapter(getContext(),
                phOptionsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rvAdapter);
        TextView estCostTV = (TextView)rootView.findViewById(R.id.toolbar_est_cost_tv);
//        getActivity().getSupportLoaderManager().initLoader(3, null, this).forceLoad();
        return rootView;
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp < 900) {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            return lm;
        } else {
            RecyclerView.LayoutManager glm = new GridLayoutManager(getContext(), 2);
            return glm;
        }
    }

    // query first for existing plan URI, then insert new plan entry if not present
//        if (planUri != null) {
//            values.put(PlanEntry.COLUMN_ ? ??,heading);
//            getContext().getContentResolver().update(planUri, values, null, null);
//        } else {
//            values.put(UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME, planName);
//            // etc...
//            Uri planUri = getContext().getContentResolver().insert(UserSelectionContract.PlanEntry.CONTENT_URI, values);
//        }

    private ArrayList<Integer> loadFragmentResources(String fragmentTag) {
        ArrayList<Integer> resourcesList = new ArrayList<>();


        return resourcesList;
    }

    private void addOptionToPlan(String selection) {
        ContentValues values = new ContentValues();
        switch (selection) {
//            case ceremony:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_CEREMONY_SELECTION, selection);
//                break;
//            case reception:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_RECEPTION_SELECTION, selection);
//                break;
//            case visitation:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_VISITATION_SELECTION, selection);
//                break;
//            case siteFragment:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_SITE_SELECTION, selection);
//                break;
//            case container:
//                values.put(UserSelectionContract.PlanEntry.COLUMN_CONTAINER_SELECTION, selection);
//                break;
            default:
                Timber.e("Invalid Fragment tag");
        }
    }

    /** plan option loader */
    @Override
    public Loader<ArrayList<PlanOption>> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader:");
        String queryString = "https://www.thedignityplanner.com/create-plan/funeralceremony";
        return new PlanPageLoader(getContext(), queryString);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PlanOption>> loader, ArrayList<PlanOption> data) {
        Timber.d("onLoadFinished:");
        Timber.d("data=" + data.get(0).getHeading());
//        PlanOptionRecyclerViewAdapter rvAdapter = new PlanOptionRecyclerViewAdapter(getActivity(), );
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PlanOption>> loader) {

    }

}


