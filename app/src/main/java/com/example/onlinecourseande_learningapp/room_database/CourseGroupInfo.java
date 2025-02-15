package com.example.onlinecourseande_learningapp.room_database;

import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

public class CourseGroupInfo {
    private Course course;
    private Group group;
    private Mentor mentor;
    private List<Student> students;

    // No-argument constructor
    public CourseGroupInfo() {
    }

    // Constructor with arguments (if needed)
    public CourseGroupInfo(Course course, Group group, Mentor mentor, List<Student> students) {
        this.course = course;
        this.group = group;
        this.mentor = mentor;
        this.students = students;
    }

    // Getters and setters...
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
