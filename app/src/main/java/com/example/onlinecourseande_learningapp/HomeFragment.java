package com.example.onlinecourseande_learningapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Ad;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private AppViewModel appViewModel;

    private ImageView ivHomeUserProfilePicture, ivHomeNotification;
    private ViewPager2 viewPagerAds;
    private RecyclerView mentorsRecyclerView, coursesRecyclerView;
    private TabLayout tabLayoutCategories;
    private TextView tvUserFullName;
    private EditText et_home_search;
    private Handler adHandler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // UI Initialization
        ivHomeUserProfilePicture = view.findViewById(R.id.iv_home_user_profile_picture);
        ivHomeNotification = view.findViewById(R.id.iv_home_notification);
        viewPagerAds = view.findViewById(R.id.viewPagerAds);
        mentorsRecyclerView = view.findViewById(R.id.mentorsRecyclerView);
        tabLayoutCategories = view.findViewById(R.id.tabLayoutCategories);
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        tvUserFullName = view.findViewById(R.id.tv_home_user_name);
        et_home_search = view.findViewById(R.id.et_home_search);
        TextView tvHomeGreeting = view.findViewById(R.id.tv_home_greeting);
        TextView tvTopMentorSeeAll = view.findViewById(R.id.tv_top_mentor_see_all);
        TextView tvMostPopularCoursesSeeAll = view.findViewById(R.id.tv_most_popular_courses_see_all);
        ImageView ivHomeBookmarks = view.findViewById(R.id.iv_home_bookmarks);


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


        ivHomeNotification.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main, new NotificationFragment())
                    .addToBackStack(null)
                    .commit();
        });



        String greeting = getGreetingMessage();
        tvHomeGreeting.setText(greeting);

        // Load data
        loadProfilePhotoAndName();
        setupViewPagerAds();
        setupMentorRecyclerView();
        setupTabLayoutCategories();
        setupCourseRecyclerView();


        ivHomeBookmarks.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookmarksActivity.class);
            startActivity(intent);
        });

        et_home_search.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ((MainActivity) requireActivity()).loadSearchFragment();
            }
        });

        tvTopMentorSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MentorsActivity.class);
            startActivity(intent);
        });

        tvMostPopularCoursesSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllCoursesActivity.class);
            startActivity(intent);
        });

        return view;
    }


    private String getGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }


    private void loadProfilePhotoAndName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Observe Room database for profile photo
            appViewModel.getStudentByIdLive(userId).observe(getViewLifecycleOwner(), student -> {
                if (student != null) {
                    String profilePhotoUrl = student.getProfile_photo();
                    String fullName = student.getFirst_name() + " " + student.getLast_name();
                    if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
                        ImageLoaderUtil.loadImageFromFirebaseStorage(getContext(), profilePhotoUrl, ivHomeUserProfilePicture);
                    } else {
                        ivHomeUserProfilePicture.setImageResource(R.drawable.head_icon); // Default image
                    }
                    if (fullName != null && !fullName.isEmpty()) {
                        tvUserFullName.setText(fullName);
                    }
                }
            });
        }
    }

    private void setupViewPagerAds() {
        appViewModel.getAllAds().observe(getViewLifecycleOwner(), adList -> {
            if (adList != null && !adList.isEmpty()) {
                AdAdapter adAdapter = new AdAdapter(getContext(), adList);
                viewPagerAds.setAdapter(adAdapter);
                autoSlideAds(adList.size());
                Log.d(TAG, "Ads loaded: " + adList.size());
            } else {
                Log.w(TAG, "No ads available to display.");
            }
        });
    }

    private void autoSlideAds(int adCount) {
        Runnable adSlider = new Runnable() {
            int currentAd = 0;

            @Override
            public void run() {
                if (currentAd >= adCount) currentAd = 0;
                viewPagerAds.setCurrentItem(currentAd++, true);
                adHandler.postDelayed(this, 3000); // Slide every 3 seconds
            }
        };
        adHandler.post(adSlider);
    }

    private void setupMentorRecyclerView() {
        mentorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        appViewModel.getAllMentors().observe(getViewLifecycleOwner(), mentorList -> {
            Log.d(TAG, "Observer triggered with data: " + mentorList);
            if (mentorList != null && !mentorList.isEmpty()) {
                MentorAdapter mentorAdapter = new MentorAdapter(getContext(), mentorList);
                mentorsRecyclerView.setAdapter(mentorAdapter);
                Log.d(TAG, "Mentors loaded: " + mentorList.size());
            } else {
                Log.w(TAG, "No mentors available to display.");
            }
        });
    }

    private void setupTabLayoutCategories() {
        String[] categories = {"All", "Programming", "Design", "Marketing", "Finance", "Languages", "Health", "Business"};
        int[] icons = {R.drawable.icon_13, R.drawable.programming_icon, R.drawable.design_draw_drawing_icon,
                R.drawable.marketing_icon, R.drawable.finance_icon, R.drawable.language_icon,
                R.drawable.health_icon, R.drawable.business_icon};

        for (int i = 0; i < categories.length; i++) {
            TabLayout.Tab tab = tabLayoutCategories.newTab();
            tab.setText(categories[i]);
            Drawable icon = ContextCompat.getDrawable(getContext(), icons[i]);
            tabLayoutCategories.setTabIconTint(null);
            tab.setIcon(icon);
            tabLayoutCategories.addTab(tab);
        }


        tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void filterCoursesByCategory(String category) {
        appViewModel.getAllCourses().observe(getViewLifecycleOwner(), courseList -> {
            List<Course> filteredCourses = new ArrayList<>();
            if (category.equals("All")) {
                filteredCourses.addAll(courseList);
            } else {
                for (Course course : courseList) {
                    if (course.getCategory().equals(category)) {
                        filteredCourses.add(course);
                    }
                }
            }
            CourseAdapter courseAdapter = new CourseAdapter(getContext(), filteredCourses, appViewModel, getViewLifecycleOwner());
            coursesRecyclerView.setAdapter(courseAdapter);
        });
    }

    private void setupCourseRecyclerView() {
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appViewModel.getAllCourses().observe(getViewLifecycleOwner(), courseList -> {
            Log.d(TAG, "Observer triggered with data: " + courseList);
            if (courseList != null && !courseList.isEmpty()) {
                CourseAdapter courseAdapter = new CourseAdapter(getContext(), courseList, appViewModel, getViewLifecycleOwner());
                coursesRecyclerView.setAdapter(courseAdapter);
                Log.d(TAG, "Courses loaded: " + courseList.size());
            } else {
                Log.w(TAG, "No courses available to display.");
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adHandler.removeCallbacksAndMessages(null); // Clear pending messages
    }

}