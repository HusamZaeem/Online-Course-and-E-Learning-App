package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.entities.CourseWithProgress;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.CourseViewHolder> {
    private List<CourseWithProgress> courses = new ArrayList<>();
    private List<CourseWithProgress> filteredCourses = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CourseWithProgress course);
    }

    public MyCoursesAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseWithProgress courseWithProgress = filteredCourses.get(position);
        holder.tvCourseName.setText(courseWithProgress.getCourse().getCourse_name());
        ImageLoaderUtil.loadImageFromFirebaseStorage(context,
                courseWithProgress.getCourse().getPhoto_url(), holder.ivCourseImage);
        holder.tvCourseHours.setText(courseWithProgress.getCourse().getHours() + " Hours");

        // Calculate progress percentage.
        int progress = (int) ((courseWithProgress.getCompletedLessons() / (float) courseWithProgress.getTotalLessons()) * 100);
        holder.tvLessonsCount.setText(courseWithProgress.getCompletedLessons() + "/" + courseWithProgress.getTotalLessons());

        int color = getProgressColor(progress);


        if (progress == 100 && courseWithProgress.isCompleted()) {
            // Hide the circular progress container.
            holder.fl_circularProgress.setVisibility(View.GONE);
            // Show the "View Certificate" button.
            holder.btnViewCertificate.setVisibility(View.VISIBLE);
            holder.horizontalProgressBar.setProgress(progress);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.horizontalProgressBar.setProgressTintList(ColorStateList.valueOf(color));
            } else {
                holder.horizontalProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }

            holder.btnViewCertificate.setOnClickListener(v -> {
                // Bundle the enrollment id and launch the certificate fragment.
                Bundle bundle = new Bundle();
                bundle.putString("enrollment_id", courseWithProgress.getEnrollment().getEnrollment_id());
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    CourseCertificateFragment certificateFragment = new CourseCertificateFragment();
                    certificateFragment.setArguments(bundle);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_completed, certificateFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            // Course not complete: show progress indicator.
            holder.fl_circularProgress.setVisibility(View.VISIBLE);
            holder.btnViewCertificate.setVisibility(View.GONE);
            holder.horizontalProgressBar.setProgress(progress);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.horizontalProgressBar.setProgressTintList(ColorStateList.valueOf(color));
            } else {
                holder.horizontalProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            ((CircularProgressIndicator) holder.circularProgressIndicator).setProgress(progress);
            ((CircularProgressIndicator) holder.circularProgressIndicator).setIndicatorColor(color);
            holder.tvProgressPercentage.setText(progress + "%");
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(courseWithProgress));
    }


    @Override
    public int getItemCount() {
        return filteredCourses.size();
    }

    public void submitList(List<CourseWithProgress> courseList) {
        courses.clear();
        if (courseList != null) {
            courses.addAll(courseList);
        }
        filteredCourses.clear();
        filteredCourses.addAll(courses);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredCourses.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredCourses.addAll(courses);
        } else {
            for (CourseWithProgress cwp : courses) {
                String courseName = cwp.getCourse().getCourse_name();
                if (courseName != null && courseName.toLowerCase().contains(query.toLowerCase())) {
                    filteredCourses.add(cwp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private int getProgressColor(int progress) {
        if (progress <= 25) return Color.GREEN;
        else if (progress <= 50) return Color.YELLOW;
        else if (progress <= 75) return Color.parseColor("#FFA500"); // Orange
        else if (progress <= 90) return Color.RED;
        else return Color.BLUE;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvLessonsCount, tvProgressPercentage, tvCourseHours;
        ProgressBar horizontalProgressBar;
        View circularProgressIndicator;
        ImageView ivCourseImage;
        Button btnViewCertificate;
        FrameLayout fl_circularProgress;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvLessonsCount = itemView.findViewById(R.id.tvLessonsCount);
            tvProgressPercentage = itemView.findViewById(R.id.tvProgressPercentage);
            horizontalProgressBar = itemView.findViewById(R.id.horizontalProgressBar);
            circularProgressIndicator = itemView.findViewById(R.id.circularProgressIndicator);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            tvCourseHours = itemView.findViewById(R.id.tvCourseHours);
            btnViewCertificate = itemView.findViewById(R.id.btnViewCertificate);
            fl_circularProgress = itemView.findViewById(R.id.fl_circularProgress);
        }
    }
}
