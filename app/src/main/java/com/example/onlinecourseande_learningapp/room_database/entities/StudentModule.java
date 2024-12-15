package com.example.onlinecourseande_learningapp.room_database.entities;


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


    private Integer student_id;
    private Integer module_id;
    private Double module_grade;

    public StudentModule(Integer module_id, Double module_grade, Integer student_id) {
        this.module_id = module_id;
        this.module_grade = module_grade;
        this.student_id = student_id;
    }


    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getModule_id() {
        return module_id;
    }

    public void setModule_id(Integer module_id) {
        this.module_id = module_id;
    }

    public Double getModule_grade() {
        return module_grade;
    }

    public void setModule_grade(Double module_grade) {
        this.module_grade = module_grade;
    }
}
