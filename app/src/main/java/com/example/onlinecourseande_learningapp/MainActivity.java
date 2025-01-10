package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.onlinecourseande_learningapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        if (savedInstanceState == null) {
            String fragment = getIntent().getStringExtra("FRAGMENT");
            if (fragment != null && fragment.equals("HOME")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, new HomeFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, new HomeFragment())
                        .commit();
            }
        }

        // Setup BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set Home selected by default
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;



            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_my_courses) {
                selectedFragment = new MyCoursesFragment();
            } else if (item.getItemId() == R.id.nav_inbox) {
                selectedFragment = new InboxFragment();
            } else if (item.getItemId() == R.id.nav_transactions) {
                selectedFragment = new TransactionsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }



            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}