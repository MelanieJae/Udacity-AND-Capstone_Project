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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanPageFragment extends Fragment {

    static List<PlanOption> options = new ArrayList<>();
    android.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    View rootView;

    public PlanPageFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.plan_viewpager_item, container, false);
        toolbar = (android.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.plan_option_rv);
        options.add(new PlanOption("Hotels", "hotels"));
        options.add(new PlanOption("Ceremony", "ceremony"));
        setupRecyclerView(recyclerView);
        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        PlanOptionRecyclerViewAdapter rvAdapter = new PlanOptionRecyclerViewAdapter(options);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
    }

    public class PlanOptionRecyclerViewAdapter
            extends RecyclerView.Adapter<PlanPageFragment.PlanOptionRecyclerViewAdapter.ViewHolder> {

        List<PlanOption> options;
        ImageView itemImage;
        ImageLoader.ImageListener imageListener;
        Bitmap bitmap;
        public String SHARED_IMAGE_VIEW_NAME = "Shared Element ImageView";

        public PlanOptionRecyclerViewAdapter(List<PlanOption> options) {
            this.options = options;
        }

        @Override
        public PlanOptionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.plan_options_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PlanOptionRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.option = options.get(position);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()) {
                        case R.id.add_button:
                            addOptionToPlan();
                            break;
                        case R.id.details_button:
//                            ArrayList<String> dialogStringExtras = new ArrayList<>();
//                            dialogStringExtras.add(imageUrlString);
//                            dialogStringExtras.add(detailTextString);


//    // BEGIN_INCLUDE(detail_set_view_name)
//    /**
//     * Set the name of the view's which will be transition to, using the static values above.
//     * This could be done in the layout XML, but exposing it via static variables allows easy
//     * querying from other Activities
//     */
                            ViewCompat.setTransitionName(itemImage, SHARED_IMAGE_VIEW_NAME);
//    // END_INCLUDE(detail_set_view_name)


                            // Construct an Intent as normal
                            Intent intent = new Intent(getContext(), PlanViewPagerActivity.class);
                            intent.putExtra
                                    (PlanDetailsDialogActivity.EXTRA_KEY, position);
                            startActivity(intent);
                            // BEGIN_INCLUDE(start_activity)
                            /**
                             * Now create an {@link android.app.ActivityOptions} instance using the
                             * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
                             * method.
                             */
                            ActivityOptionsCompat activityOptions
                                    = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    getActivity(),
                                    new Pair<View, String>(
                                            itemImage,
                                            PlanDetailsDialogActivity.SHARED_IMAGE_VIEW_NAME));

                                    // Now we can start the Activity, providing the activity options as a bundle
                                    ActivityCompat.startActivity(getContext(), intent, activityOptions.toBundle());
//                             END_INCLUDE(start_activity)

                    }
                }
            };
            holder.heading.setText(holder.option.getHeading());
            holder.addBtnView.setOnClickListener(clickListener);
            holder.detailsBtnView.setOnClickListener(clickListener);
            ImageHandler.getSharedInstance(getContext()).load(BuildConfig.APP_BAR_LOGO_URL)
                    .fit().centerCrop().into(itemImage);

            holder.addBtnView.setContentDescription(String.format(getString(R.string.add_button_cd)
                    , options.get(position).getHeading()));
            holder.detailsBtnView.setContentDescription
                    (String.format(getString(R.string.details_button_cd)
                    , options.get(position).getHeading()));
        }

        @Override
        public int getItemCount() {
            return options.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final ImageButton addBtnView;
            public final Button detailsBtnView;
            public final TextView heading;

            public PlanOption option;

            public ViewHolder(View view) {
                super(view);
                heading = (TextView)view.findViewById(R.id.heading);
                itemImage = (ImageView)view.findViewById(R.id.imageview);
                addBtnView = (ImageButton) view.findViewById(R.id.add_button);
                detailsBtnView = (Button) view.findViewById(R.id.details_button);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + heading.getText() + "'";
            }
        }
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

    private void addOptionToPlan() {
        ContentValues values = new ContentValues();
        // query first for existing plan URI, then insert new plan entry if not present
//        if (planUri != null) {
//            values.put(PlanEntry.COLUMN_ ? ??,heading);
//            getContext().getContentResolver().update(planUri, values, null, null);
//        } else {
//            values.put(UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME, planName);
//            // etc...
//            Uri planUri = getContext().getContentResolver().insert(UserSelectionContract.PlanEntry.CONTENT_URI, values);
//        }

    }

}
