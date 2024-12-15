package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bookmark",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Student.class, parentColumns = "student_id", childColumns = "student_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "course_id"),
        @Index(value = "student_id")
        }
        )
public class Bookmark {

    @PrimaryKey(autoGenerate = true)
    private int bookmark_id;
    private int student_id;
    private int course_id;


    public Bookmark(int student_id, int course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
    }

    public int getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(int bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public int getUser_id() {
        return student_id;
    }

    public void setUser_id(int user_id) {
        this.student_id = user_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
