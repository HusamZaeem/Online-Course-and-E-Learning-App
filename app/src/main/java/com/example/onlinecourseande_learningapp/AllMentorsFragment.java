package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentAllCoursesBinding;
import com.example.onlinecourseande_learningapp.databinding.FragmentAllMentorsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AllMentorsFragment extends Fragment {

    private FragmentAllMentorsBinding binding;
    private MentorAdapterSearch mentorAdapterSearch;
    private AppViewModel appViewModel;
    private List<Mentor> allMentors = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllMentorsBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        setupRecyclerView();

        appViewModel.getAllMentors().observe(getViewLifecycleOwner(), mentorList -> {
            if (mentorList != null) {
                allMentors = mentorList;
                mentorAdapterSearch.updateData(allMentors);
            }
        });

        appViewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            if (query != null) {
                mentorAdapterSearch.updateData(filterMentorsByQuery(query));
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recyclerViewAllMentors.setLayoutManager(new LinearLayoutManager(getContext()));
        mentorAdapterSearch = new MentorAdapterSearch(getContext(), allMentors);
        binding.recyclerViewAllMentors.setAdapter(mentorAdapterSearch);
    }

    private List<Mentor> filterMentorsByQuery(String query) {
        return allMentors.stream()
                .filter(mentor -> mentor.getMentor_fName().toLowerCase().contains(query.toLowerCase()) ||
                        mentor.getMentor_lName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }


    public void updateResults(String query) {
        if (query != null) {
            List<Mentor> filteredMentors = filterMentorsByQuery(query);
            mentorAdapterSearch.updateData(filteredMentors);
        }
    }


    public int getItemCount() {
        return mentorAdapterSearch != null ? mentorAdapterSearch.getItemCount() : 0;
    }


}

