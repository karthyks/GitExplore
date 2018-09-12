package com.github.karthyks.gitexplore.search;

import android.support.annotation.Nullable;

public interface IRepositoryListPresenter {

    void onSearchRepository(String query, @Nullable ISearchListener searchListener);
}
