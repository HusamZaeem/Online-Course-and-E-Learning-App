package com.example.onlinecourseande_learningapp.room_database.DAOs;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;

import java.util.List;

@Dao
public interface EnrollmentDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEnrollment (Enrollment enrollment);

    @Update
    void updateEnrollment (Enrollment enrollment);

    @Delete
    void deleteEnrollment (Enrollment enrollment);

    @Query("SELECT * FROM Enrollment")
    LiveData<List<Enrollment>> getAllEnrollments();



    @Query("SELECT * FROM Enrollment")
    List<Enrollment> getAllEnrollmentsList();



    @Query("SELECT * FROM Enrollment WHERE enrollment_id = :enrollment_id")
    LiveData<Enrollment> getEnrollmentById(String enrollment_id);


    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id AND course_id = :course_id")
    Enrollment getStudentEnrollmentInCourse(String student_id, String course_id);


    @Query(
            "SELECT COUNT(*) " +
                    "FROM Lesson " +
                    "INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "WHERE Module.course_id = :course_id"
    )
    int getTotalLessonsForCourse(String course_id);


    @Query(
            "SELECT COUNT(*) " +
                    "FROM StudentLesson " +
                    "INNER JOIN Lesson ON StudentLesson.lesson_id = Lesson.lesson_id " +
                    "INNER JOIN Module ON Lesson.module_id = Module.module_id " +
                    "WHERE StudentLesson.student_id = :student_id AND Module.course_id = :course_id AND StudentLesson.completion_status = 1"
    )
    int getCompletedLessonsForStudentInCourse(String student_id, String course_id);



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
    void updateEnrollmentProgress(String student_id, String course_id);


    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id")
    LiveData<List<Enrollment>> getEnrollmentsForStudent(String student_id);


    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id")
    List<Enrollment> getEnrollmentsForStudentList(String student_id);

    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id AND status = 'Completed' ")
    List<Enrollment> getCompletedEnrollmentsList(String student_id);


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
    void calculateFinalGrade(String student_id, String course_id);




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
    void setCompletionDateIfAllLessonsCompleted(String student_id, String course_id);



    @Query(
            "UPDATE Enrollment " +
                    "SET fee = :fee, status = 'Ongoing', timestamp = CURRENT_TIMESTAMP " +
                    "WHERE student_id = :student_id AND course_id = :course_id"
    )
    void updateFeeStatusAndTimestamp(String student_id, String course_id, double fee);




    @Query(
            "INSERT OR REPLACE INTO Enrollment (enrollment_id, student_id, course_id, fee, progress, status,is_synced , timestamp) " +
                    "VALUES (:enrollment_id, :student_id, :course_id, 0, 0,'Ongoing',0 ,CURRENT_TIMESTAMP)"
    )
    void enrollInFreeCourse(String enrollment_id, String student_id, String course_id);



    @Query("SELECT * FROM Enrollment WHERE student_id = :student_id AND course_id = :course_id")
    LiveData<Enrollment> checkEnrollment(String student_id, String course_id);



    @Query("SELECT * FROM Enrollment WHERE enrollment_id = :enrollment_id")
    Enrollment getEnrollmentByEnrollmentId(String enrollment_id);

    @Query("SELECT * FROM Enrollment WHERE is_synced = 0")
    List<Enrollment> getUnsyncedEnrollment();


    @Query("SELECT * FROM Enrollment WHERE student_id = :studentId AND course_id IN (:courseIds)")
    LiveData<List<Enrollment>> checkEnrollmentInMentorCourses(String studentId, List<String> courseIds);

    @Query("UPDATE Enrollment SET progress = :progress WHERE student_id = :studentId AND course_id = :courseId")
    void updateProgress(String studentId, String courseId, int progress);

    @Query("SELECT * FROM Enrollment WHERE student_id = :studentId AND status = 'Completed'")
    List<Enrollment> getCompletedEnrollments(String studentId);


    @Query("SELECT * FROM Enrollment WHERE course_id = :courseId")
    List<Enrollment> getEnrollmentsForCourse(String courseId);


    @Query("SELECT student_id FROM Enrollment WHERE course_id = :courseId")
    List<String> getStudentIdsByCourseId(String courseId);

}
