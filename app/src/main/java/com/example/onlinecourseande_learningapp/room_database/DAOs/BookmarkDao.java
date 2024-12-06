package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert
    void insertBookmark (Bookmark bookmark);

    @Update
    void updateBookmark (Bookmark bookmark);

    @Delete
    void deleteBookmark (Bookmark bookmark);

    @Query("SELECT * FROM Bookmark")
    LiveData<List<Bookmark>> getAllBookmarks ();

    @Query("SELECT * FROM Bookmark WHERE bookmark_id = :bookmark_id")
    LiveData<List<Bookmark>> getBookmarkById (int bookmark_id);


}
