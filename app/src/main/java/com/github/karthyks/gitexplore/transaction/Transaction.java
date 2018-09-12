package com.github.karthyks.gitexplore.transaction;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;

/**
 * @param <P> Parameter type
 * @param <R> Return type
 */
public abstract class Transaction<P, R> {
    public static final String TAG = Transaction.class.getSimpleName();
    public static final OkHttpClient client = new OkHttpClient();

    R result;

    public void execute(P... params) throws IOException {
        Log.d(TAG, "execute: Not implemented");
    }

    public R retrieveResult() {
        return result;
    }
}
