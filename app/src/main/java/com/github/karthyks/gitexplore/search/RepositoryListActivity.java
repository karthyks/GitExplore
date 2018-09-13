package com.github.karthyks.gitexplore.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.frameworks.CustomActivity;
import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.github.karthyks.gitexplore.transaction.SearchRepoTransaction;
import com.google.firebase.auth.FirebaseAuth;

public class RepositoryListActivity extends CustomActivity {

    private static final String TAG = RepositoryListActivity.class.getSimpleName();

    private IRepositoryListPresenter listPresenter;

    private RepoListAdapter repoListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listPresenter = new RepositoryListPresenter(
                new SearchRepoTransaction(FirebaseAuth.getInstance()));
        RecyclerView rvRepoList = findViewById(R.id.rv_repo_list);
        rvRepoList.setLayoutManager(new LinearLayoutManager(this));
        repoListAdapter = new RepoListAdapter(LayoutInflater.from(this), null);
        rvRepoList.setAdapter(repoListAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showProgress("Please wait...");
            listPresenter.onSearchRepository(query, new ISearchListener() {
                @Override
                public void onSearchResult(RepositoryPage repositoryPage) {
                    repoListAdapter.swapItems(repositoryPage);
                    dismissProgress();
                }
            });
        }
    }
}
