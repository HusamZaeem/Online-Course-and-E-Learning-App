package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Enrollment",
        foreignKeys = {@ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE),
                       @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE)
    },
        indices = {
            @Index(value = "student_id"),
            @Index(value = "course_id")
        }
    )
public class Enrollment {


    @PrimaryKey(autoGenerate = true)
    private int enrollment_id;
    private int student_id;
    private int course_id;
    private int progress;
    private String status;
    private Date completion_date;
    private String certificate_url;
    private double final_grade;
    private Date timestamp;
    private double fee;


    public Enrollment(int student_id, int course_id, int progress, String status, Date completion_date, String certificate_url, double final_grade, Date timestamp, double fee) {
        this.student_id = student_id;
        this.course_id = course_id;
        this.progress = progress;
        this.status = status;
        this.completion_date = completion_date;
        this.certificate_url = certificate_url;
        this.final_grade = final_grade;
        this.timestamp = timestamp;
        this.fee = fee;
    }

    public int getEnrollment_id() {
        return enrollment_id;
    }

    public void setEnrollment_id(int enrollment_id) {
        this.enrollment_id = enrollment_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
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

    public double getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(double final_grade) {
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
