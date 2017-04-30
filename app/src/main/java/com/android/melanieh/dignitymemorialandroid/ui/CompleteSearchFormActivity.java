package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import timber.log.Timber;

public class CompleteSearchFormActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView firstNameTV;
    TextView lastNameTV;
    EditText zipCodeET;
    EditText firstNameET;
    EditText lastNameET;
    TextView zipCodeTV;
    TextView useCurrentLocationTV;
    Button startSearchBtn;
    String queryType;
    String zipCode;
    String firstName;
    String lastName;
    String searchParams[];
    LinearLayout obitsFormLayout;
    LinearLayout providerFormLayout;
    TextView locSvcsView;
    boolean isUsingCurrentLocation;
    boolean validEntriesArePresent;
    GoogleApiClient googleApiClient;
    Location currentLocation;
    LocationRequest locationRequest;
    Preference zipCodePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_search_form);

//        /* Location services Api client */
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

        // views
        obitsFormLayout = (LinearLayout) findViewById(R.id.obits_search_form_layout);
        providerFormLayout = (LinearLayout) findViewById(R.id.provider_search_form_layout);
        locSvcsView = (TextView) findViewById(R.id.location_services_readout);
        useCurrentLocationTV = (TextView) findViewById(R.id.location_services_readout);
        startSearchBtn = (Button) findViewById(R.id.start_search_btn);

        // provider form input
        zipCodeTV = (TextView) findViewById(R.id.zip_code);
        zipCodeET = (EditText) findViewById(R.id.search_form_zipcode_et);

        //obits form input
        firstNameTV = (TextView) findViewById(R.id.first_name);
        firstNameET = (EditText) findViewById(R.id.search_form_first_name_et);

        lastNameTV = (TextView) findViewById(R.id.last_name);
        lastNameET = (EditText) findViewById(R.id.search_form_last_name_et);

        // initialize isUsingCurrentLocation to its default value of false
        isUsingCurrentLocation = false;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.location_services_readout:
                        isUsingCurrentLocation = true;
                        break;
                    case R.id.start_search_btn:
                        startSearch();
                        break;
                }
            }
        };

        startSearchBtn.setOnClickListener(clickListener);
        locSvcsView.setOnClickListener(clickListener);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
////        googleApiClient.connect();
//
//    }

    @Override
    protected void onStop() {
//        googleApiClient.disconnect();
        super.onStop();
    }

    private boolean checkValidEntriesPresent() {
            boolean entryIsEmpty;
            // initially all edit text fields are empty; if counter is still three after all are checked,
            // then the method returns false for "areValidEntriesPresent" and the start search functionality
            // cannot be initialized

            int emptyViewCounter = 3;
            ArrayList<EditText> viewsToValidateList = new ArrayList<EditText>();
            viewsToValidateList.add(firstNameET);
            viewsToValidateList.add(lastNameET);
            viewsToValidateList.add(zipCodeET);
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

        private void startSearch() {
            // first check if valid entries are present; button functionality can only be activated if this is true
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
            Intent getSearchResults = new Intent(this, SearchResultActivity.class);
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
                getSearchResults.putExtra("first name", firstNameET.getText().toString());
                getSearchResults.putExtra("last name", lastNameET.getText().toString());
            }
            startActivity(getSearchResults);
        }

        public void promptUserToSaveZipCodeEntry() {
            showSaveZipCodeEntryDialog();

        }

    private void showSaveZipCodeEntryDialog() {
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(this);
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
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(this);
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
//        DialogInterface.OnClickListener noButtonListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (dialogInterface != null) {
//                    return;
//                }
//            }
//        };

        String okString = getString(R.string.dialog_ok_btn);
        deleteConfADBuilder.setPositiveButton(okString, yesButtonListener);
    }

    private void saveZipCodeEntry() {
        if (!Utility.entryIsEmpty(zipCodeET)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.pref_zip_code_key), zipCodeET.getText().toString());
            editor.commit();

        }
        // provider name is saved in the search result screen
    }

    /** Location services methods **/
    // using coarse filter and low power accuracy (city level, within 10km) for obit and provider searches since it's the zip code
    // or lat/long that is being used and accuracy within 100 m is not necessary
    // if doing the navigation to plot, this would need HIGH_ACCURACY and FINE_LOCATION settings
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Google API Client is connected");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // millisecs
        try {
            PendingResult<Status> statusPendingResult =
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            //TODO check permission; requires runtime permission grants that can be rejected by user and denial must be handled here
        } catch (SecurityException e) {
            Timber.wtf(e, "Security Exception: user has not granted all necessary permissions");
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Google API Client connection has been suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Google API Client connection has failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("location string: " + location.toString());
        locSvcsView.setText(location.toString());
        // TODO: parse lat/lng values from location and append to search queries
    }

}











