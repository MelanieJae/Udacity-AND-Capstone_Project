package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 4/19/17.
 */

public class PlanOptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PlanOptionRecyclerViewAdapter.OptionViewHolder> {

    ArrayList<PlanOption> options;
    String detailText;
    String itemImageURL;
    String optionSelection;
    Context context;
    Uri planUri;

    public static String DETAIL_TEXT_ARG_KEY = "Option item detail text";
    public static String IMAGE_STRING_ARG_KEY = "ImageURL string";

    public PlanOptionRecyclerViewAdapter(Context context, ArrayList<PlanOption> options, Uri planUri) {
        this.context = context;
        this.options = options;
        this.planUri = planUri;
    }

    @Override
    public PlanOptionRecyclerViewAdapter.OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder:");
        View inflateView = LayoutInflater.from(context).inflate(R.layout.plan_options_list_item, parent, false);
        return new OptionViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(PlanOptionRecyclerViewAdapter.OptionViewHolder holder, int position) {
        Timber.d("onBindViewHolder:");
        final PlanOption currentOption = options.get(position);
        // temporarily capture detail text to pass as dialog fragment argument
        // it is not displayed in the cardview, only in the dialog fragment
        detailText = currentOption.getDetailText();
        itemImageURL = currentOption.getImageUrlString();
        optionSelection = currentOption.getHeading();
        holder.heading.setText(currentOption.getHeading());
        ImageHandler.getSharedInstance(context).load(itemImageURL).
                fit().centerCrop().into(holder.itemImage);

        holder.detailsBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog();
            }
        });

        holder.addBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionToPlan(currentOption.getTitle(), optionSelection);
            }
        });

    }

    @Override
    public int getItemCount() {
        return options.size();
    }


    public class OptionViewHolder extends RecyclerView.ViewHolder {
        public final Button addBtnView;
        public final Button detailsBtnView;
        public final TextView heading;
        public final ImageView itemImage;

        public OptionViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);
            itemImage = (ImageView) view.findViewById(R.id.imageview);
            addBtnView = (Button) view.findViewById(R.id.add_button);
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
            String[] args = new String[]{detailText, itemImageURL};
            PlanDetailsDialogFragment fragment = PlanDetailsDialogFragment.newInstance(args);
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction t = fm.beginTransaction();
            fragment.show(fm, "fragment_plan_detail_dialog");
        }
    }


    private void addOptionToPlan(String optionsTitle, String optionSelection) {
        ContentValues values = new ContentValues();
        String cvKey = "";
        // query first for existing plan URI and add to existing plan if there;
        // otherwise insert new plan entry if not present

        switch (optionsTitle) {
            case "ceremony":
                cvKey = UserSelectionContract.PlanEntry.COLUMN_CEREMONY_SELECTION;
                break;
            case "reception":
                cvKey = UserSelectionContract.PlanEntry.COLUMN_RECEPTION_SELECTION;
                break;
            case "visitation":
                cvKey = UserSelectionContract.PlanEntry.COLUMN_VISITATION_SELECTION;
                break;
            case "site":
                cvKey = UserSelectionContract.PlanEntry.COLUMN_SITE_SELECTION;
                break;
            case "container":
                cvKey = UserSelectionContract.PlanEntry.COLUMN_CONTAINER_SELECTION;
                break;
            default:
                Timber.e("Invalid Fragment tag");
        }
        values.put(cvKey, optionSelection);
        context.getContentResolver().update(planUri, values, null, null);

    }


}
