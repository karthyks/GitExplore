package com.github.karthyks.gitexplore.repository;

import android.os.AsyncTask;

import com.github.karthyks.gitexplore.model.Contributor;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.transaction.RepoContributorTransaction;

import java.io.IOException;
import java.util.List;

public class RepoDetailPresenter implements IRepoDetailPresenter {

    private IRepoDetailView repoDetailView;
    private FetchContributorTask fetchContributorTask;
    private Repository repository;

    public RepoDetailPresenter(IRepoDetailView repoDetailView) {
        this.repoDetailView = repoDetailView;
    }

    @Override
    public void loadRepository(Repository repository) {
        if (repository == null) {
            repoDetailView.getHostingActivity().finish();
        } else {
            this.repository = repository;
            repoDetailView.renderRepository(repository);
        }
    }

    @Override
    public void fetchContributors(RepoContributorTransaction repoContributorTransaction) {
        if (fetchContributorTask != null) fetchContributorTask.cancel(true);
        fetchContributorTask = new FetchContributorTask(repoContributorTransaction);
        fetchContributorTask.setListener(new IContributorDownloadListener() {
            @Override
            public void onContributorsAvailable(List<Contributor> contributors) {
                repoDetailView.renderContributors(contributors);
            }

            @Override
            public void onErrorFetching() {
                repoDetailView.getHostingActivity().showMessage("Something went wrong!");
            }
        });
        fetchContributorTask.execute(repository.getFullName());
    }

    @Override
    public void onRepoLinkClick() {
        repoDetailView.openWeb(repository.getHttpUrl());
    }

    private static class FetchContributorTask extends AsyncTask<String, Void, List<Contributor>> {

        private RepoContributorTransaction transaction;
        private IContributorDownloadListener listener;

        FetchContributorTask(RepoContributorTransaction transaction) {
            this.transaction = transaction;
        }

        void setListener(IContributorDownloadListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Contributor> doInBackground(String... strings) {
            try {
                transaction.execute(strings);
                return transaction.retrieveResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Contributor> contributors) {
            super.onPostExecute(contributors);
            if (listener != null) {
                if (contributors != null) {
                    listener.onContributorsAvailable(contributors);
                } else {
                    listener.onErrorFetching();
                }
            }
        }
    }
}
