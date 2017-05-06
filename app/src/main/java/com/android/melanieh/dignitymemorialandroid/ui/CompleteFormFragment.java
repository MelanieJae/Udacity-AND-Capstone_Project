package com.android.melanieh.dignitymemorialandroid.ui;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.android.melanieh.dignitymemorialandroid.R;

import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by melanieh on 5/1/17.
 */

public class CompleteFormFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView firstNameTV;
    TextView lastNameTV;
    EditText firstNameET;
    EditText lastNameET;
    TextView zipCodeTV;
    TextView useCurrentLocationTV;
    Button startObitSearchBtn;
    Button startProviderSearchBtn;
    String queryType;
    TextView locSvcsView;
    boolean isUsingCurrentLocation;
    boolean validEntriesArePresent;
    GoogleApiClient googleApiClient;
    Location currentLocation;
    LocationRequest locationRequest;
    AppCompatSpinner planTypeSpinner;
    int planType;
    String zipCode;
    EditText planNameET;
    EditText contactEmailET;
    SharedPreferences sharedPrefs;
    Button startPlanningBtn;
    Uri newPlanUri;
    String spinnerSelection;
    String contactEmail;

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        /* Location services Api client */
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.plan_form, container, false);

//        //obits form input
        firstNameET = (EditText) rootView.findViewById(R.id.search_form_first_name_et);
        lastNameET = (EditText) rootView.findViewById(R.id.search_form_last_name_et);

        planTypeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.planTypeSpinner);
        planNameET = (EditText) rootView.findViewById(R.id.plan_form_plan_name_et);
        contactEmailET = (EditText) rootView.findViewById(R.id.plan_form_contact_email_et);
//
        locSvcsView = (TextView) rootView.findViewById(R.id.location_svcs_readout);
//        useCurrentLocationTV = (TextView) rootView.findViewById(R.id.location_svcs_readout);
        startObitSearchBtn = (Button) rootView.findViewById(R.id.start_obit_search_btn);
        startPlanningBtn = (Button) rootView.findViewById(R.id.start_planning_btn);

        // initialize isUsingCurrentLocation to its default value of false
        isUsingCurrentLocation = false;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.location_svcs_readout:
                        isUsingCurrentLocation = true;
                        break;
                    case R.id.start_obit_search_btn:
                    case R.id.start_provider_search_btn:
                        startSearch();
                        break;
                    case R.id.start_planning_btn:
                        startPlanning();
                        break;
                }
            }
        };
//
//        startObitSearchBtn.setOnClickListener(clickListener);
//        startProviderSearchBtn.setOnClickListener(clickListener);
        startPlanningBtn.setOnClickListener(clickListener);
//        locSvcsView.setOnClickListener(clickListener);
        populatePlanTypeSpinner();
        return rootView;

    }

    private void startSearch() {
        Timber.d("startSearch:");

        // first check if valid entries are present; button functionality can only be activated
        // if this is true
        Double queryLocationLatExtra;
        Double queryLocationLongExtra;

//        if (!validEntriesArePresent) {
//            showInvalidEntriesDialog();
//        } else {
//            if (isUsingCurrentLocation) {
//                queryType = "provider";
//            } else {
//                boolean zipCodeIsEmpty = Utility.entryIsEmpty(zipCodeET);
//                if (zipCodeIsEmpty) {
//                    queryType = "obituaries";
//                }
//            }
//        }

//        if (zipCodeET != null) {
//            promptUserToSaveZipCodeEntry();
//        }
        Intent getSearchResults = new Intent(getActivity(), SearchResultActivity.class);
        getSearchResults.putExtra("query type", queryType);

        Timber.d("completesearch: queryType: " + queryType);
//        if (currentLocation != null && isUsingCurrentLocation) {
//            queryLocationLatExtra = currentLocation.getLatitude();
//            queryLocationLongExtra = currentLocation.getLongitude();
//            getSearchResults.putExtra("current location lat", queryLocationLatExtra);
//            getSearchResults.putExtra("current location long", queryLocationLongExtra);
//        } else if (!isUsingCurrentLocation && zipCodeET != null) {
//            getSearchResults.putExtra("zipCode", zipCodeET.getText().toString());
//        } else {
            Timber.d("first name", firstNameET.getText().toString());

            getSearchResults.putExtra("first name", "john");
            getSearchResults.putExtra("last name", "smith");
//        }
        startActivity(getSearchResults);
    }

    /**
     * Type of plan, possible valid values are in the UserSelectionContract.java file:
     * {@link UserSelectionContract.PlanEntry#BURIAL}, {@link UserSelectionContract.PlanEntry#CREMATION}, or
     * {@link UserSelectionContract.PlanEntry#UNDECIDED}.
     * Default value is UNDECIDED
     */


    private void populatePlanTypeSpinner() {
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_plan_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        planTypeSpinner.setAdapter(spinnerAdapter);

        // Set the integer mSelected to the constant values
        planTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelection = (String) parent.getItemAtPosition(position);
                if (spinnerSelection.equals(getString(R.string.cremation))) {
                    planType = UserSelectionContract.CREMATION;
                } else if (spinnerSelection.equals(getString(R.string.burial))) {
                    planType = UserSelectionContract.BURIAL;
                } else {
                    planType = UserSelectionContract.UNDECIDED;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                planType = UserSelectionContract.UNDECIDED;
            }
        });
    }

    private void startPlanning() {
        // store plan form inputs from user in plan DB
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanEntry.COLUMN_PLAN_NAME, planNameET.getText().toString());
        contentValues.put(PlanEntry.COLUMN_PLAN_TYPE, spinnerSelection);
        contentValues.put(PlanEntry.COLUMN_CONTACT_EMAIL, contactEmailET.getText().toString());
        Uri newPlanUri = getContext().getContentResolver().insert(PlanEntry.CONTENT_URI, contentValues);
        if (newPlanUri == null) {
            Toast.makeText(getContext(), "Error saving new plan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "New plan saved successfully", Toast.LENGTH_LONG).show();
        }

        Intent beginPlanningIntent = new Intent(getActivity(), PlanViewPagerActivity.class);
        beginPlanningIntent.setData(newPlanUri);
        startActivity(beginPlanningIntent);
    }

//    public void promptUserToSaveZipCodeEntry() {
//        showSaveZipCodeEntryDialog();
//
//    }

//    private void showSaveZipCodeEntryDialog() {
//        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(getContext());
////        ButterKnife.bind(this);
//        deleteConfADBuilder.setMessage(getString(R.string.save_entries_dialog_msg));
//
//        // positive button=yes, save entry to DB or sharedprefs as applicable
//        DialogInterface.OnClickListener yesButtonListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                saveZipCodeEntry();
//            }
//        };
//
//        // negative button=no, keep editing, dismiss dialog
//        DialogInterface.OnClickListener noButtonListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (dialogInterface != null) {
//                    return;
//                }
//            }
//        };
//
//        String yesString = getString(R.string.dialog_yes_btn);
//        String noString = getString(R.string.dialog_no_btn);
//        deleteConfADBuilder.setPositiveButton(yesString, yesButtonListener);
//        deleteConfADBuilder.setNegativeButton(noString, noButtonListener);
//
//    }

    private void showInvalidEntriesDialog() {
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(getContext());
//        ButterKnife.bind(this);
        deleteConfADBuilder.setMessage(getString(R.string.no_valid_entries_present_error_msg));

        // positive button=ok
        DialogInterface.OnClickListener yesButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

//        // negative button = dismiss dialog
        DialogInterface.OnClickListener noButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    return;
                }
            }
        };

        String okString = getString(R.string.dialog_ok_btn);
        deleteConfADBuilder.setPositiveButton(okString, yesButtonListener);
    }

//    private void saveZipCodeEntry() {
//        if (!TextUtils.isEmpty(zipCodeET.getText())) {
//            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(getString(R.string.pref_zip_code_key), zipCodeET.getText().toString());
//            editor.commit();
//
//        }
//        // provider name is saved in the search result screen
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Location Services API client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Location Services API client suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Location Services API client failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        locSvcsView.setText(location.toString());
    }
}