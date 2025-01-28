package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.onlinecourseande_learningapp.databinding.FragmentReviewsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.PeriodicSyncWorker;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private AppViewModel appViewModel;
    private ReviewsAdapter reviewsAdapter;

    private static final String ARG_TARGET_ID = "target_id";
    private static final String ARG_TYPE = "type";
    private String targetId;
    private String type;
    private String currentStudentId;

    // Static method to create a new instance of ReviewsFragment
    public static ReviewsFragment newInstance(String targetId, String type) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TARGET_ID, targetId);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);





        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        // Initialize the RecyclerView and Adapter
        reviewsAdapter = new ReviewsAdapter(appViewModel,getViewLifecycleOwner());
        binding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewsRecyclerView.setAdapter(reviewsAdapter);

        // Get the targetId and type arguments passed to the fragment
        if (getArguments() != null) {
            targetId = getArguments().getString(ARG_TARGET_ID);
            type = getArguments().getString(ARG_TYPE);
        }else{
            Log.e("ReviewsFragment", "Missing targetId or type arguments");
        }

        // Load reviews based on targetId and type
        loadReviews();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReviews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewLifecycleOwnerLiveData().observe(this, owner -> {
            if (owner != null) {
                loadReviews();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String studentId = getStudentIdFromSharedPreferences();
        if (studentId != null) {
            currentStudentId = studentId;
            Log.d("ReviewsFragment", "Student ID retrieved from SharedPreferences: " + currentStudentId);

            // Call enrollment checks
            if ("Course".equals(type)) {
                appViewModel.checkEnrollment(currentStudentId, targetId)
                        .observe(getViewLifecycleOwner(), enrollment -> {
                            boolean isEnrolled = enrollment != null;
                            Log.d("ReviewsFragment", "Enrollment check result: " + isEnrolled);
                            showReviewInput(isEnrolled);
                        });
            } else if ("Mentor".equals(type)) {
                appViewModel.getCoursesForAMentor(targetId)
                        .observe(getViewLifecycleOwner(), courseIds -> {
                            if (courseIds != null && !courseIds.isEmpty()) {
                                appViewModel.checkEnrollmentInMentorCourses(currentStudentId, courseIds)
                                        .observe(getViewLifecycleOwner(), enrollments -> {
                                            boolean isEligible = enrollments != null && !enrollments.isEmpty();
                                            Log.d("ReviewsFragment", "Eligibility for Mentor Review: " + isEligible);
                                            showReviewInput(isEligible);
                                        });
                            } else {
                                Log.d("ReviewsFragment", "No courses found for mentor ID: " + targetId);
                                showReviewInput(false);
                            }
                        });
            }
        } else {
            Log.e("ReviewsFragment", "No Student ID found in SharedPreferences.");
            showReviewInput(false);
        }





        binding.btnSubmitReview.setOnClickListener(v -> {
            String comment = binding.etReviewComment.getText().toString().trim();
            double rating = Math.round(binding.rbReviewRating.getRating() * 10) / 10.0;
            Date reviewDate = new Date(System.currentTimeMillis());

            if (!comment.isEmpty() && rating > 0) {
                String review_id = UUID.randomUUID().toString();
                Review review = new Review(review_id, currentStudentId, targetId, type, rating, comment, reviewDate, false);
                appViewModel.insertReview(review);
                binding.etReviewComment.setText("");
                binding.rbReviewRating.setRating(0);
                enqueueOneTimeSync();
            }
        });
    }


    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("student_id", null);
    }


    private void loadReviews() {
        if ("Course".equals(type)) {
            appViewModel.getReviewsForCourse(targetId).observe(getViewLifecycleOwner(), reviews -> {
                reviewsAdapter.submitList(reviews);
            });

        } else if ("Mentor".equals(type)) {
            appViewModel.getReviewsForMentor(targetId).observe(getViewLifecycleOwner(), reviews -> {
                reviewsAdapter.submitList(reviews);
            });
        }
    }

    private void showReviewInput(boolean isEligible) {
        binding.reviewInputLayout.setVisibility(isEligible ? View.VISIBLE : View.GONE);
    }



    private void enqueueOneTimeSync() {
        WorkManager workManager = WorkManager.getInstance(requireContext());
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(PeriodicSyncWorker.class)
                .setInitialDelay(3, TimeUnit.SECONDS)
                .build();

        workManager.enqueue(syncRequest);


        workManager.getWorkInfoByIdLiveData(syncRequest.getId()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                Log.d("ReviewSync", "Sync completed successfully");
            } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                Log.e("ReviewSync", "Sync failed");
            }
        });
    }
}