package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.User;

import java.util.List;

@Dao
public interface UserDao {


    @Insert
    void insertUser (User user);

    @Update
    void updateUser (User user);

    @Delete
    void deleteUser (User user);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers ();

    @Query("SELECT * FROM User WHERE user_id = :user_id")
    LiveData<List<User>> getUserById (int user_id);

}
