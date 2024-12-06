package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Course",
        foreignKeys = @ForeignKey(entity = Mentor.class,parentColumns = "mentor_id", childColumns = "mentor_id"), indices = @Index(value = "mentor_id"))
public class Course {


    @PrimaryKey(autoGenerate = true)
    private int course_id;
    private String course_name;
    private int mentor_id;
    private double price;
    private double hours;
    private String description;
    private String tools_used;
    private int students_count;
    private String photo_url;


    public Course(String course_name, int mentor_id, double price, double hours, String description, String tools_used, int students_count, String photo_url) {
        this.course_name = course_name;
        this.mentor_id = mentor_id;
        this.price = price;
        this.hours = hours;
        this.description = description;
        this.tools_used = tools_used;
        this.students_count = students_count;
        this.photo_url = photo_url;
    }


    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(int mentor_id) {
        this.mentor_id = mentor_id;
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
