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
        tableName = "StudentMentor",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Mentor.class,
                        parentColumns = "mentor_id",
                        childColumns = "mentor_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        indices = {
                @Index(value = "student_id"),
                @Index(value = "mentor_id"),
        }
)
public class StudentMentor implements Syncable {

    @NonNull
    @PrimaryKey
    private String student_mentor_id="";
    @NonNull
    private String student_id="";
    @NonNull
    private String mentor_id="";
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public StudentMentor(){}


    public StudentMentor(@NonNull String student_mentor_id, @NonNull String student_id, @NonNull String mentor_id, boolean is_synced, Date last_updated) {
        this.student_mentor_id = student_mentor_id;
        this.student_id = student_id;
        this.mentor_id = mentor_id;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    @NonNull
    public String getStudent_mentor_id() {
        return student_mentor_id;
    }

    public void setStudent_mentor_id(@NonNull String student_mentor_id) {
        this.student_mentor_id = student_mentor_id;
    }

    @NonNull
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(@NonNull String student_id) {
        this.student_id = student_id;
    }

    @NonNull
    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(@NonNull String mentor_id) {
        this.mentor_id = mentor_id;
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
        repository.updateStudentMentor(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertStudentMentor(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return student_mentor_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.student_mentor_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("mentor_id", mentor_id);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}
