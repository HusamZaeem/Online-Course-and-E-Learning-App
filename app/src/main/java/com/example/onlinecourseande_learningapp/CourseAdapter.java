package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private Context context;
    private AppViewModel appViewModel;
    private LifecycleOwner lifecycleOwner;
    private String studentId;

    public CourseAdapter(Context context, List<Course> courseList, AppViewModel appViewModel, LifecycleOwner lifecycleOwner) {
        this.courseList = courseList;
        this.context = context;
        this.appViewModel = appViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.studentId = getStudentIdFromSharedPreferences();
    }

    public void updateData(List<Course> newCourseList) {
        this.courseList = newCourseList;
        notifyDataSetChanged();
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
        double courseDiscountPrice = course.getPrice() - (course.getPrice() * 0.4);

        holder.courseName.setText(course.getCourse_name());
        holder.courseCategory.setText(course.getCategory());
        holder.courseActualPrice.setText(String.format("$%.2f", course.getPrice()));
        holder.courseCurrentPrice.setText(String.format("$%.2f", courseDiscountPrice));
        holder.courseRating.setText(String.valueOf(course.getCourse_rating()));
        holder.courseStudentsCount.setText(String.valueOf(course.getStudents_count()) + " students");
        ImageLoaderUtil.loadImageFromFirebaseStorage(context, course.getPhoto_url(), holder.photo);

        // Directly check if the course is bookmarked
        appViewModel.isCourseBookmarked(course.getCourse_id(), studentId, isBookmarked -> {
            holder.bookmarkIcon.setImageResource(isBookmarked
                    ? R.drawable.baseline_bookmark_added_24
                    : R.drawable.baseline_bookmark_add_24);
        });

        // Handle bookmark click
        holder.bookmarkIcon.setOnClickListener(view -> {
            // Toggle bookmark state
            toggleBookmark(course, holder);
        });


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("course_id", course.getCourse_id());
            context.startActivity(intent);
        });

    }

    private void toggleBookmark(Course course, CourseViewHolder holder) {
        appViewModel.isCourseBookmarked(course.getCourse_id(), studentId, isBookmarked -> {
            if (isBookmarked) {
                // Remove bookmark
                appViewModel.removeBookmark(course.getCourse_id(), studentId);
                showToast("Course removed from bookmarks");
                holder.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_add_24);
            } else {
                // Add bookmark
                String bookmarkId = UUID.randomUUID().toString();
                Bookmark newBookmark = new Bookmark();
                newBookmark.setBookmark_id(bookmarkId);
                newBookmark.setCourse_id(course.getCourse_id());
                newBookmark.setStudent_id(studentId);
                newBookmark.setIs_synced(false);
                newBookmark.setLast_updated(new Date());

                appViewModel.insertBookmark(newBookmark);
                showToast("Course saved to bookmarks");
                holder.bookmarkIcon.setImageResource(R.drawable.baseline_bookmark_added_24);
            }
        });
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseCategory, courseActualPrice, courseRating, courseStudentsCount, courseCurrentPrice;
        ImageView photo, bookmarkIcon;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.tv_item_course_name);
            courseCategory = itemView.findViewById(R.id.tv_item_course_category);
            courseActualPrice = itemView.findViewById(R.id.tv_item_course_actual_price);
            courseCurrentPrice = itemView.findViewById(R.id.tv_item_course_current_price);
            courseRating = itemView.findViewById(R.id.tv_item_course_rating);
            courseStudentsCount = itemView.findViewById(R.id.tv_item_course_students_no);
            photo = itemView.findViewById(R.id.iv_item_course_photo);
            bookmarkIcon = itemView.findViewById(R.id.item_course_add_bookmark);
        }
    }
}
