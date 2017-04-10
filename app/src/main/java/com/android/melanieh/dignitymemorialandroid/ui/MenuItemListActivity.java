package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.melanieh.dignitymemorialandroid.content.MenuContent;
import com.android.melanieh.dignitymemorialandroid.R;


import java.net.MalformedURLException;
import java.net.URL;
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
public class MenuItemListActivity extends AppCompatActivity {

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
    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuitem_list);

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

//        try {
            appBarImageUrl =
                    "http://www.dignitymemorial.com/resources/dm20/assets/pages/plan_now/dm_en_US/plan_now_main.jpg";
            appBarLogoUrl =
                    "http://www.dignitymemorial.com/resources/dm20/assets/global/themed/lwc/lwc-logo.png";
//        } catch (MalformedURLException e) {
            Timber.e("Malformed URL Exception");
//        }

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
//            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
//            if (childAdapterPosition % 2 != 0) {
//                recyclerView.addItemDecoration(itemDecoration);
//            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mBtnView.setText(mValues.get(position).content);
            holder.mBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mItem.details.contains("http")) {
                        Uri url = Uri.parse(holder.mItem.details);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, url);
                        startActivity(launchBrowser);
//                        launchBrowser(holder.mItem.details);
                    }
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ARG_ITEM_ID, holder.mItem.id);
                        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.menuitem_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MenuItemDetailActivity.class);
                        intent.putExtra(ARG_ITEM_ID, holder.mItem.id);
                        context.startActivity(intent);
                    }
                }

            });

            // Talkback support
            holder.mBtnView.setContentDescription(holder.mItem.content);
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

    private void launchBrowser(String urlString) {
        Uri url = Uri.parse(urlString);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, url);
        startActivity(launchBrowser);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().screenWidthDp < 900) {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL);
            sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            sglm.offsetChildrenVertical(2000);
            sglm.offsetChildrenHorizontal(getResources().getInteger(R.integer.horiz_offset));
            return sglm;
        } else {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            return lm;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_pref_settings:
                launchSettingsForm();
                break;
            case R.id.action_view_plan_selections:
                viewPlanSelections();
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchSettingsForm() {
        Intent launchSettingsForm = new Intent(this, SettingsActivity.class);
        startActivity(launchSettingsForm);
    }

    private void viewPlanSelections() {
        Intent viewPlanSelections = new Intent(this, PlanSelectionsActivity.class);
        startActivity(viewPlanSelections);
    }
}

