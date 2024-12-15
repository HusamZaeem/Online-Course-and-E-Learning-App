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

    @Query("SELECT * FROM Message WHERE group_id = :group_id")
    LiveData<List<Message>> getGroupMessagesByGroupId(int group_id);

    @Query("SELECT * FROM Message WHERE chat_id = :chat_id")
    LiveData<List<Message>> getChatMessagesByChatId(int chat_id);

    @Query("SELECT * FROM Message WHERE message_type = :message_type")
    LiveData<List<Message>> getChatMessagesByMessageType(String message_type);


    @Query("SELECT * FROM Message WHERE content LIKE '%' || :content || '%'")
    LiveData<List<Message>> searchMessagesByContent(String content);

}
