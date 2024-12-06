package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Call;

import java.util.List;

@Dao
public interface CallDao {


    @Insert
    void insertCall (Call call);

    @Update
    void updateCall (Call call);

    @Delete
    void deleteCall (Call call);

    @Query("SELECT * FROM Call")
    LiveData<List<Call>> getAllCalls ();

    @Query("SELECT * FROM Call WHERE call_id = :call_id")
    LiveData<List<Call>> getCallById (int call_id);


}
