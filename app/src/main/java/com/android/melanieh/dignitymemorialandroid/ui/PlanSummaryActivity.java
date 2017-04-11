package com.android.melanieh.dignitymemorialandroid.ui;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.melanieh.dignitymemorialandroid.R;

public class PlanSummaryActivity extends AppCompatActivity
        implements ToolbarOptionsInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_selections);
        Fragment fragment = new PlanSummaryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(R.id.planSelectionFragment, fragment);
        fragmentTransaction.commit();
        getSupportFragmentManager().beginTransaction();
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
                launchMenuIntent(this, SettingsActivity.class);
                break;
        }
        return true;
    }

    public void launchMenuIntent(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        startActivity(intent);
    }

    public void launchShareAction() {};

}
