package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;

import java.util.List;

@Dao
public interface AttachmentDao {



    @Insert
    void insertAttachment (Attachment attachment);

    @Update
    void updateAttachment (Attachment attachment);

    @Delete
    void deleteAttachment (Attachment attachment);

    @Query("SELECT * FROM Attachment")
    LiveData<List<Attachment>> getAllAttachments ();


    @Query(
            "SELECT Attachment.* " +
                    "FROM Attachment " +
                    "INNER JOIN Message ON Attachment.message_id = Message.message_id " +
                    "INNER JOIN Chat ON Message.chat_id = Chat.chat_id " +
                    "WHERE Chat.sender_id = :student_id"
    )
    List<Attachment> getStudentAttachmentsInChat(int student_id);



    @Query(
            "SELECT Attachment.* " +
                    "FROM Attachment " +
                    "INNER JOIN Message ON Attachment.message_id = Message.message_id " +
                    "INNER JOIN Chat ON Message.chat_id = Chat.chat_id " +
                    "WHERE Chat.sender_id = :student_id AND Chat.chat_id = :chat_id"
    )
    List<Attachment> getStudentAttachmentsInAChat(int student_id, int chat_id);



}
