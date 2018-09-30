package com.github.karthyks.gitexplore.transaction;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;

/**
 * @param <P> Parameter type
 * @param <R> Return type
 */
public abstract class Transaction<P, R> {
    public static final String TAG = Transaction.class.getSimpleName();
    public static final OkHttpClient client = new OkHttpClient();
    public static final Gson gson = new Gson();

    R result;

    public void execute(P... params) throws IOException {
        Log.d(TAG, "execute: Not implemented");
    }

    public R retrieveResult() {
        return result;
    }
}
