package com.github.karthyks.gitexplore.login;

public interface ILoginView {

    void renderLogin();

    void renderGithubWeb();

    void showProgressLogin(boolean show);
}
