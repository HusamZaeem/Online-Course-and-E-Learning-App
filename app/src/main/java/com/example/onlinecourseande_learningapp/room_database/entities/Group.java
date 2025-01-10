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

@Entity(tableName = "Group",
    foreignKeys = @ForeignKey(entity = Course.class,parentColumns = "course_id",childColumns = "course_id",onDelete = ForeignKey.CASCADE),
    indices = @Index(value = "course_id")
)
public class Group implements Syncable {

    @PrimaryKey
    @NonNull
    private String group_id="";
    private String group_name;
    private String course_id;
    private boolean is_synced;
    private Date last_updated;

    @Ignore
    public Group() {
    }

    @Ignore
    public Group(String group_name, String course_id){
    }

    public Group(@NonNull String group_id, String group_name, String course_id, boolean is_synced,Date last_updated ) {
        this.group_id=group_id;
        this.group_name = group_name;
        this.course_id = course_id;
        this.is_synced=is_synced;
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
        repository.updateGroup(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertGroup(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return group_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.group_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("group_name", group_name);
        map.put("course_id", course_id);
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(@NonNull String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
