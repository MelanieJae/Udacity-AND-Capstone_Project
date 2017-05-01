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
    Button startSearchBtn;
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
    EditText zipCodeET;
    SharedPreferences sharedPrefs;
    Button startPlanningBtn;
    Uri newPlanUri;


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
        if (TextUtils.isEmpty(planNameET.getText().toString())) {
            rootView = inflater.inflate(R.layout.search_form, container, false);
            // provider form input
            zipCodeTV = (TextView) rootView.findViewById(R.id.zip_code);
            zipCodeET = (EditText) rootView.findViewById(R.id.search_form_zipcode_et);

            //obits form input
            firstNameTV = (TextView) rootView.findViewById(R.id.first_name);
            firstNameET = (EditText) rootView.findViewById(R.id.search_form_first_name_et);

            lastNameTV = (TextView) rootView.findViewById(R.id.last_name);
            lastNameET = (EditText) rootView.findViewById(R.id.search_form_last_name_et);

            locSvcsView = (TextView) rootView.findViewById(R.id.location_svcs_readout);
            useCurrentLocationTV = (TextView) rootView.findViewById(R.id.location_svcs_readout);
            startSearchBtn = (Button) rootView.findViewById(R.id.start_search_btn);

        } else {
            rootView = inflater.inflate(R.layout.activity_complete_form, container, false);
            planTypeSpinner = (AppCompatSpinner) rootView.findViewById(R.id.planTypeSpinner);
            planNameET = (EditText) rootView.findViewById(R.id.plan_form_plan_name_et);
            zipCodeET = (EditText) rootView.findViewById(R.id.plan_form_zip_code_et);
            startPlanningBtn = (Button) rootView.findViewById(R.id.start_planning_btn);

            /**
             * Type of plan, possible valid values are in the UserSelectionContract.java file:
             * {@link UserSelectionContract.PlanEntry#BURIAL}, {@link UserSelectionContract.PlanEntry#CREMATION}, or
             * {@link UserSelectionContract.PlanEntry#UNDECIDED}.
             * Default value is UNDECIDED
             */

            planType = UserSelectionContract.PlanEntry.UNDECIDED;
            populatePlanTypeSpinner();

            // grab inputs
            String planName = planNameET.getText().toString();
            // content values
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserSelectionContract.PlanEntry.COLUMN_PLAN_NAME, planName);
            contentValues.put(UserSelectionContract.PlanEntry.COLUMN_PLAN_TYPE, planType);
            // insert in DB
            newPlanUri = getActivity().getContentResolver().insert(UserSelectionContract.PlanEntry.CONTENT_URI, contentValues);
            if (newPlanUri != null) {
                Toast.makeText(getContext(), getString(R.string.plan_entry_creation_successful), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.error_creating_plan), Toast.LENGTH_LONG).show();
            }

            // initialize isUsingCurrentLocation to its default value of false
            isUsingCurrentLocation = false;

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.location_svcs_readout:
                            isUsingCurrentLocation = true;
                            break;
                        case R.id.start_search_btn:
                            startSearch();
                            break;
                        case R.id.start_planning_btn:
                            startPlanning();
                    }
                }
            };

            startSearchBtn.setOnClickListener(clickListener);
            locSvcsView.setOnClickListener(clickListener);
            startPlanningBtn.setOnClickListener(clickListener);
        }

        return rootView;

    }

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
                String selection = (String) parent.getItemAtPosition(position);
                if (selection.equals(getString(R.string.cremation))) {
                    planType = UserSelectionContract.PlanEntry.CREMATION;
                } else if (selection.equals(getString(R.string.burial))) {
                    planType = UserSelectionContract.PlanEntry.BURIAL;
                } else {
                    planType = UserSelectionContract.PlanEntry.UNDECIDED;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                planType = UserSelectionContract.PlanEntry.UNDECIDED;
            }
        });
    }

    private void startSearch() {
        Timber.d("startSearch:");

        // first check if valid entries are present; button functionality can only be activated
        // if this is true
        validEntriesArePresent = checkValidEntriesPresent();
        Double queryLocationLatExtra;
        Double queryLocationLongExtra;

        if (!validEntriesArePresent) {
            showInvalidEntriesDialog();
        } else {
            if (isUsingCurrentLocation) {
                queryType = "provider";
            } else {
                boolean zipCodeIsEmpty = Utility.entryIsEmpty(zipCodeET);
                if (zipCodeIsEmpty) {
                    queryType = "obituaries";
                }
            }
        }

        if (zipCodeET != null) {
            promptUserToSaveZipCodeEntry();
        }
        Intent getSearchResults = new Intent(getActivity(), SearchResultActivity.class);
        getSearchResults.putExtra("query type", queryType);

        Timber.d("completesearch: queryType: " + queryType);
        if (currentLocation != null && isUsingCurrentLocation) {
            queryLocationLatExtra = currentLocation.getLatitude();
            queryLocationLongExtra = currentLocation.getLongitude();
            getSearchResults.putExtra("current location lat", queryLocationLatExtra);
            getSearchResults.putExtra("current location long", queryLocationLongExtra);
        } else if (!isUsingCurrentLocation && zipCodeET != null) {
            getSearchResults.putExtra("zipCode", zipCodeET.getText().toString());
        } else {
            Timber.d("first name", firstNameET.getText().toString());

            getSearchResults.putExtra("first name", "john");
            getSearchResults.putExtra("last name", "smith");
        }
        startActivity(getSearchResults);
    }

    private void startPlanning() {
        Intent beginPlanningIntent = new Intent(getActivity(), PlanViewPagerActivity.class);
        beginPlanningIntent.setData(newPlanUri);
        startActivity(beginPlanningIntent);
    }

    public void promptUserToSaveZipCodeEntry() {
        showSaveZipCodeEntryDialog();

    }

    private void showSaveZipCodeEntryDialog() {
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(getContext());
//        ButterKnife.bind(this);
        deleteConfADBuilder.setMessage(getString(R.string.save_entries_dialog_msg));

        // positive button=yes, save entry to DB or sharedprefs as applicable
        DialogInterface.OnClickListener yesButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveZipCodeEntry();
            }
        };

        // negative button=no, keep editing, dismiss dialog
        DialogInterface.OnClickListener noButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    return;
                }
            }
        };

        String yesString = getString(R.string.dialog_yes_btn);
        String noString = getString(R.string.dialog_no_btn);
        deleteConfADBuilder.setPositiveButton(yesString, yesButtonListener);
        deleteConfADBuilder.setNegativeButton(noString, noButtonListener);

    }

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

    private void saveZipCodeEntry() {
        if (!Utility.entryIsEmpty(zipCodeET)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.pref_zip_code_key), zipCodeET.getText().toString());
            editor.commit();

        }
        // provider name is saved in the search result screen
    }

    private boolean checkValidEntriesPresent() {
        Timber.d("checkValidEntriesArePresent:");
        boolean entryIsEmpty;
        // initially all edit text fields are empty; if counter is still three after all are checked,
        // then the method returns false for "areValidEntriesPresent" and the start search functionality
        // cannot be initialized

        int emptyViewCounter = 3;
        ArrayList<EditText> viewsToValidateList = new ArrayList<EditText>();
        viewsToValidateList.add(firstNameET);
        viewsToValidateList.add(lastNameET);
        viewsToValidateList.add(zipCodeET);
        Timber.d("viewsToValidateList: " + viewsToValidateList);

        for (int index = 0; index < viewsToValidateList.size(); index++) {
            entryIsEmpty = Utility.entryIsEmpty(viewsToValidateList.get(index));
            if (!entryIsEmpty) {
                emptyViewCounter--;
            }
        }

        if (emptyViewCounter < 3 || isUsingCurrentLocation) {
            return true;
        } else {
            return false;
        }
    }

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
