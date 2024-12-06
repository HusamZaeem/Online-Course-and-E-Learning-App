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



}
