package com.github.karthyks.gitexplore.transaction;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

import okhttp3.HttpUrl;

/**
 * @param <P> Parameter type
 * @param <R> Return type
 */
public abstract class GithubTransaction<P, R> extends Transaction<P, R> {
    static final String BASE_URL = "https://api.github.com";

    HttpUrl httpUrl = HttpUrl.parse(BASE_URL);
    String accessToken;

    public GithubTransaction(FirebaseAuth firebaseAuth) {
        firebaseAuth.getAccessToken(false)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        accessToken = task.getResult().getToken();
                    }
                });
    }

    @Override
    public void execute(P... params) throws IOException {
        if (accessToken == null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
