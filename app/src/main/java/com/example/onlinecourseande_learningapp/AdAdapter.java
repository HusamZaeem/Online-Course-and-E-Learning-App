package com.example.onlinecourseande_learningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.entities.Ad;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {
    private List<Ad> adList;

    public AdAdapter(List<Ad> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertisement_slide, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Ad adItem = adList.get(position);
        holder.mainTitle.setText(adItem.getMain_title());
        holder.subTitle.setText(adItem.getSub_title());
        holder.description.setText(adItem.getDescription());
        Glide.with(holder.itemView.getContext()).load(adItem.getImageUrl()).into(holder.backgroundImage);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        TextView mainTitle, subTitle, description;
        ImageView backgroundImage;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            mainTitle = itemView.findViewById(R.id.tv_item_ad_main_title);
            subTitle = itemView.findViewById(R.id.tv_item_ad_sub_title);
            description = itemView.findViewById(R.id.tv_item_ad_description);
            backgroundImage = itemView.findViewById(R.id.iv_ad_background);
        }
    }
}
