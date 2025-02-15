package com.example.onlinecourseande_learningapp.room_database.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.Syncable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(
        tableName = "Call",
        foreignKeys = {
                @ForeignKey(entity = Chat.class, parentColumns = "chat_id", childColumns = "chat_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Group.class, parentColumns = "group_id", childColumns = "group_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "chat_id"),
                @Index(value = "group_id")
        }
)
public class Call implements Syncable {

    @PrimaryKey
    @NonNull
    private String call_id="";

    private String chat_id;

    private String caller_id;
    private String caller_type; //Mentor,Student

    private String receiver_id;
    private String receiver_type; //Mentor,Student
    private String group_id;
    private String channel_id;

    private String callType; // "Voice", "Video"

    private Date startTime;

    private Date endTime;

    private String callStatus; // "ONGOING", "ENDED", "MISSED"
    private Date timestamp;
    private boolean is_synced;
    private Date last_updated;


    @Ignore
    public Call(){}

    public Call(@NonNull String call_id, String chat_id, String caller_id, String caller_type, String receiver_id, String receiver_type, String group_id, String channel_id, String callType, Date startTime, Date endTime, String callStatus, Date timestamp, boolean is_synced, Date last_updated) {
        this.call_id = call_id;
        this.chat_id = chat_id;
        this.caller_id = caller_id;
        this.caller_type = caller_type;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.group_id = group_id;
        this.channel_id = channel_id;
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.callStatus = callStatus;
        this.timestamp = timestamp;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }

    public String getCaller_type() {
        return caller_type;
    }

    public void setCaller_type(String caller_type) {
        this.caller_type = caller_type;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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
        repository.updateCall(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertCall(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return call_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.call_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chat_id);
        map.put("channel_id", channel_id);
        map.put("group_id", group_id);
        map.put("caller_id", caller_id);
        map.put("caller_type", caller_type);
        map.put("receiver_id", receiver_id);
        map.put("receiver_type", receiver_type);
        map.put("callType", callType);
        map.put("startTime", Converter.toFirestoreTimestamp(startTime));
        map.put("endTime", Converter.toFirestoreTimestamp(endTime));
        map.put("callStatus", callStatus);
        map.put("timestamp", Converter.toFirestoreTimestamp(timestamp));
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(@NonNull String call_id) {
        this.call_id = call_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(String caller_id) {
        this.caller_id = caller_id;
    }


    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }
}
