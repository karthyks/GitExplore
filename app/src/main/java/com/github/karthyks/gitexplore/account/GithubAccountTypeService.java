package com.github.karthyks.gitexplore.account;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.karthyks.gitexplore.SplashActivity;

import androidx.annotation.Nullable;

public class GithubAccountTypeService extends Service {

    private static final String TAG = GithubAccountTypeService.class.getSimpleName();
    private AbstractAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        AppSession sessionManager = AppSession.get(this);
        Log.d(TAG, "onCreate: Service created");
        authenticator = new GithubAccountAuthenticator(this, sessionManager, SplashActivity.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
