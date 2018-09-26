package com.github.karthyks.gitexplore.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.frameworks.CustomActivity;
import com.github.karthyks.gitexplore.model.Contributor;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.github.karthyks.gitexplore.repository.RepoDetailActivity;
import com.github.karthyks.gitexplore.search.RepoListAdapter;
import com.github.karthyks.gitexplore.transaction.FetchUserRepositoryTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserInfoActivity extends CustomActivity implements IUserInfoView, RepoListAdapter.IRepoClickListener {
    private static final String EXTRA_CONTRIBUTOR = "extra_contributor";

    public static Intent getIntent(Contributor contributor, Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(EXTRA_CONTRIBUTOR, contributor);
        return intent;
    }

    private UserInfoViewHolder userInfoViewHolder;
    private IUserInfoPresenter userInfoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userInfoViewHolder = new UserInfoViewHolder();
        userInfoPresenter = new UserInfoPresenter(this);
        Contributor contributor = getIntent().getParcelableExtra(EXTRA_CONTRIBUTOR);
        if (contributor == null) {
            finish();
            return;
        }
        renderUserInfo(contributor);
    }

    @Override
    public void renderRepositories(RepositoryPage repositoryPage) {
        userInfoViewHolder.repoListAdapter.swapItems(repositoryPage.repositories);
    }

    @Override
    public UserInfoActivity getHostingActivity() {
        return this;
    }

    @Override
    public void renderUserInfo(Contributor contributor) {
        userInfoViewHolder.populateUser(contributor);
        userInfoPresenter.fetchRepositories(new FetchUserRepositoryTransaction(FirebaseAuth.getInstance()), contributor.getLogin());
    }

    @Override
    public void onRepositoryClick(Repository repository) {
        startActivity(RepoDetailActivity.getIntent(repository, this));
    }

    public class UserInfoViewHolder {
        private ImageView imgUser;
        private TextView tvLogin;
        private RecyclerView rvRepoList;

        private RepoListAdapter repoListAdapter;

        UserInfoViewHolder() {
            tvLogin = findViewById(R.id.tv_login);
            imgUser = findViewById(R.id.img_user);
            rvRepoList = findViewById(R.id.rv_repo_list);
            rvRepoList.setLayoutManager(new LinearLayoutManager(UserInfoActivity.this));
            repoListAdapter = new RepoListAdapter(LayoutInflater.from(UserInfoActivity.this), null);
            rvRepoList.setAdapter(repoListAdapter);
            repoListAdapter.setOnRepoClickListener(UserInfoActivity.this);
        }

        void populateUser(Contributor contributor) {
            tvLogin.setText(contributor.getLogin());
            Picasso.get().load(contributor.getAvatarUrl()).into(imgUser);
        }
    }
}
