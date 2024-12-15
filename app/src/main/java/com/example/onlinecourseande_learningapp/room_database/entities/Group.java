package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Group",
    foreignKeys = @ForeignKey(entity = Course.class,parentColumns = "course_id",childColumns = "course_id",onDelete = ForeignKey.CASCADE),
    indices = @Index(value = "course_id")
)
public class Group {

    @PrimaryKey(autoGenerate = true)
    private int group_id;
    private String group_name;
    private int course_id;

    public Group(String group_name, int course_id) {
        this.group_name = group_name;
        this.course_id = course_id;
    }


    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
