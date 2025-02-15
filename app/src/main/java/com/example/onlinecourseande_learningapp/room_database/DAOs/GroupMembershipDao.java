package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;

import java.util.List;

@Dao
public interface GroupMembershipDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupMembership (GroupMembership groupMembership);

    @Update
    void updateGroupMembership (GroupMembership groupMembership);

    @Delete
    void deleteGroupMembership (GroupMembership groupMembership);


    @Query("SELECT * FROM GroupMembership WHERE group_membership_id = :group_membership_id")
    LiveData<List<GroupMembership>> getGroupMembershipById(String group_membership_id);



    @Query("SELECT * FROM GroupMembership WHERE group_membership_id = :group_membership_id")
    GroupMembership getGroupMembershipByGroupMembershipId(String group_membership_id);


    @Query("SELECT * FROM GroupMembership WHERE group_membership_id = :group_membership_id AND member_id = :member_id")
    LiveData<GroupMembership> getGroupMembershipByGroupIdAndMemberId(String group_membership_id, String member_id);


    @Query("SELECT * FROM GroupMembership WHERE group_id = :group_id")
    LiveData<List<GroupMembership>> getGroupMembershipsByGroupId(String group_id);

    @Query("SELECT * FROM GroupMembership WHERE is_synced = 0")
    List<GroupMembership> getUnsyncedGroupMembership();

    @Query("SELECT * FROM GroupMembership")
    List<GroupMembership> getAllGroupMemberships();


    @Query("SELECT member_id FROM GroupMembership WHERE group_id = :groupId")
    List<String> getGroupParticipants(String groupId);

}
