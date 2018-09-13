package com.github.karthyks.gitexplore.repository;

import com.github.karthyks.gitexplore.model.Contributor;

import java.util.List;

public interface IContributorDownloadListener {

    void onContributorsAvailable(List<Contributor> contributors);

    void onErrorFetching();
}
