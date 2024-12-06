package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;

import java.util.List;

@Dao
public interface EnrollmentDao {


    @Insert
    void insertEnrollment (Enrollment enrollment);

    @Update
    void updateEnrollment (Enrollment enrollment);

    @Delete
    void deleteEnrollment (Enrollment enrollment);

    @Query("SELECT * FROM Enrollment")
    LiveData<List<Enrollment>> getAllEnrollments();

    @Query("SELECT * FROM Enrollment WHERE enrollment_id = :enrollment_id")
    LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id);


}
