package com.github.karthyks.gitexplore.user;

import com.github.karthyks.gitexplore.transaction.FetchUserRepositoryTransaction;

public interface IUserInfoPresenter {

    void fetchRepositories(FetchUserRepositoryTransaction transaction, String user);
}
