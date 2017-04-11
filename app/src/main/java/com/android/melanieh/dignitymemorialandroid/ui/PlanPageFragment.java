package com.android.melanieh.dignitymemorialandroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.melanieh.dignitymemorialandroid.R;

/**
 * Created by melanieh on 4/11/17.
 */

public class PlanPageFragment extends Fragment {

    public PlanPageFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_plan_viewpager, container, false);

        return rootView;
    }
}
