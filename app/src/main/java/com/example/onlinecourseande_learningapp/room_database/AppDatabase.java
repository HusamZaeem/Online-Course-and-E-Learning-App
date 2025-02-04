package com.example.onlinecourseande_learningapp.room_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.onlinecourseande_learningapp.room_database.DAOs.AdDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.AttachmentDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.BookmarkDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.CallDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ChatDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.CourseDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.EnrollmentDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.GroupDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.GroupMembershipDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.LessonDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.MentorCourseDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.MentorDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.MessageDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ModuleDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.NotificationDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ReviewDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentLessonDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentMentorDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentModuleDao;
import com.example.onlinecourseande_learningapp.room_database.entities.Ad;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;
import com.example.onlinecourseande_learningapp.room_database.entities.Call;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.MentorCourse;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Attachment.class, Bookmark.class, Call.class, Chat.class, Course.class, Enrollment.class, Group.class, GroupMembership.class, Lesson.class, MentorCourse.class, Mentor.class, Message.class, Module.class, Notification.class, Review.class, Student.class, StudentLesson.class, StudentMentor.class, StudentModule.class, Ad.class}, version = 1 , exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {


    public abstract AttachmentDao attachmentDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract CallDao callDao();
    public abstract ChatDao chatDao();
    public abstract CourseDao courseDao();
    public abstract EnrollmentDao enrollmentDao();
    public abstract GroupDao groupDao();
    public abstract GroupMembershipDao groupMembershipDao();
    public abstract LessonDao lessonDao();
    public abstract MentorCourseDao mentorCourseDao();
    public abstract MentorDao mentorDao();
    public abstract MessageDao messageDao();
    public abstract ModuleDao moduleDao();
    public abstract NotificationDao notificationDao();
    public abstract ReviewDao reviewDao();
    public abstract StudentDao studentDao();
    public abstract StudentLessonDao studentLessonDao();
    public abstract StudentMentorDao studentMentorDao();
    public abstract StudentModuleDao studentModuleDao();
    public abstract AdDao adDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "e_learning_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    db.setForeignKeyConstraintsEnabled(true);
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }



    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

}