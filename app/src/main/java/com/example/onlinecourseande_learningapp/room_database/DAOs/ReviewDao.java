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

    @Query("SELECT * FROM Review")
    LiveData<List<Review>> getAllReviews();

    @Query("SELECT * FROM Review WHERE review_id = :review_id")
    LiveData<List<Review>> getReviewById(int review_id);

    // Get all reviews for a specific course
    @Query("SELECT * FROM Review WHERE type = 'Course' AND target_id = :course_id")
    LiveData<List<Review>> getReviewsForCourse(int course_id);

    // Get all reviews for a specific mentor
    @Query("SELECT * FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    LiveData<List<Review>> getReviewsForMentor(int mentor_id);

    // Delete all reviews for a specific course
    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id")
    void deleteReviewsForCourse(int course_id);

    // Delete all reviews for a specific mentor
    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    void deleteReviewsForMentor(int mentor_id);

    // Delete a review for a specific course
    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id AND review_id = :review_id")
    void deleteReviewForCourse(int course_id, int review_id);


    // Delete a review for a specific mentor
    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id AND review_id = :review_id")
    void deleteReviewForMentor(int mentor_id, int review_id);

}
