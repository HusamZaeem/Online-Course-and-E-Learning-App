package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "StudentModule",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Module.class,
                        parentColumns = "module_id",
                        childColumns = "module_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        primaryKeys = {"student_id", "module_id"} ,
        indices = {
                @Index(value = "student_id"),
                @Index(value = "module_id"),
        }
)
public class StudentModule {


    @NonNull
    private String student_id;
    private int module_id;
    private double module_grade;

    public StudentModule(int module_id, double module_grade, String student_id) {
        this.module_id = module_id;
        this.module_grade = module_grade;
        this.student_id = student_id;
    }


    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public double getModule_grade() {
        return module_grade;
    }

    public void setModule_grade(double module_grade) {
        this.module_grade = module_grade;
    }
}
