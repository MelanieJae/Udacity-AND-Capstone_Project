package com.android.melanieh.dignitymemorialandroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.melanieh.dignitymemorialandroid.R;

public class PlanSelectionsActivity extends AppCompatActivity
        implements ToolbarOptionsInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_selections);
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
            case R.id.action_view_plan_selections:
                launchMenuIntent(this, PlanSelectionsActivity.class);
        }
        return true;
    }

    public void launchMenuIntent(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        startActivity(intent);
    }
    
    public void launchShareAction() {};

}
