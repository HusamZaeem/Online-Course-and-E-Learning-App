package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Chat",
        foreignKeys = {@ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE),
                      @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "receiver_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = "sender_id"),
                    @Index(value = "receiver_id")
        })
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private int chat_id;
    private int sender_id;
    private int receiver_id;
    private Date timestamp;


    public Chat(int sender_id, int receiver_id, Date timestamp) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = timestamp;
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

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
