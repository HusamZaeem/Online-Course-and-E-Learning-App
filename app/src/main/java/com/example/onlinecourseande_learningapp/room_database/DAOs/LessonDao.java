package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;

import java.util.List;

@Dao
public interface LessonDao {


    @Insert
    void insertLesson (Lesson lesson);

    @Update
    void updateLesson (Lesson lesson);

    @Delete
    void deleteLesson (Lesson lesson);

    @Query("SELECT * FROM Lesson")
    LiveData<List<Lesson>> getAllLessons ();

    @Query("SELECT * FROM Lesson WHERE lesson_id = :lesson_id")
    LiveData<List<Lesson>> getLessonById (int lesson_id);

    @Query("SELECT * FROM Lesson WHERE module_id = :module_id")
    LiveData<List<Lesson>> getAllLessonsByModuleId (int module_id);

    @Query("SELECT * FROM Lesson WHERE module_id = :module_id AND is_exam = 1")
    LiveData<List<Lesson>> getModuleExamByModuleId (int module_id);


    @Query("UPDATE Lesson SET is_exam = CASE WHEN lesson_id = (SELECT MAX(lesson_id) FROM Lesson WHERE module_id = :module_id) THEN 1 ELSE 0 END WHERE module_id = :module_id")
    void setLastLessonAsExam(int module_id);


    @Transaction
    default void insertLessonAndUpdateModule(Lesson lesson, ModuleDao moduleDao) {
        insertLesson(lesson);
        moduleDao.updateModuleDuration(lesson.getModule_id());
    }


    @Transaction
    default void deleteLessonAndUpdateModule(Lesson lesson, ModuleDao moduleDao) {
        deleteLesson(lesson);
        moduleDao.updateModuleDuration(lesson.getModule_id());
    }


}
