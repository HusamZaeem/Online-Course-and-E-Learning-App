package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

@Dao
public interface ReviewDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview (Review review);

    @Update
    void updateReview (Review review);

    @Delete
    void deleteReview (Review review);

    @Insert
    default void insertMentorReview(String student_id, String mentor_id, double rate, String comment) {
        Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
        insertReview(review);
    }


    @Insert
    default void insertCourseReview(String student_id, String course_id, double rate, String comment) {
        Review review = new Review(student_id, course_id, "Course", rate, comment);
        insertReview(review);
    }

    @Query("SELECT * FROM Review")
    LiveData<List<Review>> getAllReviews();

    @Query("SELECT * FROM Review WHERE review_id = :review_id")
    LiveData<List<Review>> getReviewById(String review_id);

    @Query("SELECT * FROM Review WHERE type = 'Course' AND target_id = :course_id")
    LiveData<List<Review>> getReviewsForCourse(String course_id);

    @Query("SELECT * FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    LiveData<List<Review>> getReviewsForMentor(String mentor_id);

    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id")
    void deleteAllCourseReviews(String course_id);

    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id")
    void deleteAllMentorReviews(String mentor_id);

    @Query("DELETE FROM Review WHERE type = 'Course' AND target_id = :course_id AND review_id = :review_id")
    void deleteReviewForCourse(String course_id, String review_id);


    @Query("DELETE FROM Review WHERE type = 'Mentor' AND target_id = :mentor_id AND review_id = :review_id")
    void deleteReviewForMentor(String mentor_id, String review_id);

    @Query("SELECT * FROM Review WHERE review_id = :review_id")
    Review getReviewByReviewId(String review_id);

    @Query("SELECT * FROM Review WHERE is_synced = 0")
    List<Review> getUnsyncedReview();

    @Query("SELECT COUNT(*) FROM Review WHERE type = 'Course' AND target_id = :courseId")
    LiveData<Integer> getReviewCountForCourse(String courseId);

    @Query("SELECT * FROM Review")
    List<Review> getAllReviewsList();


    @Query("SELECT COUNT(*) FROM Review WHERE type = 'Mentor' AND target_id = :mentorId")
    LiveData<Integer> getReviewCountForMentor(String mentorId);






}
