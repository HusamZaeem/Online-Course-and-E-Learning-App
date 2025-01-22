package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

@Dao
public interface MentorDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor (Mentor mentor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentorList(List<Mentor> mentorList);

    @Update
    void updateMentor (Mentor mentor);

    @Delete
    void deleteMentor (Mentor mentor);

    @Query("SELECT * FROM Mentor")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM Mentor")
    List<Mentor> getAllMentorsList();

    @Query("SELECT * FROM Mentor WHERE mentor_id = :mentor_id")
    Mentor getMentorById(String mentor_id);

    @Query("SELECT * FROM Mentor WHERE mentor_id = :mentor_id")
    LiveData<Mentor> getMentorByIdLive(String mentor_id);

    @Query("SELECT * FROM Mentor WHERE is_synced = 0")
    List<Mentor> getUnsyncedMentors();

}
