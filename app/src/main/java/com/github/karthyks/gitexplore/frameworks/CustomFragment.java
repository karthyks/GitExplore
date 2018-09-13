package com.github.karthyks.gitexplore.frameworks;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * @param <A> Activity to which the fragment is attached.
 */
public abstract class CustomFragment<A extends AppCompatActivity> extends Fragment {

    public A getHostingActivity() {
        return (A) getActivity();
    }
}
