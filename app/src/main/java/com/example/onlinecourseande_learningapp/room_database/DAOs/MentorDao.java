package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.util.List;

@Dao
public interface MentorDao {


    @Insert
    void insertMentor (Mentor mentor);

    @Update
    void updateMentor (Mentor mentor);

    @Delete
    void deleteMentor (Mentor mentor);

    @Query("SELECT * FROM Mentor")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM Mentor WHERE mentor_id = :mentor_id")
    LiveData<List<Mentor>> getMentorById(int mentor_id);


}
