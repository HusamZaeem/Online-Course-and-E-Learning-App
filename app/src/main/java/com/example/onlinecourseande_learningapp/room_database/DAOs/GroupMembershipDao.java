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

    @Query("SELECT student_id FROM GroupMembership WHERE group_id = :group_id")
    LiveData<List<String>> getAllGroupStudents(String group_id);

    @Query("SELECT group_id FROM GroupMembership WHERE student_id = :student_id")
    LiveData<List<String>> getAllStudentGroups(String student_id);


    @Query("SELECT * FROM GroupMembership WHERE group_membership_id = :group_membership_id")
    GroupMembership getGroupMembershipByGroupMembershipId(String group_membership_id);

    @Query("SELECT * FROM GroupMembership WHERE is_synced = 0")
    List<GroupMembership> getUnsyncedGroupMembership();

}
