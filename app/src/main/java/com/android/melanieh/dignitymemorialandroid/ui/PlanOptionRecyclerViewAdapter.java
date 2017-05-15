package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.PlanOption;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionDBHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    String planUriString;
    String currentEstCostString;
    String estCostSelectionString;
    String updatedEstCostString;

    public static String DETAIL_TEXT_ARG_KEY = "Option item detail text";
    public static String IMAGE_STRING_ARG_KEY = "ImageURL string";
    private int NOTIFICATION_ID = 900;

    public PlanOptionRecyclerViewAdapter(Context context, ArrayList<PlanOption> options,
                                         String planUriString) {
        Timber.d("PlanOptionRecyclerAdapter constructor");
        this.context = context;
        this.options = options;
        this.planUriString = planUriString;
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
        Timber.d("options: " + options.toString());
        // temporarily capture detail text to pass as dialog fragment argument
        // it is not displayed in the cardview, only in the dialog fragment
        detailText = currentOption.getDetailText();
        itemImageURL = currentOption.getImageUrlString();
        optionSelection = currentOption.getHeading();

        estCostSelectionString = currentOption.getEstimatedCost();
        // estimated cost will be displayed and tracked so it needs to also
        // be formatted as a number for calculation
        Pattern regex = Pattern.compile("\\d[\\d,\\.]+");
        Matcher finder = regex.matcher(estCostSelectionString);
        if (finder.find()) { // or while() if you want to process each
            try {
                int lowEndValue = Integer.parseInt(finder.group(0).replaceAll(",", ""));
                int highEndValue = Integer.parseInt(finder.group(1).replaceAll(",", ""));

                int totalValueRangeLowEnd = Integer.parseInt(currentEstCostString) + lowEndValue;
                int totalValueRangeHighEnd = Integer.parseInt(currentEstCostString) + highEndValue;
                updatedEstCostString = "$" + totalValueRangeLowEnd + "-" + totalValueRangeHighEnd;

            } catch (NumberFormatException e) {
                Timber.wtf(e, "");
            }
        }
        final String[] dialogArgs = new String[]{detailText, itemImageURL, estCostSelectionString};
        Timber.d("itemImageUrl: " + itemImageURL);
        Timber.d("detailText: " + detailText);
        Timber.d("estCost: " + estCostSelectionString);

        ImageHandler.getSharedInstance(context).load(itemImageURL).
                fit().centerCrop().into(holder.itemImage);

        holder.detailsBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog(dialogArgs);
            }
        });

        holder.addBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionToPlan(currentOption.getTitle(), optionSelection, updatedEstCostString);
            }
        });

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton addBtnView;
        public final Button detailsBtnView;
        public final ImageView itemImage;

        public OptionViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.imageview);
            addBtnView = (ImageButton) view.findViewById(R.id.add_button);
            detailsBtnView = (Button) view.findViewById(R.id.details_button);
        }

    }

    private void showDetailDialog(String[] dialogArgs) {
        Timber.d("showDetailDialog:");
        if (context instanceof FragmentActivity) {
            // We can get the fragment manager
            FragmentActivity activity = (FragmentActivity) context;
            PlanDetailsDialogFragment fragment = PlanDetailsDialogFragment.newInstance(dialogArgs);
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction t = fm.beginTransaction();
            fragment.show(fm, "fragment_plan_detail_dialog");
        }
    }


    private void addOptionToPlan(String optionsTitle, String optionSelection, String updatedEstCostString) {
        ContentValues values = new ContentValues();
        String cvKey = "";
        // query first for existing plan URI and add to existing plan if there;
        // otherwise insert new plan entry if not present

        boolean isCeremonyScreen = Pattern.compile(Pattern.quote("ceremony"), Pattern.CASE_INSENSITIVE).matcher(optionsTitle).find();
        boolean isReceptionScreen = Pattern.compile(Pattern.quote("reception"), Pattern.CASE_INSENSITIVE).matcher(optionsTitle).find();
        boolean isVisitationScreen = Pattern.compile(Pattern.quote("visitation"), Pattern.CASE_INSENSITIVE).matcher(optionsTitle).find();
        boolean isSiteScreen = Pattern.compile(Pattern.quote("resting place"), Pattern.CASE_INSENSITIVE).matcher(optionsTitle).find();
        boolean isContainerScreen = Pattern.compile(Pattern.quote("casket"), Pattern.CASE_INSENSITIVE).matcher(optionsTitle).find();

        if (isCeremonyScreen) {
            cvKey = PlanEntry.COLUMN_CEREMONY_SELECTION;
        } else if (isReceptionScreen) {
            cvKey = PlanEntry.COLUMN_RECEPTION_SELECTION;
        } else if (isVisitationScreen) {
            cvKey = PlanEntry.COLUMN_VISITATION_SELECTION;
        } else if (isSiteScreen) {
            cvKey = PlanEntry.COLUMN_SITE_SELECTION;
        } else if (isContainerScreen) {
            cvKey = PlanEntry.COLUMN_CONTAINER_SELECTION;
        } else {
            Timber.e("Invalid option selection");
        }

        values.put(cvKey, optionSelection);
        values.put(PlanEntry.COLUMN_EST_COST, updatedEstCostString);
        Timber.d("cvKey: " + cvKey);
        Timber.d("planUri: " + planUriString);
        Timber.d("context: " + context);
        Timber.d("values: " + values.toString());
        // selection/where clause will always indicate the row to be updated and that is in the content provider
        // so null is passed here for the selection/whereClause argument

        int numRowsUpdated = context.getContentResolver().update(Uri.parse(planUriString), values,
                null,
                null);

        Timber.d("numRowsUpdated: " + numRowsUpdated);
        if (numRowsUpdated == 0) {
            Toast.makeText(context, "Error updating plan with selection", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Plan update with selection successful", Toast.LENGTH_LONG).show();
        }


        // if this is the container selection screen, i.e. the last screen, once the selection is
        // saved to the databse, send the user a notification that a new plan has been created.
        if (isContainerScreen) {
            sendNewPlanNotification();
        }
    }

    private void sendNewPlanNotification() {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_black_48dp);
        context.getResources().getDrawable(R.drawable.ic_add_black_48dp);
        String notifBodyText = configureNotificationText();
        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.dm_notifications_title))
                .setContentText(context.getString(R.string.dm_notifications_body_tap_text))
                .setSmallIcon(R.drawable.ic_add_black_48dp)
                .setLargeIcon(largeIcon)
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private String configureNotificationText() {
        String notifText = "";
        Cursor cursor = queryPlanEntry(planUriString);
        cursor.moveToLast();

        String planEntryId = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_ID);
        String planName = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_PLAN_NAME);
        String contactEmail = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_CONTACT_EMAIL);
        String providerName = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_PROVIDER);
        String ceremonySelection = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_CEREMONY_SELECTION);
        String visitationSelection = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_VISITATION_SELECTION);
        String receptionSelection = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_RECEPTION_SELECTION);
        String siteSelection = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_SITE_SELECTION);
        String containerSelection = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_CONTAINER_SELECTION);
        String estCost = cursor.getString(PlanSummaryFragment.INDEX_COLUMN_EST_COST);

        notifText = String.format(context.getString(R.string.plan_summary_intro), planName) +
                "\n" + String.format(context.getString(R.string.ceremony_selection), ceremonySelection) +
                "\n" + String.format(context.getString(R.string.visitation_selection), visitationSelection) +
                "\n" + String.format(context.getString(R.string.reception_selection), receptionSelection) +
                "\n" + String.format(context.getString(R.string.site_selection), siteSelection) +
                "\n" + String.format(context.getString(R.string.container_selection), containerSelection) +
                "\n" + String.format(context.getString(R.string.est_cost), estCost);

        return notifText;
    }

    private Cursor queryPlanEntry(String planUriString) {
        Uri planUri = Uri.parse(planUriString);
        Cursor cursor = context.getContentResolver().
                query(planUri, PlanSummaryFragment.SELECTION_COLUMNS, null, null, null);
        // widget displays last (i.e. most recent) plan created
        return cursor;
    }

}
