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

    @Query("SELECT * FROM Chat WHERE group_id = :group_id")
    LiveData<Chat>getChatByGroupId(String group_id);


    @Query("SELECT * FROM Chat WHERE sender_id = :student_id")
    LiveData<List<Chat>> getAllStudentChats(String student_id);


    @Query("SELECT * FROM Chat WHERE is_synced = 0")
    List<Chat> getUnsyncedChat();

    @Query("SELECT * FROM Chat WHERE (sender_id = :senderId AND receiver_id = :receiverId) OR (sender_id = :receiverId AND receiver_id = :senderId) LIMIT 1")
    Chat findChatBetweenUsers(String senderId, String receiverId);


    @Query("SELECT * FROM Chat WHERE (sender_id = :userId OR receiver_id = :userId) OR (is_group_chat = 1 AND group_id IN (SELECT group_id FROM GroupMembership WHERE member_id = :userId))")
    LiveData<List<Chat>> getAllChatsIncludingGroups(String userId);




}
