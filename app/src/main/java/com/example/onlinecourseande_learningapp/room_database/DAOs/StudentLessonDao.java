package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

@Dao
public interface StudentLessonDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudentLesson(StudentLesson studentLesson);

    @Update
    void updateStudentLesson(StudentLesson studentLesson);

    @Delete
    void deleteStudentLesson(StudentLesson studentLesson);


    @Query("UPDATE StudentLesson SET completion_status = :completion_status WHERE student_id = :student_id AND lesson_id = :lesson_id")
    void updateCompletionStatus(String student_id, String lesson_id, boolean completion_status);


    @Query("SELECT completion_status FROM StudentLesson WHERE student_id = :student_id AND lesson_id = :lesson_id")
    LiveData<Boolean> getCompletionStatus(String student_id, String lesson_id);


    @Query("UPDATE StudentLesson SET completion_status = :completion_status WHERE student_id = :student_id AND lesson_id = :lesson_id")
    void updateLessonCompletionStatus(String student_id, String lesson_id, boolean completion_status);

    // Update lesson status and progress
    @Transaction
    default void updateLessonStatusAndProgress(String student_id, String lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, String course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }

    @Query("SELECT EXISTS(SELECT 1 FROM StudentLesson WHERE student_id = :studentId AND lesson_id = :lessonId)")
    LiveData<Boolean> isLessonAlreadyInserted(String studentId, String lessonId);


    @Query("UPDATE StudentLesson SET completion_status = 1 WHERE student_id = :studentId AND lesson_id = :lessonId")
    void unlockNextLesson(String studentId, String lessonId);


    @Query("SELECT * FROM StudentLesson WHERE student_lesson_id = :student_lesson_id")
    StudentLesson getStudentLessonById(String student_lesson_id);

    @Query("SELECT * FROM StudentLesson WHERE is_synced = 0")
    List<StudentLesson> getUnsyncedStudentLesson();

    @Query("SELECT * FROM StudentLesson")
    List<StudentLesson> getAllStudentLesson();

    @Query("SELECT COUNT(*) FROM StudentLesson WHERE student_id = :studentId AND completion_status = 1")
    LiveData<Integer> getCompletedLessonsCount(String studentId);

    @Query("SELECT COUNT(*) FROM StudentLesson WHERE student_id = :studentId AND lesson_id IN (:lessonIds) AND completion_status = 1")
    int countCompletedLessons(String studentId, List<String> lessonIds);



}
