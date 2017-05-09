package com.android.melanieh.dignitymemorialandroid.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.melanieh.dignitymemorialandroid.R;

import timber.log.Timber;

public class PlanSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("activity: onCreate");

        setContentView(R.layout.activity_plan_summary);
    }
}
