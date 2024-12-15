package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;

import java.util.List;

@Dao
public interface EnrollmentDao {


    @Insert
    void insertEnrollment (Enrollment enrollment);

    @Update
    void updateEnrollment (Enrollment enrollment);

    @Delete
    void deleteEnrollment (Enrollment enrollment);

    @Query("SELECT * FROM Enrollment")
    LiveData<List<Enrollment>> getAllEnrollments();

    @Query("SELECT * FROM Enrollment WHERE enrollment_id = :enrollment_id")
    LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id);


    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id AND course_id = :course_id")
    Enrollment getStudentEnrollmentInCourse(int student_id, int course_id);


    @Query(
            "SELECT COUNT(*) " +
                    "FROM Lesson " +
                    "INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "WHERE Module.course_id = :course_id"
    )
    int getTotalLessonsForCourse(int course_id);


    @Query(
            "SELECT COUNT(*) " +
                    "FROM StudentLesson " +
                    "INNER JOIN Lesson ON StudentLesson.lesson_id = Lesson.lesson_id " +
                    "INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "WHERE StudentLesson.student_id = :student_id AND Module.course_id = :course_id AND StudentLesson.completion_status = 1"
    )
    int getCompletedLessonsForStudentInCourse(int student_id, int course_id);



    @Query(
            "UPDATE Enrollment " +
                    "SET progress = (" +
                    "  SELECT CAST(COUNT(*) AS FLOAT) / (SELECT COUNT(*) FROM Lesson INNER JOIN Module ON Lesson.module_id = Module.module_id WHERE Module.course_id = :course_id) * 100 " +
                    "  FROM StudentLesson " +
                    "  INNER JOIN Lesson ON StudentLesson.lesson_id = Lesson.lesson_id " +
                    "  INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "  WHERE StudentLesson.student_id = :student_id AND Module.course_id = :course_id AND StudentLesson.completion_status = 1" +
                    ") " +
                    "WHERE student_id = :student_id AND course_id = :course_id"
    )
    void updateEnrollmentProgress(int student_id, int course_id);


    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id")
    LiveData<List<Enrollment>> getEnrollmentsForStudent(int student_id);




    @Query(
            "UPDATE Enrollment " +
                    "SET final_grade = (" +
                    "  SELECT SUM(module_grade) / COUNT(module_grade) * 100 " +
                    "  FROM StudentModule " +
                    "  INNER JOIN Module ON StudentModule.module_id = Module.module_id " +
                    "  WHERE Module.course_id = :course_id AND StudentModule.student_id = :student_id" +
                    ") " +
                    "WHERE student_id = :student_id AND course_id = :course_id"
    )
    void calculateFinalGrade(int student_id, int course_id);




    @Query(
            "UPDATE Enrollment " +
                    "SET completion_date = CURRENT_DATE " +
                    "WHERE student_id = :student_id AND course_id = :course_id AND NOT EXISTS (" +
                    "  SELECT 1 FROM StudentLesson " +
                    "  INNER JOIN Lesson ON StudentLesson.lesson_id = Lesson.lesson_id " +
                    "  INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "  WHERE Module.course_id = :course_id AND StudentLesson.student_id = :student_id AND StudentLesson.completion_status = 0" +
                    ")"
    )
    void setCompletionDateIfAllLessonsCompleted(int student_id, int course_id);



    @Query(
            "UPDATE Enrollment " +
                    "SET fee = :fee, status = 'enrolled', timestamp = CURRENT_TIMESTAMP " +
                    "WHERE student_id = :student_id AND course_id = :course_id"
    )
    void updateFeeStatusAndTimestamp(int student_id, int course_id, double fee);




    @Query(
            "INSERT OR REPLACE INTO Enrollment (student_id, course_id, fee, status, timestamp) " +
                    "VALUES (:student_id, :course_id, 0, 'enrolled', CURRENT_TIMESTAMP)"
    )
    void enrollInFreeCourse(int student_id, int course_id);



    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id AND course_id = :course_id AND status = 'enrolled'")
    Enrollment checkEnrollment(int student_id, int course_id);


}
