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
                )

        },
        indices = {
                @Index(value = "group_id")
        }
)
public class GroupMembership implements Syncable {

    @PrimaryKey
    @NonNull
    private String group_membership_id="";
    private String group_id;
    private String member_id;
    private String member_type; //mentor,student
    private boolean is_synced;
    private Date last_updated;

    public GroupMembership() {
    }



    @Ignore
    public GroupMembership(@NonNull String group_membership_id, String group_id, String member_id, String member_type, boolean is_synced, Date last_updated) {
        this.group_membership_id = group_membership_id;
        this.group_id = group_id;
        this.member_id = member_id;
        this.member_type = member_type;
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
        map.put("member_id", member_id);
        map.put("member_type", member_type);
        map.put("last_updated", Converter.toFirestoreTimestamp(last_updated));
        return map;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
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

}
