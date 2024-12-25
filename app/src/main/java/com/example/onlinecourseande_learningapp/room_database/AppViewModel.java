package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.onlinecourseande_learningapp.AppExecutors;
import com.example.onlinecourseande_learningapp.room_database.DAOs.EnrollmentDao;
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

public class AppViewModel extends AndroidViewModel {

    AppRepository appRepository;
    private final MutableLiveData<Boolean> lessonCompletionStatus = new MutableLiveData<>();


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }






    // AttachmentDao --------------------------------------------



    public void insertAttachment (Attachment attachment){
            appRepository.insertAttachment(attachment);
    }


    public void updateAttachment (Attachment attachment){
        appRepository.updateAttachment(attachment);
    }


    public void deleteAttachment (Attachment attachment){
            appRepository.deleteAttachment(attachment);
    }


    public LiveData<List<Attachment>> getAllAttachments (){
        return appRepository.getAllAttachments();
    }



    public List<Attachment> getStudentAttachmentsInChat(String student_id){
        return appRepository.getStudentAttachmentsInChat(student_id);
    }




    public List<Attachment> getStudentAttachmentsInAChat(String student_id, int chat_id){
        return appRepository.getStudentAttachmentsInAChat(student_id,chat_id);
    }





    // BookmarkDao --------------------------------------------




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


    public LiveData<List<Bookmark>> getBookmarkById (int bookmark_id){
        return appRepository.getBookmarkById(bookmark_id);
    }


    public LiveData<List<Bookmark>> getAllStudentBookmarks (String student_id){
        return appRepository.getAllStudentBookmarks(student_id);
    }





    // CallDao --------------------------------------------




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


    Call getCallById (int call_id){
        return appRepository.getCallById(call_id);
    }




    public LiveData<List<Call>> getAllStudentCalls (String student_id){
        return appRepository.getAllStudentCalls(student_id);
    }



    public LiveData<List<Call>> getAllStudentCallsForAChat (String student_id, int chat_id){
        return appRepository.getAllStudentCallsForAChat(student_id,chat_id);
    }







    // ChatDao --------------------------------------------




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


    Chat getChatById(int chat_id){
        return appRepository.getChatById(chat_id);
    }



    public LiveData<List<Chat>> getAllStudentChats(String student_id){
        return appRepository.getAllStudentChats(student_id);
    }






    // CourseDao --------------------------------------------




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

    Course getCourseById (int course_id){
        return appRepository.getCourseById(course_id);
    }





    // EnrollmentDao --------------------------------------------




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


    public Enrollment getStudentEnrollmentInCourse(String student_id, int course_id){
        return appRepository.getStudentEnrollmentInCourse(student_id,course_id);
    }


    public int getTotalLessonsForCourse(int course_id){
        return appRepository.getTotalLessonsForCourse(course_id);
    }


    public int getCompletedLessonsForStudentInCourse(String student_id, int course_id){
        return appRepository.getCompletedLessonsForStudentInCourse(student_id,course_id);
    }



    public void updateEnrollmentProgress(String student_id, int course_id){
            appRepository.updateEnrollmentProgress(student_id,course_id);
    }


    public LiveData<List<Enrollment>> getEnrollmentsForStudent(String student_id){
        return appRepository.getEnrollmentsForStudent(student_id);
    }




    public void calculateFinalGrade(String student_id, int course_id){
            appRepository.calculateFinalGrade(student_id,course_id);
    }



    public void setCompletionDateIfAllLessonsCompleted(String student_id, int course_id){
        appRepository.setCompletionDateIfAllLessonsCompleted(student_id,course_id);
    }




    // Enroll in a paid course
    public void completeEnrollment(String studentId, int courseId, double fee, String courseName) {
        appRepository.completeEnrollment(studentId, courseId, fee, courseName);
    }



    // Enroll in a free course
    public void enrollInFreeCourse(String studentId, int courseId) {
            appRepository.enrollInFreeCourse(studentId, courseId);
    }

    public Enrollment checkEnrollment(String student_id, int course_id) {
        return appRepository.checkEnrollment(student_id, course_id);
    }




    // GroupDao --------------------------------------------



    public void insertGroup (Group group){
            appRepository.insertGroup(group);
    }

    public void updateGroup (Group group){
        appRepository.updateGroup(group);
    }

    public void deleteGroup (Group group){
            appRepository.deleteGroup(group);
    }

    public LiveData<List<Group>> getAllGroups(){
        return appRepository.getAllGroups();
    }

    public LiveData<List<Group>> getGroupById(int group_id){
        return appRepository.getGroupById(group_id);
    }

    public LiveData<List<Group>> getGroupByGroupName(String group_name){
        return appRepository.getGroupByGroupName(group_name);
    }



    Group getGroupByCourseId(int course_id){
        return appRepository.getGroupByCourseId(course_id);
    }



    public void createGroupForCourse(int courseId, String courseName) {
            if (appRepository.getGroupByCourseId(courseId) == null){
                Group group = new Group(courseName,courseId);
                appRepository.insertGroup(group);
            }
    }

    // Add a student to a group
    public void addStudentToGroup(String studentId, int courseId) {
            Group group = appRepository.getGroupByCourseId(courseId);
            if (group != null) {
                GroupMembership groupMembership = new GroupMembership();
                groupMembership.setGroup_id(group.getGroup_id());
                groupMembership.setStudent_id(studentId);
                appRepository.insertGroupMembership(groupMembership);
            }
    }



    // GroupMembershipDao --------------------------------------------





    public void insertGroupMembership (GroupMembership groupMembership){
            appRepository.insertGroupMembership(groupMembership);
    }

    public void updateGroupMembership (GroupMembership groupMembership){
        appRepository.updateGroupMembership(groupMembership);
    }

    public void deleteGroupMembership (GroupMembership groupMembership){
            appRepository.deleteGroupMembership(groupMembership);
    }


    public LiveData<List<GroupMembership>> getGroupMembershipById(int group_membership_id){
        return appRepository.getGroupMembershipById(group_membership_id);
    }

    public LiveData<List<String>> getAllGroupStudents(int group_id){
        return appRepository.getAllGroupStudents(group_id);
    }

    public LiveData<List<Integer>> getAllStudentGroups(String student_id){
        return appRepository.getAllStudentGroups(student_id);
    }







    // LessonDao --------------------------------------------


    public void insertLesson(Lesson lesson) {
            appRepository.insertLesson(lesson);
    }


    public void updateLesson(Lesson lesson) {
        appRepository.updateLesson(lesson);
    }


    public void deleteLesson(Lesson lesson) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            appRepository.deleteLesson(lesson);
        });
    }


    public LiveData<List<Lesson>> getAllLessons() {
        return appRepository.getAllLessons();
    }


    public LiveData<List<Lesson>> getLessonById(int lessonId) {
        return appRepository.getLessonById(lessonId);
    }


    public LiveData<List<Lesson>> getAllLessonsByModuleId(int moduleId) {
        return appRepository.getAllLessonsByModuleId(moduleId);
    }


    public LiveData<List<Lesson>> getModuleExamByModuleId(int moduleId) {
        return appRepository.getModuleExamByModuleId(moduleId);
    }


    public void setLastLessonAsExam(int moduleId) {
        appRepository.setLastLessonAsExam(moduleId);
    }


    public void insertLessonAndUpdateModule(Lesson lesson) {
            appRepository.insertLessonAndUpdateModule(lesson);
    }


    public void deleteLessonAndUpdateModule(Lesson lesson) {
            appRepository.deleteLessonAndUpdateModule(lesson);
    }



    public void finishExam(String studentId, int lessonId, int moduleId, int courseId, double grade) {
            // Set module grade
            appRepository.setModuleGrade(studentId, moduleId, grade);

            // Recalculate final grade
            appRepository.calculateFinalGrade(studentId, courseId);

            // Check and set completion date if all lessons are done
            appRepository.setCompletionDateIfAllLessonsCompleted(studentId, courseId);
    }





    // MentorCourseDao --------------------------------------------




    public LiveData<List<Integer>> getAllCourseMentors(int course_id){
        return appRepository.getAllCourseMentors(course_id);
    }

    public LiveData<List<Integer>> getAllMentorCourses(int mentor_id){
        return appRepository.getAllMentorCourses(mentor_id);
    }





    // MentorDao --------------------------------------------






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





    // MessageDao --------------------------------------------





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

    public LiveData<List<Message>> getGroupMessagesByGroupId(int group_id){
        return appRepository.getGroupMessagesByGroupId(group_id);
    }

    public LiveData<List<Message>> getChatMessagesByChatId(int chat_id){
        return appRepository.getChatMessagesByChatId(chat_id);
    }

    public LiveData<List<Message>> getChatMessagesByMessageType(String message_type){
        return appRepository.getChatMessagesByMessageType(message_type);
    }


    public LiveData<List<Message>> searchMessagesByContent(String content){
        return appRepository.searchMessagesByContent(content);
    }






    // ModuleDao --------------------------------------------




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


    public LiveData<List<Module>> getAllCourseModules (int course_id){
        return appRepository.getAllCourseModules(course_id);
    }


    int getModuleDuration(int module_id){
        return appRepository.getModuleDuration(module_id);
    }


    public void updateModuleDuration(int module_id){
            appRepository.updateModuleDuration(module_id);
    }







    // NotificationDao --------------------------------------------




    public void insertNotification (Notification notification){
            appRepository.insertNotification(notification);
    }


    public void updateNotification (Notification notification){
            appRepository.updateNotification(notification);
    }


    public void deleteNotification (Notification notification){
            appRepository.deleteNotification(notification);
    }


    public LiveData<List<Notification>> getAllNotifications(){
        return appRepository.getAllNotifications();
    }


    public LiveData<List<Notification>> getNotificationById(int notification_id){
        return appRepository.getNotificationById(notification_id);
    }



    public LiveData<List<Notification>> getAllStudentNotifications(String student_id){
        return appRepository.getAllStudentNotifications(student_id);
    }






    // ReviewDao --------------------------------------------





    public void insertReview (Review review){
            appRepository.insertReview(review);
    }

    public void updateReview (Review review){
            appRepository.updateReview(review);
    }

    public void deleteReview (Review review){
            appRepository.deleteReview(review);
    }

    public void insertMentorReview(String student_id, int mentor_id, double rate, String comment) {
            Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
            insertReview(review);
    }


    public void insertCourseReview(String student_id, int course_id, double rate, String comment) {
            Review review = new Review(student_id, course_id, "Course", rate, comment);
            insertReview(review);
    }

    public LiveData<List<Review>> getAllReviews(){
        return appRepository.getAllReviews();
    }

    public LiveData<List<Review>> getReviewById(int review_id){
        return appRepository.getReviewById(review_id);
    }


    public LiveData<List<Review>> getReviewsForCourse(int course_id){
        return appRepository.getReviewsForCourse(course_id);
    }


    public LiveData<List<Review>> getReviewsForMentor(int mentor_id){
        return appRepository.getReviewsForMentor(mentor_id);
    }


    public void deleteAllCourseReviews(int course_id){
            appRepository.deleteAllCourseReviews(course_id);
    }


    public void deleteAllMentorReviews(int mentor_id){
        appRepository.deleteAllMentorReviews(mentor_id);
    }


    public void deleteReviewForCourse(int course_id, int review_id){
        appRepository.deleteReviewForCourse(course_id,review_id);
    }



    public void deleteReviewForMentor(int mentor_id, int review_id){
            appRepository.deleteReviewForMentor(mentor_id,review_id);
    }






    // StudentDao --------------------------------------------




    public void insertStudent (Student student){
            appRepository.insertStudent(student);
    }

     public void insertStudents(List<Student> students){
            appRepository.insertStudents(students);
    }

    public void updateStudent (Student student){
            appRepository.updateStudent(student);
    }

    public void deleteStudent (Student student){
        appRepository.deleteStudent(student);
    }

    public LiveData<List<Student>> getAllStudents (){
        return appRepository.getAllStudents();
    }

    public Student getStudentById (String student_id){
        return appRepository.getStudentById(student_id);
    }


    public List<Student> getUnsyncedStudents(){
        return appRepository.getUnsyncedStudents();
    }




    public LiveData<Student> getStudentByEmail (String email){
        return appRepository.getStudentByEmail(email);
    }







    // StudentLessonDao --------------------------------------------






    public void insertStudentLesson(StudentLesson studentLesson){
            appRepository.insertStudentLesson(studentLesson);
    }

    public void updateStudentLesson(StudentLesson studentLesson){
        appRepository.insertStudentLesson(studentLesson);
    }

    public void deleteStudentLesson(StudentLesson studentLesson){
            appRepository.deleteStudentLesson(studentLesson);
    }


    public void updateCompletionStatus(String student_id, int lesson_id, boolean completion_status){
            appRepository.updateCompletionStatus(student_id,lesson_id,completion_status);
    }


    boolean getCompletionStatus(String student_id, int lesson_id){
        return appRepository.getCompletionStatus(student_id,lesson_id);
    }


    public void updateLessonCompletionStatus(String student_id, int lesson_id, boolean completion_status){
            appRepository.updateLessonCompletionStatus(student_id,lesson_id,completion_status);
    }

    public void updateLessonStatusAndProgress(String student_id, int lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, int course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }


    public void markLessonAsCompleted(String studentId, int lessonId, int courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                appRepository.markLessonAsCompleted(studentId, lessonId, courseId);
                lessonCompletionStatus.postValue(true); // Notify success
            } catch (Exception e) {
                lessonCompletionStatus.postValue(false); // Notify failure
            }
        });
    }



    // StudentMentorDao --------------------------------------------






    public void insertStudentMentor(StudentMentor studentMentor){
            appRepository.insertStudentMentor(studentMentor);
    }

    public void updateStudentMentor(StudentMentor studentMentor){
        appRepository.updateStudentMentor(studentMentor);
    }

    public void deleteStudentMentor(StudentMentor studentMentor){
            appRepository.deleteStudentMentor(studentMentor);
    }

    public LiveData<List<String>> getAllMentorStudents(int mentor_id){
        return appRepository.getAllMentorStudents(mentor_id);
    }

    public LiveData<List<Integer>> getAllStudentMentors(String student_id){
        return appRepository.getAllStudentMentors(student_id);
    }






    // StudentModuleDao --------------------------------------------





    public void insertStudentModule(StudentModule studentModule){
            appRepository.insertStudentModule(studentModule);
    }

    public void updateStudentModule(StudentModule studentModule){
            appRepository.updateStudentModule(studentModule);
    }

    public void deleteStudentModule(StudentModule studentModule){
            appRepository.deleteStudentModule(studentModule);
    }

    public LiveData<List<Double>> getAllStudentModuleGrade(String student_id){
        return appRepository.getAllStudentModuleGrade(student_id);
    }

    public LiveData<List<Double>> getStudentModuleGrade(String student_id, int module_id){
        return appRepository.getStudentModuleGrade(student_id,module_id);
    }


    public void setModuleGrade(String student_id, int module_id, double module_grade){
            appRepository.setModuleGrade(student_id,module_id,module_grade);
    }





}
