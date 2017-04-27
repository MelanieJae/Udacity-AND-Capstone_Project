package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.melanieh.dignitymemorialandroid.R;


/**
 * An activity representing a single MenuItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MenuItemListActivity}.
 */
public class MenuItemDetailActivity extends AppCompatActivity implements MenuOptionsInterface {

    private FragmentManager fragmentManager;
    MenuItemDetailFragment fragment;
    FragmentTransaction fragmentTransaction;
    Class destClass;
    String extraContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuitem_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            fragment = new MenuItemDetailFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .add(R.id.detailFragment, fragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction();
        } else {
            fragment = (MenuItemDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag("detailFragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = this;
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
                break;
            case R.id.action_view_plan_selections:
                destClass = PlanSummaryActivity.class;
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

    public Intent launchShareIntent() { return null; };

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "detailFragment", fragment);
    }

}
