package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

@Dao
public interface CourseDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse (Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourseList(List<Course> courseList);

    @Update
    void updateCourse (Course course);

    @Delete
    void deleteCourse (Course course);

    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCourses ();

    @Query("SELECT * FROM Course")
    List<Course> getAllCoursesList ();

    @Query("SELECT * FROM Course WHERE course_id = :course_id")
    Course getCourseById (String course_id);

    @Query("SELECT * FROM Course WHERE course_id = :course_id")
    LiveData<Course> getCourseByIdLiveData(String course_id);

    @Query("SELECT * FROM Course WHERE category = :category")
    Course getCoursesByCategory (String category);

    @Query("SELECT * FROM Course WHERE is_synced = 0")
    List<Course> getUnsyncedCourses();

}
