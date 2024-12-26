package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

@Dao
public interface StudentDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudent (Student student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudents(List<Student> students);

    @Update
    void updateStudent (Student student);

    @Query("UPDATE Student SET password = :newPassword WHERE email = :email")
    void updateStudentPassword(String email, String newPassword);

    @Delete
    void deleteStudent (Student student);

    @Query("SELECT * FROM Student")
    LiveData<List<Student>> getAllStudents ();

    @Query("SELECT * FROM Student WHERE student_id = :student_id")
    Student getStudentById (String student_id);

    @Query("SELECT * FROM Student WHERE email = :email LIMIT 1")
    LiveData<Student> getStudentByEmail (String email);

    @Query("SELECT * FROM Student WHERE is_synced = 0")
    List<Student> getUnsyncedStudents();



}
