package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Student")
public class Student {

    @PrimaryKey(autoGenerate = true)
    private Integer student_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String profile_photo;
    private Date date_of_birth;


    public Student(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Ignore
    public Student(String first_name, String last_name, String email, String password, String profile_photo, Date date_of_birth) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.profile_photo = profile_photo;
        this.date_of_birth = date_of_birth;
    }

    public Integer getUser_id() {
        return student_id;
    }

    public void setUser_id(Integer user_id) {
        this.student_id = user_id;
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

}
