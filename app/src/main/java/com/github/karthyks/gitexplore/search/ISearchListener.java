package com.github.karthyks.gitexplore.search;

import com.github.karthyks.gitexplore.model.RepositoryPage;

public interface ISearchListener {

    void onSearchResult(RepositoryPage repositoryPage);
}
