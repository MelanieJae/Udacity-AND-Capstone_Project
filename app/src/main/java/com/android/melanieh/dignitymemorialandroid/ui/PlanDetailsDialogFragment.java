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

/**
 * Created by melanieh on 4/12/17.
 */

public class PlanDetailsDialogFragment extends DialogFragment {

    TextView detailsTextView;
    ImageView detailsImageView;

    // Extra name for the ID parameter
//    public static final String EXTRA_PARAM_ID = "detail:_id";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;

    private PlanOption option;

    public PlanDetailsDialogFragment() {
        //
    }

    public static PlanDetailsDialogFragment newInstance(String title) {
        PlanDetailsDialogFragment frag = new PlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        detailsTextView.setText(getString(R.string.sample_details_text));
//        detailsTextView.setText(PlanDetailsDialogActivity.dialogText);

        loadImage();

    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */
    private void loadImage() {
        Picasso.with(getContext())
                .load(PlanDetailsDialogActivity.dialogImageUrl)
                .noFade()
                .noPlaceholder().fit().centerCrop()
                .into(detailsImageView);
    }

    /**
     * Try and add a {@link Transition.TransitionListener} to the entering shared element
     * {@link Transition}. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */

    @TargetApi(21)
    private boolean addTransitionListener() {
        final Transition transition = getActivity().getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }


}
