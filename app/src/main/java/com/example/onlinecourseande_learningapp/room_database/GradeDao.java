package com.example.onlinecourseande_learningapp.room_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GradeDao {


    @Insert
    void insertGrade (Grade grade);

    @Update
    void updateGrade (Grade grade);

    @Delete
    void deleteGrade (Grade grade);

    @Query("SELECT * FROM Grade")
    LiveData<List<Grade>> getAllGrades();

    @Query("SELECT * FROM Grade WHERE grade_id = :grade_id")
    LiveData<List<Grade>> getGradeById(int grade_id);


}
