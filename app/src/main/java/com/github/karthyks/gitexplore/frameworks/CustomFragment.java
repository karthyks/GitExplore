package com.github.karthyks.gitexplore.frameworks;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class CustomFragment<A extends AppCompatActivity> extends Fragment {

    public A getHostingActivity() {
        return (A) getActivity();
    }
}
