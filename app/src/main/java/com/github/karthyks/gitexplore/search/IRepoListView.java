package com.github.karthyks.gitexplore.search;

import com.github.karthyks.gitexplore.model.Repository;

import java.util.List;

public interface IRepoListView {

    void showRepositories(List<Repository> repositories, boolean reset);

    RepositoryListActivity getHostingActivity();
}
