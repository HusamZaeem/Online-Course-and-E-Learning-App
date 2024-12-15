package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;

import java.util.List;

@Dao
public interface GroupMembershipDao {


    @Insert
    void insertGroupMembership (GroupMembership groupMembership);

    @Update
    void updateGroupMembership (GroupMembership groupMembership);

    @Delete
    void deleteGroupMembership (GroupMembership groupMembership);


    @Query("SELECT * FROM GroupMembership WHERE group_membership_id = :group_membership_id")
    LiveData<List<GroupMembership>> getGroupMembershipById(int group_membership_id);

    @Query("SELECT student_id FROM GroupMembership WHERE group_id = :group_id")
    LiveData<List<Integer>> getAllStudentsInGroup(int group_id);

}
