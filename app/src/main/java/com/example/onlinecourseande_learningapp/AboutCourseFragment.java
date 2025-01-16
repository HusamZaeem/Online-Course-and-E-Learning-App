package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentAboutCourseBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;


public class AboutCourseFragment extends Fragment {

    private FragmentAboutCourseBinding binding;
    private AppViewModel appViewModel;
    private String courseId;


    public static AboutCourseFragment newInstance(String courseId) {
        AboutCourseFragment fragment = new AboutCourseFragment();
        Bundle args = new Bundle();
        args.putString("course_id", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutCourseFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize binding
        binding = FragmentAboutCourseBinding.inflate(inflater, container, false);



        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
        }


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


        loadCourseDetails();

        return binding.getRoot();
    }

    private void loadCourseDetails() {
        // Observe LiveData for the course details
        appViewModel.getCourseByIdLiveData(courseId).observe(getViewLifecycleOwner(), course -> {
            if (course != null) {

                binding.tvCourseDescription.setText(course.getDescription());
                binding.tvCourseTools.setText(course.getTools_used());


                appViewModel.getMentorsByCourseId(courseId).observe(getViewLifecycleOwner(), mentor -> {
                    if (mentor != null) {



                        binding.tvMentorName.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
                        binding.tvMentorJobTitle.setText(mentor.getJob_title());

                        // Load mentor's photo
                        ImageLoaderUtil.loadImageFromFirebaseStorage(
                                requireContext(),
                                mentor.getMentor_photo(),
                                binding.ivCourseMentorPhoto
                        );


                        binding.ivCourseMentorChat.setOnClickListener(view -> {
                            // Handle chat button click
                            // e.g., navigate to a chat activity
                        });
                    } else {

                        binding.tvMentorName.setText("Mentor not available");
                        binding.tvMentorJobTitle.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}