package com.github.karthyks.gitexplore.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.model.Repository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

    private LayoutInflater inflater;
    private List<Repository> repositories;
    private IRepoClickListener clickListener;

    public RepoListAdapter(LayoutInflater inflater, @Nullable List<Repository> repositories) {
        this.inflater = inflater;
        this.repositories = repositories;
    }

    public void setOnRepoClickListener(IRepoClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(inflater.inflate(R.layout.repository_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        if (repositories == null) return;
        holder.updateRepoDetails(repositories.get(position));
    }

    public void swapItems(List<Repository> repositories) {
        this.repositories = repositories;
        notifyDataSetChanged();
    }

    public void appendItems(List<Repository> repositories) {
        this.repositories.addAll(repositories);
        notifyItemRangeInserted(getItemCount(), repositories.size());
    }

    @Override
    public int getItemCount() {
        return repositories == null ? 0 : repositories.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);
        }

        public void updateRepoDetails(Repository repository) {
            tvRepoTitle.setText(repository.getName());
            tvRepoFullName.setText(repository.getFullName());
            tvContributor.setText(repository.getContributor().getLogin());
            tvWatchersCount.setText(repository.getWatchersCount());
            Picasso.get().load(repository.getContributor().getAvatarUrl()).into(imgContributor);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onRepositoryClick(repositories.get(getAdapterPosition()));
            }
        }
    }

    public interface IRepoClickListener {
        void onRepositoryClick(Repository repository);
    }
}
