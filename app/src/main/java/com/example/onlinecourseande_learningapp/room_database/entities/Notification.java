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

@Entity(tableName = "Notification",
        foreignKeys = @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "student_id")
    )
public class Notification implements Syncable {


    @PrimaryKey
    @NonNull
    private String notification_id="";
    private String student_id;
    private String content;
    private Date timestamp;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Notification(){}

    public Notification(@NonNull String notification_id, String student_id, String content, Date timestamp, boolean is_synced, Date last_updated) {
        this.notification_id = notification_id;
        this.student_id = student_id;
        this.content = content;
        this.timestamp = timestamp;
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
        repository.updateNotification(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertNotification(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return notification_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.notification_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("content", content);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(@NonNull String notification_id) {
        this.notification_id = notification_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
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
