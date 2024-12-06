package com.example.onlinecourseande_learningapp.room_database;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Message",
        foreignKeys = {
                @ForeignKey(entity = Chat.class, parentColumns = "chat_id", childColumns = "chat_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "receiver_id",onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "chat_id"),
        @Index(value = "sender_id"),
        @Index(value = "receiver_id")
        }
        )
public class Message {


    @PrimaryKey(autoGenerate = true)
    private int message_id;
    private int chat_id;
    private int sender_id;

    @Nullable
    private Integer receiver_id;
    private String content;
    private Date timestamp;
    private String message_type;
    private String media_url;
    private double duration;
    private String status;


    public Message(int chat_id, int sender_id, @Nullable Integer receiver_id, String content, Date timestamp, String message_type, String media_url, double duration, String status) {
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.timestamp = timestamp;
        this.message_type = message_type;
        this.media_url = media_url;
        this.duration = duration;
        this.status = status;
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

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }


    @Nullable
    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(@Nullable Integer receiver_id) {
        this.receiver_id = receiver_id;
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

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
