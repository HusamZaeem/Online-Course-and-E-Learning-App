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



}
