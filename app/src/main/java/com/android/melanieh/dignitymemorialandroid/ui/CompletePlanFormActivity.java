package com.android.melanieh.dignitymemorialandroid.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

public class CompletePlanFormActivity extends AppCompatActivity {

    AppCompatSpinner planTypeSpinner;
    AppCompatSpinner zipCodeSpinner;
    int planType;
    long zipCode;
    EditText planNameET;
    EditText zipCodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_plan_form);

        planTypeSpinner = (AppCompatSpinner)findViewById(R.id.planTypeSpinner);
        planNameET = (EditText)findViewById(R.id.planNameET);
        zipCodeSpinner = (AppCompatSpinner) findViewById(R.id.zipCodeSpinner);

        /**
         * Type of plan, possible valid values are in the UserSelectionContract.java file:
         * {@link PlanEntry#BURIAL}, {@link PlanEntry#CREMATION}, or
         * {@link PlanEntry#UNDECIDED}.
         * Default value is UNDECIDED
         */

        planType = PlanEntry.UNDECIDED;
        zipCode = Long.valueOf(getString(R.string.pref_zip_code_default));
        populateSpinner();

        // grab inputs
        String planName = planNameET.getText().toString();
        // content values
        String zipCode = planNameET.getText().toString();

        // insert in DB

    }

    private void populateSpinner() {
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
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.cremation))) {
                        planType = PlanEntry.CREMATION;
                    } else if (selection.equals(getString(R.string.burial))) {
                        planType = PlanEntry.BURIAL;
                    } else {
                        planType = PlanEntry.UNDECIDED;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                planType = PlanEntry.UNDECIDED;
            }
        });
    }


}
