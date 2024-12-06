package com.example.onlinecourseande_learningapp.room_database;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Certificate",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "user_id"),
                @Index(value = "course_id")
        }
)
public class Certificate {

    @PrimaryKey(autoGenerate = true)
    private int Certificate_id;
    private int user_id;
    private int course_id;
    private Date issue_date;
    private double final_grade;
    private String Certificate_url;

    public Certificate(int user_id, int course_id, Date issue_date, double final_grade, String Certificate_url) {
        this.user_id = user_id;
        this.course_id = course_id;
        this.issue_date = issue_date;
        this.final_grade = final_grade;
        this.Certificate_url = Certificate_url;
    }

    public int getCertificate_id() {
        return Certificate_id;
    }

    public void setCertificate_id(int certificate_id) {
        Certificate_id = certificate_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public double getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(double final_grade) {
        this.final_grade = final_grade;
    }

    public String getCertificate_url() {
        return Certificate_url;
    }

    public void setCertificate_url(String certificate_url) {
        this.Certificate_url = certificate_url;
    }
}
