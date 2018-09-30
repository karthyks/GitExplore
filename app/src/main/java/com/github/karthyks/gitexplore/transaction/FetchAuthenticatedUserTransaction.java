package com.github.karthyks.gitexplore.transaction;

import com.github.karthyks.gitexplore.model.GitUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.HttpUrl;
import okhttp3.Response;

public class FetchAuthenticatedUserTransaction extends GithubTransaction<Void, GitUser> {

    public FetchAuthenticatedUserTransaction(String accessToken) {
        super(accessToken);
    }

    @Override
    public void execute(Void... params) throws IOException {
        httpUrl = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("user")
                .build();
        Response response = client.newCall(getRequestBuilder().get().build()).execute();
        String res = response.body().string();
        JsonElement jsonElement = new JsonParser().parse(res);

        GitUser gitUser = new GitUser();
        gitUser.setAuthToken(accessToken);
        gitUser.setUsername(jsonElement.getAsJsonObject().get("login").getAsString());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        gitUser.setExpiryTime(calendar.getTimeInMillis());
        result = gitUser;
    }
}
