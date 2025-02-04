package com.example.onlinecourseande_learningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecentSearchAllLessonsAdapter extends RecyclerView.Adapter<RecentSearchAllLessonsAdapter.ViewHolder> {
    public interface OnRecentSearchClickListener {
        void onRecentSearchClick(String query);
        void onDeleteRecentSearchClick(String query, int position);
    }

    private List<String> recentSearches;
    private OnRecentSearchClickListener listener;

    public RecentSearchAllLessonsAdapter(List<String> recentSearches, OnRecentSearchClickListener listener) {
        this.recentSearches = recentSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentSearchAllLessonsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the provided XML layout for recent search items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchAllLessonsAdapter.ViewHolder holder, int position) {
        String query = recentSearches.get(position);
        holder.tvSearchText.setText(query);
        // When the item (TextView) is clicked, notify the listener.
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecentSearchClick(query);
            }
        });
        // When the delete icon is clicked, notify the listener.
        holder.ivDeleteIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteRecentSearchClick(query, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    public void removeItem(int position) {
        recentSearches.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearchText;
        ImageView ivDeleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearchText = itemView.findViewById(R.id.tvSearchText);
            ivDeleteIcon = itemView.findViewById(R.id.ivDeleteIcon);
        }
    }
}