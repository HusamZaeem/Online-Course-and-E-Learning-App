package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Call",
        foreignKeys = {
                @ForeignKey(entity = Chat.class, parentColumns = "chat_id", childColumns = "chat_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "caller_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "receiver_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "chat_id"),
                @Index(value = "caller_id"),
                @Index(value = "receiver_id")
        }
)
public class Call {

    @PrimaryKey(autoGenerate = true)
    private int call_id;

    private int chat_id;

    private int caller_id;


    @Nullable
    private Integer receiver_id;

    private String callType; // "Voice", "Video"

    private String startTime; // Store as ISO 8601 formatted string

    private String endTime; // Store as ISO 8601 formatted string

    private String callStatus;


    public Call(int chat_id, int caller_id, @Nullable Integer receiver_id, String callType, String startTime, String endTime, String callStatus) {
        this.chat_id = chat_id;
        this.caller_id = caller_id;
        this.receiver_id = receiver_id;
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.callStatus = callStatus;
    }

    public int getCall_id() {
        return call_id;
    }

    public void setCall_id(int call_id) {
        this.call_id = call_id;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(int caller_id) {
        this.caller_id = caller_id;
    }

    @Nullable
    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(@Nullable Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }
}
