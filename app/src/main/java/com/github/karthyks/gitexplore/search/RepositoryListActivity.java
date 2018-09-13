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
import com.github.karthyks.gitexplore.frameworks.EndlessRecyclerViewScrollListener;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.transaction.SearchRepoTransaction;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RepositoryListActivity extends CustomActivity implements IRepoListView {

    private static final String TAG = RepositoryListActivity.class.getSimpleName();

    private IRepositoryListPresenter listPresenter;

    private RepoListAdapter repoListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listPresenter = new RepositoryListPresenter(this,
                new SearchRepoTransaction(FirebaseAuth.getInstance()));
        RecyclerView rvRepoList = findViewById(R.id.rv_repo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRepoList.setLayoutManager(linearLayoutManager);
        repoListAdapter = new RepoListAdapter(LayoutInflater.from(this), null);
        rvRepoList.setAdapter(repoListAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listPresenter.onSearchRepository();
            }
        };
        rvRepoList.addOnScrollListener(scrollListener);
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
            listPresenter.onSearchRepository(query);
        }
    }

    @Override
    public void showRepositories(List<Repository> repositories, boolean reset) {
        dismissProgress();
        if (reset) {
            scrollListener.resetState();
            repoListAdapter.swapItems(repositories);
        } else {
            repoListAdapter.appendItems(repositories);
        }
    }

    @Override
    public RepositoryListActivity getHostingActivity() {
        return this;
    }
}
