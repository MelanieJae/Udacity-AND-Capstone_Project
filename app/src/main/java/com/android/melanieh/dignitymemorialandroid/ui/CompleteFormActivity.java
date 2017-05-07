package com.android.melanieh.dignitymemorialandroid.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.melanieh.dignitymemorialandroid.R;
import com.android.melanieh.dignitymemorialandroid.data.UserSelectionContract.PlanEntry;

import timber.log.Timber;

public class CompleteFormActivity extends AppCompatActivity {

    AppCompatSpinner planTypeSpinner;
    int planType;
    String zipCode;
    EditText planNameET;
    EditText zipCodeET;
    SharedPreferences sharedPrefs;
    Button startPlanningBtn;
    Uri newPlanUri;
    CompleteFormFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_form);
//        String formType = getIntent().getStringExtra("button_extra_content");
//        Timber.d("formType: " + formType);

//        if (savedInstanceState == null) {
//            Bundle args = new Bundle();
//            args.putString("formType", formType);
//            fragment = new CompleteFormFragment();
//            fragment.setArguments(args);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.formFragment, fragment);
//            fragmentTransaction.commit();
//        } else {
//            fragment = (CompleteFormFragment) getSupportFragmentManager()
//                    .findFragmentByTag("formFragment");
//        }

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, "formFragment", fragment);
//    }


}
