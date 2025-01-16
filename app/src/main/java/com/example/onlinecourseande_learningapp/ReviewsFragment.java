package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentLessonsBinding;
import com.example.onlinecourseande_learningapp.databinding.FragmentReviewsBinding;


public class ReviewsFragment extends Fragment {


    FragmentReviewsBinding binding;
    private String courseId;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    public static ReviewsFragment newInstance(String courseId) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString("course_id", courseId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentReviewsBinding.inflate(inflater, container, false);





        return binding.getRoot();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
        }
    }







}