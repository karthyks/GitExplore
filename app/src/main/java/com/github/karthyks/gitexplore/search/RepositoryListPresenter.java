package com.github.karthyks.gitexplore.search;

import android.os.AsyncTask;
import android.util.Log;

import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.github.karthyks.gitexplore.transaction.SearchRepoTransaction;

import java.io.IOException;

public class RepositoryListPresenter implements IRepositoryListPresenter {

    private SearchRepoTask repoTask;
    private SearchRepoTransaction searchRepoTransaction;

    public RepositoryListPresenter(SearchRepoTransaction searchRepoTransaction) {
        this.searchRepoTransaction = searchRepoTransaction;
    }

    @Override
    public void onSearchRepository(String query, ISearchListener listener) {
        if (repoTask != null) repoTask.cancel(true);
        repoTask = new SearchRepoTask(searchRepoTransaction);
        if (listener != null) {
            repoTask.setSearchListener(listener);
        }
        repoTask.execute(query);
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
                transaction.execute(strings[0]);
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
