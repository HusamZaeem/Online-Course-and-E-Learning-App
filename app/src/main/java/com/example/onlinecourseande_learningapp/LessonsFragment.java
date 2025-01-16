package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentLessonsBinding;


public class LessonsFragment extends Fragment {

    FragmentLessonsBinding binding;
    private String courseId;



    public LessonsFragment() {
        // Required empty public constructor
    }


    public static LessonsFragment newInstance(String courseId) {
        LessonsFragment fragment = new LessonsFragment();
        Bundle args = new Bundle();
        args.putString("course_id", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLessonsBinding.inflate(inflater, container, false);





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