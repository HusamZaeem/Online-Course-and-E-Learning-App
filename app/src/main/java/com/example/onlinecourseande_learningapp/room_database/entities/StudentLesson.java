package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.annotation.NonNull;
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

    @NonNull
    private String student_id;
    private int lesson_id;
    private boolean completion_status;


    public StudentLesson(String student_id, int lesson_id, boolean completion_status) {
        this.student_id = student_id;
        this.lesson_id = lesson_id;
        this.completion_status = completion_status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public boolean getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(boolean completion_status) {
        this.completion_status = completion_status;
    }
}
