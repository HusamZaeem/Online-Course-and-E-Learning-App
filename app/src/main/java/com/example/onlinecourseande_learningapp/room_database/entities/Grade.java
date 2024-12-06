package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Grade",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Module.class, parentColumns = "module_id", childColumns = "module_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
        @Index(value = "user_id"),
        @Index(value = "module_id")
        }
    )
public class Grade {


    @PrimaryKey(autoGenerate = true)
    private int grade_id;
    private int user_id;
    private int module_id;
    private double score;
    private double max_score;


    public Grade(int user_id, int module_id, double score, double max_score) {
        this.user_id = user_id;
        this.module_id = module_id;
        this.score = score;
        this.max_score = max_score;
    }


    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getMax_score() {
        return max_score;
    }

    public void setMax_score(double max_score) {
        this.max_score = max_score;
    }
}
