package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by melanieh on 4/12/17.
 */

public class PlanDetailsDialogFragment extends DialogFragment {

    static String detailText;
    static String imageURL;
    TextView detailsTextView;
    ImageView detailsImageView;

    public PlanDetailsDialogFragment() {
        //
    }

    public static PlanDetailsDialogFragment newInstance(String[] passedArgs) {
        PlanDetailsDialogFragment frag = new PlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString(PlanOptionRecyclerViewAdapter.DETAIL_TEXT_ARG_KEY, passedArgs[0]);
        args.putString(PlanOptionRecyclerViewAdapter.IMAGE_STRING_ARG_KEY, passedArgs[1]);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailText = getArguments().getString(PlanOptionRecyclerViewAdapter.DETAIL_TEXT_ARG_KEY);
        imageURL = getArguments().getString(PlanOptionRecyclerViewAdapter.IMAGE_STRING_ARG_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_details_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        detailsTextView = (TextView) view.findViewById(R.id.details_text);
        detailsImageView = (ImageView) view.findViewById(R.id.shared_element_transition_image);
        detailsTextView.setText(detailText);
        loadImage();

    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */
    private void loadImage() {
        // toolbar image
        ImageHandler.getSharedInstance(getContext()).load(imageURL).
                fit().centerCrop().into(detailsImageView);
    }

}
