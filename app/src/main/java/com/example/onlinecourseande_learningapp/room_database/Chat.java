package com.example.onlinecourseande_learningapp.room_database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Chat")
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private int chat_id;
    private String chat_type;
    private String chat_name;


    public Chat(String chat_type, String chat_name) {
        this.chat_type = chat_type;
        this.chat_name = chat_name;
    }


    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }
}
