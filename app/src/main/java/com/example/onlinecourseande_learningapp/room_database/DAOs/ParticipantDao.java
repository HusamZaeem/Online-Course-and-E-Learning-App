package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Participant;

import java.util.List;

@Dao
public interface ParticipantDao {


    @Insert
    void insertParticipant (Participant participant);

    @Update
    void updateParticipant (Participant participant);

    @Delete
    void deleteParticipant (Participant participant);

    @Query("SELECT * FROM Participant")
    LiveData<List<Participant>> getAllParticipants();

    @Query("SELECT * FROM Participant WHERE participant_id = :participant_id")
    LiveData<List<Participant>> getParticipantById(int participant_id);


}
