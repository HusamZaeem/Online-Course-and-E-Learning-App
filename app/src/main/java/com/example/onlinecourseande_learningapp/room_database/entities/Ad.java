package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Ad {

    @NonNull
    @PrimaryKey
    private String id;
    private String imageUrl;
    private String main_title;
    private String sub_title;
    private String description;


    @Ignore
    public Ad() {
    }


    public Ad(String id, String imageUrl, String main_title, String sub_title, String description) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.main_title = main_title;
        this.sub_title = sub_title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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