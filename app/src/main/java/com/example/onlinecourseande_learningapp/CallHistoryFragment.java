package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;

import java.util.ArrayList;

public class CallHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private CallAdapter callAdapter;
    private AppViewModel appViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCalls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        callAdapter = new CallAdapter(new ArrayList<>());
        recyclerView.setAdapter(callAdapter);

        // Observe Room data changes via ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        appViewModel.getAllCalls().observe(getViewLifecycleOwner(), calls -> {
            callAdapter.updateCalls(calls);
        });

        return view;
    }
}
