package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.List;

public class MentorAdapterSearch extends RecyclerView.Adapter<MentorAdapterSearch.MentorViewHolder>{
    private List<Mentor> mentorList;
    private Context context;

    public MentorAdapterSearch(Context context, List<Mentor> mentorList) {
        this.mentorList = mentorList;
        this.context=context;
    }

    @NonNull
    @Override
    public MentorAdapterSearch.MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mentor_search, parent, false);
        return new MentorAdapterSearch.MentorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorAdapterSearch.MentorViewHolder holder, int position) {
        Mentor mentor = mentorList.get(position);
        holder.tv_mentor_full_name_search.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
        holder.tv_mentor_job_title_search.setText(mentor.getJob_title());
        ImageLoaderUtil.loadImageFromFirebaseStorage(context, mentor.getMentor_photo(), holder.photo);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MentorProfileActivity.class);
            intent.putExtra("mentor_id", mentor.getMentor_id());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public void updateData(List<Mentor> newMentorList) {
        this.mentorList = newMentorList;
        notifyDataSetChanged();
    }

    public static class MentorViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView tv_mentor_full_name_search,tv_mentor_job_title_search;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_mentor_photo_search);
            tv_mentor_full_name_search = itemView.findViewById(R.id.tv_mentor_full_name_search);
            tv_mentor_job_title_search = itemView.findViewById(R.id.tv_mentor_job_title_search);
        }
    }
}