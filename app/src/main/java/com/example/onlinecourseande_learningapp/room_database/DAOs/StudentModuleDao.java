package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

@Dao
public interface StudentModuleDao {


    @Insert
    void insertStudentModule(StudentModule studentMentor);


    @Query("SELECT module_grade FROM StudentModule WHERE student_id = :student_id")
    LiveData<List<Double>> getAllStudentModuleGrade(int student_id);

    @Query("SELECT module_grade FROM StudentModule WHERE student_id = :student_id AND module_id = :module_id")
    LiveData<List<Double>> getStudentModuleGrade(int student_id, int module_id);


}
