package com.github.karthyks.gitexplore.repository;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.account.AppSession;
import com.github.karthyks.gitexplore.frameworks.CustomActivity;
import com.github.karthyks.gitexplore.model.Contributor;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.transaction.RepoContributorTransaction;
import com.github.karthyks.gitexplore.user.UserInfoActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RepoDetailActivity extends CustomActivity implements IRepoDetailView,
        ContributorListAdapter.OnContributorClickListener {
    public static final String EXTRA_REPOSITORY = "extra_repository";

    private RepoDetailViewHolder viewHolder;

    public static Intent getIntent(Repository repository, Context context) {
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    private IRepoDetailPresenter repoDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        repoDetailPresenter = new RepoDetailPresenter(this);
        viewHolder = new RepoDetailViewHolder();
        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        setTitle(repository.getName());
        repoDetailPresenter.loadRepository(repository);
    }

    @Override
    public RepoDetailActivity getHostingActivity() {
        return this;
    }

    @Override
    public void renderRepository(Repository repository) {
        viewHolder.populateDetails(repository);
        repoDetailPresenter.fetchContributors(new RepoContributorTransaction(AppSession.get(this).getActiveUser().getAuthToken()));
    }

    @Override
    public void renderContributors(List<Contributor> contributors) {
        viewHolder.populateContributors(contributors);
    }

    @Override
    public void openWeb(String url) {
        startActivity(WebActivity.getIntent(url, this));
    }

    @Override
    public void onContributorClick(Contributor contributor) {
        if (contributor.getLogin() == null
                || Contributor.DEFAULT_LOGIN.equals(contributor.getLogin())) {
            showMessage("User unavailable!");
            return;
        }
        startActivity(UserInfoActivity.getIntent(contributor, this));
    }

    public class RepoDetailViewHolder {
        private TextView tvRepoDesc;
        private TextView tvRepoUrl;
        private RecyclerView rvContributors;

        private ContributorListAdapter listAdapter;

        RepoDetailViewHolder() {
            tvRepoDesc = findViewById(R.id.tv_repo_desc);
            tvRepoUrl = findViewById(R.id.tv_repo_url);
            tvRepoUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repoDetailPresenter.onRepoLinkClick();
                }
            });
            rvContributors = findViewById(R.id.rv_contributor_list);
            rvContributors.setLayoutManager(new GridLayoutManager(RepoDetailActivity.this,
                    4, LinearLayoutManager.HORIZONTAL, false));
            listAdapter = new ContributorListAdapter(LayoutInflater.from(RepoDetailActivity.this),
                    null);
            listAdapter.setContributorClickListener(RepoDetailActivity.this);
            rvContributors.setAdapter(listAdapter);
        }

        void populateDetails(Repository repository) {
            tvRepoDesc.setText(repository.getDescription());
            tvRepoUrl.setText(repository.getUrlToView());
        }

        void populateContributors(List<Contributor> contributors) {
            listAdapter.swapItems(contributors);
        }
    }
}
