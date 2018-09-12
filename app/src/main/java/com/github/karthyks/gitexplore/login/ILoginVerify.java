package com.github.karthyks.gitexplore.login;

public interface ILoginVerify {

    void verify();

    void onSuccessfulLogin();

    void onLoginError();
}
