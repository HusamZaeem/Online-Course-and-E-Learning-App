package com.example.onlinecourseande_learningapp.room_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LessonDao {


    @Insert
    void insertLesson (Lesson lesson);

    @Update
    void updateLesson (Lesson lesson);

    @Delete
    void deleteLesson (Lesson lesson);

    @Query("SELECT * FROM Lesson")
    LiveData<List<Lesson>> getAllLessons ();

    @Query("SELECT * FROM Lesson WHERE lesson_id = :lesson_id")
    LiveData<List<Lesson>> getLessonById (int lesson_id);



}
