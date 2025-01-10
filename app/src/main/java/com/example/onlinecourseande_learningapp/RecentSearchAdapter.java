package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    private List<String> recentSearches;
    private Context context;
    private OnSearchItemClickListener listener;

    public interface OnSearchItemClickListener {
        void onDeleteClick(String item);
        void onItemClick(String item);
    }

    public RecentSearchAdapter(Context context, List<String> recentSearches, OnSearchItemClickListener listener) {
        this.context = context;
        this.recentSearches = recentSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String search = recentSearches.get(position);
        holder.tvSearchText.setText(search);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(search); // Define this in your listener
            }
        });

        holder.ivDeleteIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(search);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearchText;
        ImageView ivDeleteIcon;

        ViewHolder(View itemView) {
            super(itemView);
            tvSearchText = itemView.findViewById(R.id.tvSearchText);
            ivDeleteIcon = itemView.findViewById(R.id.ivDeleteIcon);
        }
    }


}
