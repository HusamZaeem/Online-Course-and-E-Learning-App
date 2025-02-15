package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.MentorCourse;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;

import java.util.List;

@Dao
public interface MentorCourseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentorCourse(MentorCourse mentorCourse);
    @Update
    void updateMentorCourse(MentorCourse mentorCourse);
    @Delete
    void deleteMentorCourse(MentorCourse mentorCourse);


    @Query("SELECT mentor_id FROM MentorCourse WHERE course_id = :course_id")
    LiveData<List<String>> getAllCourseMentors(String course_id);

    @Query("SELECT course_id FROM MentorCourse WHERE mentor_id = :mentor_id")
    LiveData<List<String>> getAllMentorCourses(String mentor_id);

    @Query("SELECT * FROM MentorCourse")
    List<MentorCourse> getAllMentorsCourses();

    @Query("SELECT * FROM MentorCourse WHERE mentor_course_id = :mentor_course_id")
    MentorCourse getMentorCourseByMentorCourseId(String mentor_course_id);

    @Query("SELECT * FROM MentorCourse WHERE is_synced = 0")
    List<MentorCourse> getUnsyncedMentorCourse();

    @Transaction
    @Query("SELECT * FROM Mentor WHERE mentor_id IN (SELECT mentor_id FROM MentorCourse WHERE course_id = :courseId LIMIT 1)")
    LiveData<Mentor> getMentorsByCourseId(String courseId);


    @Query("SELECT COUNT(*) FROM MentorCourse WHERE mentor_id = :mentorId")
    LiveData<Integer> getMentorCourseCount(String mentorId);


    @Query("SELECT course_id FROM MentorCourse WHERE mentor_id = :mentorId")
    List<String> getCoursesForAMentor(String mentorId);


    @Query("SELECT mentor_id FROM MentorCourse WHERE course_id = :course_id")
    List<String> getAllCourseMentorsList(String course_id);

    @Query("SELECT mentor_id FROM MentorCourse WHERE course_id = :courseId")
    String getMentorIdByCourseId(String courseId);

}
