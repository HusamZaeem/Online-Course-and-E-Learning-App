package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notification",
        foreignKeys = @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "student_id")
    )
public class Notification {


    @PrimaryKey(autoGenerate = true)
    private int notification_id;
    private int student_id;
    private String content;
    private Date timestamp;


    public Notification(int student_id, String content, Date timestamp) {
        this.student_id = student_id;
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
        return student_id;
    }

    public void setUser_id(int user_id) {
        this.student_id = user_id;
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
