package com.github.karthyks.gitexplore.login;

public interface ILoginListener {

    void onSuccess(String accessToken);

    void onFailure();
}
