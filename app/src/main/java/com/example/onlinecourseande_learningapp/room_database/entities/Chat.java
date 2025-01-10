package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.Syncable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Chat",
        foreignKeys = {@ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE),
                      @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "receiver_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = "sender_id"),
                    @Index(value = "receiver_id")
        })
public class Chat implements Syncable {

    @PrimaryKey
    @NonNull
    private String chat_id="";
    private String sender_id;
    private String receiver_id;
    private Date timestamp;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Chat(){}

    public Chat(@NonNull String chat_id, String sender_id, String receiver_id, Date timestamp, boolean is_synced,Date last_updated ) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = timestamp;
        this.is_synced=is_synced;
        this.last_updated=last_updated;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    @Override
    public void markAsSynced() {
        this.is_synced=true;
        this.last_updated=new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateChat(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertChat(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return chat_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.chat_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chat_id);
        map.put("sender_id", sender_id);
        map.put("receiver_id", receiver_id);
        map.put("is_synced", is_synced);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(@NonNull String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
