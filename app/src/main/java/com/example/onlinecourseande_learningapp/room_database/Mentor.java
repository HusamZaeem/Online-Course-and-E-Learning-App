package com.example.onlinecourseande_learningapp.room_database;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentor",
        foreignKeys = @ForeignKey(entity = User.class,parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "user_id")
    )
public class Mentor {

    @PrimaryKey(autoGenerate = true)
    private int mentor_id;
    private int user_id;
    private String job_title;
    private String website;
    private int students_taught;


    public Mentor(String job_title, String website) {
        this.job_title = job_title;
        this.website = website;
    }


    public int getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(int mentor_id) {
        this.mentor_id = mentor_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
}
