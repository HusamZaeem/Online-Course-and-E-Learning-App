package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinecourseande_learningapp.databinding.ActivityOnboardingBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class Onboarding extends AppCompatActivity {

    ActivityOnboardingBinding binding;



    private OnBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        adapter = new OnBoardingAdapter(this);
        binding.vpOnboarding.setAdapter(adapter);

        // Attach dots indicator
        binding.dotsIndicatorOnboarding.attachTo(binding.vpOnboarding);

        // Set initial button text
        updateButtonText(binding.vpOnboarding.getCurrentItem());

        // Button click listener
        binding.btnNextOnboarding.setOnClickListener(v -> {
            if (binding.vpOnboarding.getCurrentItem() < adapter.getItemCount() - 1) {
                // Move to the next fragment
                binding.vpOnboarding.setCurrentItem(binding.vpOnboarding.getCurrentItem() + 1);
            } else {
                // Navigate to LoginActivity
                navigateToLogin();
            }
        });

        // ViewPager page change listener
        binding.vpOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonText(position);
            }
        });
    }

    private void updateButtonText(int position) {
        if (position == adapter.getItemCount() - 1) {
            // Last fragment
            binding.btnNextOnboarding.setText("Get Started");
        } else {
            // Other fragments
            binding.btnNextOnboarding.setText("Next");
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getBaseContext(), SignupLogin.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // smooth transition
        finish();
    }
}