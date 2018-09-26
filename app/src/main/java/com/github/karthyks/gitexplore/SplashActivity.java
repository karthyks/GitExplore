package com.github.karthyks.gitexplore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.karthyks.gitexplore.login.ILoginVerify;
import com.github.karthyks.gitexplore.login.LoginActivity;
import com.github.karthyks.gitexplore.search.RepositoryListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GithubAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements ILoginVerify {
    public static final String ACCESS_TOKEN = "access_token";
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Handler accountVerifier;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
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
        if (getIntent().getExtras() != null) {
            String accessToken = getIntent().getExtras().getString(ACCESS_TOKEN, "");
            Log.d(TAG, "verify: " + accessToken);
            if (!accessToken.isEmpty()) {
                mAuth.signInWithCredential(GithubAuthProvider.getCredential(accessToken))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    showMessage("Login Successful");
                                    onSuccessfulLogin();
                                } else if (task.isCanceled()) {
                                    showMessage("Login canceled");
                                    onLoginError();
                                } else {
                                    showMessage("Login Error");
                                    onLoginError();
                                }
                            }
                        });
            } else {
                checkForActiveUser();
            }
        } else {
            checkForActiveUser();
        }
    }

    private void checkForActiveUser() {
        if (mAuth.getCurrentUser() == null) {
            onLoginError();
        } else {
            onSuccessfulLogin();
        }
    }

    private void showMessage(String message) {
        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfulLogin() {
        if (mAuth.getCurrentUser() != null)
            showMessage("Logged In " + mAuth.getCurrentUser().getDisplayName());
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
