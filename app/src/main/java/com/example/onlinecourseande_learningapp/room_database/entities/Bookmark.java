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

@Entity(tableName = "Bookmark",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "course_id"),
        @Index(value = "student_id")
        }
        )
public class Bookmark implements Syncable {

    @PrimaryKey
    @NonNull
    private String bookmark_id="";
    private String student_id;
    private String course_id;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Bookmark(){}


    public Bookmark(@NonNull String bookmark_id, String student_id, String course_id, boolean is_synced, Date last_updated) {
        this.bookmark_id = bookmark_id;
        this.student_id = student_id;
        this.course_id = course_id;
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
        repository.updateBookmark(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertBookmark(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return bookmark_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.bookmark_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("course_id", course_id);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(@NonNull String bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
