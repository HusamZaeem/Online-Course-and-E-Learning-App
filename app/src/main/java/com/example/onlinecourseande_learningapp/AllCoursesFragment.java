package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentAllCoursesBinding;
import com.example.onlinecourseande_learningapp.databinding.FragmentSearchBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AllCoursesFragment extends Fragment {

    private FragmentAllCoursesBinding binding;
    private CourseAdapter courseAdapter;
    private AppViewModel appViewModel;
    private List<Course> allCourses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllCoursesBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        setupRecyclerView();

        appViewModel.getAllCourses().observe(getViewLifecycleOwner(), courseList -> {
            if (courseList != null) {
                allCourses = courseList;
                courseAdapter.updateData(allCourses);
            }
        });

        appViewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            if (query != null) {
                courseAdapter.updateData(filterCoursesByQuery(query));
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recyclerViewAllCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        courseAdapter = new CourseAdapter(getContext(), allCourses, appViewModel, getViewLifecycleOwner());
        binding.recyclerViewAllCourses.setAdapter(courseAdapter);
    }

    private List<Course> filterCoursesByQuery(String query) {
        return allCourses.stream()
                .filter(course -> course.getCourse_name().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void updateResults(String query) {
        if (query != null) {
            List<Course> filteredCourses = filterCoursesByQuery(query);
            courseAdapter.updateData(filteredCourses);
        }
    }

    public int getItemCount() {
        return courseAdapter != null ? courseAdapter.getItemCount() : 0;
    }


}
