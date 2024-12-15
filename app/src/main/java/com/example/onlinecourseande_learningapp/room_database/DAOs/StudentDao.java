package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

@Dao
public interface StudentDao {


    @Insert
    void insertStudent (Student student);

    @Update
    void updateStudent (Student student);

    @Delete
    void deleteStudent (Student student);

    @Query("SELECT * FROM Student")
    LiveData<List<Student>> getAllStudents ();

    @Query("SELECT * FROM Student WHERE student_id = :student_id")
    LiveData<List<Student>> getStudentById (int student_id);

}
