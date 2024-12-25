package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Group;

import java.util.List;

@Dao
public interface GroupDao {



    @Insert
    void insertGroup (Group group);

    @Update
    void updateGroup (Group group);

    @Delete
    void deleteGroup (Group group);

    @Query("SELECT * FROM `Group`")
    LiveData<List<Group>> getAllGroups();

    @Query("SELECT * FROM `Group` WHERE group_id = :group_id")
    LiveData<List<Group>> getGroupById(int group_id);

    @Query("SELECT * FROM `Group` WHERE group_name = :group_name")
    LiveData<List<Group>> getGroupByGroupName(String group_name);



    @Query("SELECT * FROM `Group` WHERE course_id = :course_id")
    Group getGroupByCourseId(int course_id);


}
