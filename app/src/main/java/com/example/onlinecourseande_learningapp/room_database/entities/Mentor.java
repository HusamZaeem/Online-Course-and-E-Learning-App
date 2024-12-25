package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Mentor")
public class Mentor {

    @PrimaryKey(autoGenerate = true)
    private int mentor_id;
    private String job_title;
    private String website;
    private int students_taught;
    private String mentor_email;
    private String mentor_password;
    private String mentor_fName;
    private String mentor_lName;
    private String mentor_photo;


    public Mentor(String job_title, String website, int students_taught, String mentor_email, String mentor_password, String mentor_fName, String mentor_lName, String mentor_photo) {
        this.job_title = job_title;
        this.website = website;
        this.students_taught = students_taught;
        this.mentor_email = mentor_email;
        this.mentor_password = mentor_password;
        this.mentor_fName = mentor_fName;
        this.mentor_lName = mentor_lName;
        this.mentor_photo = mentor_photo;
    }


    public int getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(int mentor_id) {
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
