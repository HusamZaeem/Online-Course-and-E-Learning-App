package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lesson",
        foreignKeys = @ForeignKey(entity = Module.class, parentColumns = "module_id", childColumns = "module_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "module_id")
        )
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    private int lesson_id;
    private int module_id;
    private String lesson_title;
    private int lesson_duration;
    private String content_url;
    private boolean is_exam;


    public Lesson(int module_id, String lesson_title, int lesson_duration, String content_url, boolean is_exam) {
        this.module_id = module_id;
        this.lesson_title = lesson_title;
        this.lesson_duration = lesson_duration;
        this.content_url = content_url;
        this.is_exam = is_exam;
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public void setLesson_title(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public int getLesson_duration() {
        return lesson_duration;
    }

    public void setLesson_duration(int lesson_duration) {
        this.lesson_duration = lesson_duration;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public boolean isIs_exam() {
        return is_exam;
    }

    public void setIs_exam(boolean is_exam) {
        this.is_exam = is_exam;
    }
}
