package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private final AppRepository appRepository;


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }






    //UserDao -----------------------------

    void insertUser (User user){
            appRepository.insertUser(user);
    }


    void updateUser (User user){
        appRepository.updateUser(user);
    }


    void deleteUser (User user){
            appRepository.deleteUser(user);
    }


    LiveData<List<User>> getAllUsers (){
        return appRepository.getAllUsers();
    }


    LiveData<List<User>> getUserById (int user_id){
        return appRepository.getUserById(user_id);
    }


    //CourseDao ------------------------


    void insertCourse (Course course){
            appRepository.insertCourse(course);
    }


    void updateCourse (Course course){
            appRepository.updateCourse(course);
    }


    void deleteCourse (Course course){
            appRepository.deleteCourse(course);
    }


    LiveData<List<Course>> getAllCourses (){
        return appRepository.getAllCourses();
    }


    LiveData<List<Course>> getCourseById (int course_id){
        return appRepository.getCourseById(course_id);
    }


    //BookmarkDao --------------------

    void insertBookmark (Bookmark bookmark){
        appRepository.insertBookmark(bookmark);
    }


    void updateBookmark (Bookmark bookmark){
            appRepository.updateBookmark(bookmark);
    }


    void deleteBookmark (Bookmark bookmark){
            appRepository.deleteBookmark(bookmark);
    }


    LiveData<List<Bookmark>> getAllBookmarks (){
        return appRepository.getAllBookmarks();
    }


    LiveData<List<Bookmark>> getBookmarkById (int bookmark_id){
        return appRepository.getBookmarkById(bookmark_id);
    }


    //CallDao ----------------


    void insertCall (Call call){
        appRepository.insertCall(call);
    }


    void updateCall (Call call){
            appRepository.updateCall(call);
    }


    void deleteCall (Call call){
            appRepository.deleteCall(call);
    }


    LiveData<List<Call>> getAllCalls (){
        return appRepository.getAllCalls();
    }


    LiveData<List<Call>> getCallById (int call_id){
        return appRepository.getCallById(call_id);
    }





    //CertificateDao ----------------



    void insertCertificate (Certificate certificate){
            appRepository.insertCertificate(certificate);
    }


    void updateCertificate (Certificate certificate){
        appRepository.updateCertificate(certificate);
    }


    void deleteCertificate (Certificate certificate){
            appRepository.deleteCertificate(certificate);
    }


    LiveData<List<Certificate>> getAllCertificates (){
        return appRepository.getAllCertificates();
    }


    LiveData<List<Certificate>> getCertificateById (int certificate_id){
        return appRepository.getCertificateById(certificate_id);
    }





    //ChatDao ----------------



    void insertChat (Chat chat){
            appRepository.insertChat(chat);
    }


    void updateChat (Chat chat){
            appRepository.updateChat(chat);
    }


    void deleteChat (Chat chat){
            appRepository.deleteChat(chat);
    }


    LiveData<List<Chat>> getAllChats(){
        return appRepository.getAllChats();
    }


    LiveData<List<Chat>> getChatById(int chat_id){
        return appRepository.getChatById(chat_id);
    }




    //CommentDao ----------------



    void insertComment (Comment comment){
            appRepository.insertComment(comment);
    }


    void updateComment (Comment comment){
            appRepository.updateComment(comment);
    }


    void deleteComment (Comment comment){
        appRepository.deleteComment(comment);
    }


    LiveData<List<Comment>> getAllComments(){
        return appRepository.getAllComments();
    }


    LiveData<List<Comment>> getCommentById(int comment_id){
        return appRepository.getCommentById(comment_id);
    }



    //DiscussionPostDao ----------------



    void insertDiscussionPost (DiscussionPost discussionPost){
            appRepository.insertDiscussionPost(discussionPost);
    }


    void updateDiscussionPost (DiscussionPost discussionPost){
        appRepository.updateDiscussionPost(discussionPost);
    }


    void deleteDiscussionPost (DiscussionPost discussionPost){
            appRepository.deleteDiscussionPost(discussionPost);
    }


    LiveData<List<DiscussionPost>> getAllDiscussionPosts(){
        return appRepository.getAllDiscussionPosts();
    }


    LiveData<List<DiscussionPost>> getDiscussionPostById(int post_id){
        return appRepository.getDiscussionPostById(post_id);
    }





    //EnrollmentDao ----------------



    void insertEnrollment (Enrollment enrollment){
            appRepository.insertEnrollment(enrollment);
    }


    void updateEnrollment (Enrollment enrollment){
        appRepository.updateEnrollment(enrollment);
    }


    void deleteEnrollment (Enrollment enrollment){
            appRepository.deleteEnrollment(enrollment);
    }


    LiveData<List<Enrollment>> getAllEnrollments(){
        return appRepository.getAllEnrollments();
    }


    LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id){
        return appRepository.getEnrollmentById(enrollment_id);
    }







    //GradeDao ----------------




    void insertGrade (Grade grade){
            appRepository.insertGrade(grade);
    }


    void updateGrade (Grade grade){
        appRepository.updateGrade(grade);
    }


    void deleteGrade (Grade grade){
            appRepository.deleteGrade(grade);
    }


    LiveData<List<Grade>> getAllGrades(){
        return appRepository.getAllGrades();
    }


    LiveData<List<Grade>> getGradeById(int grade_id){
        return appRepository.getGradeById(grade_id);
    }






    //LessonDao ----------------




    void insertLesson (Lesson lesson){
        appRepository.insertLesson(lesson);
    }


    void updateLesson (Lesson lesson){
            appRepository.updateLesson(lesson);
    }


    void deleteLesson (Lesson lesson){
        appRepository.deleteLesson(lesson);
    }


    LiveData<List<Lesson>> getAllLessons (){
        return appRepository.getAllLessons();
    }


    LiveData<List<Lesson>> getLessonById (int lesson_id){
        return appRepository.getLessonById(lesson_id);
    }






    //MentorDao ----------------





    void insertMentor (Mentor mentor){
            appRepository.insertMentor(mentor);
    }


    void updateMentor (Mentor mentor){
            appRepository.updateMentor(mentor);
    }


    void deleteMentor (Mentor mentor){
            appRepository.deleteMentor(mentor);
    }


    LiveData<List<Mentor>> getAllMentors(){
        return appRepository.getAllMentors();
    }


    LiveData<List<Mentor>> getMentorById(int mentor_id){
        return appRepository.getMentorById(mentor_id);
    }





    //MessageDao ----------------




    void insertMessage (Message message){
        appRepository.insertMessage(message);
    }


    void updateMessage (Message message){
            appRepository.updateMessage(message);
    }


    void deleteMessage (Message message){
            appRepository.deleteMessage(message);
    }


    LiveData<List<Message>> getAllMessages(){
        return appRepository.getAllMessages();
    }


    LiveData<List<Message>> getMessageById(int message_id){
        return appRepository.getMessageById(message_id);
    }







    //ModuleDao ---------------




    void insertModule (Module module){
        appRepository.insertModule(module);
    }


    void updateModule (Module module){
        appRepository.updateModule(module);
    }


    void deleteModule (Module module){
            appRepository.deleteModule(module);
    }


    LiveData<List<Module>> getAllModules (){
        return appRepository.getAllModules();
    }


    LiveData<List<Module>> getModuleById (int module_id){
        return appRepository.getModuleById(module_id);
    }








    // NotificationDao ------------




    void insertNotification (Notification notification){
        appRepository.insertNotification(notification);
    }


    void updateNotification (Notification notification){
        appRepository.updateNotification(notification);
    }


    void deleteNotification (Notification notification){
            appRepository.deleteNotification(notification);
    }


    LiveData<List<Notification>> getAllNotifications(){
        return appRepository.getAllNotifications();
    }


    LiveData<List<Notification>> getNotificationById(int notification_id){
        return appRepository.getNotificationById(notification_id);
    }








    //ParticipantDao ----------------





    void insertParticipant (Participant participant){
        appRepository.insertParticipant(participant);
    }


    void updateParticipant (Participant participant){
            appRepository.updateParticipant(participant);
    }


    void deleteParticipant (Participant participant){
            appRepository.deleteParticipant(participant);
    }


    LiveData<List<Participant>> getAllParticipants(){
        return appRepository.getAllParticipants();
    }


    LiveData<List<Participant>> getParticipantById(int participant_id){
        return appRepository.getParticipantById(participant_id);
    }





    //ReviewDao ----------------



    void insertReview (Review review){
            appRepository.insertReview(review);
    }


    void updateReview (Review review){
        appRepository.updateReview(review);
    }


    void deleteReview (Review review){
        appRepository.deleteReview(review);
    }


    LiveData<List<Review>> getAllReviews(){
        return appRepository.getAllReviews();
    }


    LiveData<List<Review>> getReviewById(int review_id){
        return appRepository.getReviewById(review_id);
    }








}
