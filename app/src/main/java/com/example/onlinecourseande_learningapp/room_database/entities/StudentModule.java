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
        tableName = "StudentModule",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Module.class,
                        parentColumns = "module_id",
                        childColumns = "module_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },

        indices = {
                @Index(value = "student_id"),
                @Index(value = "module_id"),
        }
)
public class StudentModule implements Syncable {

    @NonNull
    @PrimaryKey
    private String student_module_id="";
    @NonNull
    private String student_id="";
    @NonNull
    private String module_id="";
    private double module_grade;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public StudentModule(){}

    public StudentModule(@NonNull String student_module_id, @NonNull String student_id, @NonNull String module_id, double module_grade, boolean is_synced, Date last_updated) {
        this.student_module_id = student_module_id;
        this.student_id = student_id;
        this.module_id = module_id;
        this.module_grade = module_grade;
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

    @NonNull
    public String getStudent_module_id() {
        return student_module_id;
    }

    public void setStudent_module_id(@NonNull String student_module_id) {
        this.student_module_id = student_module_id;
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateStudentModule(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertStudentModule(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return student_module_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.student_module_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("module_id", module_id);
        map.put("module_grade", module_grade);
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
    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public double getModule_grade() {
        return module_grade;
    }

    public void setModule_grade(double module_grade) {
        this.module_grade = module_grade;
    }
}
