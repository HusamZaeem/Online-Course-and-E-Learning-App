package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Enrollment",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                       @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE)
    },
        indices = {
            @Index(value = "user_id"),
            @Index(value = "course_id")
        }
    )
public class Enrollment {


    @PrimaryKey(autoGenerate = true)
    private int enrollment_id;
    private int user_id;
    private int course_id;
    private int progress;
    private String status;

    public Enrollment(int user_id, int course_id, int progress, String status) {
        this.user_id = user_id;
        this.course_id = course_id;
        this.progress = progress;
        this.status = status;
    }


    public int getEnrollment_id() {
        return enrollment_id;
    }

    public void setEnrollment_id(int enrollment_id) {
        this.enrollment_id = enrollment_id;
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
}
