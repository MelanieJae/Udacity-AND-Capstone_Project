package com.android.melanieh.dignitymemorialandroid.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;

/**
 * Controller for the create-a-plan screens organized via a viewpager
 */

public class PlanViewPagerActivity extends FragmentActivity {

    private static final int NUM_PAGES = 7;
    private ViewPager mPager;
    private boolean mTwoPane;
    public int optionId;
    PlanOption option;
    ImageView itemImage;
    public static String SHARED_IMAGE_VIEW_NAME = "Shared Element ImageView";


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_viewpager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        itemImage = (ImageView)findViewById(R.id.imageview);

        if (findViewById(R.id.menuitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // shared element transition to option details dialog
//        ViewCompat.setTransitionName(itemImage, SHARED_IMAGE_VIEW_NAME);

        optionId = getIntent().getIntExtra(PlanDetailsDialogActivity.EXTRA_KEY, 0);
        showDetailDialog();

        // Retrieve the correct PlanOption instance, using the ID/position provided in the Intent
//        option = PlanPageFragment.options.get(optionId);
        // elements being shared between the plan options list and here

//    // BEGIN_INCLUDE(detail_set_view_name)
//    /**
//     * Set the name of the view's which will be transition to, using the static values above.
//     * This could be done in the layout XML, but exposing it via static variables allows easy
//     * querying from other Activities
//     */
//    // END_INCLUDE(detail_set_view_name)

    }

    private void showDetailDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PlanDetailsDialogFragment fragment = PlanDetailsDialogFragment.newInstance("title");
        fragment.show(fm, "fragment_plan_detail_dialog");
    }

    // elements being shared between the plan options list and here
//        sharedElementImageView = (ImageView) findViewById(R.id.shared_element_transition_image);

//    // BEGIN_INCLUDE(detail_set_view_name)
//    /**
//     * Set the name of the view's which will be transition to, using the static values above.
//     * This could be done in the layout XML, but exposing it via static variables allows easy
//     * querying from other Activities
//     */
//        ViewCompat.setTransitionName(sharedElementImageView, SHARED_IMAGE_VIEW_NAME);
//    // END_INCLUDE(detail_set_view_name)

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PlanPageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}

