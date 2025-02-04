package com.example.onlinecourseande_learningapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinecourseande_learningapp.databinding.ActivityCourseDetailsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseDetailsActivity extends AppCompatActivity {

    ActivityCourseDetailsBinding binding;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLifecycle().addObserver(binding.youtubePlayerView);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        String courseId = getIntent().getStringExtra("course_id");
        if (courseId == null) {
            Log.e("CourseDetailsActivity", "No course_id found, returning to previous page.");
            Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        if (savedInstanceState == null) {
            setCourseDetails(courseId);
        }


        String openFragment = getIntent().getStringExtra("open_fragment");
        if ("LessonsFragment".equals(openFragment)) {
            binding.vbCourseDetails.setCurrentItem(1);  // Open "Lessons" tab
        }


        CourseDetailsPagerAdapter adapter = new CourseDetailsPagerAdapter(this,courseId);
        binding.vbCourseDetails.setAdapter(adapter);
        binding.vbCourseDetails.setOffscreenPageLimit(3);


        new TabLayoutMediator(binding.courseDetailsTabLayout, binding.vbCourseDetails, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("About");
                    break;
                case 1:
                    tab.setText("Lessons");
                    break;
                case 2:
                    tab.setText("Reviews");
                    break;
            }
        }).attach();




    }


    public void setCourseDetails(String course_id){
        appViewModel.getCourseByIdLiveData(course_id).observe(this, course -> {
            String courseName = course.getCourse_name();
            String courseCategory = course.getCategory();
            String courseRating = String.valueOf(course.getCourse_rating());
            String courseActualPrice = String.format("$%.2f",course.getPrice());
            double courseDiscountPrice = course.getPrice() - (course.getPrice() * 0.4);
            String courseCurrentPrice = String.format("$%.2f", courseDiscountPrice);
            String courseStudentNo = String.valueOf(course.getStudents_count());
            String courseHours = String.valueOf(course.getHours());
            //ImageLoaderUtil.loadImageFromFirebaseStorage(getApplicationContext(), course.getPhoto_url(), binding.ivCourseDetailsCoursePhoto);
            binding.tvCourseDetailsCourseName.setText(courseName);
            binding.tvCourseDetailsCourseCategory.setText(courseCategory);
            binding.tvCourseDetailsCourseRating.setText(courseRating);
            binding.tvCourseDetailsCourseCurrentPrice.setText(courseCurrentPrice);
            binding.tvCourseDetailsCourseActualPrice.setText(courseActualPrice);
            binding.tvCourseDetailsCourseStudentsNo.setText(courseStudentNo + " Students");
            binding.tvCourseDetailsCourseHours.setText(courseHours + " Hours");



            appViewModel.getReviewCountForCourse(course_id).observe(this, reviewCount -> {
                if (reviewCount != null){
                    binding.tvCourseDetailsCourseReviewsNo.setText("(" + reviewCount + " reviews)");
                }else binding.tvCourseDetailsCourseReviewsNo.setText("0 reviews");
            });


            String courseIntro = course.getCourse_intro();


            String videoId = extractYouTubeVideoId(courseIntro);

            if (videoId != null && !videoId.isEmpty()) {
                loadYouTubeVideo(videoId);
            }else {
                Log.e("CourseDetailsActivity", "Video ID is null");
            }


        });


    }


    private String extractYouTubeVideoId(String url) {
        if (url != null && !url.isEmpty()) {
            Log.d("YouTube", "URL before extraction: " + url);  // Log URL for debugging
            // Simplified regex for YouTube video ID extraction
            Pattern pattern = Pattern.compile("(?:youtube\\.com/(?:.*[?&]v=|embed/|v/|.*[?&]vi=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String videoId = matcher.group(1);
                Log.d("YouTube", "Extracted Video ID: " + videoId);  // Log the extracted ID
                return videoId;
            }
        }
        Log.e("YouTube", "Video ID extraction failed for URL: " + url);  // Log failure
        return null;
    }





    private void loadYouTubeVideo(String videoId) {
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f);
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    enterFullScreenMode();  // Auto-enter fullscreen
                } else if (state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.ENDED) {
                    exitFullScreenMode(); // Exit fullscreen on pause or finish
                }
            }
        });
    }


    private void enterFullScreenMode() {
        // Force landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Hide the system bars (status and navigation bars)
        WindowInsetsControllerCompat insetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (insetsController != null) {
            insetsController.hide(WindowInsetsCompat.Type.systemBars());
            // Set behavior for showing transient bars if needed
            insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        // Additional fallback for aggressive hiding (Android versions before Android 10)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void exitFullScreenMode() {
        // Switch to portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Show the system bars (status and navigation bars)
        WindowInsetsControllerCompat insetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (insetsController != null) {
            insetsController.show(WindowInsetsCompat.Type.systemBars());
        }

        // Fallback for showing the system bars (Android versions before Android 10)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            exitFullScreenMode();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding.youtubePlayerView != null) {
            binding.youtubePlayerView.release();
        }
    }

}