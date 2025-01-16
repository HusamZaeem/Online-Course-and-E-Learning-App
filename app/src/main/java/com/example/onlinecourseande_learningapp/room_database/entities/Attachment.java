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

@Entity(tableName = "Attachment",
        foreignKeys = @ForeignKey(entity = Message.class,parentColumns = "message_id", childColumns = "message_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "message_id")
)
public class Attachment implements Syncable {


    @PrimaryKey
    @NonNull
    private String attachment_id="";
    private String media_url;
    private String message_id;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Attachment(){}

    public Attachment(@NonNull String attachment_id, String media_url, String message_id, boolean is_synced, Date last_updated) {
        this.attachment_id = attachment_id;
        this.media_url = media_url;
        this.message_id = message_id;
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
        repository.updateAttachment(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertAttachment(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return attachment_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.attachment_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("media_url", media_url);
        map.put("message_id", message_id);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(@NonNull String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }


}
