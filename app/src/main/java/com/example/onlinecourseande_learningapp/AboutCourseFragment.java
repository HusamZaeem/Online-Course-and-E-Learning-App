package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentAboutCourseBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.UUID;


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

        // Check if user is enrolled
        String studentId = getStudentIdFromSharedPreferences();
        appViewModel.checkEnrollment(studentId, courseId).observe(getViewLifecycleOwner(), enrollment -> {
            if (enrollment != null) {
                // Student is enrolled, disable the button
                binding.btnEnroll.setEnabled(false);
                binding.btnEnroll.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.gray));
                binding.btnEnroll.setText("Enrolled");
            } else {
                // Student is not enrolled
                binding.btnEnroll.setEnabled(true);
                binding.btnEnroll.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.btn_color));
                binding.btnEnroll.setText(getString(R.string.enroll_course));
            }
        });


        loadCourseDetails();


        binding.btnEnroll.setOnClickListener(v -> {
            if (studentId != null && courseId != null) {
                String enrollment_id = UUID.randomUUID().toString();
                appViewModel.enrollInFreeCourse(enrollment_id, studentId, courseId);
                Toast.makeText(getContext(), "You are now enrolled in this course", Toast.LENGTH_SHORT).show();

                binding.btnEnroll.setEnabled(false);
                binding.btnEnroll.setText("Enrolled");
                binding.btnEnroll.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.gray));
            }
        });

        return binding.getRoot();
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("student_id", null);
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

                        binding.ivCourseMentorPhoto.setOnClickListener(view -> {
                            Intent intent = new Intent(requireContext(), MentorProfileActivity.class);
                            intent.putExtra("mentor_id", mentor.getMentor_id());
                            startActivity(intent);
                        });

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