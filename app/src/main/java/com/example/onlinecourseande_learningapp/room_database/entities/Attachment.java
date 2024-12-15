package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Attachment",
        foreignKeys = @ForeignKey(entity = Message.class,parentColumns = "message_id", childColumns = "message_id", onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "message_id")
)
public class Attachment {


    @PrimaryKey(autoGenerate = true)
    private int attachment_id;
    private String media_url;
    private int message_id;

    public Attachment(String media_url, int message_id) {
        this.media_url = media_url;
        this.message_id = message_id;
    }


    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }


}
