package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.DiscussionPost;

import java.util.List;

@Dao
public interface DiscussionPostDao {


    @Insert
    void insertDiscussionPost (DiscussionPost discussionPost);

    @Update
    void updateDiscussionPost (DiscussionPost discussionPost);

    @Delete
    void deleteDiscussionPost (DiscussionPost discussionPost);

    @Query("SELECT * FROM DiscussionPost")
    LiveData<List<DiscussionPost>> getAllDiscussionPosts();

    @Query("SELECT * FROM DiscussionPost WHERE post_id = :post_id")
    LiveData<List<DiscussionPost>> getDiscussionPostById(int post_id);


}
