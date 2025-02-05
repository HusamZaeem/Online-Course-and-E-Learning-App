package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.CourseViewHolder> {
    private List<Course> courses = new ArrayList<>();
    private List<Course> filteredCourses = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;
    private String studentId;

    public interface OnItemClickListener {
        void onItemClick(Course course);
        void onRemoveBookmarkClick(Course course);
    }

    public BookmarksAdapter(Context context, String studentId, OnItemClickListener listener) {
        this.context = context;
        this.studentId = studentId;  // Store the studentId in the adapter
        this.listener = listener;
    }

    public void submitList(List<Course> courseList) {
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
            for (Course course : courses) {
                if (course.getCourse_name() != null &&
                        course.getCourse_name().toLowerCase().contains(query.toLowerCase())) {
                    filteredCourses.add(course);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        filteredCourses.clear();
        if (category == null || category.trim().isEmpty()) {
            filteredCourses.addAll(courses);
        } else {
            for (Course course : courses) {
                if (course.getCategory() != null && course.getCategory().equalsIgnoreCase(category)) {
                    filteredCourses.add(course);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = filteredCourses.get(position);
        holder.tvCourseName.setText(course.getCourse_name());
        holder.tvCourseCategory.setText(course.getCategory());
        double discountedPrice = course.getPrice() - (course.getPrice() * 0.4);
        holder.tvCourseCurrentPrice.setText(String.format("$%.2f", discountedPrice));
        holder.tvCourseActualPrice.setText(String.format("$%.2f", course.getPrice()));
        holder.tvCourseRating.setText(String.valueOf(course.getCourse_rating()));
        holder.tvCourseStudentsNo.setText(course.getStudents_count() + " students");
        ImageLoaderUtil.loadImageFromFirebaseStorage(context, course.getPhoto_url(), holder.ivCoursePhoto);

        holder.ivBookmark.setImageResource(R.drawable.baseline_bookmark_remove_24);
        holder.ivBookmark.setOnClickListener(v -> {
            RemoveBookmarkBottomSheetFragment fragment = new RemoveBookmarkBottomSheetFragment(course, new RemoveBookmarkBottomSheetFragment.OnRemoveBookmarkListener() {
                @Override
                public void onRemoveBookmark(Course course) {
                    AppViewModel appViewModel = new ViewModelProvider((AppCompatActivity) context).get(AppViewModel.class);
                    appViewModel.removeBookmark(course.getCourse_id(), studentId);
                    listener.onRemoveBookmarkClick(course);
                }
            });
            fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), fragment.getTag());
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCourses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCategory, tvCourseCurrentPrice, tvCourseActualPrice, tvCourseRating, tvCourseStudentsNo;
        ImageView ivCoursePhoto, ivBookmark;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_item_course_name);
            tvCourseCategory = itemView.findViewById(R.id.tv_item_course_category);
            tvCourseCurrentPrice = itemView.findViewById(R.id.tv_item_course_current_price);
            tvCourseActualPrice = itemView.findViewById(R.id.tv_item_course_actual_price);
            tvCourseRating = itemView.findViewById(R.id.tv_item_course_rating);
            tvCourseStudentsNo = itemView.findViewById(R.id.tv_item_course_students_no);
            ivCoursePhoto = itemView.findViewById(R.id.iv_item_course_photo);
            ivBookmark = itemView.findViewById(R.id.item_course_add_bookmark);
        }
    }
}
