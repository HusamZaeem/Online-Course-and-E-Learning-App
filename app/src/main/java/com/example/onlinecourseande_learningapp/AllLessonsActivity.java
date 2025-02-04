package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.databinding.ActivityAllLessonsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllLessonsActivity extends AppCompatActivity {

    private ActivityAllLessonsBinding binding;
    private AppViewModel appViewModel;
    private String courseId, studentId;
    private ModulesAdapter modulesAdapter;
    private RecentSearchAllLessonsAdapter recentSearchAdapter;
    private List<String> recentSearches = new ArrayList<>();
    // Use "RecentSearchesAllLessons" as the SharedPreferences name.
    private SharedPreferences recentPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllLessonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve extras from intent
        courseId = getIntent().getStringExtra("course_id");
        studentId = getIntent().getStringExtra("student_id");
        if (courseId == null || studentId == null) {
            Toast.makeText(this, "Course or student information missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Setup top bar
        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvTitle.setText("All Lessons");
        binding.ivSearch.setOnClickListener(v -> showSearchUI());

        // Setup search EditText behavior
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (modulesAdapter != null) {
                    modulesAdapter.filter(query);
                }
                // Show recent searches if query is empty; hide if not
                if (query.isEmpty()) {
                    binding.rvRecentSearches.setVisibility(RecyclerView.VISIBLE);
                    loadRecentSearches();
                } else {
                    binding.rvRecentSearches.setVisibility(RecyclerView.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        // When the user submits search via keyboard (action "search")
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    saveRecentSearch(query);
                }
                hideSearchUI();
                return true;
            }
            return false;
        });

        // Initialize recent search SharedPreferences with name "RecentSearchesAllLessons"
        recentPrefs = getSharedPreferences("RecentSearchesAllLessons", Context.MODE_PRIVATE);
        recentSearchAdapter = new RecentSearchAllLessonsAdapter(recentSearches, new RecentSearchAllLessonsAdapter.OnRecentSearchClickListener() {
            @Override
            public void onRecentSearchClick(String query) {
                binding.etSearch.setText(query);
                binding.etSearch.setSelection(query.length());
            }

            @Override
            public void onDeleteRecentSearchClick(String query, int position) {
                removeRecentSearch(query);
                recentSearchAdapter.removeItem(position);
            }
        });
        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecentSearches.setAdapter(recentSearchAdapter);

        // Load modules & lessons
        appViewModel.getModulesWithLessons(courseId).observe(this, modules -> {
            int totalLessons = modules.stream().mapToInt(m -> m.getLessons() != null ? m.getLessons().size() : 0).sum();
            binding.tvLessonCount.setText(totalLessons + " Lessons");

            modulesAdapter = new ModulesAdapter(this, modules);
            binding.rvModules.setLayoutManager(new LinearLayoutManager(this));
            binding.rvModules.setAdapter(modulesAdapter);
        });

        // Initially hide search EditText and recent searches UI
        binding.etSearch.setVisibility(View.GONE);
        binding.rvRecentSearches.setVisibility(View.GONE);
    }

    private void showSearchUI() {
        binding.etSearch.setVisibility(View.VISIBLE);
        binding.rvRecentSearches.setVisibility(View.VISIBLE);
        binding.ivBack.setVisibility(View.GONE);
        binding.tvTitle.setVisibility(View.GONE);
        binding.tvLessonCount.setVisibility(View.GONE);
        binding.etSearch.requestFocus();
    }

    private void hideSearchUI() {
        binding.etSearch.setVisibility(View.GONE);
        binding.rvRecentSearches.setVisibility(View.GONE);
        binding.ivBack.setVisibility(View.VISIBLE);
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvLessonCount.setVisibility(View.VISIBLE);
    }

    private void saveRecentSearch(String query) {
        SharedPreferences.Editor editor = recentPrefs.edit();
        // Save as a comma-separated string; avoid duplicates.
        String saved = recentPrefs.getString("AllLessons_recent", "");
        if (!saved.contains(query)) {
            if (saved.isEmpty()) {
                saved = query;
            } else {
                saved = query + "," + saved;
            }
            editor.putString("AllLessons_recent", saved);
            editor.apply();
        }
        loadRecentSearches();
    }

    private void loadRecentSearches() {
        String saved = recentPrefs.getString("AllLessons_recent", "");
        recentSearches.clear();
        if (!saved.isEmpty()) {
            recentSearches.addAll(Arrays.asList(saved.split(",")));
        }
        recentSearchAdapter.notifyDataSetChanged();
    }

    private void removeRecentSearch(String query) {
        SharedPreferences prefs = getSharedPreferences("RecentSearchesAllLessons", Context.MODE_PRIVATE);
        String saved = prefs.getString("AllLessons_recent", "");
        // Remove the query, taking care of comma separation.
        String updated = saved.replace(query + ",", "").replace("," + query, "").replace(query, "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("AllLessons_recent", updated);
        editor.apply();
        loadRecentSearches();
    }
}
