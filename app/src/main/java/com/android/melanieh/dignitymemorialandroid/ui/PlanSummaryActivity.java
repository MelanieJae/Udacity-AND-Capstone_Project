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
    MenuItem dynamicMenuItem;

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
        dynamicMenuItem = menu.findItem(R.id.action_view_plan_selections);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        dynamicMenuItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
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
                launchMenuIntent(destClass, null);
                break;
            case R.id.action_share:
                startActivity(Intent.createChooser(launchShareIntent(),
                        getString(R.string.share_app_chooser_dialog_title)));
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchMenuIntent(Class destinationClass, String extraContent) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
    // this is launched from the fragment
    public Intent launchShareIntent() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = getString(R.string.share_msg_body_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        return shareIntent;
    }


}
