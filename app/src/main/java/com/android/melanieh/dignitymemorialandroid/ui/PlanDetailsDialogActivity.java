package com.android.melanieh.dignitymemorialandroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;

import java.util.ArrayList;

/**
 * Created by melanieh on 4/12/17.
 */

public class PlanDetailsDialogActivity extends AppCompatActivity {
    // Note: `FragmentActivity` works here as well
    public static String dialogImageUrl;
    public static String dialogText;
    public int optionId;
    PlanOption option;
    ImageView sharedElementImageView;

    public static String EXTRA_KEY = "Shared Element Object ID";
    public static String SHARED_IMAGE_VIEW_NAME = "Shared Element ImageView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details_dialog);
        optionId = getIntent().getIntExtra(EXTRA_KEY, 0);
        showDetailDialog();

        // Retrieve the correct PlanOption instance, using the ID/position provided in the Intent
        option = PlanPageFragment.options.get(optionId);
        // elements being shared between the plan options list and here
        sharedElementImageView = (ImageView) findViewById(R.id.shared_element_transition_image);

//    // BEGIN_INCLUDE(detail_set_view_name)
//    /**
//     * Set the name of the view's which will be transition to, using the static values above.
//     * This could be done in the layout XML, but exposing it via static variables allows easy
//     * querying from other Activities
//     */
        ViewCompat.setTransitionName(sharedElementImageView, SHARED_IMAGE_VIEW_NAME);
//    // END_INCLUDE(detail_set_view_name)

    }

        private void showDetailDialog() {
            FragmentManager fm = getSupportFragmentManager();
            PlanDetailsDialogFragment fragment = PlanDetailsDialogFragment.newInstance("title");
            fragment.show(fm, "fragment_plan_detail_dialog");
        }


}
