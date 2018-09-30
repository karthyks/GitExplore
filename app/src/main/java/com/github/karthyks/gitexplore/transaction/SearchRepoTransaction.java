package com.github.karthyks.gitexplore.transaction;

import android.util.Log;

import com.github.karthyks.gitexplore.deserializers.RepositoryListDeserializer;
import com.github.karthyks.gitexplore.model.PageLink;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class SearchRepoTransaction extends GithubTransaction<String, RepositoryPage> {
    private static final String TAG = SearchRepoTransaction.class.getSimpleName();
    private static final String PER_PAGE_LIMIT = "10";

    public SearchRepoTransaction(String accessToken) {
        super(accessToken);
    }

    @Override
    public void execute(String... params) throws IOException {
        super.execute(params);
        httpUrl = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("search")
                .addPathSegment("repositories")
                .addQueryParameter("q", params[0])
                .addQueryParameter("per_page", PER_PAGE_LIMIT)
                .addQueryParameter("page", params.length < 2 ? "1" : params[1])
                .addQueryParameter("sort", "stars")
                .build();
        Request request = getRequestBuilder()
                .addHeader("Accept", "application/vnd.github.mercy-preview+json")
                .url(httpUrl)
                .build();
        Log.d(TAG, "execute: " + httpUrl.toString());
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        JsonElement jsonResponse = new JsonParser().parse(res);
        GsonBuilder builder = new GsonBuilder();
        Type type = new TypeToken<List<Repository>>() { }.getType();
        builder.registerTypeAdapter(type, new RepositoryListDeserializer());
        RepositoryPage repositoryPage = new RepositoryPage();
        repositoryPage.repositories = builder.create().fromJson(jsonResponse, type);
       /* RepoContributorTransaction contributorTransaction = new RepoContributorTransaction(FirebaseAuth.getInstance());
        for (Repository repository : repositoryPage.repositories) {
            contributorTransaction.execute("" + repository.getFullName());
        }*/
        repositoryPage.pageLink = PageLink.fromLinkHeader(response.header("Link"));
        result = repositoryPage;
    }
}