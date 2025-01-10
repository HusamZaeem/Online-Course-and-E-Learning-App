package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.Syncable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Ad implements Syncable {

    @NonNull
    @PrimaryKey
    private String id="";
    private String imageUrl;
    private String main_title;
    private String sub_title;
    private String description;
    private boolean is_synced;
    private Date last_updated;

    @Ignore
    public Ad() {
    }


    public Ad(@NonNull String id, String imageUrl, String main_title, String sub_title, String description, boolean is_synced,Date last_updated ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.main_title = main_title;
        this.sub_title = sub_title;
        this.description = description;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    @Override
    public void markAsSynced() {
        this.is_synced=true;
        this.last_updated=new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateAd(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertAd(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("imageUrl", imageUrl);
        map.put("main_title", main_title);
        map.put("sub_title", sub_title);
        map.put("description", description);
        map.put("is_synced", is_synced);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}