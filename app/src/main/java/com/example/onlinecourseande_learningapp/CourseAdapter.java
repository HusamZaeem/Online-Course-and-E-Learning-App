package com.example.onlinecourseande_learningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        double courseDiscountPrice = course.getPrice()-(course.getPrice()*0.4);
        holder.courseName.setText(course.getCourse_name());
        holder.courseCategory.setText(course.getCategory());
        holder.courseActualPrice.setText(String.format("$%.2f",course.getPrice()));
        holder.courseCurrentPrice.setText(String.format("$%.2f",courseDiscountPrice));
        holder.courseRating.setText(String.valueOf(course.getCourse_rating()));
        holder.courseStudentsCount.setText(String.valueOf(course.getStudents_count()) + " students");
        Glide.with(holder.itemView.getContext()).load(course.getPhoto_url()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseCategory, courseActualPrice, courseRating, courseStudentsCount, courseCurrentPrice;
        ImageView photo;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.tv_item_course_name);
            courseCategory = itemView.findViewById(R.id.tv_item_course_category);
            courseActualPrice = itemView.findViewById(R.id.tv_item_course_actual_price);
            courseCurrentPrice = itemView.findViewById(R.id.tv_item_course_current_price);
            courseRating = itemView.findViewById(R.id.tv_item_course_rating);
            courseStudentsCount = itemView.findViewById(R.id.tv_item_course_students_no);
            photo = itemView.findViewById(R.id.iv_item_course_photo);
        }
    }
}
