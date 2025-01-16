package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;

import java.util.List;

@Dao
public interface GroupDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup (Group group);

    @Update
    void updateGroup (Group group);

    @Delete
    void deleteGroup (Group group);

    @Query("SELECT * FROM `Group`")
    LiveData<List<Group>> getAllGroups();

    @Query("SELECT * FROM `Group`")
    List<Group> getAllGroupsList();

    @Query("SELECT * FROM `Group` WHERE group_id = :group_id")
    LiveData<List<Group>> getGroupById(String group_id);

    @Query("SELECT * FROM `Group` WHERE group_name = :group_name")
    LiveData<List<Group>> getGroupByGroupName(String group_name);



    @Query("SELECT * FROM `Group` WHERE course_id = :course_id")
    Group getGroupByCourseId(String course_id);


    @Query("SELECT * FROM `Group` WHERE group_id = :group_id")
    Group getGroupByGroupId(String group_id);

    @Query("SELECT * FROM `Group` WHERE is_synced = 0")
    List<Group> getUnsyncedGroup();


}
