package com.example.onlinecourseande_learningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RemoveBookmarkBottomSheetFragment extends BottomSheetDialogFragment {

    private Course course;
    private OnRemoveBookmarkListener listener;

    // Constructor to pass the course and listener for interaction
    public RemoveBookmarkBottomSheetFragment(Course course, OnRemoveBookmarkListener listener) {
        this.course = course;
        this.listener = listener;
    }

    // Interface for listening when the course is removed
    public interface OnRemoveBookmarkListener {
        void onRemoveBookmark(Course course);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the BottomSheet
        View view = inflater.inflate(R.layout.bottom_sheet_course_remove, container, false);

        // Bind views from the XML
        TextView tvConfirmationMessage = view.findViewById(R.id.tvConfirmationMessage);
        TextView tvCourseName = view.findViewById(R.id.tv_item_course_name);
        TextView tvCourseCategory = view.findViewById(R.id.tv_item_course_category);
        TextView tvCourseCurrentPrice = view.findViewById(R.id.tv_item_course_current_price);
        TextView tvCourseActualPrice = view.findViewById(R.id.tv_item_course_actual_price);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnRemove = view.findViewById(R.id.btnRemove);
        ImageView courseImage = view.findViewById(R.id.iv_item_course_photo);
        TextView tvCourseRating = view.findViewById(R.id.tv_item_course_rating);
        TextView tvCourseStudentsNo = view.findViewById(R.id.tv_item_course_students_no);

        // Set confirmation message
        tvConfirmationMessage.setText("Are you sure you want to remove this course from Bookmarks?");

        // Set course details dynamically from the course object
        tvCourseName.setText(course.getCourse_name());
        tvCourseCategory.setText(course.getCategory());
        tvCourseCurrentPrice.setText(String.format("$%.2f", course.getPrice() - (course.getPrice() * 0.4))); // 40% off
        tvCourseActualPrice.setText(String.format("$%.2f", course.getPrice()));
        tvCourseStudentsNo.setText(String.valueOf(course.getStudents_count()) + " students");

        // Use ImageLoaderUtil to load the image
        ImageLoaderUtil.loadImageFromFirebaseStorage(getContext(), course.getPhoto_url(), courseImage);

        // Set course rating dynamically
        tvCourseRating.setText(String.format("%.1f", course.getCourse_rating()));

        // Cancel button dismisses the BottomSheet
        btnCancel.setOnClickListener(v -> dismiss());

        // Remove button triggers the callback to remove the bookmark
        btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveBookmark(course);  // Notify the listener to remove the bookmark
            }
            dismiss();  // Close the BottomSheet
        });

        return view;
    }
}
