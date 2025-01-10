package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;

import java.util.List;

@Dao
public interface ModuleDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModule (Module module);

    @Update
    void updateModule (Module module);

    @Delete
    void deleteModule (Module module);

    @Query("SELECT * FROM Module")
    LiveData<List<Module>> getAllModules ();

    @Query("SELECT * FROM Module WHERE module_id = :module_id")
    LiveData<List<Module>> getModuleById (String module_id);

    @Query("SELECT * FROM Module WHERE course_id = :course_id")
    LiveData<List<Module>> getAllCourseModules (String course_id);

    @Query("SELECT SUM(lesson_duration) FROM Lesson WHERE module_id = :module_id")
    int getModuleDuration(String module_id);

    @Query("UPDATE Module SET duration = (SELECT SUM(lesson_duration) FROM Lesson WHERE module_id = :module_id) WHERE module_id = :module_id")
    void updateModuleDuration(String module_id);


    @Query("SELECT * FROM Module WHERE module_id = :module_id")
    Module getModuleByModuleId(String module_id);

    @Query("SELECT * FROM Module WHERE is_synced = 0")
    List<Module> getUnsyncedModule();



}
