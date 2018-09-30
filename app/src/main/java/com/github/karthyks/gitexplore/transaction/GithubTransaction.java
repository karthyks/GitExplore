package com.github.karthyks.gitexplore.transaction;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @param <P> Parameter type
 * @param <R> Return type
 */
public abstract class GithubTransaction<P, R> extends Transaction<P, R> {
    static final String BASE_URL = "https://api.github.com";

    HttpUrl httpUrl = HttpUrl.parse(BASE_URL);
    String accessToken;

    public GithubTransaction(String accessToken) {
        this.accessToken = accessToken;
    }

    public Request.Builder getRequestBuilder() {
        return new Request.Builder()
                .addHeader("Authorization", "token " + accessToken)
                .url(httpUrl);
    }
}
