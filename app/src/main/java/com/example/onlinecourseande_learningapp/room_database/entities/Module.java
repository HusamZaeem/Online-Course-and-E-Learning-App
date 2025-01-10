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

@Entity(tableName = "Module",
        foreignKeys = @ForeignKey(entity = Course.class,parentColumns = "course_id", childColumns = "course_id",onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "course_id")
    )
public class Module implements Syncable {


    @PrimaryKey
    @NonNull
    private String module_id="";
    private String module_name;
    private String course_id;
    private int duration;
    private boolean is_synced;
    private Date last_updated;



    @Ignore
    public Module(){}

    public Module(@NonNull String module_id, String module_name, String course_id, int duration, boolean is_synced,Date last_updated ) {
        this.module_id=module_id;
        this.module_name = module_name;
        this.course_id = course_id;
        this.duration = duration;
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
        repository.updateModule(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertModule(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return module_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.module_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("module_id", module_id);
        map.put("module_name", module_name);
        map.put("course_id", course_id);
        map.put("duration", duration);
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(@NonNull String module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
