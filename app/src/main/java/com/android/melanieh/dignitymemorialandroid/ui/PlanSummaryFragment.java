package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;

import java.util.Calendar;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanSummaryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, MenuOptionsInterface {

    TextView planSummaryView;
    TextView estCostView;
    CursorAdapter adapter;
    private ShareActionProvider mShareActionProvider;

    // notifications
    public static final int NOTIFICATION_ID = 1;

    public String[] PLAN_COLUMNS = {
            UserSelectionContract.PlanEntry.COLUMN_ID,
            UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME,
            UserSelectionContract.PlanEntry.COLUMN_CEREMONY_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_VISITATION_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_RECEPTION_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_SITE_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_CONTAINER_SELECTION,
            UserSelectionContract.PlanEntry.COLUMN_EST_COST
    };

    private static final int INDEX_ID = 1;
    private static final int INDEX_PLAN_NAME = 2;
    private static final int INDEX_CEREMONY_SELECTION = 3;
    private static final int INDEX_VISITATION_SELECTION = 4;
    private static final int INDEX_RECEPTION_SELECTION = 5;
    private static final int INDEX_SITE_SELCTION = 6;
    private static final int INDEX_CONTAINER_SELECTION = 7;
    private static final int INDEX_EST_COST = 8;

    public PlanSummaryFragment() {
        //
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plan_summary, container, false);
        planSummaryView = (TextView)rootView.findViewById(R.id.planSummaryDBTest);
        getLoaderManager().initLoader(4, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new
                CursorLoader(getContext(), UserSelectionContract.PlanEntry.CONTENT_URI,
                PLAN_COLUMNS, null, null, null);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            String planName = data.getString(INDEX_PLAN_NAME);
            String ceremony = data.getString(INDEX_CEREMONY_SELECTION);
            String visitation = data.getString(INDEX_VISITATION_SELECTION);
            String reception = data.getString(INDEX_RECEPTION_SELECTION);
            String siteOrNiche = data.getString(INDEX_SITE_SELCTION);
            String container = data.getString(INDEX_CONTAINER_SELECTION);
            double estCost = data.getDouble(INDEX_EST_COST);

            StringBuilder builder = new StringBuilder();
            builder.append(String.format(getString(R.string.plan_summary_intro), planName));
            builder.append(getString(R.string.ceremony_selection_label)
                    + String.format(getString(R.string.ceremony_selection), ceremony));
            builder.append(getString(R.string.visitation_selection_label)
                    + String.format(getString(R.string.visitation_selection), visitation));
            builder.append(getString(R.string.reception_selection_label)
                    + String.format(getString(R.string.reception_selection), reception));
            builder.append(getString(R.string.site_cremation_label)
                    + String.format(getString(R.string.site_selection), siteOrNiche));
            builder.append(getString(R.string.container_selection_label)
                    + String.format(getString(R.string.ceremony_selection), container));
            builder.append(String.format(getString(R.string.est_cost), estCost));
            planSummaryView.setText("Database test readout: " + planName);
        }
        data.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(4, null, this);
    }

    @Override
    public void launchMenuIntent(Class activity, String extraContent) {}

    @Override
    public Intent launchShareIntent() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }

    /*** Notifications */
    public void sendNotification() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        long endMillis = endTime.getTimeInMillis();
        // TODO: change to whatever clicking on the notification will do (e.g. notify the user and link to
        // loved one's obituary, link to calendar event for consult and/or service)
        Intent addEventIntent = addEvent(getString(R.string.sample_notif_cal_event_title),
                getString(R.string.sample_notif_cal_event_desc),
                getString(R.string.sample_notif_cal_event_loc), startMillis, endMillis);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, addEventIntent, 0);

        // BEGIN_INCLUDE (build_notification)
        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());

        /* Android design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */
        builder.setSmallIcon(R.drawable.lwc_logo_icon);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);

        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.lwc_logo_icon);
        builder.setLargeIcon(largeIcon);

        /**
         * Set the text of the notification. This sample sets the three most commononly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */
        builder.setContentTitle(getString(R.string.dm_notifications_title));
        builder.setContentText(getString(R.string.dm_notifications_body_text));
        builder.setSubText(getString(R.string.dm_notifications_body_tap_text));
        // the new WearableExtender.addAction decouples the notification actions for the wearable
        // from notification actions for the phone so that actions for one will not appear on the other
//        builder.extend(wearableExtender
//                .addAction(action));
        // END_INCLUDE (build_notification)

        // BEGIN_INCLUDE(send_notification)
        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(NOTIFICATION_ID,builder.build());
        // END_INCLUDE(send_notification)
    }

    public Intent addEvent(String title, String location, String desc, long begin, long end) {

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, desc)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        return intent;
    }
}
