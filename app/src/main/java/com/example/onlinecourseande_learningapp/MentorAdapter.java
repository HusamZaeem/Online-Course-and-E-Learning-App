package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder> {
    private List<Mentor> mentorList;
    private Context context;

    public MentorAdapter(Context context, List<Mentor> mentorList) {
        this.mentorList = mentorList;
        this.context=context;
    }

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mentor_card, parent, false);
        return new MentorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        Mentor mentor = mentorList.get(position);
        holder.firstName.setText(mentor.getMentor_fName());
        ImageLoaderUtil.loadImageFromFirebaseStorage(context, mentor.getMentor_photo(), holder.photo);
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public static class MentorViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView firstName;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_item_mentor_photo);
            firstName = itemView.findViewById(R.id.tv_item_mentor_first_name);
        }
    }
}

