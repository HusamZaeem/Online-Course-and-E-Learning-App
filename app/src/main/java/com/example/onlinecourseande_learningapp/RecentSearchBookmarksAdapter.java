package com.example.onlinecourseande_learningapp;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentSearchBookmarksAdapter extends RecyclerView.Adapter<RecentSearchBookmarksAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String query);
        void onDeleteRecentSearch(String query, int position);
    }

    private List<String> recentSearches;
    private OnItemClickListener listener;
    private SharedPreferences sharedPreferences;

    public RecentSearchBookmarksAdapter(List<String> recentSearches, OnItemClickListener listener, SharedPreferences sharedPreferences) {
        this.recentSearches = recentSearches;
        this.listener = listener;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String query = recentSearches.get(position);
        holder.tvSearchText.setText(query);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(query));
        holder.ivDeleteIcon.setOnClickListener(v -> listener.onDeleteRecentSearch(query, position));
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearchText;
        ImageView ivDeleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearchText = itemView.findViewById(R.id.tvSearchText);
            ivDeleteIcon = itemView.findViewById(R.id.ivDeleteIcon);
        }
    }
}
