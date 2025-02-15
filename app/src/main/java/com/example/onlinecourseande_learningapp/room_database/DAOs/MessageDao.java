package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage (Message message);

    @Update
    void updateMessage (Message message);

    @Delete
    void deleteMessage (Message message);

    @Query("SELECT * FROM Message")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM Message")
    List<Message> getAllMessagesList();

    @Query("SELECT * FROM Message WHERE message_id = :message_id")
    LiveData<List<Message>> getMessageById(String message_id);

    @Query("SELECT * FROM Message WHERE group_id = :group_id")
    LiveData<List<Message>> getGroupMessagesByGroupId(String group_id);

    @Query("SELECT * FROM Message WHERE chat_id = :chat_id")
    LiveData<List<Message>> getChatMessagesByChatId(String chat_id);

    @Query("SELECT * FROM Message WHERE message_type = :message_type")
    LiveData<List<Message>> getChatMessagesByMessageType(String message_type);


    @Query("SELECT * FROM Message WHERE content LIKE '%' || :content || '%'")
    LiveData<List<Message>> searchMessagesByContent(String content);



    @Query("SELECT * FROM Message WHERE message_id = :message_id")
    Message getMessageByMessageId(String message_id);

    @Query("SELECT * FROM Message WHERE is_synced = 0")
    List<Message> getUnsyncedMessage();


    @Query("SELECT * FROM Message WHERE chat_id = :chat_id ORDER BY timestamp DESC LIMIT 1")
    LiveData<Message> getLastMessageForChat(String chat_id);



    @Query("SELECT * FROM Message WHERE group_id = :groupId ORDER BY timestamp DESC LIMIT 1")
    LiveData<Message> getLastMessageForGroup(String groupId);


    @Query("SELECT * FROM Message WHERE chat_id = :chatId AND sender_id != :currentUserId AND status < 2")
    LiveData<List<Message>> getUnreadMessages(String chatId, String currentUserId);

    @Query("SELECT * FROM Message WHERE chat_id = :chatId AND sender_id != :currentUserId AND status = 0")
    List<Message> getSentMessagesForChat(String chatId, String currentUserId);

}
