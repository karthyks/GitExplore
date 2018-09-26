package com.github.karthyks.gitexplore.frameworks;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @param <A> Activity to which the fragment is attached.
 */
public abstract class CustomFragment<A extends AppCompatActivity> extends Fragment {

    public A getHostingActivity() {
        return (A) getActivity();
    }
}
