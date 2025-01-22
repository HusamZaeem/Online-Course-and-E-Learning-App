package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinecourseande_learningapp.databinding.ActivityMentorsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MentorsActivity extends AppCompatActivity {

    private ActivityMentorsBinding binding;
    private MentorAdapterSearch mentorAdapterSearch;
    private RecentSearchAdapterHome recentSearchAdapter;
    private final List<String> recentSearches = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SearchPreferences";
    private static final String KEY_RECENT_SEARCHES = "recent_searches";
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMentorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        setupRecyclerViews();
        loadAllMentors();
        setupSearchBar();
        setupToolbar();

        binding.tvClearAll.setOnClickListener(v -> clearAllSearches());
        loadRecentSearches();
        toggleRecyclerViewVisibility();
    }

    private void loadAllMentors() {
        appViewModel.getAllMentors().observe(this, mentors -> {
            mentorAdapterSearch.updateData(mentors);
        });
    }

    private void setupToolbar() {
        binding.toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerViews() {
        binding.mentorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mentorAdapterSearch = new MentorAdapterSearch(this, new ArrayList<>());
        binding.mentorsRecyclerView.setAdapter(mentorAdapterSearch);

        // RecyclerView for recent searches
        binding.recentSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentSearchAdapter = new RecentSearchAdapterHome(
                recentSearches,
                this::onRecentSearchClick,
                this::onDeleteSearch
        );
        binding.recentSearchRecyclerView.setAdapter(recentSearchAdapter);
    }

    private void setupSearchBar() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = v.getText().toString().trim();
                performSearch(query);
                hideKeyboard(binding.etSearch);
                return true;
            }
            return false;
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    binding.recentSearchRecyclerView.setVisibility(View.GONE);
                    updateMentorList(query);
                } else {
                    loadRecentSearches();
                    toggleRecyclerViewVisibility();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            toggleSearchBar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSearchBar() {
        if (binding.etSearch.getVisibility() == View.GONE) {
            binding.etSearch.setVisibility(View.VISIBLE);
            binding.etSearch.requestFocus();
            showKeyboard(binding.etSearch);
            binding.toolbar.setTitle("");
            binding.toolbar.setNavigationIcon(null);
        } else {
            // Hide the EditText in the toolbar
            binding.etSearch.setVisibility(View.GONE);
            hideKeyboard(binding.etSearch);
            binding.toolbar.setTitle("All Mentors");
            binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        }
    }



    private void performSearch(String query) {
        if (!query.isEmpty()) {
            saveSearch(query);
            updateMentorList(query);
            binding.recentSearchRecyclerView.setVisibility(View.GONE);
        }
    }

    private void saveSearch(String searchQuery) {
        if (!searchQuery.isEmpty() && !recentSearches.contains(searchQuery)) {
            recentSearches.add(0, searchQuery);
            sharedPreferences.edit().putString(KEY_RECENT_SEARCHES, String.join(",", recentSearches)).apply();
            recentSearchAdapter.notifyDataSetChanged();
            toggleRecyclerViewVisibility();
        }
    }

    private void loadRecentSearches() {
        String savedSearches = sharedPreferences.getString(KEY_RECENT_SEARCHES, "");
        if (!savedSearches.isEmpty()) {
            Set<String> uniqueSearches = new HashSet<>(Arrays.asList(savedSearches.split(",")));
            recentSearches.clear();
            recentSearches.addAll(uniqueSearches);
        }
        recentSearchAdapter.notifyDataSetChanged();
    }

    private void clearAllSearches() {
        recentSearches.clear();
        sharedPreferences.edit().remove(KEY_RECENT_SEARCHES).apply();
        recentSearchAdapter.notifyDataSetChanged();
        toggleRecyclerViewVisibility();
    }

    private void toggleRecyclerViewVisibility() {
        if (recentSearches.isEmpty()) {
            binding.recentSearchRecyclerView.setVisibility(View.GONE);
            binding.tvClearAll.setVisibility(View.GONE);
        } else {
            binding.recentSearchRecyclerView.setVisibility(View.VISIBLE);
            binding.tvClearAll.setVisibility(View.VISIBLE);
        }
    }

    private void onDeleteSearch(int position) {
        if (position >= 0 && position < recentSearches.size()) {
            recentSearches.remove(position);
            sharedPreferences.edit().putString(KEY_RECENT_SEARCHES, String.join(",", recentSearches)).apply();
            recentSearchAdapter.notifyItemRemoved(position);
            toggleRecyclerViewVisibility();
        }
    }

    private void onRecentSearchClick(String searchText) {
        binding.etSearch.setText(searchText);
        updateMentorList(searchText);
    }

    private void updateMentorList(String query) {
        AppViewModel.getInstance(this.getApplication()).getFilteredMentors(query, new AppViewModel.MentorFilterCallback() {
            @Override
            public void onFilterComplete(List<Mentor> filteredMentors) {
                if (filteredMentors.isEmpty()) {
                    binding.noResultsImage.setVisibility(View.VISIBLE);
                    binding.mentorsRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.noResultsImage.setVisibility(View.GONE);
                    binding.mentorsRecyclerView.setVisibility(View.VISIBLE);
                    mentorAdapterSearch.updateData(filteredMentors);
                }
            }
        });
    }




    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
