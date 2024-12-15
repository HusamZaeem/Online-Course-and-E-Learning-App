package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;




@Entity(
        tableName = "GroupMembership",
        foreignKeys = {
                @ForeignKey(
                        entity = Group.class,
                        parentColumns = "group_id",
                        childColumns = "group_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        indices = {
                @Index(value = "group_id"),
                @Index(value = "student_id"),
        }
)
public class GroupMembership {

    @PrimaryKey
    private int group_membership_id;
    private int group_id;
    private int student_id;


    public GroupMembership() {
    }

    @Ignore
    public GroupMembership(int group_id, int student_id) {
        this.group_id = group_id;
        this.student_id = student_id;
    }

    public int getGroup_membership_id() {
        return group_membership_id;
    }

    public void setGroup_membership_id(int group_membership_id) {
        this.group_membership_id = group_membership_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}
