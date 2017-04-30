package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

public class CompletePlanFormActivity extends AppCompatActivity {

    AppCompatSpinner planTypeSpinner;
    int planType;
    String zipCode;
    EditText planNameET;
    EditText zipCodeET;
    SharedPreferences sharedPrefs;
    Button startPlanningBtn;
    Uri newPlanUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_plan_form);

        planTypeSpinner = (AppCompatSpinner)findViewById(R.id.planTypeSpinner);
        planNameET = (EditText)findViewById(R.id.plan_form_plan_name_et);
        zipCodeET = (EditText)findViewById(R.id.plan_form_zip_code_et);
        startPlanningBtn = (Button)findViewById(R.id.start_planning_btn);

        // grab incoming data from menu item button selection and any sharedprefs info
        // grab any saved zip codes
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPrefs.contains("zipCode")) {
            zipCode = sharedPrefs.getString("zipCode", "");
            zipCodeET.setText(zipCode);
        }

        /**
         * Type of plan, possible valid values are in the UserSelectionContract.java file:
         * {@link PlanEntry#BURIAL}, {@link PlanEntry#CREMATION}, or
         * {@link PlanEntry#UNDECIDED}.
         * Default value is UNDECIDED
         */

        planType = PlanEntry.UNDECIDED;
        populatePlanTypeSpinner();

        // grab inputs
        String planName = planNameET.getText().toString();
        // content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanEntry.COLUMN_PLAN_NAME, planName);
        contentValues.put(PlanEntry.COLUMN_PLAN_TYPE, planType);
        // insert in DB
        newPlanUri = getContentResolver().insert(PlanEntry.CONTENT_URI, contentValues);
        if (newPlanUri != null) {
            Toast.makeText(this, getString(R.string.plan_entry_creation_successful), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.error_creating_plan), Toast.LENGTH_LONG).show();
        }

        startPlanningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent beginPlanningIntent = new Intent(CompletePlanFormActivity.this, PlanViewPagerActivity.class);
                beginPlanningIntent.setData(newPlanUri);
                startActivity(beginPlanningIntent);
            }
        });

    }

    private void populatePlanTypeSpinner() {
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_plan_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        planTypeSpinner.setAdapter(spinnerAdapter);

        // Set the integer mSelected to the constant values
        planTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (selection.equals(getString(R.string.cremation))) {
                    planType = PlanEntry.CREMATION;
                } else if (selection.equals(getString(R.string.burial))) {
                    planType = PlanEntry.BURIAL;
                } else {
                    planType = PlanEntry.UNDECIDED;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                planType = PlanEntry.UNDECIDED;
            }
        });
    }


}
