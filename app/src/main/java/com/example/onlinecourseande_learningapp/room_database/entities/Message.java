package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Message",
        foreignKeys = {
                @ForeignKey(entity = Chat.class, parentColumns = "chat_id", childColumns = "chat_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Group.class, parentColumns = "group_id", childColumns = "group_id", onDelete = ForeignKey.CASCADE),
        },
        indices = {
        @Index(value = "chat_id"),
        @Index(value = "group_id")
        }
        )
public class Message {


    @PrimaryKey(autoGenerate = true)
    private int message_id;
    private int chat_id;
    private String content;
    private Date timestamp;
    private String message_type;
    private int group_id;


    public Message(int chat_id, String content, Date timestamp, String message_type, int group_id) {
        this.chat_id = chat_id;
        this.content = content;
        this.timestamp = timestamp;
        this.message_type = message_type;
        this.group_id = group_id;
    }


    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
