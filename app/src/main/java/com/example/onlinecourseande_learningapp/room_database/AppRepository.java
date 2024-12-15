package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.onlinecourseande_learningapp.room_database.DAOs.BookmarkDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.CallDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ChatDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.CourseDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.EnrollmentDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.LessonDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.MentorDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.MessageDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ModuleDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.NotificationDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.ReviewDao;
import com.example.onlinecourseande_learningapp.room_database.DAOs.StudentDao;
import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;
import com.example.onlinecourseande_learningapp.room_database.entities.Call;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;

import java.util.List;

public class AppRepository {


    private StudentDao studentDao;
    private CourseDao courseDao;
    private BookmarkDao bookmarkDao;
    private CallDao callDao;
    private CertificateDao certificateDao;
    private ChatDao chatDao;
    private CommentDao commentDao;
    private DiscussionPostDao discussionPostDao;
    private EnrollmentDao enrollmentDao;
    private GradeDao gradeDao;
    private LessonDao lessonDao;
    private MentorDao mentorDao;
    private MessageDao messageDao;
    private ModuleDao moduleDao;
    private NotificationDao notificationDao;
    private ParticipantDao participantDao;
    private ReviewDao reviewDao;


    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        studentDao = db.userDao();
        courseDao = db.courseDao();
        bookmarkDao = db.bookmarkDao();
        callDao = db.callDao();
        certificateDao = db.certificateDao();
        chatDao = db.chatDao();
        commentDao = db.commentDao();
        discussionPostDao = db.discussionPostDao();
        enrollmentDao = db.enrollmentDao();
        gradeDao = db.gradeDao();
        lessonDao = db.lessonDao();
        mentorDao = db.mentorDao();
        messageDao = db.messageDao();
        moduleDao = db.moduleDao();
        notificationDao = db.notificationDao();
        participantDao = db.participantDao();
        reviewDao = db.reviewDao();
    }



    //UserDao -----------------------------

    public void insertUser (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.insertUser(student);
        });
    }


    public void updateUser (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.updateUser(student);
        });
    }


    public void deleteUser (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.deleteUser(student);
        });
    }


    LiveData<List<Student>> getAllUsers (){
        return studentDao.getAllUsers();
    }


    LiveData<List<Student>> getUserById (int user_id){
        return studentDao.getUserById(user_id);
    }


    //CourseDao ------------------------


    public void insertCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insertCourse(course);
        });
    }


    public void updateCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.updateCourse(course);
        });
    }


    public void deleteCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.deleteCourse(course);
        });
    }


    LiveData<List<Course>> getAllCourses (){
        return courseDao.getAllCourses();
    }


    LiveData<List<Course>> getCourseById (int course_id){
        return courseDao.getCourseById(course_id);
    }


    //BookmarkDao --------------------

    public void insertBookmark (Bookmark bookmark){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.insertBookmark(bookmark);
        });
    }


    public void updateBookmark (Bookmark bookmark){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.updateBookmark(bookmark);
        });
    }


    public void deleteBookmark (Bookmark bookmark){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.deleteBookmark(bookmark);
        });
    }


    LiveData<List<Bookmark>> getAllBookmarks (){
        return bookmarkDao.getAllBookmarks();
    }


    LiveData<List<Bookmark>> getBookmarkById (int bookmark_id){
        return bookmarkDao.getBookmarkById(bookmark_id);
    }


    //CallDao ----------------


    void insertCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.insertCall(call);
        });
    }


    void updateCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.updateCall(call);
        });
    }


    void deleteCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.deleteCall(call);
        });
    }


    LiveData<List<Call>> getAllCalls (){
        return callDao.getAllCalls();
    }


    LiveData<List<Call>> getCallById (int call_id){
        return callDao.getCallById(call_id);
    }





    //CertificateDao ----------------



    void insertCertificate (Certificate certificate){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            certificateDao.insertCertificate(certificate);
        });
    }


    void updateCertificate (Certificate certificate){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            certificateDao.updateCertificate(certificate);
        });
    }


    void deleteCertificate (Certificate certificate){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            certificateDao.deleteCertificate(certificate);
        });
    }


    LiveData<List<Certificate>> getAllCertificates (){
        return certificateDao.getAllCertificates();
    }


    LiveData<List<Certificate>> getCertificateById (int certificate_id){
        return certificateDao.getCertificateById(certificate_id);
    }





    //ChatDao ----------------



    void insertChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.insertChat(chat);
        });
    }


    void updateChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.updateChat(chat);
        });
    }


    void deleteChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.deleteChat(chat);
        });
    }


    LiveData<List<Chat>> getAllChats(){
        return chatDao.getAllChats();
    }


    LiveData<List<Chat>> getChatById(int chat_id){
        return chatDao.getChatById(chat_id);
    }




    //CommentDao ----------------



    void insertComment (Comment comment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.insertComment(comment);
        });
    }


    void updateComment (Comment comment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.updateComment(comment);
        });
    }


    void deleteComment (Comment comment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.deleteComment(comment);
        });
    }


    LiveData<List<Comment>> getAllComments(){
        return commentDao.getAllComments();
    }


    LiveData<List<Comment>> getCommentById(int comment_id){
        return commentDao.getCommentById(comment_id);
    }



    //DiscussionPostDao ----------------



    void insertDiscussionPost (DiscussionPost discussionPost){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            discussionPostDao.insertDiscussionPost(discussionPost);
        });
    }


    void updateDiscussionPost (DiscussionPost discussionPost){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            discussionPostDao.updateDiscussionPost(discussionPost);
        });
    }


    void deleteDiscussionPost (DiscussionPost discussionPost){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            discussionPostDao.deleteDiscussionPost(discussionPost);
        });
    }


    LiveData<List<DiscussionPost>> getAllDiscussionPosts(){
        return discussionPostDao.getAllDiscussionPosts();
    }


    LiveData<List<DiscussionPost>> getDiscussionPostById(int post_id){
        return discussionPostDao.getDiscussionPostById(post_id);
    }





    //EnrollmentDao ----------------



    void insertEnrollment (Enrollment enrollment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.insertEnrollment(enrollment);
        });
    }


    void updateEnrollment (Enrollment enrollment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.updateEnrollment(enrollment);
        });
    }


    void deleteEnrollment (Enrollment enrollment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.deleteEnrollment(enrollment);
        });
    }


    LiveData<List<Enrollment>> getAllEnrollments(){
        return enrollmentDao.getAllEnrollments();
    }


    LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id){
        return enrollmentDao.getEnrollmentById(enrollment_id);
    }







    //GradeDao ----------------




    void insertGrade (Grade grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gradeDao.insertGrade(grade);
        });
    }


    void updateGrade (Grade grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gradeDao.updateGrade(grade);
        });
    }


    void deleteGrade (Grade grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gradeDao.deleteGrade(grade);
        });
    }


    LiveData<List<Grade>> getAllGrades(){
        return gradeDao.getAllGrades();
    }


    LiveData<List<Grade>> getGradeById(int grade_id){
        return gradeDao.getGradeById(grade_id);
    }






    //LessonDao ----------------




    void insertLesson (Lesson lesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            lessonDao.insertLesson(lesson);
        });
    }


    void updateLesson (Lesson lesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            lessonDao.updateLesson(lesson);
        });
    }


    void deleteLesson (Lesson lesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            lessonDao.deleteLesson(lesson);
        });
    }


    LiveData<List<Lesson>> getAllLessons (){
        return lessonDao.getAllLessons();
    }


    LiveData<List<Lesson>> getLessonById (int lesson_id){
        return lessonDao.getLessonById(lesson_id);
    }






    //MentorDao ----------------





    void insertMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.insertMentor(mentor);
        });
    }


    void updateMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.updateMentor(mentor);
        });
    }


    void deleteMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.deleteMentor(mentor);
        });
    }


    LiveData<List<Mentor>> getAllMentors(){
        return mentorDao.getAllMentors();
    }


    LiveData<List<Mentor>> getMentorById(int mentor_id){
        return mentorDao.getMentorById(mentor_id);
    }





    //MessageDao ----------------




    void insertMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insertMessage(message);
        });
    }


    void updateMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateMessage(message);
        });
    }


    void deleteMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.deleteMessage(message);
        });
    }


    LiveData<List<Message>> getAllMessages(){
        return messageDao.getAllMessages();
    }


    LiveData<List<Message>> getMessageById(int message_id){
        return messageDao.getMessageById(message_id);
    }







    //ModuleDao ---------------




    void insertModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.insertModule(module);
        });
    }


    void updateModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.updateModule(module);
        });
    }


    void deleteModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.deleteModule(module);
        });
    }


    LiveData<List<Module>> getAllModules (){
        return moduleDao.getAllModules();
    }


    LiveData<List<Module>> getModuleById (int module_id){
        return moduleDao.getModuleById(module_id);
    }








    // NotificationDao ------------




    void insertNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.insertNotification(notification);
        });
    }


    void updateNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.updateNotification(notification);
        });
    }


    void deleteNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.deleteNotification(notification);
        });
    }


    LiveData<List<Notification>> getAllNotifications(){
        return notificationDao.getAllNotifications();
    }


    LiveData<List<Notification>> getNotificationById(int notification_id){
        return notificationDao.getNotificationById(notification_id);
    }








    //ParticipantDao ----------------





    void insertParticipant (Participant participant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            participantDao.insertParticipant(participant);
        });
    }


    void updateParticipant (Participant participant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            participantDao.updateParticipant(participant);
        });
    }


    void deleteParticipant (Participant participant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            participantDao.deleteParticipant(participant);
        });
    }


    LiveData<List<Participant>> getAllParticipants(){
        return participantDao.getAllParticipants();
    }


    LiveData<List<Participant>> getParticipantById(int participant_id){
        return participantDao.getParticipantById(participant_id);
    }





    //ReviewDao ----------------



    void insertReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.insertReview(review);
        });
    }


    void updateReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.updateReview(review);
        });
    }


    void deleteReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReview(review);
        });
    }


    LiveData<List<Review>> getAllReviews(){
        return reviewDao.getAllReviews();
    }


    LiveData<List<Review>> getReviewById(int review_id){
        return reviewDao.getReviewById(review_id);
    }









}
