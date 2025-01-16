package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;

import java.util.List;

@Dao
public interface ChatDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat (Chat chat);

    @Update
    void updateChat (Chat chat);

    @Delete
    void deleteChat (Chat chat);

    @Query("SELECT * FROM Chat")
    LiveData<List<Chat>> getAllChats();

    @Query("SELECT * FROM Chat")
    List<Chat> getAllChatsList();

    @Query("SELECT * FROM Chat WHERE chat_id = :chat_id")
    Chat getChatById(String chat_id);


    @Query("SELECT * FROM Chat WHERE sender_id = :student_id")
    LiveData<List<Chat>> getAllStudentChats(String student_id);


    @Query("SELECT * FROM Chat WHERE is_synced = 0")
    List<Chat> getUnsyncedChat();

}
