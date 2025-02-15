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

@Entity(tableName = "Chat")
public class Chat implements Syncable {

    @PrimaryKey
    @NonNull
    private String chat_id="";
    private String sender_id;  // Used for one-to-one chats
    private String sender_type; // "Mentor" or "Student"
    private String receiver_id; // Used for one-to-one chats
    private String receiver_type; // "Mentor" or "Student"
    private String group_id; // Used for group chats
    private boolean is_group_chat; // New flag to differentiate chat types
    private Map<String, Boolean> typingStatus;
    private Date timestamp;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Chat(){}

    public Chat(@NonNull String chat_id, String sender_id, String sender_type, String receiver_id, String receiver_type, String group_id, boolean is_group_chat, Map<String, Boolean> typingStatus, Date timestamp, boolean is_synced, Date last_updated) {
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.group_id = group_id;
        this.is_group_chat = is_group_chat;
        this.typingStatus = typingStatus;
        this.timestamp = timestamp;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public boolean isIs_group_chat() {
        return is_group_chat;
    }

    public void setIs_group_chat(boolean is_group_chat) {
        this.is_group_chat = is_group_chat;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public Map<String, Boolean> getTypingStatus() {
        return typingStatus;
    }

    public void setTypingStatus(Map<String, Boolean> typingStatus) {
        this.typingStatus = typingStatus;
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
        map.put("sender_id", sender_id);
        map.put("sender_type", sender_type);
        map.put("receiver_id", receiver_id);
        map.put("receiver_type", receiver_type);
        map.put("group_id", group_id);
        map.put("is_group_chat", is_group_chat);
        map.put("typingStatus", typingStatus);
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
