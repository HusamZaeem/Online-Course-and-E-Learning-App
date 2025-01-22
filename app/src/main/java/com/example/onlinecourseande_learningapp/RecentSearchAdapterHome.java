package com.example.onlinecourseande_learningapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecentSearchAdapterHome extends RecyclerView.Adapter<RecentSearchAdapterHome.ViewHolder> {

    private final List<String> recentSearches;
    private final OnSearchItemClickListener onSearchItemClickListener;
    private final OnSearchItemDeleteListener onSearchItemDeleteListener;

    public RecentSearchAdapterHome(List<String> recentSearches,
                               OnSearchItemClickListener onSearchItemClickListener,
                               OnSearchItemDeleteListener onSearchItemDeleteListener) {
        this.recentSearches = recentSearches;
        this.onSearchItemClickListener = onSearchItemClickListener;
        this.onSearchItemDeleteListener = onSearchItemDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchText = recentSearches.get(position);
        holder.tvSearchText.setText(searchText);

        // Set click listener for search text
        holder.tvSearchText.setOnClickListener(v -> onSearchItemClickListener.onSearchItemClick(searchText));

        // Set click listener for delete icon
        holder.ivDeleteIcon.setOnClickListener(v -> {
            if (position >= 0 && position < recentSearches.size()) {
                onSearchItemDeleteListener.onSearchItemDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearches != null ? recentSearches.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSearchText;
        private final ImageView ivDeleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearchText = itemView.findViewById(R.id.tvSearchText);
            ivDeleteIcon = itemView.findViewById(R.id.ivDeleteIcon);
        }
    }

    // Listener interfaces for item click and delete actions
    public interface OnSearchItemClickListener {
        void onSearchItemClick(String searchText);
    }

    public interface OnSearchItemDeleteListener {
        void onSearchItemDelete(int position);
    }

    public void addSearch(String searchQuery) {
        if (!recentSearches.contains(searchQuery)) {
            recentSearches.add(0, searchQuery);
            notifyItemInserted(0);
        }
    }



}

