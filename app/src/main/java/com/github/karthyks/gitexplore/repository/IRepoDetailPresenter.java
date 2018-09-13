package com.github.karthyks.gitexplore.repository;

import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.transaction.RepoContributorTransaction;

public interface IRepoDetailPresenter {

    void loadRepository(Repository repository);

    void fetchContributors(RepoContributorTransaction repoContributorTransaction);

    void onRepoLinkClick();
}