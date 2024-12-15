package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Review;

import java.util.List;

@Dao
public interface ReviewDao {


    @Insert
    void insertReview (Review review);

    @Update
    void updateReview (Review review);

    @Delete
    void deleteReview (Review review);

    @Insert
    default void insertMentorReview(int student_id, int mentor_id, double rate, String comment) {
        Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
        insertReview(review);
    }


    @Insert
    default void insertCourseReview(int student_id, int course_id, double rate, String comment) {
        Review review = new Review(student_id, course_id, "Course", rate, comment);
        insertReview(review);
    }

    @Query("SELECT * FROM Review")
    LiveData<List<Review>> getAllReviews();

    @Query("SELECT * FROM Review WHERE review_id = :review_id")
    LiveData<List<Review>> getReviewById(int review_id);

    @Query("SELECT * FROM Review WHERE type = 'Course' AND target_id = :course_id")
    LiveData<List<Review>> getReviewsForCourse(int course_id);

    @Query("SELECT * FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    LiveData<List<Review>> getReviewsForMentor(int mentor_id);

    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id")
    void deleteAllCourseReviews(int course_id);

    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    void deleteAllMentorReviews(int mentor_id);

    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id AND review_id = :review_id")
    void deleteReviewForCourse(int course_id, int review_id);


    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id AND review_id = :review_id")
    void deleteReviewForMentor(int mentor_id, int review_id);

}
