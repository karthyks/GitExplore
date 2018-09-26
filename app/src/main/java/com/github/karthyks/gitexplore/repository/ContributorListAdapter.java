package com.github.karthyks.gitexplore.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.model.Contributor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContributorListAdapter extends RecyclerView.Adapter<ContributorListAdapter.ContributorViewHolder> {


    private LayoutInflater inflater;
    @Nullable
    private List<Contributor> contributors;
    private OnContributorClickListener contributorClickListener;

    public ContributorListAdapter(LayoutInflater inflater, @Nullable List<Contributor> contributors) {
        this.inflater = inflater;
        this.contributors = contributors;
    }

    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContributorViewHolder(inflater.inflate(R.layout.contributor_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {
        if (contributors != null) {
            holder.populateDetails(contributors.get(position));
        }
    }

    public void setContributorClickListener(OnContributorClickListener contributorClickListener) {
        this.contributorClickListener = contributorClickListener;
    }

    @Override
    public int getItemCount() {
        return contributors == null ? 0 : contributors.size();
    }

    public void swapItems(List<Contributor> contributors) {
        if (this.contributors != null) {
            this.contributors.clear();
            this.contributors.addAll(contributors);
        } else {
            this.contributors = contributors;
        }
        notifyDataSetChanged();
    }

    class ContributorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvContributor;
        private ImageView imgContributor;
        private TextView tvContributions;

        ContributorViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvContributor = itemView.findViewById(R.id.tv_contributor);
            imgContributor = itemView.findViewById(R.id.img_contributor);
            tvContributions = itemView.findViewById(R.id.tv_contributions);
        }

        void populateDetails(Contributor contributor) {
            Picasso.get().load(contributor.getAvatarUrl()).into(imgContributor);
            tvContributor.setText(contributor.getLogin());
            tvContributions.setText(contributor.getContributionCount());
        }

        @Override
        public void onClick(View view) {
            if (contributorClickListener != null && contributors != null) {
                contributorClickListener.onContributorClick(contributors.get(getAdapterPosition()));
            }
        }
    }

    public interface OnContributorClickListener {
        void onContributorClick(Contributor contributor);
    }
}
