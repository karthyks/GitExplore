package com.github.karthyks.gitexplore.search;

import android.os.AsyncTask;
import android.util.Log;

import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.github.karthyks.gitexplore.transaction.SearchRepoTransaction;

import java.io.IOException;

public class RepositoryListPresenter implements IRepositoryListPresenter, ISearchListener {

    private SearchRepoTask repoTask;
    private IRepoListView repoListView;
    private SearchRepoTransaction searchRepoTransaction;

    private RepositoryPage lastRepoPage;
    private String lastQuery;
    private boolean resetList = true;

    public RepositoryListPresenter(IRepoListView repoListView,
                                   SearchRepoTransaction searchRepoTransaction) {
        this.repoListView = repoListView;
        this.searchRepoTransaction = searchRepoTransaction;
    }

    @Override
    public void onSearchRepository(String... query) {
        if (repoTask != null) repoTask.cancel(true);
        repoListView.getHostingActivity().showProgress("Please wait...");
        repoTask = new SearchRepoTask(searchRepoTransaction);
        repoTask.setSearchListener(this);
        if (query == null || query.length <= 0) {
            if (lastRepoPage.pageLink.hasNext) {
                resetList = false;
                repoTask.execute(lastQuery, "" + (lastRepoPage.pageLink.currentPageIndex + 1));
            } else {
                repoListView.getHostingActivity().dismissProgress();
            }
        } else {
            lastQuery = query[0];
            repoTask.execute(lastQuery);
            resetList = true;
        }
    }

    @Override
    public void onSearchResult(RepositoryPage repositoryPage) {
        repoListView.showRepositories(repositoryPage.repositories, resetList);
        lastRepoPage = repositoryPage;
    }

    private static class SearchRepoTask extends AsyncTask<String, Void, RepositoryPage> {

        private static final String TAG = SearchRepoTask.class.getSimpleName();
        private ISearchListener listener;
        private SearchRepoTransaction transaction;

        public SearchRepoTask(SearchRepoTransaction transaction) {
            this.transaction = transaction;
        }

        public void setSearchListener(ISearchListener listener) {
            this.listener = listener;
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
            if (listener != null && repositoryPage != null && repositoryPage.repositories != null) {
                Log.d(TAG, "onPostExecute:hasNext " + repositoryPage.pageLink.hasNext);
                listener.onSearchResult(repositoryPage);
            }
        }
    }
}
