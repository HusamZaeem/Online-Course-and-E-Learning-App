package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinecourseande_learningapp.databinding.FragmentMyCoursesBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCoursesFragment extends Fragment {

    private FragmentMyCoursesBinding binding;
    private AppViewModel appViewModel;
    private MyCoursesPagerAdapter pagerAdapter;
    private RecentSearchMyCoursesAdapter recentSearchAdapter;
    private SharedPreferences recentPrefs;
    private List<String> recentSearches = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        setupTabs();
        setupSearch();

        return binding.getRoot();
    }

    private void setupTabs() {
        String studentId = getStudentIdFromSharedPreferences();
        pagerAdapter = new MyCoursesPagerAdapter(this, studentId);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Ongoing" : "Completed")).attach();
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }

    private void setupSearch() {
        binding.ivSearch.setOnClickListener(v -> toggleSearchUI());

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                pagerAdapter.filterCourses(query);
                if (!query.isEmpty()) {
                    saveRecentSearch(query);
                }
                hideSearchUI();
                return true;
            }
            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                pagerAdapter.filterCourses(query);
                binding.rvRecentSearches.setVisibility(query.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        recentPrefs = requireContext().getSharedPreferences("RecentSearchesMyCourses", Context.MODE_PRIVATE);
        recentSearchAdapter = new RecentSearchMyCoursesAdapter(recentSearches, query -> {
            binding.etSearch.setText(query);
            binding.etSearch.setSelection(query.length());
        }, recentPrefs);
        loadRecentSearches();

        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentSearches.setAdapter(recentSearchAdapter);
    }

    private void toggleSearchUI() {
        if (binding.etSearch.getVisibility() == View.VISIBLE) {
            hideSearchUI();
        } else {
            showSearchUI();
        }
    }

    private void showSearchUI() {
        binding.etSearch.setVisibility(View.VISIBLE);
        binding.rvRecentSearches.setVisibility(View.VISIBLE);
        // Hide logo and title
        binding.ivAppLogo.setVisibility(View.GONE);
        binding.tvTitle.setVisibility(View.GONE);
        binding.etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideSearchUI() {
        binding.etSearch.setVisibility(View.GONE);
        binding.rvRecentSearches.setVisibility(View.GONE);
        // Restore logo and title
        binding.ivAppLogo.setVisibility(View.VISIBLE);
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.etSearch.setText("");
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
    }

    private void saveRecentSearch(String query) {
        SharedPreferences.Editor editor = recentPrefs.edit();
        String saved = recentPrefs.getString("MyCourses_recent", "");
        if (!saved.contains(query)) {
            saved = saved.isEmpty() ? query : query + "," + saved;
            editor.putString("MyCourses_recent", saved);
            editor.apply();
        }
        loadRecentSearches();
    }

    private void loadRecentSearches() {
        String saved = recentPrefs.getString("MyCourses_recent", "");
        recentSearches.clear();
        if (!saved.isEmpty()) {
            recentSearches.addAll(Arrays.asList(saved.split(",")));
        }
        if (recentSearchAdapter != null) {
            recentSearchAdapter.notifyDataSetChanged();
        }
    }
}
