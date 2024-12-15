package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "MentorCourse",
        foreignKeys = {
                @ForeignKey(
                        entity = Mentor.class,
                        parentColumns = "mentor_id",
                        childColumns = "mentor_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Course.class,
                        parentColumns = "course_id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        primaryKeys = {"mentor_id", "course_id"} ,
        indices = {
                @Index(value = "mentor_id"),
                @Index(value = "course_id"),
        }
)
public class MentorCourse {

    private int mentor_id;
    private int course_id;

    public MentorCourse(int mentor_id, int course_id) {
        this.mentor_id = mentor_id;
        this.course_id = course_id;
    }

    public int getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(int mentor_id) {
        this.mentor_id = mentor_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
