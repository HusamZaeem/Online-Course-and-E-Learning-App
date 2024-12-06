package com.example.onlinecourseande_learningapp.room_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {



    @Insert
    void insertChat (Chat chat);

    @Update
    void updateChat (Chat chat);

    @Delete
    void deleteChat (Chat chat);

    @Query("SELECT * FROM Chat")
    LiveData<List<Chat>> getAllChats();

    @Query("SELECT * FROM Chat WHERE chat_id = :chat_id")
    LiveData<List<Chat>> getChatById(int chat_id);


}
