package com.github.karthyks.gitexplore.login;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.github.karthyks.gitexplore.R;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Fragment pendingFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        renderLogin();
        Uri appLinkData = getIntent().getData();
        Log.d(TAG, "onCreate: " + appLinkData);
    }

    @Override
    public void renderLogin() {
        renderFragment(LoginFragment.getFragment());
    }

    @Override
    public void renderGithubWeb() {
        renderFragment(GithubLoginFragment.getFragment());
    }

    private void renderFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layout_main, fragment).commit();
            pendingFragment = null;
        } catch (Exception e) {
            e.printStackTrace();
            pendingFragment = fragment;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pendingFragment != null) {
            renderFragment(pendingFragment);
        }
    }
}
