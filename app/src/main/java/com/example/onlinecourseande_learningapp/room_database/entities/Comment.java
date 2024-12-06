package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Comment",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id",onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = DiscussionPost.class, parentColumns = "post_id", childColumns = "post_id",onDelete = ForeignKey.CASCADE)
    },
        indices = {
            @Index(value = "user_id"),
            @Index(value = "post_id")
        }
    )
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int comment_id;
    private int post_id;
    private int user_id;
    private String content;
    private Date timestamp;


    public Comment(int post_id, int user_id, String content, Date timestamp) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.timestamp = timestamp;
    }


    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
