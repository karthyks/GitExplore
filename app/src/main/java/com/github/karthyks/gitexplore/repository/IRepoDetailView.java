package com.github.karthyks.gitexplore.repository;

import com.github.karthyks.gitexplore.model.Contributor;
import com.github.karthyks.gitexplore.model.Repository;

import java.util.List;

public interface IRepoDetailView {

    RepoDetailActivity getHostingActivity();

    void renderRepository(Repository repository);

    void renderContributors(List<Contributor> contributors);

    void openWeb(String url);
}
