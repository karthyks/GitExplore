package com.github.karthyks.gitexplore.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.SplashActivity;
import com.github.karthyks.gitexplore.account.AppSession;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Fragment pendingFragment = null;
    private View progressLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressLogin = findViewById(R.id.progress_login);
        renderLogin();
        Uri appLinkData = getIntent().getData();
        Log.d(TAG, "onCreate: " + appLinkData);
        if (AppSession.get(this).hasActiveAccount()) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
    }

    @Override
    public void renderLogin() {
        renderFragment(LoginFragment.getFragment());
        showProgressLogin(false);
    }

    @Override
    public void showProgressLogin(boolean show) {
        if (show) {
            progressLogin.setVisibility(View.VISIBLE);
        } else {
            progressLogin.setVisibility(View.GONE);
        }
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
