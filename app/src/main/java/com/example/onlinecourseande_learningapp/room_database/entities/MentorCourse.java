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

@Entity(
        tableName = "MentorCourse",
        foreignKeys = {
                @ForeignKey(
                        entity = Mentor.class,
                        parentColumns = "mentor_id",
                        childColumns = "mentor_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Course.class,
                        parentColumns = "course_id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        indices = {
                @Index(value = "mentor_id"),
                @Index(value = "course_id"),
        }
)
public class MentorCourse implements Syncable {

    @NonNull
    @PrimaryKey
    private String mentor_course_id="";
    @NonNull
    private String mentor_id="";
    @NonNull
    private String course_id="";
    private boolean is_synced;
    private Date last_updated;

    @Ignore
    public MentorCourse(){}



    public MentorCourse(@NonNull String mentor_course_id, @NonNull String mentor_id, @NonNull String course_id) {
        this.mentor_course_id=mentor_course_id;
        this.mentor_id = mentor_id;
        this.course_id = course_id;
    }

    @Ignore
    public MentorCourse(@NonNull String mentor_course_id, @NonNull String mentor_id, @NonNull String course_id, boolean is_synced, Date last_updated) {
        this.mentor_course_id = mentor_course_id;
        this.mentor_id = mentor_id;
        this.course_id = course_id;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }




    @NonNull
    public String getMentor_course_id() {
        return mentor_course_id;
    }

    public void setMentor_course_id(@NonNull String mentor_course_id) {
        this.mentor_course_id = mentor_course_id;
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
        repository.updateMentorCourse(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertMentorCourse(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return mentor_course_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.mentor_course_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mentor_id", mentor_id);
        map.put("course_id", course_id);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(@NonNull String mentor_id) {
        this.mentor_id = mentor_id;
    }

    @NonNull
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(@NonNull String course_id) {
        this.course_id = course_id;
    }
}
