package com.example.onlinecourseande_learningapp.room_database;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notification",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "user_id")
    )
public class Notification {


    @PrimaryKey(autoGenerate = true)
    private int notification_id;
    private int user_id;
    private String content;
    private Date timestamp;


    public Notification(int user_id, String content, Date timestamp) {
        this.user_id = user_id;
        this.content = content;
        this.timestamp = timestamp;
    }


    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
}
