package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/19/17.
 */

public class PlanOptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PlanOptionRecyclerViewAdapter.OptionViewHolder>
        implements View.OnClickListener {

    ArrayList<PlanOption> options;
    ImageView itemImage;
    Bitmap bitmap;
    Context context;
    String detailText;
    String sampleImageURL;
    public String SHARED_IMAGE_VIEW_NAME = "Shared Element ImageView";
    public static String DETAIL_TEXT_ARG_KEY = "Option item detail text";
    public static String IMAGE_STRING_ARG_KEY = "ImageURL string";

    public PlanOptionRecyclerViewAdapter(Context context, ArrayList<PlanOption> options) {
        Timber.d("recyclerview adapter constructor: ");
        this.context = context;
        this.options = options;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.v("onCreateViewHolder: ");

        View view = LayoutInflater.from(context)
                .inflate(R.layout.plan_options_list_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (options != null) {
            return options.size();
        } else {
            return 0;
        }
    }


    @Override
    public void onBindViewHolder(final PlanOptionRecyclerViewAdapter.OptionViewHolder holder,
                                 final int position) {
        Timber.v("onBindViewHolder: ");
        PlanOption currentOption = options.get(position);
        // temporarily capture detail text to pass as dialog fragment argument
        // it is not displayed in the cardview, only in the dialog fragment
        detailText = currentOption.getDetailText();
        holder.heading.setText(currentOption.getHeading());
//        holder.addBtnView.setOnClickListener(clickListener);
        holder.detailsBtnView.setOnClickListener(this);
        sampleImageURL = "https://s3.amazonaws.com/busites_www/tdp/1/1/media/option-media" +
                "/thumb_cooking03_1457631930_1349.png";
        ImageHandler.getSharedInstance(context).load(
                sampleImageURL)
                .fit().centerCrop().into(holder.itemImage);

        holder.addBtnView.setContentDescription(String.format(context.getString(R.string.add_button_cd)
                , options.get(position).getHeading()));
        holder.detailsBtnView.setContentDescription
                (String.format(context.getString(R.string.details_button_cd)
                        , options.get(position).getHeading()));
    }

    // Perform the HTTP request for book listings data and process the response.

//                            ArrayList<String> dialogStringExtras = new ArrayList<>();
//                            dialogStringExtras.add(imageUrlString);
//                            dialogStringExtras.add(detailTextString);


//    // BEGIN_INCLUDE(detail_set_view_name)
//    /**
//     * Set the name of the view's which will be transition to, using the static values above.
//     * This could be done in the layout XML, but exposing it via static variables allows easy
//     * querying from other Activities
//     */
//                            ViewCompat.setTransitionName(itemImage, SHARED_IMAGE_VIEW_NAME);
//    // END_INCLUDE(detail_set_view_name)

                    // Construct an Intent as normal
//                            Intent intent = new Intent(getContext(), PlanViewPagerActivity.class);
//                            intent.putExtra
//                                    (PlanDetailsDialogActivity.EXTRA_KEY, position);
//                            startActivity(intent);
                    // BEGIN_INCLUDE(start_activity)
                    /**
                     * Now create an {@link android.app.ActivityOptions} instance using the
                     * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
                     * method.
                     */
//                            ActivityOptionsCompat activityOptions
//                                    = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    getActivity(),
//                                    new Pair<View, String>(
//                                            itemImage,
//                                            PlanDetailsDialogActivity.SHARED_IMAGE_VIEW_NAME));

                    // Now we can start the Activity, providing the activity options as a bundle
//                                    ActivityCompat.startActivity(getContext(), intent, activityOptions.toBundle());
//                             END_INCLUDE(start_activity)

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton addBtnView;
        public final Button detailsBtnView;
        public final TextView heading;
        public final ImageView itemImage;

        public OptionViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);
            itemImage = (ImageView) view.findViewById(R.id.imageview);
            addBtnView = (ImageButton) view.findViewById(R.id.add_button);
            detailsBtnView = (Button) view.findViewById(R.id.details_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + heading.getText() + "'";
        }

    }

    private void showDetailDialog() {
        Timber.d("showDetailDialog:");
        if (context instanceof FragmentActivity) {
            // We can get the fragment manager
            FragmentActivity activity = (FragmentActivity) context;
            String[] args = new String[]{detailText, sampleImageURL};
            PlanDetailsDialogFragment fragment = PlanDetailsDialogFragment.newInstance(args);
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction t = fm.beginTransaction();
            fragment.show(fm, "fragment_plan_detail_dialog");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
//                addOptionToPlan(holder.option.getHeading());
                break;
            case R.id.details_button:
                showDetailDialog();
                break;
        }
    }

}
