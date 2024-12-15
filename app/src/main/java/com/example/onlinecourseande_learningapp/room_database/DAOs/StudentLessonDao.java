package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;

@Dao
public interface StudentLessonDao {


    @Insert
    void insertStudentLesson(StudentLesson studentLesson);

    @Update
    void updateStudentLesson(StudentLesson studentLesson);

    @Delete
    void deleteStudentLesson(StudentLesson studentLesson);


    @Query("UPDATE StudentLesson SET completion_status = :completion_status WHERE student_id = :student_id AND lesson_id = :lesson_id")
    void updateCompletionStatus(int student_id, int lesson_id, boolean completion_status);


    @Query("SELECT completion_status FROM StudentLesson WHERE student_id = :student_id AND lesson_id = :lesson_id")
    String getCompletionStatus(int student_id, int lesson_id);


    @Query("UPDATE StudentLesson SET completion_status = :completion_status WHERE student_id = :student_id AND lesson_id = :lesson_id")
    void updateLessonCompletionStatus(int student_id, int lesson_id, boolean completion_status);

    // Update lesson status and progress
    @Transaction
    default void updateLessonStatusAndProgress(int student_id, int lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, int course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }
}
