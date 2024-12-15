package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Module;

import java.util.List;

@Dao
public interface ModuleDao {


    @Insert
    void insertModule (Module module);

    @Update
    void updateModule (Module module);

    @Delete
    void deleteModule (Module module);

    @Query("SELECT * FROM Module")
    LiveData<List<Module>> getAllModules ();

    @Query("SELECT * FROM Module WHERE module_id = :module_id")
    LiveData<List<Module>> getModuleById (int module_id);

    @Query("SELECT * FROM Module WHERE course_id = :course_id")
    LiveData<List<Module>> getAllCourseModules (int course_id);

    @Query("SELECT SUM(lesson_duration) FROM Lesson WHERE module_id = :module_id")
    int getModuleDuration(int module_id);

    @Query("UPDATE Module SET duration = (SELECT SUM(lesson_duration) FROM Lesson WHERE module_id = :module_id) WHERE module_id = :module_id")
    void updateModuleDuration(int module_id);


}
