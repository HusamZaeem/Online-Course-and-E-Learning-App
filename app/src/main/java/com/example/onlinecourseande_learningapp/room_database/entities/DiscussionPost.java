package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "DiscussionPost",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = ForeignKey.CASCADE)
    },
        indices = {
            @Index(value = "user_id"),
            @Index(value = "course_id")
        }
    )
public class DiscussionPost {

    @PrimaryKey(autoGenerate = true)
    private int post_id;
    private int course_id;
    private int user_id;
    private String content;
    private String media_url;
    private int like_count;
    private Date timestamp;


    public DiscussionPost(int course_id, int user_id, String content, String media_url, int like_count, Date timestamp) {
        this.course_id = course_id;
        this.user_id = user_id;
        this.content = content;
        this.media_url = media_url;
        this.like_count = like_count;
        this.timestamp = timestamp;
    }


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
