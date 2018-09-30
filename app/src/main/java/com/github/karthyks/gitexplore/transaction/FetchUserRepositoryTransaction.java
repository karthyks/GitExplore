package com.github.karthyks.gitexplore.transaction;

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

public class FetchUserRepositoryTransaction extends GithubTransaction<String, RepositoryPage> {

    public FetchUserRepositoryTransaction(String accessToken) {
        super(accessToken);
    }

    @Override
    public void execute(String... params) throws IOException {
        super.execute(params);
        httpUrl = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("users")
                .addPathSegment(params[0])
                .addPathSegment("repos")
                .build();
        Request request = getRequestBuilder()
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        JsonElement jsonResponse = new JsonParser().parse(res);
        GsonBuilder builder = new GsonBuilder();
        Type type = new TypeToken<List<Repository>>() { }.getType();
        builder.registerTypeAdapter(type, new RepositoryListDeserializer());
        RepositoryPage repositoryPage = new RepositoryPage();
        repositoryPage.repositories = builder.create().fromJson(jsonResponse, type);
        repositoryPage.pageLink = PageLink.fromLinkHeader(response.header("Link"));
        result = repositoryPage;
    }
}
