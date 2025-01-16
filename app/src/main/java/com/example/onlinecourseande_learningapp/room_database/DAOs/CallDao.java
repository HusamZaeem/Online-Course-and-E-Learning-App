package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Call;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;

import java.util.List;

@Dao
public interface CallDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCall (Call call);

    @Update
    void updateCall (Call call);

    @Delete
    void deleteCall (Call call);

    @Query("SELECT * FROM Call")
    LiveData<List<Call>> getAllCalls ();

    @Query("SELECT * FROM Call")
    List<Call> getAllCallsList ();

    @Query("SELECT * FROM Call WHERE call_id = :call_id")
    Call getCallById (String call_id);



    @Query("SELECT * FROM Call WHERE caller_id = :student_id")
    LiveData<List<Call>> getAllStudentCalls (String student_id);


    @Query("SELECT * FROM Call WHERE caller_id = :student_id AND chat_id = :chat_id")
    LiveData<List<Call>> getAllStudentCallsForAChat (String student_id, String chat_id);


    @Query("SELECT * FROM Call WHERE is_synced = 0")
    List<Call> getUnsyncedCall();

}
