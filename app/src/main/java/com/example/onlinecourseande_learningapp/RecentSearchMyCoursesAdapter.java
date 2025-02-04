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

public class RecentSearchMyCoursesAdapter extends RecyclerView.Adapter<RecentSearchMyCoursesAdapter.ViewHolder> {

    private List<String> recentSearches;
    private OnItemClickListener onItemClickListener;
    private SharedPreferences sharedPreferences;

    public interface OnItemClickListener {
        void onItemClick(String query);
    }

    public RecentSearchMyCoursesAdapter(List<String> recentSearches, OnItemClickListener onItemClickListener, SharedPreferences sharedPreferences) {
        this.recentSearches = recentSearches;
        this.onItemClickListener = onItemClickListener;
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
        String search = recentSearches.get(position);
        holder.tvSearchText.setText(search);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(search));

        // Delete search item
        holder.ivDeleteIcon.setOnClickListener(v -> {
            recentSearches.remove(position);
            notifyItemRemoved(position);
            saveRecentSearches();
        });
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
            tvSearchText = itemView.findViewById(R.id.tvSearchText);  // FIXED: Using correct ID
            ivDeleteIcon = itemView.findViewById(R.id.ivDeleteIcon);
        }
    }

    private void saveRecentSearches() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MyCourses_recent", String.join(",", recentSearches));
        editor.apply();
    }
}
