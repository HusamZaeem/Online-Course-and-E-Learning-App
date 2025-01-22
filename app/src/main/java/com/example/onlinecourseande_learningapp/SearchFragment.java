package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinecourseande_learningapp.databinding.FragmentSearchBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private AppViewModel appViewModel;
    private RecentSearchAdapterHome adapter;
    private final List<String> recentSearches = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SearchPreferences";
    private static final String KEY_RECENT_SEARCHES = "recent_searches";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.etSearch.post(() -> {
            binding.etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        adapter = new RecentSearchAdapterHome(
                recentSearches,
                this::onRecentSearchClick,
                this::onDeleteSearch
        );
        binding.recyclerViewRecentSearches.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewRecentSearches.setAdapter(adapter);


        loadRecentSearches();
        toggleRecyclerViewVisibility();


        binding.etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchQuery = v.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    saveSearch(searchQuery);
                    performSearch(searchQuery);
                }
                return true;
            }
            return false;
        });


        binding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loadRecentSearches();
                toggleRecyclerViewVisibility();
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                appViewModel.setSearchQuery(query);

                if (!query.isEmpty()) {
                    binding.recyclerViewRecentSearches.setVisibility(View.GONE);
                    updateSearchResults(query);
                } else {
                    loadRecentSearches();
                    toggleRecyclerViewVisibility();
                    showAllResults();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });




        setupViewPager();
    }


    private void showAllResults() {

        AllCoursesFragment allCoursesFragment = (AllCoursesFragment) getChildFragmentManager().findFragmentByTag("f0");
        if (allCoursesFragment != null) {
            allCoursesFragment.updateResults("");
        }

        AllMentorsFragment allMentorsFragment = (AllMentorsFragment) getChildFragmentManager().findFragmentByTag("f1");
        if (allMentorsFragment != null) {
            allMentorsFragment.updateResults("");
        }

    }


    private void setupViewPager() {
        ViewPagerAdapterSearch adapter = new ViewPagerAdapterSearch(this);
        adapter.addFragment(new AllCoursesFragment(), "Courses");
        adapter.addFragment(new AllMentorsFragment(), "Mentors");
        binding.viewPagerSearchResults.setAdapter(adapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayoutSearch, binding.viewPagerSearchResults,
                (tab, position) -> tab.setText(position == 0 ? "Courses" : "Mentors")).attach();

        // Listen for tab changes
        binding.tabLayoutSearch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String currentQuery = binding.etSearch.getText().toString().trim();
                updateSearchResults(currentQuery);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String currentQuery = binding.etSearch.getText().toString().trim();
                updateSearchResults(currentQuery);
            }
        });
    }

    private void saveSearch(String searchQuery) {
        if (!searchQuery.isEmpty() && !recentSearches.contains(searchQuery)) {
            recentSearches.add(0, searchQuery);
            adapter.addSearch(searchQuery);
            sharedPreferences.edit().putString(KEY_RECENT_SEARCHES, String.join(",", recentSearches)).apply();
            toggleRecyclerViewVisibility();
        }
    }

    private void loadRecentSearches() {
        String savedSearches = sharedPreferences.getString(KEY_RECENT_SEARCHES, "");
        if (!savedSearches.isEmpty()) {
            Set<String> uniqueSearches = new HashSet<>(Arrays.asList(savedSearches.split(",")));
            recentSearches.clear();
            recentSearches.addAll(new ArrayList<>(uniqueSearches));
        }
        adapter.notifyDataSetChanged();
    }

    private void onDeleteSearch(int position) {
        if (position >= 0 && position < recentSearches.size()) {
            recentSearches.remove(position);
            sharedPreferences.edit().putString(KEY_RECENT_SEARCHES, String.join(",", recentSearches)).apply();
            adapter.notifyItemRemoved(position);
            toggleRecyclerViewVisibility();
        }
    }

    private void toggleRecyclerViewVisibility() {
        if (recentSearches.isEmpty()) {
            binding.recyclerViewRecentSearches.setVisibility(View.GONE);
        } else {
            binding.recyclerViewRecentSearches.setVisibility(View.VISIBLE);
        }
    }

    private void onRecentSearchClick(String searchText) {
        binding.etSearch.setText(searchText);
        appViewModel.setSearchQuery(searchText);
        updateSearchResults(searchText);
    }

    private void performSearch(String searchQuery) {
        if (!searchQuery.isEmpty()) {
            appViewModel.setSearchQuery(searchQuery);
            updateSearchResults(searchQuery);
            binding.recyclerViewRecentSearches.setVisibility(View.GONE);
        }
    }

    private void updateSearchResults(String searchQuery) {

        int selectedTabPosition = binding.tabLayoutSearch.getSelectedTabPosition();

        if (selectedTabPosition == 0) {

            AllCoursesFragment allCoursesFragment = (AllCoursesFragment) getChildFragmentManager().findFragmentByTag("f0");
            if (allCoursesFragment != null) {
                allCoursesFragment.updateResults(searchQuery);
            }


            if (!searchQuery.isEmpty()) {
                binding.tvResultsFor.setText("Results for '" + searchQuery + "'");
            } else {
                binding.tvResultsFor.setText("All Courses");
            }


            int coursesCount = allCoursesFragment != null ? allCoursesFragment.getItemCount() : 0;
            binding.tvNumberOfResults.setText(coursesCount + " Courses Found");

        } else if (selectedTabPosition == 1) {

            AllMentorsFragment allMentorsFragment = (AllMentorsFragment) getChildFragmentManager().findFragmentByTag("f1");
            if (allMentorsFragment != null) {
                allMentorsFragment.updateResults(searchQuery);
            }


            if (!searchQuery.isEmpty()) {
                binding.tvResultsFor.setText("Results for '" + searchQuery + "'");
            } else {
                binding.tvResultsFor.setText("All Mentors");
            }


            int mentorsCount = allMentorsFragment != null ? allMentorsFragment.getItemCount() : 0;
            binding.tvNumberOfResults.setText(mentorsCount + " Mentors Found");
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && binding.etSearch != null) {
            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
        binding = null;
    }
}
