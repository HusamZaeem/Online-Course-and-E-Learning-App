package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private AppViewModel appViewModel;

    private ImageView ivHomeUserProfilePicture;
    private ViewPager2 viewPagerAds;
    private RecyclerView mentorsRecyclerView, coursesRecyclerView;
    private TabLayout tabLayoutCategories;

    private List<Mentor> mentorList = new ArrayList<>();
    private List<Course> courseList = new ArrayList<>();
    private FirebaseFirestore db;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // UI Initialization
        ivHomeUserProfilePicture = view.findViewById(R.id.iv_home_user_profile_picture);
        viewPagerAds = view.findViewById(R.id.viewPagerAds);
        mentorsRecyclerView = view.findViewById(R.id.mentorsRecyclerView);
        tabLayoutCategories = view.findViewById(R.id.tabLayoutCategories);
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        TextView tvTopMentorSeeAll = view.findViewById(R.id.tv_top_mentor_see_all);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Load data
        loadProfilePhoto();
        setupViewPagerAds();
        setupMentorRecyclerView();
        setupTabLayoutCategories();
        setupCourseRecyclerView();


        // Handle "See All" Click
        tvTopMentorSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MentorsActivity.class);
            startActivity(intent);
        });

        return view;
    }


    private void loadProfilePhoto() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();  // Get Firebase UID

            if (isNetworkAvailable()) {
                // Fetch the profile photo URL from Firebase Firestore when online
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                DocumentReference studentDocRef = firestore.collection("Student").document(userId);

                studentDocRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String profilePhotoUrl = document.getString("profile_photo");

                            if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
                                // Sync the profile photo URL to the Room database
                                syncProfilePhotoToRoomDatabase(userId, profilePhotoUrl);

                                // Load the profile picture using Glide from the URL
                                Glide.with(getContext())
                                        .load(profilePhotoUrl)
                                        .placeholder(R.drawable.head_icon)  // Default placeholder image
                                        .into(ivHomeUserProfilePicture);   // Set the image in ImageView
                            } else {
                                ivHomeUserProfilePicture.setImageResource(R.drawable.head_icon);  // Default image
                            }
                        }
                    } else {
                        // Handle error if needed
                        Log.w("HomeFragment", "Error getting document: ", task.getException());
                    }
                });
            } else {
                // User is offline, fetch the profile photo URL from Room database
                loadProfilePhotoFromRoomDatabase(userId);
            }
        }
    }

    private void syncProfilePhotoToRoomDatabase(String userId, String profilePhotoUrl) {
        appViewModel.getStudentByIdLive(userId).observe(getViewLifecycleOwner(), student -> {
            if (student != null) {
                student.setProfile_photo(profilePhotoUrl);  // Set the profile photo URL
                appViewModel.updateStudent(student);  // Update in Room database
            }
        });
    }

    private void loadProfilePhotoFromRoomDatabase(String userId) {
        appViewModel.getStudentByIdLive(userId).observe(getViewLifecycleOwner(), student -> {
            if (student != null) {
                String profilePhotoUrl = student.getProfile_photo();
                if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
                    // Load the profile picture using Glide from the URL in Room database
                    Glide.with(getContext())
                            .load(profilePhotoUrl)
                            .placeholder(R.drawable.head_icon)  // Default placeholder image
                            .into(ivHomeUserProfilePicture);   // Set the image in ImageView
                } else {
                    ivHomeUserProfilePicture.setImageResource(R.drawable.head_icon);  // Default image if no URL is available
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private void setupViewPagerAds() {
        // Check Firebase connectivity and sync data
        syncAdsFromFirebase();

        // Observe LiveData from Room
        appViewModel.getAllAds().observe(getViewLifecycleOwner(), adList -> {
            if (adList != null) {
                // Update ViewPager with ads
                AdAdapter adAdapter = new AdAdapter(adList);
                viewPagerAds.setAdapter(adAdapter);
            }
        });
    }


    private void syncAdsFromFirebase() {
        db.collection("Ad").addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.e("FirestoreSync", "Error fetching ads", error);
                return;
            }

            if (snapshots != null) {
                List<Ad> adList = new ArrayList<>();
                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    Ad ad = document.toObject(Ad.class);
                    adList.add(ad);
                }

                // Save to Room database
                appViewModel.insertAdList(adList);
            }
        });
    }




    private void syncMentorsFromFirebase() {
        db.collection("Mentor").addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.e("FirestoreSync", "Error fetching mentors", error);
                return;
            }

            if (snapshots != null) {
                List<Mentor> mentorList = new ArrayList<>();
                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    Mentor mentor = document.toObject(Mentor.class);
                    mentorList.add(mentor);
                }

                // Save to Room database
                 appViewModel.insertMentorList(mentorList);
            }
        });
    }

    private void setupMentorRecyclerView() {
        syncMentorsFromFirebase();

        appViewModel.getAllMentors().observe(getViewLifecycleOwner(), mentorList -> {
            if (mentorList != null) {
                MentorAdapter mentorAdapter = new MentorAdapter(mentorList);
                mentorsRecyclerView.setAdapter(mentorAdapter);
            }
        });
    }



    private void setupTabLayoutCategories() {
        String[] categories = {"All", "Programming", "Design", "Marketing", "Finance", "Languages", "Health", "Business"};
        int[] icons = {R.drawable.flame_hot_icon, R.drawable.programming_icon, R.drawable.design_draw_drawing_icon,
                R.drawable.marketing_icon, R.drawable.finance_icon, R.drawable.language_icon,
                R.drawable.health_icon, R.drawable.business_icon};

        for (int i = 0; i < categories.length; i++) {
            TabLayout.Tab tab = tabLayoutCategories.newTab();
            tab.setText(categories[i]);
            tab.setIcon(icons[i]);
            tabLayoutCategories.addTab(tab);
        }

        tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterCoursesByCategory(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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
            CourseAdapter courseAdapter = new CourseAdapter(filteredCourses);
            coursesRecyclerView.setAdapter(courseAdapter);
        });
    }


    private void syncCoursesFromFirebase() {
        db.collection("Course").addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.e("FirestoreSync", "Error fetching courses", error);
                return;
            }

            if (snapshots != null) {
                List<Course> courseList = new ArrayList<>();
                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    Course course = document.toObject(Course.class);
                    courseList.add(course);
                }

                // Save to Room database
                appViewModel.insertCourseList(courseList);
            }
        });
    }

    private void setupCourseRecyclerView() {
        syncCoursesFromFirebase();

        appViewModel.getAllCourses().observe(getViewLifecycleOwner(), courseList -> {
            if (courseList != null) {
                CourseAdapter courseAdapter = new CourseAdapter(courseList);
                coursesRecyclerView.setAdapter(courseAdapter);
            }
        });
    }


}