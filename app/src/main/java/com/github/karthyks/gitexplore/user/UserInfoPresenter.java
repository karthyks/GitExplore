package com.github.karthyks.gitexplore.user;

import android.os.AsyncTask;

import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.github.karthyks.gitexplore.search.ISearchListener;
import com.github.karthyks.gitexplore.transaction.FetchUserRepositoryTransaction;

import java.io.IOException;

public class UserInfoPresenter implements IUserInfoPresenter {

    private IUserInfoView userInfoView;
    private FetchUserRepoTask fetchUserRepoTask;

    public UserInfoPresenter(IUserInfoView userInfoView) {
        this.userInfoView = userInfoView;
    }

    @Override
    public void fetchRepositories(FetchUserRepositoryTransaction transaction, String user) {
        if (fetchUserRepoTask != null) fetchUserRepoTask.cancel(true);
        fetchUserRepoTask = new FetchUserRepoTask(transaction);
        fetchUserRepoTask.setOnDownloadListener(new ISearchListener() {
            @Override
            public void onSearchResult(RepositoryPage repositoryPage) {
                userInfoView.renderRepositories(repositoryPage);
            }

            @Override
            public void onErrorSearch() {
                userInfoView.getHostingActivity().showMessage("Error fetching repositories!");
            }
        });
        fetchUserRepoTask.execute(user);
    }


    private class FetchUserRepoTask extends AsyncTask<String, Void, RepositoryPage> {

        private FetchUserRepositoryTransaction transaction;
        private ISearchListener searchListener;

        public FetchUserRepoTask(FetchUserRepositoryTransaction transaction) {
            this.transaction = transaction;
        }

        public void setOnDownloadListener(ISearchListener searchListener) {
            this.searchListener = searchListener;
        }

        @Override
        protected RepositoryPage doInBackground(String... strings) {
            try {
                transaction.execute(strings);
                return transaction.retrieveResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RepositoryPage repositoryPage) {
            super.onPostExecute(repositoryPage);
            if (searchListener != null) {
                if (repositoryPage != null) {
                    searchListener.onSearchResult(repositoryPage);
                } else {
                    searchListener.onErrorSearch();
                }
            }
        }
    }
}
