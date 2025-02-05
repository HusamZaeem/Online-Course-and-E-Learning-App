package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentMentorCoursesBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MentorCoursesFragment extends Fragment {

    private FragmentMentorCoursesBinding binding;
    private CourseAdapter courseAdapter;


    public MentorCoursesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMentorCoursesBinding.inflate(inflater, container,false);



        setupRecyclerView();

        // Assume mentorId is passed as an argument to the fragment
        String mentorId = getArguments() != null ? getArguments().getString("mentor_id") : null;
        if (mentorId != null) {
            fetchMentorCourses(mentorId);
        }







        return binding.getRoot();
    }


    private void setupRecyclerView() {
        binding.rvMentorCourses.setLayoutManager(new LinearLayoutManager(getContext()));

        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        courseAdapter = new CourseAdapter(getContext(), new ArrayList<>(), appViewModel, getViewLifecycleOwner());

        binding.rvMentorCourses.setAdapter(courseAdapter);
    }



    private void fetchMentorCourses(String mentorId) {
        // Observe the LiveData from the ViewModel to get courses for the mentor
        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.getAllMentorCourses(mentorId).observe(getViewLifecycleOwner(), courseIds -> {
            // Fetch all courses and filter them based on the courseIds
            appViewModel.getAllCourses().observe(getViewLifecycleOwner(), allCourses -> {
                List<Course> mentorCourses = allCourses.stream()
                        .filter(course -> courseIds.contains(course.getCourse_id()))
                        .collect(Collectors.toList());
                // Update the adapter with the filtered list
                courseAdapter.updateData(mentorCourses);
            });
        });
    }


}