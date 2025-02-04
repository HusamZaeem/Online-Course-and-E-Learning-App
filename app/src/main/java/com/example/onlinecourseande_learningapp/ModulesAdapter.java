package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModulesAdapter extends RecyclerView.Adapter<ModulesAdapter.ModuleViewHolder> {
    private Context context;
    // The current list to display (possibly filtered)
    private List<ModuleWithLessons> modules;
    // Backup copy of the full list for filtering purposes
    private List<ModuleWithLessons> originalModules;
    private String studentId;
    private int lessonIndex = 1;

    public ModulesAdapter(Context context, List<ModuleWithLessons> modules) {
        this.context = context;
        this.modules = new ArrayList<>(modules); // Store a copy of the original list
        this.originalModules = new ArrayList<>(modules); // Maintain a copy for filtering
        this.studentId = getStudentIdFromSharedPreferences();
    }


    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        ModuleWithLessons moduleWithLessons = modules.get(position);
        Module module = moduleWithLessons.getModule();
        List<Lesson> lessons = moduleWithLessons.getLessons();
        if (lessons == null) {
            lessons = new ArrayList<>();
        }
        // Sort lessons by lesson_order
        lessons.sort(Comparator.comparingInt(Lesson::getLesson_order));

        holder.tvModuleName.setText(module.getModule_name());
        holder.tvModuleDuration.setText(module.getDuration() + " mins");

        // Create LessonsAdapter for the lessons in this module
        LessonsAdapter lessonsAdapter = new LessonsAdapter(context, lessons, module, lessonIndex, originalModules);
        holder.recyclerViewLessons.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewLessons.setAdapter(lessonsAdapter);

        lessonIndex += lessons.size();
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            modules = new ArrayList<>(originalModules); // Restore the original list
        } else {
            List<ModuleWithLessons> filteredModules = new ArrayList<>();

            for (ModuleWithLessons moduleWithLessons : originalModules) {
                List<Lesson> originalLessons = moduleWithLessons.getLessons();
                if (originalLessons == null) continue;

                List<Lesson> filteredLessons = new ArrayList<>();
                for (Lesson lesson : originalLessons) {
                    if (lesson.getLesson_title() != null &&
                            lesson.getLesson_title().toLowerCase().contains(query.toLowerCase())) {
                        filteredLessons.add(lesson);
                    }
                }

                if (!filteredLessons.isEmpty()) {
                    // Preserve lesson numbers by keeping the original lessons' positions
                    ModuleWithLessons filteredModule = new ModuleWithLessons(moduleWithLessons.getModule(), filteredLessons);
                    filteredModules.add(filteredModule);
                }
            }

            modules = filteredModules;
        }
        notifyDataSetChanged();
    }


    static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleName, tvModuleDuration;
        RecyclerView recyclerViewLessons;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvModuleName);
            tvModuleDuration = itemView.findViewById(R.id.tvModuleDuration);
            recyclerViewLessons = itemView.findViewById(R.id.recyclerViewLessons);
        }
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("student_id", null);
    }
}
