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

@Entity(tableName = "Lesson",
        foreignKeys = @ForeignKey(entity = Module.class, parentColumns = "module_id", childColumns = "module_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "module_id")
        )
public class Lesson implements Syncable {

    @PrimaryKey
    @NonNull
    private String lesson_id="";
    private String module_id;
    private String lesson_title;
    private int lesson_duration;
    private String content_url;
    private boolean is_exam;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Lesson(){}


    public Lesson(@NonNull String lesson_id, String module_id, String lesson_title, int lesson_duration, String content_url, boolean is_exam, boolean is_synced,Date last_updated ) {
        this.lesson_id=lesson_id;
        this.module_id = module_id;
        this.lesson_title = lesson_title;
        this.lesson_duration = lesson_duration;
        this.content_url = content_url;
        this.is_exam = is_exam;
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
        repository.updateLesson(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertLesson(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return lesson_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.lesson_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("lesson_id", lesson_id);
        map.put("module_id", module_id);
        map.put("lesson_title", lesson_title);
        map.put("lesson_duration", lesson_duration);
        map.put("content_url", content_url);
        map.put("is_exam", is_exam);
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(@NonNull String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public void setLesson_title(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public int getLesson_duration() {
        return lesson_duration;
    }

    public void setLesson_duration(int lesson_duration) {
        this.lesson_duration = lesson_duration;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public boolean isIs_exam() {
        return is_exam;
    }

    public void setIs_exam(boolean is_exam) {
        this.is_exam = is_exam;
    }
}
