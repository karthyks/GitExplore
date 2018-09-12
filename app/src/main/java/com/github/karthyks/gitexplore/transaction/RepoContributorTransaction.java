package com.github.karthyks.gitexplore.transaction;

import android.util.Log;

import com.github.karthyks.gitexplore.model.Contributor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class RepoContributorTransaction extends GithubTransaction<String, List<Contributor>> {

    public RepoContributorTransaction(FirebaseAuth firebaseAuth) {
        super(firebaseAuth);
    }

    @Override
    public void execute(String... params) throws IOException {
        super.execute(params);
        String path1 = params[0].contains("/") ? params[0].split("/")[0] : params[0];
        String path2 = params[0].contains("/") ? params[0].split("/")[1] : "";
        httpUrl = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("repos")
                .addPathSegment(path1)
                .addPathSegment(path2)
                .addPathSegment("contributors")
                .addQueryParameter("anon", "1")
                .build();
        Log.d(TAG, "execute: " + params[0]);
        Log.d(TAG, "execute:ContriURL " + httpUrl.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization", accessToken)
                .url(httpUrl)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        JsonElement jsonResponse = new JsonParser().parse(res);
        Log.d(TAG, "execute: " + jsonResponse);
    }
}
