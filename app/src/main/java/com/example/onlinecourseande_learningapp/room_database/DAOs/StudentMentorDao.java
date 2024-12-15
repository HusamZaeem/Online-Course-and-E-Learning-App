package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onlinecourseande_learningapp.room_database.entities.MentorCourse;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;

import java.util.List;

@Dao
public interface StudentMentorDao {


    @Insert
    void insertStudentMentor(StudentMentor studentMentor);


    @Query("SELECT student_id FROM StudentMentor WHERE mentor_id = :mentor_id")
    LiveData<List<Integer>> getAllMentorStudents(int mentor_id);



}
