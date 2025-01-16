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
public class Message implements Syncable {


    @PrimaryKey
    @NonNull
    private String message_id="";
    private String chat_id;
    private String content;
    private Date timestamp;
    private String message_type;
    private String group_id;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Message(){}

    public Message(@NonNull String message_id, String chat_id, String content, Date timestamp, String message_type, String group_id, boolean is_synced, Date last_updated) {
        this.message_id = message_id;
        this.chat_id = chat_id;
        this.content = content;
        this.timestamp = timestamp;
        this.message_type = message_type;
        this.group_id = group_id;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
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
        repository.updateMessage(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertMessage(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return message_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.message_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chat_id);
        map.put("content", content);
        map.put("message_type", message_type);
        map.put("group_id", group_id);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(@NonNull String message_id) {
        this.message_id = message_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
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

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
