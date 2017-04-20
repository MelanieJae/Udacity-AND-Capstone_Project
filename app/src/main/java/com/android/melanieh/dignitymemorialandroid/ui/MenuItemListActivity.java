package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;
import com.android.melanieh.dignitymemorialandroid.R;

import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;

import timber.log.Timber;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MenuItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MenuItemListActivity extends AppCompatActivity
        implements ToolbarOptionsInterface{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private String appBarImageUrl;
    private String appBarLogoUrl;
    private ImageView toolBarIV;
    private ImageView appBarLogo;
    Class destClass;
    String extraContent;

    // notifications
    public static final int NOTIFICATION_ID = 1;


    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuitem_list);
        Timber.plant(new Timber.DebugTree());

        recyclerView = (RecyclerView)findViewById(R.id.menuitem_list);
        toolBarIV = (ImageView)findViewById(R.id.toolbar_image);
        appBarLogo = (ImageView)findViewById(R.id.appbar_logo);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.menuitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        appBarImageUrl = BuildConfig.APP_BAR_IMAGE_URL;
        appBarLogoUrl = BuildConfig.APP_BAR_LOGO_URL;

        if (getResources().getConfiguration().screenWidthDp < 900) {
            // toolbar image
            ImageHandler.getSharedInstance(this).load(appBarImageUrl.toString()).
                    fit().centerCrop().into(toolBarIV);
            // company logo
            ImageHandler.getSharedInstance(this).load(appBarLogoUrl.toString())
                    .fit().centerInside().into(appBarLogo);

        }
//        PicassoPalette.with(appBarImageUrl.toString(), toolBarIV)
//                .use(PicassoPalette.Profile.VIBRANT_LIGHT)
//                .intoBackground(scrollView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(MenuContent.ITEMS));
        recyclerView.setLayoutManager(getLayoutManager());
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<MenuContent.MenuItem> mValues;
        public SimpleItemRecyclerViewAdapter(List<MenuContent.MenuItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menuitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

            // destination class for intent; varies according to which button is selected
            final String buttonLabel = holder.mItem.content;
            holder.mBtnView.setContentDescription(buttonLabel);
            holder.mBtnView.setText(mValues.get(position).content);
            holder.mBtnView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (buttonLabel.contains("Search") || buttonLabel.contains("Find")) {
                        destClass = SearchResultActivity.class;
                        extraContent = buttonLabel;
                    } else if (buttonLabel.contains("Checklist") || buttonLabel.contains("Pay")) {
                        destClass = MenuItemDetailActivity.class;
                        extraContent = holder.mItem.details;
                    } else if (buttonLabel.contains("Start")){
                        // currently the only other option is the Start Planning button
                        destClass = PlanViewPagerActivity.class;
                    } else {
                        sendNotification(v);
                        destClass = PlanSummaryActivity.class;
                    }
                    launchMenuIntent(destClass, extraContent);
                }

//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(ARG_ITEM_ID, holder.mItem.id);
//                        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.menuitem_detail_container, fragment)
//                                    .commit();
//
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, MenuItemDetailActivity.class);
//                        intent.putExtra(ARG_ITEM_ID, holder.mItem.id);
//                        context.startActivity(intent);
//                        }

            });
        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final Button mBtnView;
            public MenuContent.MenuItem mItem;

            public ViewHolder(View view) {
                super(view);
                mBtnView = (Button) view.findViewById(R.id.menu_item_button);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mBtnView.getText() + "'";
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp < 900) {
            GridLayoutManager glm = new GridLayoutManager(this, 2);
            glm.offsetChildrenVertical(2000);
            glm.offsetChildrenHorizontal(getResources().getInteger(R.integer.horiz_offset));
            return glm;
        } else {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            return lm;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Context context = this;
        switch (id) {
            case R.id.action_pref_settings:
                destClass = SettingsActivity.class;
                break;
            case R.id.action_view_plan_selections:
                destClass = PlanSummaryActivity.class;
            case R.id.action_share:
                launchShareAction();
                break;
        }
        launchMenuIntent(destClass, null);
        return super.onOptionsItemSelected(item);
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Intent intent = new Intent(this, destinationClass);
        intent.putExtra("EXTRA_CONTENT", extraContent);
        startActivity(intent);
    }

    public void launchShareAction() {};

    /*** Notifications */
    public void sendNotification(View view) {
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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, addEventIntent, 0);

        // BEGIN_INCLUDE (build_notification)
        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
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

