package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.melanieh.dignitymemorialandroid.R;

import timber.log.Timber;

public class PlanSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("activity: onCreate");
        setContentView(R.layout.activity_plan_summary);
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
        Class destClass;
        String extraContent = "";
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
                startActivity(Intent.createChooser(launchShareIntent(),
                        getString(R.string.share_app_chooser_dialog_title)));
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
