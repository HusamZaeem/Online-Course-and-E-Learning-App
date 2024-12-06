package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Comment;

import java.util.List;

@Dao
public interface CommentDao {


    @Insert
    void insertComment (Comment comment);

    @Update
    void updateComment (Comment comment);

    @Delete
    void deleteComment (Comment comment);

    @Query("SELECT * FROM Comment")
    LiveData<List<Comment>> getAllComments();

    @Query("SELECT * FROM Comment WHERE comment_id = :comment_id")
    LiveData<List<Comment>> getCommentById(int comment_id);


}
