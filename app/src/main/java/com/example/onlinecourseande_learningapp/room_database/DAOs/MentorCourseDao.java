package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MentorCourseDao {




    @Query("SELECT mentor_id FROM MentorCourse WHERE course_id = :course_id")
    LiveData<List<Integer>> getAllCourseMentors(int course_id);

    @Query("SELECT course_id FROM MentorCourse WHERE mentor_id = :mentor_id")
    LiveData<List<Integer>> getAllMentorCourses(int mentor_id);


}
