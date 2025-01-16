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
        tableName = "GroupMembership",
        foreignKeys = {
                @ForeignKey(
                        entity = Group.class,
                        parentColumns = "group_id",
                        childColumns = "group_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "student_id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                )
                ,
        },
        indices = {
                @Index(value = "group_id"),
                @Index(value = "student_id"),
        }
)
public class GroupMembership implements Syncable {

    @PrimaryKey
    @NonNull
    private String group_membership_id="";
    private String group_id;
    private String student_id;
    private boolean is_synced;
    private Date last_updated;

    public GroupMembership() {
    }

    @Ignore
    public GroupMembership(@NonNull String group_membership_id, String group_id, String student_id, boolean is_synced, Date last_updated) {
        this.group_membership_id = group_membership_id;
        this.group_id = group_id;
        this.student_id = student_id;
        this.is_synced = is_synced;
        this.last_updated = last_updated;
    }




    @Override
    public void markAsSynced() {
        this.is_synced=true;
        this.last_updated=new Date(System.currentTimeMillis());
    }

    @Override
    public void updateInRepository(AppRepository repository) {
        repository.updateGroupMembership(this);
    }

    @Override
    public void insertInRepository(AppRepository repository) {
        repository.insertGroupMembership(this);
    }

    public Date getLast_updated() {
        return last_updated;
    }

    @Override
    public String getId() {
        return group_membership_id;
    }

    @Override
    public void setPrimaryKey(String documentId) {
        this.group_membership_id=documentId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("student_id", student_id);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }



    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    @NonNull
    public String getGroup_membership_id() {
        return group_membership_id;
    }

    public void setGroup_membership_id(@NonNull String group_membership_id) {
        this.group_membership_id = group_membership_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
