package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlinecourseande_learningapp.databinding.FragmentLessonsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;

import java.util.ArrayList;
import java.util.List;


public class LessonsFragment extends Fragment {
    private AppViewModel appViewModel;
    private RecyclerView recyclerViewModules;
    private TextView tvLessonCount, tvSeeAll;
    private String courseId;
    private String studentId;

    public static LessonsFragment newInstance(String courseId) {
        LessonsFragment fragment = new LessonsFragment();
        Bundle args = new Bundle();
        args.putString("course_id", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        recyclerViewModules = view.findViewById(R.id.recyclerViewModules);
        tvLessonCount = view.findViewById(R.id.tvLessonCount);
        tvSeeAll = view.findViewById(R.id.tvSeeAll);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        courseId = getArguments().getString("course_id");
        studentId = getStudentIdFromSharedPreferences();

        tvSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllLessonsActivity.class);
            intent.putExtra("course_id", courseId);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        appViewModel.getModulesWithLessons(courseId).observe(getViewLifecycleOwner(), modules -> {
            int totalLessons = modules.stream().mapToInt(m -> m.getLessons().size()).sum();
            tvLessonCount.setText(totalLessons + " Lessons");

            List<ModuleWithLessons> firstThreeModules = modules.size() > 3 ? modules.subList(0, 3) : modules;
            ModulesAdapter adapter = new ModulesAdapter(getContext(), firstThreeModules);
            recyclerViewModules.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewModules.setAdapter(adapter);
        });

        return view;
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("student_id", null);
    }
}
