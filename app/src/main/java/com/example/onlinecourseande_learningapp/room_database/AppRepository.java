package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.onlinecourseande_learningapp.AppExecutors;
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
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.List;

public class AppRepository {

    private AppRepository appRepository;


    private final AttachmentDao attachmentDao;
    private final BookmarkDao bookmarkDao;
    private final CallDao callDao;
    private final ChatDao chatDao;
    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;
    private final GroupDao groupDao;
    private final GroupMembershipDao groupMembershipDao;
    private final LessonDao lessonDao;
    private final MentorCourseDao mentorCourseDao;
    private final MentorDao mentorDao;
    private final MessageDao messageDao;
    private final ModuleDao moduleDao;
    private final NotificationDao notificationDao;
    private final ReviewDao reviewDao;
    private final StudentDao studentDao;
    private final StudentLessonDao studentLessonDao;
    private final StudentMentorDao studentMentorDao;
    private final StudentModuleDao studentModuleDao;


    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        attachmentDao = db.attachmentDao();
        bookmarkDao = db.bookmarkDao();
        callDao = db.callDao();
        chatDao = db.chatDao();
        courseDao = db.courseDao();
        enrollmentDao = db.enrollmentDao();
        groupDao = db.groupDao();
        groupMembershipDao = db.groupMembershipDao();
        lessonDao = db.lessonDao();
        mentorCourseDao = db.mentorCourseDao();
        mentorDao = db.mentorDao();
        messageDao = db.messageDao();
        moduleDao = db.moduleDao();
        notificationDao = db.notificationDao();
        reviewDao = db.reviewDao();
        studentDao = db.studentDao();
        studentLessonDao = db.studentLessonDao();
        studentMentorDao = db.studentMentorDao();
        studentModuleDao = db.studentModuleDao();
    }



    // AttachmentDao --------------------------------------------



    void insertAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.insertAttachment(attachment);
        });
    }


    void updateAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.updateAttachment(attachment);
        });
    }


    void deleteAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.deleteAttachment(attachment);
        });
    }


    LiveData<List<Attachment>> getAllAttachments (){
        return attachmentDao.getAllAttachments();
    }



    List<Attachment> getStudentAttachmentsInChat(int student_id){
        return attachmentDao.getStudentAttachmentsInChat(student_id);
    }




    List<Attachment> getStudentAttachmentsInAChat(int student_id, int chat_id){
        return attachmentDao.getStudentAttachmentsInAChat(student_id,chat_id);
    }





    // BookmarkDao --------------------------------------------




    void insertBookmark (Bookmark bookmark){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.insertBookmark(bookmark);
        });
    }


    void updateBookmark (Bookmark bookmark){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.updateBookmark(bookmark);
        });
    }


    void deleteBookmark (Bookmark bookmark){
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


    LiveData<List<Bookmark>> getAllStudentBookmarks (int student_id){
        return bookmarkDao.getAllStudentBookmarks(student_id);
    }





    // CallDao --------------------------------------------




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


    Call getCallById (int call_id){
        return callDao.getCallById(call_id);
    }




    LiveData<List<Call>> getAllStudentCalls (int student_id){
        return callDao.getAllStudentCalls(student_id);
    }



    LiveData<List<Call>> getAllStudentCallsForAChat (int student_id, int chat_id){
        return callDao.getAllStudentCallsForAChat(student_id,chat_id);
    }







    // ChatDao --------------------------------------------




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


    Chat getChatById(int chat_id){
        return chatDao.getChatById(chat_id);
    }



    LiveData<List<Chat>> getAllStudentChats(int student_id){
        return chatDao.getAllStudentChats(student_id);
    }






    // CourseDao --------------------------------------------




    void insertCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insertCourse(course);
        });
    }


    void updateCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.updateCourse(course);
        });
    }


    void deleteCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.deleteCourse(course);
        });
    }


    LiveData<List<Course>> getAllCourses (){
        return courseDao.getAllCourses();
    }

    Course getCourseById (int course_id){
        return courseDao.getCourseById(course_id);
    }





    // EnrollmentDao --------------------------------------------




    public void insertEnrollment (Enrollment enrollment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.insertEnrollment(enrollment);
        });
    }


    public void updateEnrollment (Enrollment enrollment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.updateEnrollment(enrollment);
        });
    }


    public void deleteEnrollment (Enrollment enrollment){
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


    Enrollment getStudentEnrollmentInCourse(int student_id, int course_id){
        return enrollmentDao.getStudentEnrollmentInCourse(student_id,course_id);
    }


    int getTotalLessonsForCourse(int course_id){
        return enrollmentDao.getTotalLessonsForCourse(course_id);
    }


    int getCompletedLessonsForStudentInCourse(int student_id, int course_id){
        return enrollmentDao.getCompletedLessonsForStudentInCourse(student_id,course_id);
    }



    void updateEnrollmentProgress(int student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.updateEnrollmentProgress(student_id,course_id);
        });
    }


    LiveData<List<Enrollment>> getEnrollmentsForStudent(int student_id){
        return enrollmentDao.getEnrollmentsForStudent(student_id);
    }




    void calculateFinalGrade(int student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.calculateFinalGrade(student_id,course_id);
        });
    }



    void setCompletionDateIfAllLessonsCompleted(int student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.setCompletionDateIfAllLessonsCompleted(student_id,course_id);
        });
    }




    // Enroll in a paid course
    public void completeEnrollment(int studentId, int courseId, double fee, String courseName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Update enrollment status
            enrollmentDao.updateFeeStatusAndTimestamp(studentId, courseId, fee);

            // Create the group if it doesn't already exist
            appRepository.createGroupForCourse(courseId, courseName);

            // Add the student to the group
            appRepository.addStudentToGroup(studentId, courseId);
        });
    }



    // Enroll in a free course
    public void enrollInFreeCourse(int studentId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            enrollmentDao.enrollInFreeCourse(studentId, courseId);
        });
    }

    public Enrollment checkEnrollment(int student_id, int course_id) {
        return enrollmentDao.checkEnrollment(student_id, course_id);
    }




    // GroupDao --------------------------------------------



    void insertGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.insertGroup(group);
        });
    }

    void updateGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.updateGroup(group);
        });
    }

    void deleteGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.deleteGroup(group);
        });
    }

    LiveData<List<Group>> getAllGroups(){
        return groupDao.getAllGroups();
    }

    LiveData<List<Group>> getGroupById(int group_id){
        return groupDao.getGroupById(group_id);
    }

    LiveData<List<Group>> getGroupByGroupName(String group_name){
        return groupDao.getGroupByGroupName(group_name);
    }



    Group getGroupByCourseId(int course_id){
        return groupDao.getGroupByCourseId(course_id);
    }



    public void createGroupForCourse(int courseId, String courseName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (groupDao.getGroupByCourseId(courseId) == null){
                Group group = new Group(courseName,courseId);
                groupDao.insertGroup(group);
            }

        });
    }

    // Add a student to a group
    public void addStudentToGroup(int studentId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Group group = groupDao.getGroupByCourseId(courseId);
            if (group != null) {
                GroupMembership groupMembership = new GroupMembership();
                groupMembership.setGroup_id(group.getGroup_id());
                groupMembership.setStudent_id(studentId);
                groupMembershipDao.insertGroupMembership(groupMembership);
            }
        });
    }



    // GroupMembershipDao --------------------------------------------





    void insertGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.insertGroupMembership(groupMembership);
        });
    }

    void updateGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.updateGroupMembership(groupMembership);
        });
    }

    void deleteGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.deleteGroupMembership(groupMembership);
        });
    }


    LiveData<List<GroupMembership>> getGroupMembershipById(int group_membership_id){
        return groupMembershipDao.getGroupMembershipById(group_membership_id);
    }

    LiveData<List<Integer>> getAllGroupStudents(int group_id){
        return groupMembershipDao.getAllGroupStudents(group_id);
    }

    LiveData<List<Integer>> getAllStudentGroups(int student_id){
        return groupMembershipDao.getAllStudentGroups(student_id);
    }







    // LessonDao --------------------------------------------


    public void insertLesson(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.insertLesson(lesson);
        });
    }


    public void updateLesson(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.updateLesson(lesson);
        });
    }


    public void deleteLesson(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.deleteLesson(lesson);
        });
    }


    public LiveData<List<Lesson>> getAllLessons() {
        return lessonDao.getAllLessons();
    }


    public LiveData<List<Lesson>> getLessonById(int lessonId) {
        return lessonDao.getLessonById(lessonId);
    }


    public LiveData<List<Lesson>> getAllLessonsByModuleId(int moduleId) {
        return lessonDao.getAllLessonsByModuleId(moduleId);
    }


    public LiveData<List<Lesson>> getModuleExamByModuleId(int moduleId) {
        return lessonDao.getModuleExamByModuleId(moduleId);
    }


    public void setLastLessonAsExam(int moduleId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.setLastLessonAsExam(moduleId);
        });
    }


    public void insertLessonAndUpdateModule(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.insertLessonAndUpdateModule(lesson, moduleDao);
        });
    }


    public void deleteLessonAndUpdateModule(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            lessonDao.deleteLessonAndUpdateModule(lesson, moduleDao);
        });
    }



    public void finishExam(int studentId, int lessonId, int moduleId, int courseId, double grade) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Set module grade
            studentModuleDao.setModuleGrade(studentId, moduleId, grade);

            // Recalculate final grade
            enrollmentDao.calculateFinalGrade(studentId, courseId);

            // Check and set completion date if all lessons are done
            enrollmentDao.setCompletionDateIfAllLessonsCompleted(studentId, courseId);
        });
    }





    // MentorCourseDao --------------------------------------------




    LiveData<List<Integer>> getAllCourseMentors(int course_id){
        return mentorCourseDao.getAllCourseMentors(course_id);
    }

    LiveData<List<Integer>> getAllMentorCourses(int mentor_id){
        return mentorCourseDao.getAllMentorCourses(mentor_id);
    }





    // MentorDao --------------------------------------------






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





    // MessageDao --------------------------------------------





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

    LiveData<List<Message>> getGroupMessagesByGroupId(int group_id){
        return messageDao.getGroupMessagesByGroupId(group_id);
    }

    LiveData<List<Message>> getChatMessagesByChatId(int chat_id){
        return messageDao.getChatMessagesByChatId(chat_id);
    }

    LiveData<List<Message>> getChatMessagesByMessageType(String message_type){
        return messageDao.getChatMessagesByMessageType(message_type);
    }


    LiveData<List<Message>> searchMessagesByContent(String content){
        return messageDao.searchMessagesByContent(content);
    }






    // ModuleDao --------------------------------------------




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


    LiveData<List<Module>> getAllCourseModules (int course_id){
        return moduleDao.getAllCourseModules(course_id);
    }


    int getModuleDuration(int module_id){
        return moduleDao.getModuleDuration(module_id);
    }


    void updateModuleDuration(int module_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.updateModuleDuration(module_id);
        });
    }







    // NotificationDao --------------------------------------------




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



    LiveData<List<Notification>> getAllStudentNotifications(int student_id){
        return notificationDao.getAllStudentNotifications(student_id);
    }






    // ReviewDao --------------------------------------------





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

    void insertMentorReview(int student_id, int mentor_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
            insertReview(review);
        });
    }


    void insertCourseReview(int student_id, int course_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, course_id, "Course", rate, comment);
            insertReview(review);
        });
    }

    LiveData<List<Review>> getAllReviews(){
        return reviewDao.getAllReviews();
    }

    LiveData<List<Review>> getReviewById(int review_id){
        return reviewDao.getReviewById(review_id);
    }


    LiveData<List<Review>> getReviewsForCourse(int course_id){
        return reviewDao.getReviewsForCourse(course_id);
    }


    LiveData<List<Review>> getReviewsForMentor(int mentor_id){
        return reviewDao.getReviewsForMentor(mentor_id);
    }


    void deleteAllCourseReviews(int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllCourseReviews(course_id);
        });
    }


    void deleteAllMentorReviews(int mentor_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllMentorReviews(mentor_id);
        });
    }


    void deleteReviewForCourse(int course_id, int review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForCourse(course_id,review_id);
        });
    }



    void deleteReviewForMentor(int mentor_id, int review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForMentor(mentor_id,review_id);
        });
    }






    // StudentDao --------------------------------------------




    void insertStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.insertStudent(student);
        });
    }

    void updateStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.updateStudent(student);
        });
    }

    void deleteStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.deleteStudent(student);
        });
    }

    LiveData<List<Student>> getAllStudents (){
        return studentDao.getAllStudents();
    }

    LiveData<List<Student>> getStudentById (int student_id){
        return studentDao.getStudentById(student_id);
    }





    // StudentLessonDao --------------------------------------------






    void insertStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.insertStudentLesson(studentLesson);
        });
    }

    void updateStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.insertStudentLesson(studentLesson);
        });
    }

    void deleteStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.deleteStudentLesson(studentLesson);
        });
    }


    void updateCompletionStatus(int student_id, int lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateCompletionStatus(student_id,lesson_id,completion_status);
        });
    }


    String getCompletionStatus(int student_id, int lesson_id){
        return studentLessonDao.getCompletionStatus(student_id,lesson_id);
    }


    void updateLessonCompletionStatus(int student_id, int lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateLessonCompletionStatus(student_id,lesson_id,completion_status);
        });
    }

    void updateLessonStatusAndProgress(int student_id, int lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, int course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }


    public void markLessonAsCompleted(int studentId, int lessonId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            studentLessonDao.updateLessonStatusAndProgress(studentId, lessonId, true, enrollmentDao, courseId);
        });
    }



    // StudentMentorDao --------------------------------------------






    void insertStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.insertStudentMentor(studentMentor);
        });
    }

    void updateStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.updateStudentMentor(studentMentor);
        });
    }

    void deleteStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.deleteStudentMentor(studentMentor);
        });
    }

    LiveData<List<Integer>> getAllMentorStudents(int mentor_id){
        return studentMentorDao.getAllMentorStudents(mentor_id);
    }

    LiveData<List<Integer>> getAllStudentMentors(int student_id){
        return studentMentorDao.getAllStudentMentors(student_id);
    }






    // StudentModuleDao --------------------------------------------





    void insertStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.insertStudentModule(studentModule);
        });
    }

    void updateStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.updateStudentModule(studentModule);
        });
    }

    void deleteStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.deleteStudentModule(studentModule);
        });
    }

    LiveData<List<Double>> getAllStudentModuleGrade(int student_id){
        return studentModuleDao.getAllStudentModuleGrade(student_id);
    }

    LiveData<List<Double>> getStudentModuleGrade(int student_id, int module_id){
        return studentModuleDao.getStudentModuleGrade(student_id,module_id);
    }


    void setModuleGrade(int student_id, int module_id, double module_grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.setModuleGrade(student_id,module_id,module_grade);
        });
    }






}
