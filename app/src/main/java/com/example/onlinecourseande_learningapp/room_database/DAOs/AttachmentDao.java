package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;

import java.util.List;

@Dao
public interface AttachmentDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttachment (Attachment attachment);

    @Update
    void updateAttachment (Attachment attachment);

    @Delete
    void deleteAttachment (Attachment attachment);

    @Query("SELECT * FROM Attachment")
    LiveData<List<Attachment>> getAllAttachments ();

    @Query("SELECT * FROM Attachment")
    List<Attachment> getAllAttachmentsList ();


    @Query(
            "SELECT Attachment.* " +
                    "FROM Attachment " +
                    "INNER JOIN Message ON Attachment.message_id = Message.message_id " +
                    "INNER JOIN Chat ON Message.chat_id = Chat.chat_id " +
                    "WHERE Chat.sender_id = :student_id"
    )
    List<Attachment> getStudentAttachmentsInChat(String student_id);



    @Query(
            "SELECT Attachment.* " +
                    "FROM Attachment " +
                    "INNER JOIN Message ON Attachment.message_id = Message.message_id " +
                    "INNER JOIN Chat ON Message.chat_id = Chat.chat_id " +
                    "WHERE Chat.sender_id = :student_id AND Chat.chat_id = :chat_id"
    )
    List<Attachment> getStudentAttachmentsInAChat(String student_id, String chat_id);


    @Query("SELECT * FROM Attachment WHERE attachment_id = :attachment_id")
    Attachment getAttachmentByAttachmentId(String attachment_id);

    @Query("SELECT * FROM Attachment WHERE is_synced = 0")
    List<Attachment> getUnsyncedAttachment();


    @Query("SELECT * FROM Attachment WHERE attachment_id = :attachmentId LIMIT 1")
    LiveData<Attachment> getAttachmentById(String attachmentId);


    @Query("SELECT * FROM Attachment WHERE message_id = :message_id LIMIT 1")
    LiveData<Attachment> getAttachmentByMessageId(String message_id);

}
