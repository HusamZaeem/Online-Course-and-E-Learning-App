package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Message;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert
    void insertMessage (Message message);

    @Update
    void updateMessage (Message message);

    @Delete
    void deleteMessage (Message message);

    @Query("SELECT * FROM Message")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM Message WHERE message_id = :message_id")
    LiveData<List<Message>> getMessageById(int message_id);


}
