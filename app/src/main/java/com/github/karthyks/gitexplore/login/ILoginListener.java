package com.github.karthyks.gitexplore.login;

import com.github.karthyks.gitexplore.model.GitUser;

public interface ILoginListener {

    void onSuccess(GitUser gitUser);

    void onFailure();
}
