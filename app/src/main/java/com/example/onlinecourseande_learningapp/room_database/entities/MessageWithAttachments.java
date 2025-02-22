package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class MessageWithAttachments {
    @Embedded
    public Message message;

    @Relation(
            parentColumn = "message_id",
            entityColumn = "message_id"
    )
    public List<Attachment> attachments;
}
