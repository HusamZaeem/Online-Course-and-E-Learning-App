package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.Syncable;
import com.example.onlinecourseande_learningapp.room_database.AppRepository;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Mentor")
public class Mentor implements Syncable {

    @PrimaryKey
    @NonNull
    private String mentor_id="";
    private String job_title;
    private String website;
    private int students_taught;
    private String mentor_email;
    private String mentor_password;
    private String mentor_fName;
    private String mentor_lName;
    private String mentor_photo;
    private double mentor_rating;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Mentor() {
    }


    public Mentor(@NonNull String mentor_id, String job_title, String website, int students_taught, String mentor_email, String mentor_password, String mentor_fName, String mentor_lName, String mentor_photo, double mentor_rating, boolean is_synced, Date last_updated) {
        this.mentor_id = mentor_id;
        this.job_title = job_title;
        this.website = website;
        this.students_taught = students_taught;
        this.mentor_email = mentor_email;
        this.mentor_password = mentor_password;
        this.mentor_fName = mentor_fName;
        this.mentor_lName = mentor_lName;
        this.mentor_photo = mentor_photo;
        this.mentor_rating = mentor_rating;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    public double getMentor_rating() {
        return mentor_rating;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    @NonNull
    public String getMentor_id() {
        return mentor_id;
    }

    @Override
    public void markAsSynced() {
        this.is_synced=true;
        this.last_updated=new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateMentor(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertMentor(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return mentor_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.mentor_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mentor_id", mentor_id);
        map.put("job_title", job_title);
        map.put("website", website);
        map.put("students_taught", students_taught);
        map.put("mentor_email", mentor_email);
        map.put("mentor_password", mentor_password);
        map.put("mentor_fName", mentor_fName);
        map.put("mentor_lName", mentor_lName);
        map.put("mentor_photo", mentor_photo);
        map.put("mentor_rating", mentor_rating);
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }




    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public void setMentor_rating(double mentor_rating) {
        this.mentor_rating = mentor_rating;
    }



    public void setMentor_id(@NonNull String mentor_id) {
        this.mentor_id = mentor_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getStudents_taught() {
        return students_taught;
    }

    public void setStudents_taught(int students_taught) {
        this.students_taught = students_taught;
    }

    public String getMentor_email() {
        return mentor_email;
    }

    public void setMentor_email(String mentor_email) {
        this.mentor_email = mentor_email;
    }

    public String getMentor_password() {
        return mentor_password;
    }

    public void setMentor_password(String mentor_password) {
        this.mentor_password = mentor_password;
    }

    public String getMentor_fName() {
        return mentor_fName;
    }

    public void setMentor_fName(String mentor_fName) {
        this.mentor_fName = mentor_fName;
    }

    public String getMentor_lName() {
        return mentor_lName;
    }

    public void setMentor_lName(String mentor_lName) {
        this.mentor_lName = mentor_lName;
    }

    public String getMentor_photo() {
        return mentor_photo;
    }

    public void setMentor_photo(String mentor_photo) {
        this.mentor_photo = mentor_photo;
    }




}
