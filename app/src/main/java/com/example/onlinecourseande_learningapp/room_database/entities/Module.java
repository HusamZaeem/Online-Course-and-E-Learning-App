package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Module",
        foreignKeys = @ForeignKey(entity = Course.class,parentColumns = "course_id", childColumns = "course_id"),
        indices = @Index(value = "course_id")
    )
public class Module {


    @PrimaryKey(autoGenerate = true)
    private int module_id;
    private String module_name;
    private int course_id;
    private int duration;


    public Module(String module_name, int course_id, int duration) {
        this.module_name = module_name;
        this.course_id = course_id;
        this.duration = duration;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
