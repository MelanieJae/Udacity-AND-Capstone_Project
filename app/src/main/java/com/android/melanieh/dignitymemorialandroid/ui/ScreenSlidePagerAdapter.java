package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.android.melanieh.dignitymemorialandroid.R;

import timber.log.Timber;

/*** Created by melanieh on 4/17/17 for the PlanViewPager Activity */

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

    Context context;
    String planUriString;
    private static final int NUM_PAGES = 6;

    public ScreenSlidePagerAdapter(Context context, FragmentManager fm, String planUriString) {
        super(fm);
        Timber.d("ScreenSlidePagerAdapter constructor:");
        this.context = context;
        this.planUriString = planUriString;
    }

    // ... and any specialty sub-item fragments, e.g. for catering
    // for the reception
    // PlanPage fragment cateringFragment;

    @Override
    public int getCount() {
        Timber.d("getPagerAdapterCount:");
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        Timber.d("getItem:");
        PlanViewPagerFragment fragment;
        // these are left blank here and appended in the fragments after actual selections are made
        // and fetched from the DB.
        String staticContent = "";
        String planName="";
        String ceremonySelection="";
        String visitationSelection="";
        String receptionSelection="";
        String siteSelection="";
        String containerSelection="";
        String estCostString="";
//
//        // each fragment needs its own loaderId to ensure content appears in the correct fragment
//        // since the PlanPage fragment is being recycled for all elements of the viewpager
        int loaderId = 0;
        switch (position) {
            case 0:
                staticContent = context.getString(R.string.ceremonyHtmlString);
                loaderId = 10;
                break;
            case 1:
                staticContent = context.getString(R.string.visitationHtmlString);
                loaderId = 20;
                break;
            case 2:
                staticContent = context.getString(R.string.receptionHtmlString);
                loaderId = 30;
                break;
            case 3:
                staticContent = context.getString(R.string.siteHtmlString);
                loaderId = 40;
                break;
            case 4:
                staticContent = context.getString(R.string.containerHtmlString);
                loaderId = 50;
                break;
            case 5:
                // left blank and appended after all selections are made and plan summary is to be displayed;
                staticContent = "";
                loaderId = 60;
                break;

        }
        fragment = PlanViewPagerFragment.newInstance(staticContent, loaderId, planUriString);
        return fragment;
    }

}

