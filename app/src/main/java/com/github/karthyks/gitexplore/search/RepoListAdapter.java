package com.github.karthyks.gitexplore.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.model.Repository;
import com.github.karthyks.gitexplore.model.RepositoryPage;
import com.squareup.picasso.Picasso;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

    private LayoutInflater inflater;
    private RepositoryPage repositoryPage;

    public RepoListAdapter(LayoutInflater inflater, @Nullable RepositoryPage repositoryPage) {
        this.inflater = inflater;
        this.repositoryPage = repositoryPage;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(inflater.inflate(R.layout.repository_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        if (repositoryPage == null || repositoryPage.repositories == null) return;
        holder.updateRepoDetails(repositoryPage.repositories.get(position));
    }

    public void swapItems(RepositoryPage repositoryPage) {
        this.repositoryPage = repositoryPage;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (repositoryPage == null || repositoryPage.repositories == null) ? 0
                : repositoryPage.repositories.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRepoTitle;
        private TextView tvRepoFullName;
        private ImageView imgContributor;
        private TextView tvContributor;
        private TextView tvWatchersCount;

        public RepoViewHolder(View itemView) {
            super(itemView);
            tvRepoTitle = itemView.findViewById(R.id.tv_repo_title);
            tvRepoFullName = itemView.findViewById(R.id.tv_repo_full_name);
            imgContributor = itemView.findViewById(R.id.img_contributor);
            tvContributor = itemView.findViewById(R.id.tv_contributor);
            tvWatchersCount = itemView.findViewById(R.id.tv_watchers_count);
        }

        public void updateRepoDetails(Repository repository) {
            tvRepoTitle.setText(repository.getName());
            tvRepoFullName.setText(repository.getFullName());
            tvContributor.setText(repository.getContributor().getLogin());
            tvWatchersCount.setText(repository.getWatchersCount());
            Picasso.get().load(repository.getContributor().getAvatarUrl()).into(imgContributor);
        }
    }
}
