package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

@Dao
public interface StudentModuleDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudentModule(StudentModule studentModule);

    @Update
    void updateStudentModule(StudentModule studentModule);

    @Delete
    void deleteStudentModule(StudentModule studentModule);

    @Query("SELECT module_grade FROM StudentModule WHERE student_id = :student_id")
    LiveData<List<Double>> getAllStudentModuleGrade(String student_id);

    @Query("SELECT module_grade FROM StudentModule WHERE student_id = :student_id AND module_id = :module_id")
    LiveData<List<Double>> getStudentModuleGrade(String student_id, String module_id);


    @Query("UPDATE StudentModule SET module_grade = :module_grade WHERE student_id = :student_id AND module_id = :module_id")
    void setModuleGrade(String student_id, String module_id, double module_grade);

    @Query("SELECT * FROM StudentModule WHERE student_module_id = :student_module_id")
    StudentModule getStudentModuleById(String student_module_id);

    @Query("SELECT * FROM StudentModule WHERE is_synced = 0")
    List<StudentModule> getUnsyncedStudentModule();

}
