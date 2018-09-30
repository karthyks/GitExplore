package com.github.karthyks.gitexplore.login;

import com.github.karthyks.gitexplore.account.IUser;

public interface ILoginVerify {

    void verify();

    void onSuccessfulLogin(IUser user);

    void onLoginError();
}
