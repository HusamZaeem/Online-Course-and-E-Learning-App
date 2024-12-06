package com.example.onlinecourseande_learningapp.room_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class , Course.class, Bookmark.class, Call.class, Certificate.class, Chat.class, Comment.class, DiscussionPost.class, Enrollment.class, Grade.class, Lesson.class, Mentor.class, Message.class, Module.class, Notification.class, Participant.class, Review.class}, version = 1 , exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CourseDao courseDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract CallDao callDao();
    public abstract CertificateDao certificateDao();
    public abstract ChatDao chatDao();
    public abstract CommentDao commentDao();
    public abstract DiscussionPostDao discussionPostDao();
    public abstract EnrollmentDao enrollmentDao();
    public abstract GradeDao gradeDao();
    public abstract LessonDao lessonDao();
    public abstract MentorDao mentorDao();
    public abstract MessageDao messageDao();
    public abstract ModuleDao moduleDao();
    public abstract NotificationDao notificationDao();
    public abstract ParticipantDao participantDao();
    public abstract ReviewDao reviewDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "e_learning_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}