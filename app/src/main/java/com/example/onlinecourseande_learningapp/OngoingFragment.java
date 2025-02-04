package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.onlinecourseande_learningapp.databinding.FragmentOngoingBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.CourseWithProgress;
import java.util.List;

public class OngoingFragment extends Fragment {
    private FragmentOngoingBinding binding;
    private MyCoursesAdapter adapter;
    private AppViewModel appViewModel;
    private String studentId;

    public static OngoingFragment newInstance(String studentId) {
        OngoingFragment fragment = new OngoingFragment();
        Bundle args = new Bundle();
        args.putString("student_id", studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOngoingBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        if(getArguments() != null) {
            studentId = getArguments().getString("student_id");
        } else {
            Toast.makeText(getContext(), "Student ID not found", Toast.LENGTH_SHORT).show();
        }
        binding.recyclerViewOngoing.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyCoursesAdapter(getContext(), course -> {
            Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
            intent.putExtra("course_id", course.getCourse().getCourse_id());
            startActivity(intent);
        });
        binding.recyclerViewOngoing.setAdapter(adapter);
        loadOngoingCourses();
        return binding.getRoot();
    }

    private void loadOngoingCourses() {
        if (studentId != null) {
            appViewModel.getOngoingCourses(studentId).observe(getViewLifecycleOwner(), courses -> {
                if (courses != null) {
                    adapter.submitList(courses);
                }
            });
        }
    }

    public void filterCourses(String query) {
        if (adapter != null) {
            adapter.filter(query);
        }
    }
}
