package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;

import java.util.ArrayList;

import timber.log.Timber;

/*** Created by melanieh on 4/17/17 for the PlanViewPager Activity */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    // list of valid fragments
    PlanPageFragment ceremonyFragment;
    PlanPageFragment visitationFragment;
    PlanPageFragment receptionFragment;
    PlanPageFragment siteFragment;
    PlanPageFragment containerFragment;
    FragmentTransaction fragmentTransaction;
    private static final int NUM_PAGES = 7;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        Timber.d("ScreenSlidePagerAdapter:");
        fragmentTransaction = fm.beginTransaction();
    }

    // ... and any specialty sub-item fragments, e.g. for catering
    // for the reception
    // PlanPage fragment cateringFragment;

    public void setFragmentTags(Context c) {
        Timber.d("setFragmentTags:");

        // Set up the simple base fragments
        ceremonyFragment = new PlanPageFragment();
        visitationFragment = new PlanPageFragment();
        receptionFragment = new PlanPageFragment();
        siteFragment = new PlanPageFragment();
        containerFragment = new PlanPageFragment();

        Resources res = c.getResources();

//        ceremonyFragment.loadOptionsListItems(APIlistCeremony);
//        visitationFragment.loadOptionsListItems(APIlistVisitation);
//        receptionFragment.loadOptionsListItems(APIListReception);
//        siteFragment.loadOptionsListItems(APIListSiteNiche);
//        containerFragment.loadOptionsListItems(APIListContainer);

    }

    @Override
    public int getCount() {
        Timber.d("getPagerAdapterCount:");
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        Timber.d("getItem:");

        String fragmentTag = "";
        Fragment planPageFragment = new PlanPageFragment();
        switch (position) {
            case 0:
                fragmentTag = "ceremony";
                break;
            case 1:
                fragmentTag = "visitation";
                break;
            case 2:
                fragmentTag = "reception";
                break;
            case 3:
                fragmentTag = "site or niche";
                break;
            case 4:
                fragmentTag = "container";
                break;
        }
//        planOptionsList = loadOptionsListItems(fragmentTag);
        return new PlanPageFragment();
//        return fragmentTransaction.add(planPageFragment, fragmentTag).commit();

    }
}

