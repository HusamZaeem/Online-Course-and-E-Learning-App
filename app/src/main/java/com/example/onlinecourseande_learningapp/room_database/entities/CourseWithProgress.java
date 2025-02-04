package com.example.onlinecourseande_learningapp.room_database.entities;


public class CourseWithProgress {
    private final Course course;
    private final Enrollment enrollment;
    private final int completedLessons;
    private final int totalLessons;

    public CourseWithProgress(Course course, Enrollment enrollment, int completedLessons, int totalLessons) {
        this.course = course;
        this.enrollment = enrollment;
        this.completedLessons = completedLessons;
        this.totalLessons = totalLessons;
    }

    public Course getCourse() {
        return course;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public boolean isCompleted() {
        return completedLessons >= totalLessons && totalLessons > 0;
    }
}

