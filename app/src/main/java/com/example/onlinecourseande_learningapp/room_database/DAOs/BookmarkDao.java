package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmark (Bookmark bookmark);

    @Update
    void updateBookmark (Bookmark bookmark);

    @Delete
    void deleteBookmark (Bookmark bookmark);

    @Query("SELECT * FROM Bookmark")
    LiveData<List<Bookmark>> getAllBookmarks ();

    @Query("SELECT * FROM Bookmark")
    List<Bookmark> getAllBookmarksList ();

    @Query("SELECT * FROM Bookmark WHERE bookmark_id = :bookmark_id")
    LiveData<List<Bookmark>> getBookmarkById (String bookmark_id);

    @Query("SELECT * FROM Bookmark WHERE student_id = :student_id")
    LiveData<List<Bookmark>> getAllStudentBookmarks (String student_id);


    @Query("SELECT Course.* FROM Course " +
            "INNER JOIN Bookmark ON Course.course_id = Bookmark.course_id " +
            "WHERE Bookmark.student_id = :student_id")
    LiveData<List<Course>> getBookmarkedCourses(String student_id);

    @Query("SELECT * FROM Bookmark WHERE course_id = :courseId AND student_id = :studentId LIMIT 1")
    Bookmark getBookmarkForCourseAndStudent(String courseId, String studentId);

    @Query("SELECT * FROM Bookmark WHERE bookmark_id = :bookmark_id")
    Bookmark getBookmarkByBookmarkId(String bookmark_id);

    @Query("SELECT * FROM Bookmark WHERE is_synced = 0")
    List<Bookmark> getUnsyncedBookmark();


    @Query("SELECT EXISTS (SELECT 1 FROM bookmark WHERE course_id = :courseId AND student_id = :studentId LIMIT 1)")
    boolean isCourseBookmarked(String courseId, String studentId);

}
