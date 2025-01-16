package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

@Dao
public interface StudentMentorDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudentMentor(StudentMentor studentMentor);

    @Update
    void updateStudentMentor(StudentMentor studentMentor);

    @Delete
    void deleteStudentMentor(StudentMentor studentMentor);

    @Query("SELECT student_id FROM StudentMentor WHERE mentor_id = :mentor_id")
    LiveData<List<String>> getAllMentorStudents(String mentor_id);

    @Query("SELECT mentor_id FROM StudentMentor WHERE student_id = :student_id")
    LiveData<List<String>> getAllStudentMentors(String student_id);

    @Query("SELECT * FROM StudentMentor WHERE student_mentor_id = :student_mentor_id")
    StudentMentor getStudentMentorById(String student_mentor_id);

    @Query("SELECT * FROM StudentMentor WHERE is_synced = 0")
    List<StudentMentor> getUnsyncedStudentMentor();

    @Query("SELECT * FROM StudentMentor")
    List<StudentMentor> getAllStudentMentor();

}
