package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinecourseande_learningapp.databinding.ActivityBookmarksBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookmarksActivity extends AppCompatActivity {

    private ActivityBookmarksBinding binding;
    private AppViewModel appViewModel;
    private BookmarksAdapter bookmarksAdapter;
    private RecentSearchBookmarksAdapter recentSearchAdapter;
    private SharedPreferences recentPrefs;
    private List<String> recentSearches = new ArrayList<>();
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get student ID from SharedPreferences.
        studentId = getStudentIdFromSharedPreferences();
        if (studentId == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Set up top bar.
        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvTitle.setText("Bookmarks");
        binding.ivSearch.setOnClickListener(v -> toggleSearchUI());

        // Setup category tabs.
        setupTabs();

        // Setup RecyclerView for courses.



        bookmarksAdapter = new BookmarksAdapter(this, studentId, new BookmarksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                Intent intent = new Intent(BookmarksActivity.this, CourseDetailsActivity.class);
                intent.putExtra("course_id", course.getCourse_id());
                intent.putExtra("student_id", studentId);
                startActivity(intent);
            }

            @Override
            public void onRemoveBookmarkClick(Course course) {
                appViewModel.removeBookmark(course.getCourse_id(), studentId);
                Toast.makeText(BookmarksActivity.this, "Course removed from bookmarks", Toast.LENGTH_SHORT).show();
            }
        });


        binding.rvBookmarkedCourses.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBookmarkedCourses.setAdapter(bookmarksAdapter);

        // Observe bookmarked courses
        appViewModel.getBookmarkedCourses(studentId).observe(this, courses -> {
            if (courses != null && !courses.isEmpty()) {
                bookmarksAdapter.submitList(courses);
                binding.tvEmpty.setVisibility(android.view.View.GONE);
            } else {
                binding.tvEmpty.setVisibility(android.view.View.VISIBLE);
            }
        });






        binding.rvBookmarkedCourses.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBookmarkedCourses.setAdapter(bookmarksAdapter);

        // Observe the bookmarked courses.
        appViewModel.getBookmarkedCourses(studentId).observe(this, courses -> {
            if (courses != null && !courses.isEmpty()) {
                bookmarksAdapter.submitList(courses);
                binding.tvEmpty.setVisibility(android.view.View.GONE);
            } else {
                binding.tvEmpty.setVisibility(android.view.View.VISIBLE);
            }
        });

        // Setup search UI.
        setupSearch();
    }

    private void setupTabs() {
        // Define categories and corresponding icons
        String[] categories = {"All", "Programming", "Design", "Marketing", "Finance", "Languages", "Health", "Business"};
        int[] icons = {R.drawable.icon_13, R.drawable.programming_icon, R.drawable.design_draw_drawing_icon,
                R.drawable.marketing_icon, R.drawable.finance_icon, R.drawable.language_icon,
                R.drawable.health_icon, R.drawable.business_icon};

        // Remove any existing tabs to reset the layout
        binding.tabLayoutCategories.removeAllTabs();

        // Loop through categories and set both text and icon for each tab
        for (int i = 0; i < categories.length; i++) {
            // Create a new tab
            TabLayout.Tab tab = binding.tabLayoutCategories.newTab();

            // Set the text for the tab
            tab.setText(categories[i]);

            // Set the icon for the tab
            Drawable icon = ContextCompat.getDrawable(this, icons[i]);
            binding.tabLayoutCategories.setTabIconTint(null);
            tab.setIcon(icon);

            // Add the tab to the TabLayout
            binding.tabLayoutCategories.addTab(tab);
        }

        // Add the OnTabSelectedListener for tab selection
        binding.tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();


                if (selectedCategory.equalsIgnoreCase("All")) {
                    bookmarksAdapter.filterByCategory(null);  // Show all bookmarks
                } else {
                    bookmarksAdapter.filterByCategory(selectedCategory);  // Show filtered bookmarks
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }


    private void setupSearch() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim().toLowerCase();
                if (!query.isEmpty()) {
                    bookmarksAdapter.filter(query);
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
                String query = s.toString().trim().toLowerCase();
                bookmarksAdapter.filter(query);

                // Show recent searches only if the query is empty and recent searches exist
                if (query.isEmpty() && !recentSearches.isEmpty()) {
                    binding.rvRecentSearches.setVisibility(android.view.View.VISIBLE);
                } else {
                    binding.rvRecentSearches.setVisibility(android.view.View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        recentPrefs = getSharedPreferences("RecentSearchesBookmarks", Context.MODE_PRIVATE);
        recentSearchAdapter = new RecentSearchBookmarksAdapter(recentSearches, new RecentSearchBookmarksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String query) {
                binding.etSearch.setText(query);  // Set the query back to the EditText
                binding.etSearch.setSelection(query.length()); // Move the cursor to the end of the text
                bookmarksAdapter.filter(query.toLowerCase()); // Perform search with selected recent search
                hideSearchUI(); // Hide search UI once the search is selected
            }

            @Override
            public void onDeleteRecentSearch(String query, int position) {
                recentSearches.remove(position);  // Remove selected recent search from the list
                recentSearchAdapter.notifyItemRemoved(position); // Notify adapter to update RecyclerView
                saveRecentSearches(); // Save the updated recent searches
            }
        }, recentPrefs);

        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(this));  // Set the layout manager
        binding.rvRecentSearches.setAdapter(recentSearchAdapter);  // Set the adapter
        loadRecentSearches();  // Load the recent searches from SharedPreferences

    }

        private void toggleSearchUI() {
        if (binding.etSearch.getVisibility() == android.view.View.VISIBLE) {
            hideSearchUI();
        } else {
            showSearchUI();
        }
    }

    private void showSearchUI() {
        binding.etSearch.setVisibility(android.view.View.VISIBLE);
        binding.rvRecentSearches.setVisibility(android.view.View.VISIBLE);
        binding.ivAppLogo.setVisibility(android.view.View.GONE);
        binding.tvTitle.setVisibility(android.view.View.GONE);
        binding.etSearch.requestFocus(); // Set focus to the search field
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT); // Show keyboard
        }
    }

    private void hideSearchUI() {
        binding.etSearch.setVisibility(android.view.View.GONE);
        binding.rvRecentSearches.setVisibility(android.view.View.GONE); // Hide recent searches when search is done
        binding.ivAppLogo.setVisibility(android.view.View.VISIBLE);
        binding.tvTitle.setVisibility(android.view.View.VISIBLE);
        binding.etSearch.setText(""); // Clear search field

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
    }

    private void saveRecentSearch(String query) {
        SharedPreferences.Editor editor = recentPrefs.edit();
        String saved = recentPrefs.getString("Bookmarks_recent", "");
        if (!saved.contains(query)) {
            saved = saved.isEmpty() ? query : query + "," + saved;
            editor.putString("Bookmarks_recent", saved);
            editor.apply();
            loadRecentSearches();
        }
    }

    private void saveRecentSearches() {
        SharedPreferences.Editor editor = recentPrefs.edit();
        editor.putString("Bookmarks_recent", String.join(",", recentSearches));
        editor.apply();
    }

    private void loadRecentSearches() {
        String saved = recentPrefs.getString("Bookmarks_recent", "");
        recentSearches.clear();
        if (!saved.isEmpty()) {
            recentSearches.addAll(Arrays.asList(saved.split(",")));
        }
        if (recentSearchAdapter != null) {
            recentSearchAdapter.notifyDataSetChanged();
        }
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }
}
