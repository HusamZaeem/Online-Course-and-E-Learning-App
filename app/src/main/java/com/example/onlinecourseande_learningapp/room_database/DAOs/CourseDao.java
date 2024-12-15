package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {


    @Insert
    void insertCourse (Course course);

    @Update
    void updateCourse (Course course);

    @Delete
    void deleteCourse (Course course);

    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCourses ();

    @Query("SELECT * FROM Course WHERE course_id = :course_id")
    Course getCourseById (int course_id);



}
