package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.melanieh.dignitymemorialandroid.R;

public class PlanSummaryActivity extends AppCompatActivity
        implements MenuOptionsInterface {

    Class destClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_summary);
        Fragment fragment = new PlanSummaryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(R.id.planSummaryFragment, fragment);
        fragmentTransaction.commit();
        getSupportFragmentManager().beginTransaction();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        // Fetch and store ShareActionProvider

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        }
        launchMenuIntent(destClass, null);
        return super.onOptionsItemSelected(item);
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
    // this is launched from the fragment
    public Intent launchShareIntent() { return null; }


}
