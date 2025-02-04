package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.AppDatabase;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentLessonDao;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder> {
    private Context context;
    private List<Lesson> lessons;
    private Module module;
    private String studentId;
    private AppViewModel appViewModel;
    private int globalLessonIndex;
    private List<ModuleWithLessons> originalModules;


    public LessonsAdapter(Context context, List<Lesson> lessons, Module module, int startIndex, List<ModuleWithLessons> originalModules) {
        this.context = context;
        this.lessons = lessons;
        this.module = module;
        this.studentId = getStudentIdFromSharedPreferences();
        this.globalLessonIndex = startIndex;
        this.originalModules = originalModules;

        if (context instanceof FragmentActivity) {
            appViewModel = new ViewModelProvider((FragmentActivity) context).get(AppViewModel.class);
        } else {
            throw new IllegalArgumentException("Context must be an instance of FragmentActivity");
        }
    }




    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        // Find the original lesson index
        int originalLessonIndex = findOriginalLessonIndex(lesson);

        // Ensure the correct lesson number is displayed
        holder.tvLessonNumber.setText(String.format("%02d", originalLessonIndex));
        holder.tvLessonName.setText(lesson.getLesson_title());
        holder.tvLessonDuration.setText(lesson.getLesson_duration() + " mins");

        // Default: Lessons are locked
        holder.ivLessonIcon.setImageResource(R.drawable.baseline_lock_24);

        // Fetch module details
        Module module = this.module;

        // Check if the student is enrolled
        appViewModel.checkEnrollment(studentId, module.getCourse_id()).observe((FragmentActivity) context, isEnrolled -> {
            if (isEnrolled == null) {
                // Student is NOT enrolled, all lessons remain locked
                holder.ivLessonIcon.setImageResource(R.drawable.baseline_lock_24);
                holder.ivLessonIcon.setOnClickListener(v ->
                        Toast.makeText(context, "You must enroll in the course first!", Toast.LENGTH_SHORT).show());
                return;
            }

            // Student is enrolled, check lesson unlocking logic
            boolean isFirstModule = module.getModule_order() == 1;
            boolean isFirstLesson = lesson.getLesson_order() == 1 && isFirstModule;

            // Log lesson unlocking state for debugging
            Log.d("LessonUnlockDebug", "Checking lesson: " + lesson.getLesson_title() +
                    " (Index: " + position + ", Order: " + lesson.getLesson_order() +
                    ", GlobalIndex: " + originalLessonIndex + ")");

            if (isFirstLesson) {
                // Unlock the first lesson of the first module
                holder.ivLessonIcon.setImageResource(R.drawable.baseline_play_circle_24);
                holder.ivLessonIcon.setOnClickListener(v -> openLesson(lesson));
                Log.d("LessonUnlockDebug", "First lesson unlocked: " + lesson.getLesson_title());
            } else {
                if (position > 0) {
                    // Log to track the comparison with the previous lesson
                    Log.d("LessonUnlockDebug", "Checking completion of previous lesson: " + lessons.get(position - 1).getLesson_title());

                    // Check if the previous lesson is completed
                    appViewModel.getCompletionStatus(studentId, lessons.get(position - 1).getLesson_id())
                            .observe((FragmentActivity) context, isPrevLessonCompleted -> {
                                boolean isUnlocked = isPrevLessonCompleted != null && isPrevLessonCompleted;
                                holder.ivLessonIcon.setImageResource(isUnlocked ? R.drawable.baseline_play_circle_24 : R.drawable.baseline_lock_24);

                                // Log the unlocking decision
                                Log.d("LessonUnlockDebug", "Previous lesson completion: " + (isUnlocked ? "Unlocked" : "Locked"));

                                holder.ivLessonIcon.setOnClickListener(v -> {
                                    if (isUnlocked) {
                                        openLesson(lesson);
                                        Log.d("LessonUnlockDebug", "Lesson unlocked: " + lesson.getLesson_title());
                                    } else {
                                        Toast.makeText(context, "You must complete the previous lesson first!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return lessons.size();
    }

    private int findOriginalLessonIndex(Lesson lesson) {
        int index = 1;
        for (ModuleWithLessons module : originalModules) { // Use the full original list
            for (Lesson originalLesson : module.getLessons()) {
                if (originalLesson.getLesson_id().equals(lesson.getLesson_id())) {
                    return index; // Return the original index of the lesson
                }
                index++;
            }
        }
        return index; // Fallback (should not happen)
    }


    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonNumber, tvLessonName, tvLessonDuration;
        ImageView ivLessonIcon;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonNumber = itemView.findViewById(R.id.tvLessonNumber);
            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvLessonDuration = itemView.findViewById(R.id.tvLessonDuration);
            ivLessonIcon = itemView.findViewById(R.id.ivLessonIcon);
        }
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("student_id", null);
    }

    private void openLesson(Lesson lesson) {
        Intent intent = new Intent(context, LessonPlayerActivity.class);
        intent.putExtra("lesson_id", lesson.getLesson_id());
        intent.putExtra("content_url", lesson.getContent_url());
        intent.putExtra("student_id", studentId);
        context.startActivity(intent);
    }

}
