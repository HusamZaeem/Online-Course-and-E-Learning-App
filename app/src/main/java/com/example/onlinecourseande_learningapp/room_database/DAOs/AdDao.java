package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Ad;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;

import java.util.List;

@Dao
public interface AdDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAd(Ad ad);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAdList(List<Ad> adList);

    @Update
    void updateAd(Ad ad);

    @Delete
    void deleteAd(Ad ad);

    @Query("SELECT * FROM Ad")
    LiveData<List<Ad>> getAllAds();

    @Query("SELECT * FROM Ad WHERE id = :ad_id")
    Ad getAdById(String ad_id);

    @Query("SELECT * FROM Ad WHERE is_synced = 0")
    List<Ad> getUnsyncedAd();

}
