package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "StudentLesson",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Lesson.class,
                        parentColumns = "lesson_id",
                        childColumns = "lesson_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        primaryKeys = {"student_id", "lesson_id"} ,
        indices = {
                @Index(value = "student_id"),
                @Index(value = "lesson_id"),
        }
)
public class StudentLesson {

    private Integer student_id;
    private Integer lesson_id;
    private String completion_status;


    public StudentLesson(Integer student_id, Integer lesson_id, String completion_status) {
        this.student_id = student_id;
        this.lesson_id = lesson_id;
        this.completion_status = completion_status;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(Integer lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(String completion_status) {
        this.completion_status = completion_status;
    }
}
