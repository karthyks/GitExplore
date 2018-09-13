package com.github.karthyks.gitexplore.user;

import com.github.karthyks.gitexplore.model.Contributor;
import com.github.karthyks.gitexplore.model.RepositoryPage;

public interface IUserInfoView {
    void renderRepositories(RepositoryPage repositoryPage);

    UserInfoActivity getHostingActivity();

    void renderUserInfo(Contributor contributor);
}
