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

@Entity(tableName = "Course")
public class Course implements Syncable {


    @PrimaryKey
    @NonNull
    private String course_id="";
    private String course_name;
    private double price;
    private double hours;
    private double course_rating;
    private String description;
    private String tools_used;
    private int students_count;
    private String photo_url;
    private String category;
    private String course_intro;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Course(){}


    public Course(@NonNull String course_id, String course_name, double price, double hours, double course_rating, String description, String tools_used, int students_count, String photo_url, String category,String course_intro, boolean is_synced, Date last_updated) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.price = price;
        this.hours = hours;
        this.course_rating = course_rating;
        this.description = description;
        this.tools_used = tools_used;
        this.students_count = students_count;
        this.photo_url = photo_url;
        this.category = category;
        this.course_intro=course_intro;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    public String getCourse_intro() {
        return course_intro;
    }

    public void setCourse_intro(String course_intro) {
        this.course_intro = course_intro;
    }

    public double getCourse_rating() {
        return course_rating;
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
        this.last_updated = new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateCourse(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertCourse(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return course_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.course_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("course_name", course_name);
        map.put("price", price);
        map.put("hours", hours);
        map.put("course_rating", course_rating);
        map.put("description", description);
        map.put("tools_used", tools_used);
        map.put("students_count", students_count);
        map.put("photo_url", photo_url);
        map.put("category", category);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }


    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public void setCourse_rating(double course_rating) {
        this.course_rating = course_rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    public String getCourse_id() {
        return course_id;
    }


    public void setCourse_id(@NonNull String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTools_used() {
        return tools_used;
    }

    public void setTools_used(String tools_used) {
        this.tools_used = tools_used;
    }

    public int getStudents_count() {
        return students_count;
    }

    public void setStudents_count(int students_count) {
        this.students_count = students_count;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
