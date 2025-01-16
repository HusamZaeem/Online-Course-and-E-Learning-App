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

@Entity(tableName = "Review",
        foreignKeys = @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "student_id")
    )
public class Review implements Syncable {

    @PrimaryKey
    @NonNull
    private String review_id="";
    private String student_id;
    private String target_id;
    private String type;
    private double rate;
    private String comment;
    private Date timestamp;
    private boolean is_synced;
    private Date last_updated;



    @Ignore
    public Review(){}


    public Review(@NonNull String review_id, String student_id, String target_id, String type, double rate, String comment, Date timestamp, boolean is_synced, Date last_updated) {
        this.review_id = review_id;
        this.student_id = student_id;
        this.target_id = target_id;
        this.type = type;
        this.rate = rate;
        this.comment = comment;
        this.timestamp = timestamp;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    @Ignore
    public Review(String student_id, String target_id, String type, double rate, String comment) {
        this.student_id = student_id;
        this.target_id = target_id;
        this.type = type;
        this.rate = rate;
        this.comment = comment;
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
        repository.updateReview(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertReview(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return review_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.review_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("target_id", target_id);
        map.put("type", type);
        map.put("rate", rate);
        map.put("comment", comment);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(@NonNull String review_id) {
        this.review_id = review_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
