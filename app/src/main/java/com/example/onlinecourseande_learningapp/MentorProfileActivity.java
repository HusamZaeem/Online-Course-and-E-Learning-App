package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.databinding.ActivityMentorProfileBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class MentorProfileActivity extends AppCompatActivity {

    private ActivityMentorProfileBinding binding;
    private AppViewModel appViewModel;
    private MentorProfilePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        binding = ActivityMentorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Get mentor_id from Intent
        String mentorId = getIntent().getStringExtra("mentor_id");

        if (mentorId != null) {
            loadMentorData(mentorId);
        } else {
            Toast.makeText(this, "Invalid Mentor ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupTabLayoutAndViewPager();
        setupBackButton();
    }

    private void loadMentorData(String mentorId) {
        appViewModel.getMentorByIdLive(mentorId).observe(this, mentor -> {
            if (mentor != null) {
                // Bind mentor data to the views
                binding.mentorFullName.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
                binding.mentorJobTitle.setText(mentor.getJob_title());
                appViewModel.getMentorCourseCount(mentorId).observe(this, mentorCourseCout -> {
                    binding.numberOfCourses.setText(String.valueOf(mentorCourseCout));
                });
                appViewModel.getReviewCountForMentor(mentorId).observe(this, reviewCount -> {
                    binding.numberOfReviews.setText(String.valueOf(reviewCount));
                });
                binding.numberOfStudents.setText(String.valueOf(mentor.getStudents_taught()));

                ImageLoaderUtil.loadImageFromFirebaseStorage(this,mentor.getMentor_photo(),binding.ivProfileMentorPhoto);

                binding.websiteButton.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mentor.getWebsite()));
                    startActivity(intent);
                });
            }
        });
    }

    private void setupTabLayoutAndViewPager() {

        String mentorId = getIntent().getStringExtra("mentor_id");

        if (mentorId == null) {
            Toast.makeText(this, "Invalid Mentor ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Pass mentor_id to the pagerAdapter
        pagerAdapter = new MentorProfilePagerAdapter(this, mentorId);
        binding.viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Courses");
                    break;
                case 1:
                    tab.setText("Reviews");
                    break;
            }
        }).attach();
    }

    private void setupBackButton() {
        binding.backButton.setOnClickListener(v -> finish());
    }
}