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

@Entity(tableName = "Student")
public class Student implements Syncable {

    @NonNull
    @PrimaryKey
    private String student_id="";
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phone;
    private String profile_photo;
    private Date date_of_birth;
    private boolean is_synced;
    private Date last_updated;



    @Ignore
    public Student(){}


    public Student(String student_id, String email, String password) {
        this.email = email;
        this.password = password;
        this.student_id=student_id;
    }

    @Ignore
    public Student(@NonNull String student_id, String email) {
        this.student_id = student_id;
        this.email = email;
    }

    @Ignore
    public Student(@NonNull String student_id,String first_name, String last_name, String email, String password, String profile_photo, Date date_of_birth,boolean is_synced, Date last_updated, String phone) {
        this.student_id=student_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.profile_photo = profile_photo;
        this.date_of_birth = date_of_birth;
        this.is_synced=is_synced;
        this.last_updated=last_updated;
        this.phone=phone;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getId() {
        return student_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.student_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("student_id", student_id);
        map.put("first_name", first_name);
        map.put("last_name", last_name);
        map.put("email", email);
        map.put("password", password);
        map.put("phone", phone);
        map.put("profile_photo", profile_photo);
        map.put("date_of_birth", Converter.toFirestoreTimestamp(date_of_birth));
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    @Override
    public void markAsSynced() {
        this.is_synced = true;
        this.last_updated = new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateStudent(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertStudent(this);
    }
}
