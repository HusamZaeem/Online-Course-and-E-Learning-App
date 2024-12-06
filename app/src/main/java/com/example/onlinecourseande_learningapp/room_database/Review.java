package com.example.onlinecourseande_learningapp.room_database;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Review",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "user_id")
    )
public class Review {

    @PrimaryKey(autoGenerate = true)
    private int review_id;
    private int user_id;
    private int target_id;
    private String type;
    private double rate;
    private String comment;


    public Review(int user_id, int target_id, String type, double rate, String comment) {
        this.user_id = user_id;
        this.target_id = target_id;
        this.type = type;
        this.rate = rate;
        this.comment = comment;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
