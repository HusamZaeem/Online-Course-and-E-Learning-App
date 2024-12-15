package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

public class AppViewModel extends AndroidViewModel {

    private AppRepository appRepository;


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }






    //UserDao -----------------------------

    public void insertUser(Student student){
            appRepository.insertUser(student);
    }


    public void updateUser (Student student){
        appRepository.updateUser(student);
    }


    public void deleteUser (Student student){
            appRepository.deleteUser(student);
    }


    public  LiveData<List<Student>> getAllUsers (){
        return appRepository.getAllUsers();
    }


    public  LiveData<List<Student>> getUserById (int user_id){
        return appRepository.getUserById(user_id);
    }


    //CourseDao ------------------------


    public void insertCourse (Course course){
            appRepository.insertCourse(course);
    }


    public void updateCourse (Course course){
            appRepository.updateCourse(course);
    }


    public void deleteCourse (Course course){
            appRepository.deleteCourse(course);
    }


    public LiveData<List<Course>> getAllCourses (){
        return appRepository.getAllCourses();
    }


    public  LiveData<List<Course>> getCourseById (int course_id){
        return appRepository.getCourseById(course_id);
    }


    //BookmarkDao --------------------

    public void insertBookmark (Bookmark bookmark){
        appRepository.insertBookmark(bookmark);
    }


    public void updateBookmark (Bookmark bookmark){
            appRepository.updateBookmark(bookmark);
    }


    public void deleteBookmark (Bookmark bookmark){
            appRepository.deleteBookmark(bookmark);
    }


    public LiveData<List<Bookmark>> getAllBookmarks (){
        return appRepository.getAllBookmarks();
    }


    public  LiveData<List<Bookmark>> getBookmarkById (int bookmark_id){
        return appRepository.getBookmarkById(bookmark_id);
    }


    //CallDao ----------------


    public void insertCall (Call call){
        appRepository.insertCall(call);
    }


    public void updateCall (Call call){
            appRepository.updateCall(call);
    }


    public void deleteCall (Call call){
            appRepository.deleteCall(call);
    }


    public LiveData<List<Call>> getAllCalls (){
        return appRepository.getAllCalls();
    }


    public LiveData<List<Call>> getCallById (int call_id){
        return appRepository.getCallById(call_id);
    }





    //CertificateDao ----------------



    public void insertCertificate (Certificate certificate){
            appRepository.insertCertificate(certificate);
    }


    public void updateCertificate (Certificate certificate){
        appRepository.updateCertificate(certificate);
    }


    public void deleteCertificate (Certificate certificate){
            appRepository.deleteCertificate(certificate);
    }


    public LiveData<List<Certificate>> getAllCertificates (){
        return appRepository.getAllCertificates();
    }


    public  LiveData<List<Certificate>> getCertificateById (int certificate_id){
        return appRepository.getCertificateById(certificate_id);
    }





    //ChatDao ----------------



    public void insertChat (Chat chat){
            appRepository.insertChat(chat);
    }


    public void updateChat (Chat chat){
            appRepository.updateChat(chat);
    }


    public void deleteChat (Chat chat){
            appRepository.deleteChat(chat);
    }


    public LiveData<List<Chat>> getAllChats(){
        return appRepository.getAllChats();
    }


    public LiveData<List<Chat>> getChatById(int chat_id){
        return appRepository.getChatById(chat_id);
    }




    //CommentDao ----------------



    public void insertComment (Comment comment){
            appRepository.insertComment(comment);
    }


    public void updateComment (Comment comment){
            appRepository.updateComment(comment);
    }


    public void deleteComment (Comment comment){
        appRepository.deleteComment(comment);
    }


    public LiveData<List<Comment>> getAllComments(){
        return appRepository.getAllComments();
    }


    public LiveData<List<Comment>> getCommentById(int comment_id){
        return appRepository.getCommentById(comment_id);
    }



    //DiscussionPostDao ----------------



    public void insertDiscussionPost (DiscussionPost discussionPost){
            appRepository.insertDiscussionPost(discussionPost);
    }


    public void updateDiscussionPost (DiscussionPost discussionPost){
        appRepository.updateDiscussionPost(discussionPost);
    }


    public void deleteDiscussionPost (DiscussionPost discussionPost){
            appRepository.deleteDiscussionPost(discussionPost);
    }


    public LiveData<List<DiscussionPost>> getAllDiscussionPosts(){
        return appRepository.getAllDiscussionPosts();
    }


    public LiveData<List<DiscussionPost>> getDiscussionPostById(int post_id){
        return appRepository.getDiscussionPostById(post_id);
    }





    //EnrollmentDao ----------------



    public void insertEnrollment (Enrollment enrollment){
            appRepository.insertEnrollment(enrollment);
    }


    public void updateEnrollment (Enrollment enrollment){
        appRepository.updateEnrollment(enrollment);
    }


    public void deleteEnrollment (Enrollment enrollment){
            appRepository.deleteEnrollment(enrollment);
    }


    public LiveData<List<Enrollment>> getAllEnrollments(){
        return appRepository.getAllEnrollments();
    }


    public LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id){
        return appRepository.getEnrollmentById(enrollment_id);
    }







    //GradeDao ----------------




    public void insertGrade (Grade grade){
            appRepository.insertGrade(grade);
    }


    public void updateGrade (Grade grade){
        appRepository.updateGrade(grade);
    }


    public void deleteGrade (Grade grade){
            appRepository.deleteGrade(grade);
    }


    public LiveData<List<Grade>> getAllGrades(){
        return appRepository.getAllGrades();
    }


    public LiveData<List<Grade>> getGradeById(int grade_id){
        return appRepository.getGradeById(grade_id);
    }






    //LessonDao ----------------




    public void insertLesson (Lesson lesson){
        appRepository.insertLesson(lesson);
    }


    public void updateLesson (Lesson lesson){
            appRepository.updateLesson(lesson);
    }


    public void deleteLesson (Lesson lesson){
        appRepository.deleteLesson(lesson);
    }


    public  LiveData<List<Lesson>> getAllLessons (){
        return appRepository.getAllLessons();
    }


    public LiveData<List<Lesson>> getLessonById (int lesson_id){
        return appRepository.getLessonById(lesson_id);
    }






    //MentorDao ----------------





    public void insertMentor (Mentor mentor){
            appRepository.insertMentor(mentor);
    }


    public void updateMentor (Mentor mentor){
            appRepository.updateMentor(mentor);
    }


    public void deleteMentor (Mentor mentor){
            appRepository.deleteMentor(mentor);
    }


    public LiveData<List<Mentor>> getAllMentors(){
        return appRepository.getAllMentors();
    }


    public LiveData<List<Mentor>> getMentorById(int mentor_id){
        return appRepository.getMentorById(mentor_id);
    }





    //MessageDao ----------------




    public void insertMessage (Message message){
        appRepository.insertMessage(message);
    }


    public void updateMessage (Message message){
            appRepository.updateMessage(message);
    }


    public void deleteMessage (Message message){
            appRepository.deleteMessage(message);
    }


    public LiveData<List<Message>> getAllMessages(){
        return appRepository.getAllMessages();
    }


    public LiveData<List<Message>> getMessageById(int message_id){
        return appRepository.getMessageById(message_id);
    }







    //ModuleDao ---------------




    public void insertModule (Module module){
        appRepository.insertModule(module);
    }


    public void updateModule (Module module){
        appRepository.updateModule(module);
    }


    public void deleteModule (Module module){
            appRepository.deleteModule(module);
    }


    public LiveData<List<Module>> getAllModules (){
        return appRepository.getAllModules();
    }


    public LiveData<List<Module>> getModuleById (int module_id){
        return appRepository.getModuleById(module_id);
    }








    // NotificationDao ------------




    public void insertNotification (Notification notification){
        appRepository.insertNotification(notification);
    }


    public void updateNotification (Notification notification){
        appRepository.updateNotification(notification);
    }


    public  void deleteNotification (Notification notification){
            appRepository.deleteNotification(notification);
    }


    public LiveData<List<Notification>> getAllNotifications(){
        return appRepository.getAllNotifications();
    }


    public LiveData<List<Notification>> getNotificationById(int notification_id){
        return appRepository.getNotificationById(notification_id);
    }








    //ParticipantDao ----------------





    public void insertParticipant (Participant participant){
        appRepository.insertParticipant(participant);
    }


    public  void updateParticipant (Participant participant){
            appRepository.updateParticipant(participant);
    }


    public void deleteParticipant (Participant participant){
            appRepository.deleteParticipant(participant);
    }


    public  LiveData<List<Participant>> getAllParticipants(){
        return appRepository.getAllParticipants();
    }


    public LiveData<List<Participant>> getParticipantById(int participant_id){
        return appRepository.getParticipantById(participant_id);
    }





    //ReviewDao ----------------



    public void insertReview (Review review){
            appRepository.insertReview(review);
    }


    public void updateReview (Review review){
        appRepository.updateReview(review);
    }


    public void deleteReview (Review review){
        appRepository.deleteReview(review);
    }


    public LiveData<List<Review>> getAllReviews(){
        return appRepository.getAllReviews();
    }


    public LiveData<List<Review>> getReviewById(int review_id){
        return appRepository.getReviewById(review_id);
    }








}
