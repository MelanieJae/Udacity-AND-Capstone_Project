package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.DMApplication;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;
import com.android.melanieh.dignitymemorialandroid.R;
import com.github.florent37.picassopalette.PicassoPalette;

import java.util.List;

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
        implements MenuOptionsInterface {

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
    NestedScrollView scrollView;
    Class destClass;
    String extraContent;

    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuitem_list);
        Timber.plant(new Timber.DebugTree());

        toolBarIV = (ImageView)findViewById(R.id.toolbar_image);
        appBarLogo = (ImageView)findViewById(R.id.appbar_logo);
        scrollView = (NestedScrollView)findViewById(R.id.scrollView);

        recyclerView = (RecyclerView)findViewById(R.id.menuitem_list);
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

        // toolbar image
        ImageHandler.getSharedInstance(this).load(appBarImageUrl.toString()).
                fit().centerCrop().into(toolBarIV);
        // company logo
        ImageHandler.getSharedInstance(this).load(appBarLogoUrl.toString())
                .fit().centerInside().into(appBarLogo);

        // start Google Analytics tracking
        Timber.d("start Analytics tracking...");
        ((DMApplication)getApplication()).startTracking();

        PicassoPalette.with(appBarImageUrl.toString(), toolBarIV)
                .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                .intoBackground(scrollView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Timber.d("setUpRecyclerView");
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
            Timber.d("onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menuitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Timber.d("onBindViewHolder");

            holder.mItem = mValues.get(position);

            // destination class for intent; varies according to which button is selected
            final String buttonLabel = holder.mItem.content;
            holder.mBtnView.setContentDescription(buttonLabel);
            holder.mBtnView.setText(mValues.get(position).content);
            holder.mBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString("menu_button_content", holder.mItem.details);
                        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.menuitem_detail_container, fragment)
                                .commit();

                    } else {
                        if (buttonLabel.contains("Search") || buttonLabel.contains("Find")
                                || (buttonLabel.contains("Plan")) ) {
                            destClass = CompleteFormActivity.class;
                            extraContent = holder.mItem.details;
                            Timber.d("extraContent: " + extraContent);

                        } else if (buttonLabel.contains("Checklist") || buttonLabel.contains("Pay")
                                || buttonLabel.contains("FAQ")) {
                            destClass = MenuItemDetailActivity.class;
                            extraContent = holder.mItem.details;
                        }
                        launchMenuIntent(destClass, extraContent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            Timber.d("getItemCount");
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
            return glm;
        } else {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            return lm;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Timber.d("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                navigateUpTo(new Intent(this, MenuItemListActivity.class));
                return true;
            case R.id.action_access_preferences:
                destClass = SettingsActivity.class;
                launchMenuIntent(destClass, null);
                break;
            case R.id.action_view_plan_selections:
                destClass = PlanSummaryActivity.class;
                launchMenuIntent(destClass, null);
                break;
            case R.id.action_share:
                startActivity(Intent.createChooser(launchShareIntent(), getString(R.string.share_app_chooser_dialog_title)));
        }
        return true;
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Timber.d("launchMenuIntent:");
        Timber.d("destinationClass=" + destinationClass.toString());
        Timber.d("extraContent: " + extraContent);
        Intent intent = new Intent(this, destinationClass);
        intent.putExtra("button_extra_content", extraContent);
        startActivity(intent);
    }

    public Intent launchShareIntent() {Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }

}

