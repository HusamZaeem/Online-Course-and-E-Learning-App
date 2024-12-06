package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Participant",
        foreignKeys = {
                @ForeignKey(entity = Chat.class, parentColumns = "chat_id", childColumns = "chat_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "chat_id"),
        @Index(value = "user_id")
        }
        )
public class Participant {


    @PrimaryKey(autoGenerate = true)
    private int participant_id;
    private int chat_id;
    private int user_id;
    private String role;


    public Participant(int chat_id, int user_id, String role) {
        this.chat_id = chat_id;
        this.user_id = user_id;
        this.role = role;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
