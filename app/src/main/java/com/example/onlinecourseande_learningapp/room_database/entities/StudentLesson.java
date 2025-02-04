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
        tableName = "StudentLesson",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Lesson.class,
                        parentColumns = "lesson_id",
                        childColumns = "lesson_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        indices = {
                @Index(value = "student_id"),
                @Index(value = "lesson_id"),
        }
)
public class StudentLesson implements Syncable {

    @NonNull
    @PrimaryKey
    private String student_lesson_id="";
    @NonNull
    private String student_id="";
    @NonNull
    private String lesson_id="";
    private boolean completion_status;
    private boolean is_synced;
    private Date last_updated;




    @Ignore
    public StudentLesson(){}

    public StudentLesson(@NonNull String student_lesson_id, @NonNull String student_id, @NonNull String lesson_id, boolean completion_status, boolean is_synced, Date last_updated) {
        this.student_lesson_id = student_lesson_id;
        this.student_id = student_id;
        this.lesson_id = lesson_id;
        this.completion_status = completion_status;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    @Ignore
    public StudentLesson(@NonNull String student_lesson_id, @NonNull String student_id, @NonNull String lesson_id, boolean completion_status){
        this.student_lesson_id = student_lesson_id;
        this.student_id=student_id;
        this.lesson_id=lesson_id;
        this.completion_status=completion_status;
    }

    @NonNull
    public String getStudent_lesson_id() {
        return student_lesson_id;
    }

    public void setStudent_lesson_id(@NonNull String student_lesson_id) {
        this.student_lesson_id = student_lesson_id;
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
        repository.updateStudentLesson(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertStudentLesson(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return student_lesson_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.student_lesson_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("lesson_id", lesson_id);
        map.put("completion_status", completion_status);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }




    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(@NonNull String student_id) {
        this.student_id = student_id;
    }

    @NonNull
    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(@NonNull String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public boolean isCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(boolean completion_status) {
        this.completion_status = completion_status;
    }
}
