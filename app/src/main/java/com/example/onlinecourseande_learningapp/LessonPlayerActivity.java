package com.example.onlinecourseande_learningapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonPlayerActivity extends AppCompatActivity {
    private YouTubePlayerView youTubePlayerView;
    private String lessonId, lessonTitle, contentUrl, studentId, courseId;
    private AppViewModel appViewModel;
    private float lastPlaybackTime = 0f;
    private boolean isLessonCompleted = false;
    private boolean isFullscreen = false;
    private Handler handler = new Handler();
    private YouTubePlayer activeYouTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            enterFullScreenMode();
        }
        setContentView(R.layout.activity_lesson_player);

        // Initialize UI Components
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        // Initialize ViewModel
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Retrieve Data from Intent
        lessonId = getIntent().getStringExtra("lesson_id");
        contentUrl = getIntent().getStringExtra("content_url");
        studentId = getIntent().getStringExtra("student_id");
        courseId = getIntent().getStringExtra("course_id");

        appViewModel.getLessonById(lessonId).observe(this, lesson -> {
            lessonTitle = lesson.getLesson_title();
        });

        // Check if the lesson is already inserted before adding it
        appViewModel.isLessonAlreadyInserted(studentId, lessonId).observe(this, isInserted -> {
            if (Boolean.FALSE.equals(isInserted)) {
                String studentLessonId = UUID.randomUUID().toString();
                StudentLesson studentLesson = new StudentLesson(studentLessonId, studentId, lessonId, false);
                appViewModel.insertStudentLesson(studentLesson);
            }
        });

        // Restore playback position and start video
        restorePlaybackPosition();
        loadYouTubeVideo(contentUrl);


    }

    private void loadYouTubeVideo(String videoUrl) {
        String videoId = extractYouTubeVideoId(videoUrl);
        if (videoId == null) {
            Toast.makeText(this, "Invalid Video URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                if (activeYouTubePlayer == null) {
                    activeYouTubePlayer = youTubePlayer;
                    youTubePlayer.loadVideo(videoId, lastPlaybackTime);
                    youTubePlayer.play();
                }
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.ENDED && !isLessonCompleted) {
                    markLessonAsCompleted();
                }
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                lastPlaybackTime = second;
            }
        });
    }


    private void markLessonAsCompleted() {
        isLessonCompleted = true;
        appViewModel.updateLessonCompletionStatus(studentId, lessonId, true);

        // Update progress in Enrollment
        appViewModel.updateEnrollmentProgress(studentId, courseId);

        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You have successfully completed the lesson: " + lessonTitle + ".\n\nRedirecting to course page...")
                .setCancelable(false)
                .show();

        appViewModel.unlockNextLesson(studentId, lessonId);
        handler.postDelayed(this::returnToCoursePage, 5000);
    }


    private void returnToCoursePage() {
        if (courseId == null) {
            courseId = getIntent().getStringExtra("course_id");
        }

        if (courseId != null) {
            Intent intent = new Intent(this, CourseDetailsActivity.class);
            intent.putExtra("course_id", courseId);
            intent.putExtra("open_fragment", "LessonsFragment");
            startActivity(intent);
            finish();
        } else {
            Log.e("LessonPlayerActivity", "courseId is null, cannot return to CourseDetailsActivity");
            Toast.makeText(this, "Error: Course details unavailable", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void restorePlaybackPosition() {
        SharedPreferences sharedPreferences = getSharedPreferences("PlaybackPrefs", MODE_PRIVATE);
        lastPlaybackTime = sharedPreferences.getFloat("lesson_" + lessonId, 0f);
        if (lastPlaybackTime < 0) {
            lastPlaybackTime = 0f;
        }
    }


    private void savePlaybackPosition() {
        SharedPreferences sharedPreferences = getSharedPreferences("PlaybackPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("lesson_" + lessonId, lastPlaybackTime);
        editor.apply();
    }

    private String extractYouTubeVideoId(String url) {
        if (url != null && !url.isEmpty()) {
            Pattern pattern = Pattern.compile("(?:youtube\\.com/(?:.*[?&]v=|embed/|v/|.*[?&]vi=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private void enterFullScreenMode() {
        if (isFullscreen) return; // Prevent multiple fullscreen calls

        isFullscreen = true;

        // Hide system UI (status bar, navigation bar)
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Ensure the activity is in landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }




    private void exitFullScreenMode() {
        if (!isFullscreen) return; // Prevent multiple calls causing glitches

        isFullscreen = false;

        // Restore portrait mode smoothly
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Show system UI again
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // Delay to prevent instant fullscreen re-entering glitch
        new Handler().postDelayed(() -> {
            Log.d("LessonPlayerActivity", "Exited Fullscreen Mode");
        }, 300);
    }



    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            exitFullScreenMode();
        } else {
            if (!isLessonCompleted) {
                savePlaybackPosition();
            }
            returnToCoursePage();
            super.onBackPressed();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
