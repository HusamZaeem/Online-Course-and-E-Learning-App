package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;

@Dao
public interface StudentLessonDao {



    @Insert
    void insertStudentLesson(StudentLesson studentLesson);

    // Update completion status
    @Query("UPDATE StudentLesson SET completion_status = :status WHERE student_id = :studentId AND lesson_id = :lessonId")
    void updateCompletionStatus(int studentId, int lessonId, String status);

    // check completion status of a specific lesson for a student
    @Query("SELECT completion_status FROM StudentLesson WHERE student_id = :studentId AND lesson_id = :lessonId")
    String getCompletionStatus(int studentId, int lessonId);



}
