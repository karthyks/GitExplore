package com.github.karthyks.gitexplore;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.karthyks.gitexplore.account.AppSession;
import com.github.karthyks.gitexplore.account.AuthenticatorActivity;
import com.github.karthyks.gitexplore.account.IUser;
import com.github.karthyks.gitexplore.login.ILoginVerify;
import com.github.karthyks.gitexplore.login.LoginActivity;
import com.github.karthyks.gitexplore.model.GitUser;
import com.github.karthyks.gitexplore.search.RepositoryListActivity;

public class SplashActivity extends AuthenticatorActivity implements ILoginVerify {
    public static final String EXTRA_USER = "extra_user";
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Handler accountVerifier;
    private AppSession appSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSession = AppSession.get(this);
        setContentView(R.layout.activity_splash);
        accountVerifier = new Handler(getMainLooper());
    }

    private Runnable verifier = new Runnable() {
        @Override
        public void run() {
            verify();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        accountVerifier.postDelayed(verifier, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        accountVerifier.removeCallbacks(verifier);
    }

    @Override
    public void verify() {
        if (appSession.getActiveAccount() != null)
            Log.d(TAG, "verify: " + appSession.getActiveUser().getExpiryTime());
        else {
            Log.d(TAG, "verify: null account");
        }
        if (appSession.hasActiveAccount()) {
            launchApp();
        } else if (getIntent().getParcelableExtra(EXTRA_USER) != null) {
            GitUser gitUser = getIntent().getParcelableExtra(EXTRA_USER);
            onSuccessfulLogin(gitUser);
        } else {
            onLoginError();
        }
    }

    private void showMessage(String message) {
        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfulLogin(IUser user) {
        final Intent authResultIntent = new Intent();
        authResultIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME,
                String.valueOf(user.getUsername()));
        authResultIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AppSession.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(authResultIntent.getExtras());
        setResult(RESULT_OK, authResultIntent);
        launchApp();

    }

    private void launchApp() {
        Intent intent = new Intent(this, RepositoryListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
