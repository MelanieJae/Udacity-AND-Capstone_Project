package com.android.melanieh.dignitymemorialandroid.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;
import com.android.melanieh.dignitymemorialandroid.R;

import com.android.melanieh.dignitymemorialandroid.Utility;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
    AutoCompleteTextView zipCodeACT;
    TextView useCurrentLocationTV;
    Button startObitSearchBtn;
    Button startProviderSearchBtn;
    String queryType;
    TextView locSvcsView;
    boolean isUsingCurrentLocation;
    boolean validEntriesArePresent;
    GoogleApiClient googleApiClient;
    Location location;
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
    String formType;
    String prefZipCode;
    GoogleApiAvailability googleApiAvailability;

    /** zip code autocomplete option(s) */
    private ArrayList<String> ZIP_CODE_AUTOCOMPLETE_OPTIONS = new ArrayList<>();
    private int REQUEST_LOCATION = 100;

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("savedInstanceState: " + savedInstanceState);
        setHasOptionsMenu(true);

        /* Location services Api client */
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // permissions request prior to connecting to api
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            googleApiClient.connect();
        }


        // initialize isUsingCurrentLocation to its default value of false
        isUsingCurrentLocation = false;

//        if (savedInstanceState == null) {
            formType = getActivity().getIntent().getStringExtra("button_extra_content");
//        } else {
//            formType = savedInstanceState.getString("formType");
//
//        }

        // populate zip code autocomplete dropdown with saved zip code
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefZipCode = sharedPrefs.getString(getString(R.string.pref_zip_code_key), "");
        ZIP_CODE_AUTOCOMPLETE_OPTIONS.add(prefZipCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView;
        TextView formHeadingView;

        if (formType.equalsIgnoreCase("Search Obituaries and Providers")) {
            rootView = inflater.inflate(R.layout.search_form, container, false);

            View.OnClickListener searchFormClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.use_current_location:
                            isUsingCurrentLocation = true;
                            googleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
                            break;
                        case R.id.start_obit_search_btn:
                            startSearch(v);
                    }
                }
            };
            // obits fields
            firstNameET = (EditText) rootView.findViewById(R.id.search_form_first_name_et);
            lastNameET = (EditText) rootView.findViewById(R.id.search_form_last_name_et);

            // form divider
            ImageView scrollDividerView = (ImageView) rootView.findViewById(R.id.form_divider);
            ImageHandler.getSharedInstance(getContext()).load(BuildConfig.SCROLL_DIVIDER_URL).
                    fit().centerInside().into(scrollDividerView);

            // provider fields
            ArrayAdapter<String> zipCodeACTAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, ZIP_CODE_AUTOCOMPLETE_OPTIONS);
            zipCodeACT = (AutoCompleteTextView) rootView.findViewById(R.id.search_form_zip_codes_act);
            zipCodeACT.setAdapter(zipCodeACTAdapter);
            useCurrentLocationTV = (TextView) rootView.findViewById(R.id.use_current_location);

            startObitSearchBtn = (Button) rootView.findViewById(R.id.start_obit_search_btn);
            startProviderSearchBtn = (Button) rootView.findViewById(R.id.start_provider_search_btn);

            startObitSearchBtn.setOnClickListener(searchFormClickListener);

            // A different listener is used here because:
            // 1. the provider search requires validation that the obit button does not; and
            // 2. the app has an option to save a zip code for later searching or locating
            // the nearest provider that is not available for the obit search

            startProviderSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(zipCodeACT.getText()) && !isUsingCurrentLocation) {
                        showInvalidEntriesDialog();
                    }

                    if (prefZipCode != null && !isUsingCurrentLocation) {
                        showSaveZipCodeEntryDialog(v);
                    }
                    startSearch(v);
                }
            });

            useCurrentLocationTV.setOnClickListener(searchFormClickListener);

        } else {
            rootView = inflater.inflate(R.layout.plan_form, container, false);
            planNameET = (EditText) rootView.findViewById(R.id.plan_form_plan_name_et);
            contactEmailET = (EditText) rootView.findViewById(R.id.plan_form_contact_email_et);
            startPlanningBtn = (Button) rootView.findViewById(R.id.start_planning_btn);

            startPlanningBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPlanning();
                }
            });

        }

        formHeadingView = (TextView)rootView.findViewById(R.id.form_heading);
//        if (formType.equalsIgnoreCase("Search Obituaries and Providers")) {
//            formHeadingView.setText(getResources().getString(R.string.search_form_heading));
//        } else {
//            formHeadingView.setText(getResources().getString(R.string.plan_form_heading));
//        }
        return rootView;

    }

    /** runtime permissions for ACCESS_FINE_LOCATION/ACCESS_COARSE_LOCATION **/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                googleApiClient.connect();
            } else {
                // Permission was denied or request was cancelled
                Timber.e("User did not grant permission to access current location");
            }
        }
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void startSearch(View view) {
        Timber.d("startSearch:");

        Intent getSearchResults = new Intent(getActivity(), SearchResultActivity.class);

        if (view == startObitSearchBtn) {
            queryType = "obituaries";
            if (!TextUtils.isEmpty(firstNameET.getText())) {
                getSearchResults.putExtra("first name", firstNameET.getText().toString());
            }
            if (!TextUtils.isEmpty(lastNameET.getText())) {
                getSearchResults.putExtra("last name", lastNameET.getText().toString());
            }
        } else {
            queryType = "providers";
            // hitting the obit button with blank fields yields all
            // obits published in the last two weeks so blank entries are okay.
            if (isUsingCurrentLocation && location != null) {
                getSearchResults.putExtra("current location", new String[]
                                {String.valueOf(location.getLatitude()),
                                        String.valueOf(location.getLongitude())});
                Timber.d("locationLat: " + String.valueOf(location.getLatitude()));
                Timber.d("locationLong: " + String.valueOf(location.getLongitude()));

            } else {
                    getSearchResults.putExtra("zipCode", zipCodeACT.getText().toString());
                }
            }

        Timber.d("completesearch: queryType: " + queryType);
        getSearchResults.putExtra("query type", queryType);
        startActivity(getSearchResults);
    }

    /**
     * Type of plan, possible valid values are in the UserSelectionContract.java file:
     * {@link UserSelectionContract.PlanEntry#BURIAL}, {@link UserSelectionContract.PlanEntry#CREMATION}, or
     * {@link UserSelectionContract.PlanEntry#UNDECIDED}.
     * Default value is UNDECIDED
     */

    private void startPlanning() {
        // store plan form inputs from user in plan DB
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanEntry.COLUMN_PLAN_NAME, planNameET.getText().toString());
        contentValues.put(PlanEntry.COLUMN_CONTACT_EMAIL, contactEmailET.getText().toString());
        Uri newPlanUri = getContext().getContentResolver().insert(PlanEntry.CONTENT_URI, contentValues);
        Timber.d("newPlanUri= " + newPlanUri);

        if (newPlanUri == null) {
            Toast.makeText(getContext(), "Error saving new plan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "New plan saved successfully", Toast.LENGTH_LONG).show();
        }

        Intent beginPlanningIntent = new Intent(getActivity(), PlanViewPagerActivity.class);
        beginPlanningIntent.setData(newPlanUri);
        startActivity(beginPlanningIntent);
    }

    private void showSaveZipCodeEntryDialog(final View view) {
        AlertDialog.Builder saveZipADBuilder = new AlertDialog.Builder(getContext());
//        ButterKnife.bind(this);
        saveZipADBuilder.setMessage(getString(R.string.save_zip_code_dialog_msg));

        // positive button=yes, save entry to DB or sharedprefs as applicable
        DialogInterface.OnClickListener yesButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveZipCodeEntry(view);
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
        saveZipADBuilder.setPositiveButton(yesString, yesButtonListener);
        saveZipADBuilder.setNegativeButton(noString, noButtonListener);
        saveZipADBuilder.create();
        saveZipADBuilder.show();
    }

    private void showInvalidEntriesDialog() {
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(getContext());
//        ButterKnife.bind(this);
        deleteConfADBuilder.setMessage(getString(R.string.no_valid_entries_present_error_msg));
        deleteConfADBuilder.create();
        deleteConfADBuilder.show();
    }

    private void saveZipCodeEntry(View view) {
        if (!TextUtils.isEmpty(zipCodeACT.getText())) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            ZIP_CODE_AUTOCOMPLETE_OPTIONS.add(zipCodeACT.getText().toString());
            editor.putString(getString(R.string.pref_zip_code_key), zipCodeACT.getText().toString());
            editor.commit();
        }

        // after saving to the settings is complete, the search is started
        startSearch(view);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("onConnected");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(5000); // millisecs, ideally greater than 1000
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest, this);
        } else {
            Timber.e("User did not grant permission to access current location");
        }
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
        Timber.d("onLocationChanged");
        Timber.d("location string: " + location.toString());
        this.location = location;
//        useCurrentLocationTV.setText(location.toString());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("formType", formType);
    }

}
