package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;



@Entity(
        tableName = "StudentMentor",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Mentor.class,
                        parentColumns = "mentor_id",
                        childColumns = "mentor_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        primaryKeys = {"student_id", "mentor_id"} ,
        indices = {
                @Index(value = "student_id"),
                @Index(value = "mentor_id"),
        }
)
public class StudentMentor {

    private Integer student_id;
    private Integer mentor_id;

    public StudentMentor(Integer student_id, Integer mentor_id) {
        this.student_id = student_id;
        this.mentor_id = mentor_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(Integer mentor_id) {
        this.mentor_id = mentor_id;
    }
}
