package com.android.melanieh.dignitymemorialandroid.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import com.android.melanieh.dignitymemorialandroid.ui.MenuItemListActivity;
import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.ui.PlanSummaryFragment;

import timber.log.Timber;

/*** Created by melanieh on 4/20/17. */

public class DMWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int appWidgetIdsSize = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<appWidgetIdsSize; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MenuItemListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            Cursor cursor = context.getContentResolver().
                    query(PlanEntry.CONTENT_URI, PlanSummaryFragment.SELECTION_COLUMNS, null, null, null);

            if (cursor == null) {
                views.setTextViewText(R.id.widget_text,
                        context.getResources().getString(R.string.widget_emptyview_text));
            } else {
                views.setTextViewText(R.id.widget_text, readTextFromCursor(context, cursor));
            }
                views.setOnClickPendingIntent(R.id.tappable_widget_layout, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private String readTextFromCursor(Context context, Cursor cursor) {
        Timber.d("Cursor: " + cursor);
        String planText = "";
            // widget displays last (i.e. most recent) plan created
            while (cursor.moveToNext()) {
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

                planText = String.format(context.getString(R.string.plan_summary_intro), planName) +
                        "\n" + String.format(context.getString(R.string.ceremony_selection), ceremonySelection) +
                        "\n" + String.format(context.getString(R.string.visitation_selection), visitationSelection) +
                        "\n" + String.format(context.getString(R.string.reception_selection), receptionSelection) +
                        "\n" + String.format(context.getString(R.string.site_selection), siteSelection) +
                        "\n" + String.format(context.getString(R.string.container_selection), containerSelection) +
                        "\n" + String.format(context.getString(R.string.est_cost), estCost);
            }
        return planText;
    }


}

