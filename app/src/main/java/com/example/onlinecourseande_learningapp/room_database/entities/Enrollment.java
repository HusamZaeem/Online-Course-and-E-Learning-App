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

@Entity(tableName = "Enrollment",
        foreignKeys = {@ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE),
                       @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE)
    },
        indices = {
            @Index(value = "student_id"),
            @Index(value = "course_id")
        }
    )
public class Enrollment implements Syncable {


    @PrimaryKey
    @NonNull
    private String enrollment_id="";
    private String student_id;
    private String course_id;
    private int progress;
    private String status;
    private Date completion_date;
    private String certificate_url;
    private Double final_grade;
    private Date timestamp;
    private double fee;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Enrollment(){
    }

    public Enrollment(@NonNull String enrollment_id, String student_id, String course_id, int progress, String status, Date completion_date, String certificate_url, Double final_grade, Date timestamp, double fee, boolean is_synced,Date last_updated ) {
        this.enrollment_id=enrollment_id;
        this.student_id = student_id;
        this.course_id = course_id;
        this.progress = progress;
        this.status = status;
        this.completion_date = completion_date;
        this.certificate_url = certificate_url;
        this.final_grade = final_grade;
        this.timestamp = timestamp;
        this.fee = fee;
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
        repository.updateEnrollment(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertEnrollment(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return enrollment_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.enrollment_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("enrollment_id", enrollment_id);
        map.put("student_id", student_id);
        map.put("course_id", course_id);
        map.put("progress", progress);
        map.put("status", status);
        map.put("completion_date", Converter.toFirestoreTimestamp(completion_date));
        map.put("certificate_url", certificate_url);
        map.put("final_grade", final_grade);
        map.put("fee", fee);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getEnrollment_id() {
        return enrollment_id;
    }

    public void setEnrollment_id(@NonNull String enrollment_id) {
        this.enrollment_id = enrollment_id;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(Date completion_date) {
        this.completion_date = completion_date;
    }

    public String getCertificate_url() {
        return certificate_url;
    }

    public void setCertificate_url(String certificate_url) {
        this.certificate_url = certificate_url;
    }

    public Double getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(Double final_grade) {
        this.final_grade = final_grade;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
