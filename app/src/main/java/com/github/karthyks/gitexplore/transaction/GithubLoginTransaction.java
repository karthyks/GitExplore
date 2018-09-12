package com.github.karthyks.gitexplore.transaction;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CALLBACK_URI;
import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CLIENT_ID;
import static com.github.karthyks.gitexplore.BuildConfig.GITHUB_CLIENT_SECRET;

public class GithubLoginTransaction extends Transaction<String, String> {
    private static final String LOGIN_URL = "https://github.com/login/oauth/access_token";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = GithubLoginTransaction.class.getSimpleName();

    @Override
    public void execute(String... params) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(LOGIN_URL).newBuilder()
                .addQueryParameter("client_id", GITHUB_CLIENT_ID)
                .addQueryParameter("client_secret", GITHUB_CLIENT_SECRET)
                .addQueryParameter("code", params[0])
                .addQueryParameter("redirect_uri", GITHUB_CALLBACK_URI).build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(JSON, ""))
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        response.close();
        Log.d(TAG, "execute: " + result);
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        result = jsonObject.get("access_token").getAsString();
    }
}
