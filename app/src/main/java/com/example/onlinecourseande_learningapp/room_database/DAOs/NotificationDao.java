package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Notification;

import java.util.List;

@Dao
public interface NotificationDao {


    @Insert
    void insertNotification (Notification notification);

    @Update
    void updateNotification (Notification notification);

    @Delete
    void deleteNotification (Notification notification);

    @Query("SELECT * FROM Notification")
    LiveData<List<Notification>> getAllNotifications();

    @Query("SELECT * FROM Notification WHERE notification_id = :notification_id")
    LiveData<List<Notification>> getNotificationById(int notification_id);


    @Query("SELECT * FROM Notification WHERE student_id = :student_id")
    LiveData<List<Notification>> getAllStudentNotifications(String student_id);

}
