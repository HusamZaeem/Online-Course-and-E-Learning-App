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

    private static AppRepository instance;


    public AttachmentDao attachmentDao;
    public BookmarkDao bookmarkDao;
    public CallDao callDao;
    public ChatDao chatDao;
    public CourseDao courseDao;
    public EnrollmentDao enrollmentDao;
    public GroupDao groupDao;
    public GroupMembershipDao groupMembershipDao;
    public LessonDao lessonDao;
    public MentorCourseDao mentorCourseDao;
    public MentorDao mentorDao;
    public MessageDao messageDao;
    public ModuleDao moduleDao;
    public NotificationDao notificationDao;
    public ReviewDao reviewDao;
    public StudentDao studentDao;
    public StudentLessonDao studentLessonDao;
    public StudentMentorDao studentMentorDao;
    public StudentModuleDao studentModuleDao;


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

    public static synchronized AppRepository getInstance(Application application) {
        if (instance == null) {
            instance = new AppRepository(application);
        }
        return instance;
    }



    // AttachmentDao --------------------------------------------



    public void insertAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.insertAttachment(attachment);
        });
    }


    public void updateAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.updateAttachment(attachment);
        });
    }


    public void deleteAttachment (Attachment attachment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            attachmentDao.deleteAttachment(attachment);
        });
    }


    public LiveData<List<Attachment>> getAllAttachments (){
        return attachmentDao.getAllAttachments();
    }



    public List<Attachment> getStudentAttachmentsInChat(String student_id){
        return attachmentDao.getStudentAttachmentsInChat(student_id);
    }




    public List<Attachment> getStudentAttachmentsInAChat(String student_id, int chat_id){
        return attachmentDao.getStudentAttachmentsInAChat(student_id,chat_id);
    }





    // BookmarkDao --------------------------------------------




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


    public LiveData<List<Bookmark>> getAllBookmarks (){
        return bookmarkDao.getAllBookmarks();
    }


    public LiveData<List<Bookmark>> getBookmarkById (int bookmark_id){
        return bookmarkDao.getBookmarkById(bookmark_id);
    }


    public LiveData<List<Bookmark>> getAllStudentBookmarks (String student_id){
        return bookmarkDao.getAllStudentBookmarks(student_id);
    }





    // CallDao --------------------------------------------




    public void insertCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.insertCall(call);
        });
    }


    public void updateCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.updateCall(call);
        });
    }


    public void deleteCall (Call call){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            callDao.deleteCall(call);
        });
    }


    public LiveData<List<Call>> getAllCalls (){
        return callDao.getAllCalls();
    }


    public Call getCallById (int call_id){
        return callDao.getCallById(call_id);
    }




    public LiveData<List<Call>> getAllStudentCalls (String student_id){
        return callDao.getAllStudentCalls(student_id);
    }



    public LiveData<List<Call>> getAllStudentCallsForAChat (String student_id, int chat_id){
        return callDao.getAllStudentCallsForAChat(student_id,chat_id);
    }







    // ChatDao --------------------------------------------




    public void insertChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.insertChat(chat);
        });
    }


    public void updateChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.updateChat(chat);
        });
    }


    public void deleteChat (Chat chat){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.deleteChat(chat);
        });
    }


    public LiveData<List<Chat>> getAllChats(){
        return chatDao.getAllChats();
    }


    public Chat getChatById(int chat_id){
        return chatDao.getChatById(chat_id);
    }



    public LiveData<List<Chat>> getAllStudentChats(String student_id){
        return chatDao.getAllStudentChats(student_id);
    }






    // CourseDao --------------------------------------------




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


    public LiveData<List<Course>> getAllCourses (){
        return courseDao.getAllCourses();
    }

    public Course getCourseById (int course_id){
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

    public LiveData<List<Enrollment>> getAllEnrollments(){
        return enrollmentDao.getAllEnrollments();
    }

    public LiveData<List<Enrollment>> getEnrollmentById(int enrollment_id){
        return enrollmentDao.getEnrollmentById(enrollment_id);
    }


    public Enrollment getStudentEnrollmentInCourse(String student_id, int course_id){
        return enrollmentDao.getStudentEnrollmentInCourse(student_id,course_id);
    }


    public int getTotalLessonsForCourse(int course_id){
        return enrollmentDao.getTotalLessonsForCourse(course_id);
    }


    public int getCompletedLessonsForStudentInCourse(String student_id, int course_id){
        return enrollmentDao.getCompletedLessonsForStudentInCourse(student_id,course_id);
    }



    public void updateEnrollmentProgress(String student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.updateEnrollmentProgress(student_id,course_id);
        });
    }


    public LiveData<List<Enrollment>> getEnrollmentsForStudent(String student_id){
        return enrollmentDao.getEnrollmentsForStudent(student_id);
    }




    public void calculateFinalGrade(String student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.calculateFinalGrade(student_id,course_id);
        });
    }



    public void setCompletionDateIfAllLessonsCompleted(String student_id, int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.setCompletionDateIfAllLessonsCompleted(student_id,course_id);
        });
    }




    // Enroll in a paid course
    public void completeEnrollment(String studentId, int courseId, double fee, String courseName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Update enrollment status
            enrollmentDao.updateFeeStatusAndTimestamp(studentId, courseId, fee);

            // Create the group if it doesn't already exist
            instance.createGroupForCourse(courseId, courseName);

            // Add the student to the group
            instance.addStudentToGroup(studentId, courseId);
        });
    }



    // Enroll in a free course
    public void enrollInFreeCourse(String studentId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            enrollmentDao.enrollInFreeCourse(studentId, courseId);
        });
    }

    public Enrollment checkEnrollment(String student_id, int course_id) {
        return enrollmentDao.checkEnrollment(student_id, course_id);
    }




    // GroupDao --------------------------------------------



    public void insertGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.insertGroup(group);
        });
    }

    public void updateGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.updateGroup(group);
        });
    }

    public void deleteGroup (Group group){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.deleteGroup(group);
        });
    }

    public LiveData<List<Group>> getAllGroups(){
        return groupDao.getAllGroups();
    }

    public LiveData<List<Group>> getGroupById(int group_id){
        return groupDao.getGroupById(group_id);
    }

    public LiveData<List<Group>> getGroupByGroupName(String group_name){
        return groupDao.getGroupByGroupName(group_name);
    }



    public Group getGroupByCourseId(int course_id){
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
    public void addStudentToGroup(String studentId, int courseId) {
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





    public void insertGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.insertGroupMembership(groupMembership);
        });
    }

    public void updateGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.updateGroupMembership(groupMembership);
        });
    }

    public void deleteGroupMembership (GroupMembership groupMembership){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            groupMembershipDao.deleteGroupMembership(groupMembership);
        });
    }


    public LiveData<List<GroupMembership>> getGroupMembershipById(int group_membership_id){
        return groupMembershipDao.getGroupMembershipById(group_membership_id);
    }

    public LiveData<List<String>> getAllGroupStudents(int group_id){
        return groupMembershipDao.getAllGroupStudents(group_id);
    }

    public LiveData<List<Integer>> getAllStudentGroups(String student_id){
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



    public void finishExam(String studentId, int lessonId, int moduleId, int courseId, double grade) {
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




    public LiveData<List<Integer>> getAllCourseMentors(int course_id){
        return mentorCourseDao.getAllCourseMentors(course_id);
    }

    public LiveData<List<Integer>> getAllMentorCourses(int mentor_id){
        return mentorCourseDao.getAllMentorCourses(mentor_id);
    }





    // MentorDao --------------------------------------------






    public void insertMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.insertMentor(mentor);
        });
    }

    public void updateMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.updateMentor(mentor);
        });
    }

    public void deleteMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.deleteMentor(mentor);
        });
    }

    public LiveData<List<Mentor>> getAllMentors(){
        return mentorDao.getAllMentors();
    }

    public LiveData<List<Mentor>> getMentorById(int mentor_id){
        return mentorDao.getMentorById(mentor_id);
    }





    // MessageDao --------------------------------------------





    public void insertMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insertMessage(message);
        });
    }

    public void updateMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateMessage(message);
        });
    }

    public void deleteMessage (Message message){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.deleteMessage(message);
        });
    }

    public LiveData<List<Message>> getAllMessages(){
        return messageDao.getAllMessages();
    }

    public LiveData<List<Message>> getMessageById(int message_id){
        return messageDao.getMessageById(message_id);
    }

    public LiveData<List<Message>> getGroupMessagesByGroupId(int group_id){
        return messageDao.getGroupMessagesByGroupId(group_id);
    }

    public LiveData<List<Message>> getChatMessagesByChatId(int chat_id){
        return messageDao.getChatMessagesByChatId(chat_id);
    }

    public LiveData<List<Message>> getChatMessagesByMessageType(String message_type){
        return messageDao.getChatMessagesByMessageType(message_type);
    }


    public LiveData<List<Message>> searchMessagesByContent(String content){
        return messageDao.searchMessagesByContent(content);
    }






    // ModuleDao --------------------------------------------




    public void insertModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.insertModule(module);
        });
    }

    public void updateModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.updateModule(module);
        });
    }


    public void deleteModule (Module module){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.deleteModule(module);
        });
    }


    public LiveData<List<Module>> getAllModules (){
        return moduleDao.getAllModules();
    }


    public LiveData<List<Module>> getModuleById (int module_id){
        return moduleDao.getModuleById(module_id);
    }


    public LiveData<List<Module>> getAllCourseModules (int course_id){
        return moduleDao.getAllCourseModules(course_id);
    }


    public int getModuleDuration(int module_id){
        return moduleDao.getModuleDuration(module_id);
    }


    public void updateModuleDuration(int module_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.updateModuleDuration(module_id);
        });
    }







    // NotificationDao --------------------------------------------




    public void insertNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.insertNotification(notification);
        });
    }


    public void updateNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.updateNotification(notification);
        });
    }


    public void deleteNotification (Notification notification){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.deleteNotification(notification);
        });
    }


    public LiveData<List<Notification>> getAllNotifications(){
        return notificationDao.getAllNotifications();
    }


    public LiveData<List<Notification>> getNotificationById(int notification_id){
        return notificationDao.getNotificationById(notification_id);
    }



    public LiveData<List<Notification>> getAllStudentNotifications(String student_id){
        return notificationDao.getAllStudentNotifications(student_id);
    }






    // ReviewDao --------------------------------------------





    public void insertReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.insertReview(review);
        });
    }

    public void updateReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.updateReview(review);
        });
    }

    public void deleteReview (Review review){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReview(review);
        });
    }

    public void insertMentorReview(String student_id, int mentor_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
            insertReview(review);
        });
    }


    public void insertCourseReview(String student_id, int course_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, course_id, "Course", rate, comment);
            insertReview(review);
        });
    }

    public LiveData<List<Review>> getAllReviews(){
        return reviewDao.getAllReviews();
    }

    public LiveData<List<Review>> getReviewById(int review_id){
        return reviewDao.getReviewById(review_id);
    }


    public LiveData<List<Review>> getReviewsForCourse(int course_id){
        return reviewDao.getReviewsForCourse(course_id);
    }


    public LiveData<List<Review>> getReviewsForMentor(int mentor_id){
        return reviewDao.getReviewsForMentor(mentor_id);
    }


    public void deleteAllCourseReviews(int course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllCourseReviews(course_id);
        });
    }


    public void deleteAllMentorReviews(int mentor_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllMentorReviews(mentor_id);
        });
    }


    public void deleteReviewForCourse(int course_id, int review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForCourse(course_id,review_id);
        });
    }



    public void deleteReviewForMentor(int mentor_id, int review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForMentor(mentor_id,review_id);
        });
    }






    // StudentDao --------------------------------------------




    public void insertStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.insertStudent(student);
        });
    }


    public void insertStudents(List<Student> students){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.insertStudents(students);
        });
    }

    public void updateStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.updateStudent(student);
        });
    }

    public void deleteStudent (Student student){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.deleteStudent(student);
        });
    }

    public LiveData<List<Student>> getAllStudents (){
        return studentDao.getAllStudents();
    }

    public LiveData<List<Student>> getStudentById (String student_id){
        return studentDao.getStudentById(student_id);
    }



    public LiveData<List<Student>> getUnsyncedStudents(){
        return studentDao.getUnsyncedStudents();
    }



    public LiveData<Student> getStudentByEmail (String email){
        return studentDao.getStudentByEmail(email);
    }


    // StudentLessonDao --------------------------------------------






    public void insertStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.insertStudentLesson(studentLesson);
        });
    }

    public void updateStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.insertStudentLesson(studentLesson);
        });
    }

    public void deleteStudentLesson(StudentLesson studentLesson){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.deleteStudentLesson(studentLesson);
        });
    }


    public void updateCompletionStatus(String student_id, int lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateCompletionStatus(student_id,lesson_id,completion_status);
        });
    }


    public boolean getCompletionStatus(String student_id, int lesson_id){
        return studentLessonDao.getCompletionStatus(student_id,lesson_id);
    }


    public void updateLessonCompletionStatus(String student_id, int lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateLessonCompletionStatus(student_id,lesson_id,completion_status);
        });
    }

    public void updateLessonStatusAndProgress(String student_id, int lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, int course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }


    public void markLessonAsCompleted(String studentId, int lessonId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            studentLessonDao.updateLessonStatusAndProgress(studentId, lessonId, true, enrollmentDao, courseId);
        });
    }



    // StudentMentorDao --------------------------------------------






    public void insertStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.insertStudentMentor(studentMentor);
        });
    }

    public void updateStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.updateStudentMentor(studentMentor);
        });
    }

    public void deleteStudentMentor(StudentMentor studentMentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentMentorDao.deleteStudentMentor(studentMentor);
        });
    }

    public LiveData<List<String>> getAllMentorStudents(int mentor_id){
        return studentMentorDao.getAllMentorStudents(mentor_id);
    }

    public LiveData<List<Integer>> getAllStudentMentors(String student_id){
        return studentMentorDao.getAllStudentMentors(student_id);
    }






    // StudentModuleDao --------------------------------------------





    public void insertStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.insertStudentModule(studentModule);
        });
    }

    public void updateStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.updateStudentModule(studentModule);
        });
    }

    public void deleteStudentModule(StudentModule studentModule){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.deleteStudentModule(studentModule);
        });
    }

    public LiveData<List<Double>> getAllStudentModuleGrade(String student_id){
        return studentModuleDao.getAllStudentModuleGrade(student_id);
    }

    public LiveData<List<Double>> getStudentModuleGrade(String student_id, int module_id){
        return studentModuleDao.getStudentModuleGrade(student_id,module_id);
    }


    public void setModuleGrade(String student_id, int module_id, double module_grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.setModuleGrade(student_id,module_id,module_grade);
        });
    }






}
