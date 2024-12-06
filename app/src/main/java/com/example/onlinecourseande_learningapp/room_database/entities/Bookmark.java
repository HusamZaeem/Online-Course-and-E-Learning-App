package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bookmark",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "course_id"),
        @Index(value = "user_id")
        }
        )
public class Bookmark {

    @PrimaryKey(autoGenerate = true)
    private int bookmark_id;
    private int user_id;
    private int course_id;


    public Bookmark(int user_id, int course_id) {
        this.user_id = user_id;
        this.course_id = course_id;
    }

    public int getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(int bookmark_id) {
        this.bookmark_id = bookmark_id;
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
}
