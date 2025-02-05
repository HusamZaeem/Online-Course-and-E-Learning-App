package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinecourseande_learningapp.databinding.ActivityAllCoursesBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllCoursesActivity extends AppCompatActivity implements RecentSearchAdapter.OnSearchItemClickListener {

    private ActivityAllCoursesBinding binding;
    private AppViewModel appViewModel;
    private CourseAdapter courseAdapter;
    private List<Course> allCourses = new ArrayList<>();
    private List<Course> filteredCourses = new ArrayList<>();
    private List<String> recentSearches = new ArrayList<>();
    private RecentSearchAdapter recentSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Load recent searches from SharedPreferences
        recentSearches = SharedPrefHelper.loadRecentSearches(this);
        if (recentSearches == null) {
            recentSearches = new ArrayList<>();
        }

        setupToolbar();
        setupTabLayoutCategories();
        setupRecyclerView();
        setupSearchEditText();
        setupRecentSearchRecyclerView();

        loadCourses("All");
        toggleDeleteAllVisibility();

        binding.tvClearAll.setOnClickListener(v -> {
            recentSearches.clear();
            recentSearchAdapter.notifyDataSetChanged();
            SharedPrefHelper.saveRecentSearches(this, recentSearches);
            toggleDeleteAllVisibility();
        });
    }

    private void setupSearchEditText() {
        binding.searchEditText.setVisibility(View.VISIBLE); // Ensure it's visible
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCoursesBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupRecentSearchRecyclerView() {
        binding.recentSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentSearchAdapter = new RecentSearchAdapter(this, recentSearches, this);
        binding.recentSearchRecyclerView.setAdapter(recentSearchAdapter);
        toggleDeleteAllVisibility();
    }

    private void toggleDeleteAllVisibility() {
        if (recentSearches.isEmpty()) {
            binding.tvClearAll.setVisibility(View.GONE);
        } else {
            binding.tvClearAll.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> navigateToHomeFragment());
    }

    private void navigateToHomeFragment() {
        Intent intent = new Intent(AllCoursesActivity.this, MainActivity.class);
        intent.putExtra("FRAGMENT", "HOME");
        startActivity(intent);
        finish();
    }

    private void setupTabLayoutCategories() {
        String[] categories = {"All", "Programming", "Design", "Marketing", "Finance", "Languages", "Health", "Business"};
        int[] icons = {R.drawable.icon_13, R.drawable.programming_icon, R.drawable.design_draw_drawing_icon,
                R.drawable.marketing_icon, R.drawable.finance_icon, R.drawable.language_icon,
                R.drawable.health_icon, R.drawable.business_icon};

        for (int i = 0; i < categories.length; i++) {
            TabLayout.Tab tab = binding.tabLayoutCategories.newTab();
            tab.setText(categories[i]);
            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), icons[i]);
            binding.tabLayoutCategories.setTabIconTint(null);
            tab.setIcon(icon);
            binding.tabLayoutCategories.addTab(tab);
        }

        binding.tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterCoursesByCategory(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupRecyclerView() {
        binding.coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(this, filteredCourses, appViewModel, this);
        binding.coursesRecyclerView.setAdapter(courseAdapter);
    }

    private void loadCourses(String category) {
        appViewModel.getAllCourses().observe(this, courseList -> {
            if (courseList != null) {
                allCourses = courseList;
                filterCoursesByCategory(category);
            }
        });
    }

    private void filterCoursesByCategory(String category) {
        filteredCourses.clear();
        if (category.equals("All")) {
            filteredCourses.addAll(allCourses);
        } else {
            for (Course course : allCourses) {
                if (course.getCategory().equalsIgnoreCase(category)) {
                    filteredCourses.add(course);
                }
            }
        }
        courseAdapter.notifyDataSetChanged();
        toggleNoResultsImage();
    }

    private void filterCoursesBySearch(String query) {
        filteredCourses.clear();
        if (query.isEmpty()) {
            filteredCourses.addAll(allCourses);
        } else {
            for (Course course : allCourses) {
                if (course.getCourse_name().toLowerCase().contains(query.toLowerCase())) {
                    filteredCourses.add(course);
                }
            }
        }
        courseAdapter.notifyDataSetChanged();
        toggleNoResultsImage();
    }

    private void toggleNoResultsImage() {
        if (filteredCourses.isEmpty()) {
            binding.noResultsImage.setVisibility(View.VISIBLE);
        } else {
            binding.noResultsImage.setVisibility(View.GONE);
        }
    }

    private void addSearchToRecent(String searchQuery) {
        if (!recentSearches.contains(searchQuery)) {
            recentSearches.add(0, searchQuery);
            recentSearchAdapter.notifyDataSetChanged();
            SharedPrefHelper.saveRecentSearches(this, recentSearches);
            toggleDeleteAllVisibility();
        }
    }


@Override
    public void onDeleteClick(String item) {
        recentSearches.remove(item);
        recentSearchAdapter.notifyDataSetChanged();
        SharedPrefHelper.saveRecentSearches(this, recentSearches);
        toggleDeleteAllVisibility();
    }

    @Override
    public void onItemClick(String item) {
        filterCoursesBySearch(item);
    }
}