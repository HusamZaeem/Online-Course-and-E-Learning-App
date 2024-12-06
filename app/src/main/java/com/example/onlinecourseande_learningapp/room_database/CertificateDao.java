package com.example.onlinecourseande_learningapp.room_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CertificateDao {


    @Insert
    void insertCertificate (Certificate certificate);

    @Update
    void updateCertificate (Certificate certificate);

    @Delete
    void deleteCertificate (Certificate certificate);

    @Query("SELECT * FROM Certificate")
    LiveData<List<Certificate>> getAllCertificates ();

    @Query("SELECT * FROM Certificate WHERE certificate_id = :certificate_id")
    LiveData<List<Certificate>> getCertificateById (int certificate_id);


}
