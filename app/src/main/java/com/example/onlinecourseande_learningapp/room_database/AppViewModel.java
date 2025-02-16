package com.example.onlinecourseande_learningapp.room_database;

import static com.example.onlinecourseande_learningapp.ConversationActivity.STATUS_DELIVERED;
import static com.example.onlinecourseande_learningapp.ConversationActivity.STATUS_READ;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.onlinecourseande_learningapp.AppExecutors;
import com.example.onlinecourseande_learningapp.room_database.DAOs.EnrollmentDao;
import com.example.onlinecourseande_learningapp.room_database.entities.Ad;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Bookmark;
import com.example.onlinecourseande_learningapp.room_database.entities.Call;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.CourseWithProgress;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.MentorCourse;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AppViewModel extends AndroidViewModel {

    AppRepository appRepository;
    private static AppViewModel instance;
    private FirebaseFirestore fs;
    private final MutableLiveData<Boolean> lessonCompletionStatus = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        fs = FirebaseFirestore.getInstance();
    }

    public static AppViewModel getInstance(@NonNull Application application) {
        if (instance == null) {
            instance = new AppViewModel(application);
        }
        return instance;
    }

    // Method to get filtered mentors based on query
    public void getFilteredMentors(String query, final MentorFilterCallback callback) {
        // Observe LiveData to get the list of mentors
        appRepository.getAllMentors().observeForever(new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> allMentors) {
                List<Mentor> filteredMentors = new ArrayList<>();

                // Filter logic
                if (allMentors != null) {
                    for (Mentor mentor : allMentors) {
                        if (mentor.getMentor_fName().toLowerCase().contains(query.toLowerCase()) ||
                                mentor.getMentor_lName().toLowerCase().contains(query.toLowerCase())) {
                            filteredMentors.add(mentor);
                        }
                    }
                }

                // Callback to return the filtered mentors
                callback.onFilterComplete(filteredMentors);
            }
        });
    }

    public interface MentorFilterCallback {
        void onFilterComplete(List<Mentor> filteredMentors);
    }


    public LiveData<CourseGroupInfo> getCourseGroupInfo(String courseId) {
        MediatorLiveData<CourseGroupInfo> mediator = new MediatorLiveData<>();
        final CourseGroupInfo info = new CourseGroupInfo();

        // Observe Group
        mediator.addSource(getGroupByCourseIdLiveData(courseId), group -> {
            info.setGroup(group);
            mediator.setValue(info);
        });

        // Observe Mentor
        mediator.addSource(appRepository.getMentorByCourseId(courseId), mentor -> {
            info.setMentor(mentor);
            mediator.setValue(info);
        });

        // Observe Students
        mediator.addSource(getStudentsByCourseIdLive(courseId), students -> {
            info.setStudents(students);
            mediator.setValue(info);
        });

        return mediator;
    }



    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }


    public LiveData<List<ChatParticipant>> getChatParticipants(String studentId) {
        return appRepository.getChatParticipants(studentId);
    }

    public void getOrCreateIndividualChat(String senderId, String senderType, String receiverId, String receiverType, AppRepository.OnChatCreatedListener listener) {
        appRepository.getOrCreateIndividualChat(senderId, senderType, receiverId, receiverType, listener);
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




    public List<Attachment> getStudentAttachmentsInAChat(String student_id, String chat_id){
        return appRepository.getStudentAttachmentsInAChat(student_id,chat_id);
    }


    public LiveData<Attachment> getAttachmentById(String attachmentId){
        return appRepository.getAttachmentById(attachmentId);
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


    public LiveData<List<Bookmark>> getBookmarkById (String bookmark_id){
        return appRepository.getBookmarkById(bookmark_id);
    }


    public LiveData<List<Bookmark>> getAllStudentBookmarks (String student_id){
        return appRepository.getAllStudentBookmarks(student_id);
    }


    public LiveData<List<Course>> getBookmarkedCourses(String student_id){
        return appRepository.getBookmarkedCourses(student_id);
    }

    public Bookmark getBookmarkForCourseAndStudent(String courseId, String studentId){
        return appRepository.getBookmarkForCourseAndStudent(courseId,studentId);
    }

    public void removeBookmark(String courseId, String studentId){
        appRepository.removeBookmark(courseId,studentId);
    }


    public void isCourseBookmarked(String courseId, String studentId, AppRepository.Callback<Boolean> callback) {
        appRepository.isCourseBookmarked(courseId, studentId, callback);
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


    Call getCallById (String call_id){
        return appRepository.getCallById(call_id);
    }




    public LiveData<List<Call>> getAllStudentCalls (String student_id){
        return appRepository.getAllStudentCalls(student_id);
    }



    public LiveData<List<Call>> getAllStudentCallsForAChat (String student_id, String chat_id){
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


    public Chat getChatById(String chat_id){
        return appRepository.getChatById(chat_id);
    }



    public LiveData<List<Chat>> getAllStudentChats(String student_id){
        return appRepository.getAllStudentChats(student_id);
    }


    public LiveData<Chat>getChatByGroupId(String group_id){
        return appRepository.getChatByGroupId(group_id);
    }



    // CourseDao --------------------------------------------




    public void insertCourse (Course course){
            appRepository.insertCourse(course);
    }

    public void insertCourseList(List<Course> courseList){
            appRepository.insertCourseList(courseList);
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

    public LiveData<List<Course>> getAllCoursesDistinct() {
        return Transformations.distinctUntilChanged(appRepository.getAllCourses());
    }


    public LiveData<List<Course>> getCoursesByIds(List<String> courseIds){
        return appRepository.getCoursesByIds(courseIds);
    }

    public Course getCourseById (String course_id){
        return appRepository.getCourseById(course_id);
    }

    public LiveData<Course> getCourseByIdLiveData(String course_id){
        return appRepository.getCourseByIdLiveData(course_id);
    }

    public Course getCoursesByCategory (String category){
        return appRepository.getCoursesByCategory(category);
    }

    public LiveData<List<Course>> getCoursesForMentor(String mentorId) {
        // Fetch course IDs for the mentor
        LiveData<List<String>> courseIdsLiveData = appRepository.getAllMentorCourses(mentorId);

        // Map course IDs to course objects once the list is available
        return Transformations.switchMap(courseIdsLiveData, courseIds -> {
            if (courseIds != null && !courseIds.isEmpty()) {
                // Fetch the courses for the given course IDs
                return appRepository.getCoursesByIds(courseIds);
            }
            // If no course IDs are available, return an empty list
            return new MutableLiveData<>(new ArrayList<>());
        });
    }


    public LiveData<Integer> getTotalLessons(String courseId){
        return appRepository.getTotalLessons(courseId);
    }

    public LiveData<List<CourseWithProgress>> getCompletedCourses(String studentId){
        return appRepository.getCompletedCourses(studentId);
    }

    public LiveData<List<CourseWithProgress>> getOngoingCourses(String studentId){
        return appRepository.getOngoingCourses(studentId);
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

    public LiveData<Enrollment> getEnrollmentById(String enrollment_id){
        return appRepository.getEnrollmentById(enrollment_id);
    }


    public Enrollment getStudentEnrollmentInCourse(String student_id, String course_id){
        return appRepository.getStudentEnrollmentInCourse(student_id,course_id);
    }


    public int getTotalLessonsForCourse(String course_id){
        return appRepository.getTotalLessonsForCourse(course_id);
    }


    public int getCompletedLessonsForStudentInCourse(String student_id, String course_id){
        return appRepository.getCompletedLessonsForStudentInCourse(student_id,course_id);
    }



    public void updateEnrollmentProgress(String student_id, String course_id){
            appRepository.updateEnrollmentProgress(student_id,course_id);
    }


    public LiveData<List<Enrollment>> getEnrollmentsForStudent(String student_id){
        return appRepository.getEnrollmentsForStudent(student_id);
    }




    public void calculateFinalGrade(String student_id, String course_id){
            appRepository.calculateFinalGrade(student_id,course_id);
    }



    public void setCompletionDateIfAllLessonsCompleted(String student_id, String course_id){
        appRepository.setCompletionDateIfAllLessonsCompleted(student_id,course_id);
    }







    public void enrollInFreeCourse(String enrollment_id, String student_id, String course_id) {
        // Enroll the student in the course
        appRepository.enrollInFreeCourse(enrollment_id, student_id, course_id);

        // Retrieve the course object as LiveData
        LiveData<Course> courseLiveData = appRepository.getCourseByIdLiveData(course_id);

        // Use observeForever to get the Course details
        courseLiveData.observeForever(new Observer<Course>() {
            @Override
            public void onChanged(Course course) {
                if (course != null) {
                    // Create a notification for the student
                    Notification notification = new Notification();
                    notification.setNotification_id(UUID.randomUUID().toString());
                    notification.setStudent_id(student_id);
                    notification.setTitle("Enrollment Successful");
                    notification.setContent("You have successfully enrolled in the " + course.getCourse_name() + " course.");
                    notification.setType("enrollment");
                    notification.setTimestamp(new Date());
                    notification.setLast_updated(new Date());
                    notification.setIs_synced(false);

                    // Insert the notification into the database
                    appRepository.insertNotification(notification);

                    // Remove this observer to avoid memory leaks
                    courseLiveData.removeObserver(this);
                }
            }
        });
    }









    public LiveData<Enrollment> checkEnrollment(String student_id, String course_id) {
        return appRepository.checkEnrollment(student_id, course_id);
    }

    public LiveData<List<Enrollment>> checkEnrollmentInMentorCourses(String studentId, List<String> courseIds){
        return appRepository.checkEnrollmentInMentorCourses(studentId,courseIds);
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

    public LiveData<List<Group>> getGroupById(String group_id){
        return appRepository.getGroupById(group_id);
    }

    public LiveData<List<Group>> getGroupByGroupName(String group_name){
        return appRepository.getGroupByGroupName(group_name);
    }



    public Group getGroupByCourseId(String course_id){
        return appRepository.getGroupByCourseId(course_id);
    }

    public LiveData<Group> getGroupByCourseIdLiveData(String courseId) {
        return appRepository.getGroupByCourseIdLiveData(courseId);
    }

    public void createGroupForCourse(String courseId, String courseName) {
            if (appRepository.getGroupByCourseId(courseId) == null){
                Group group = new Group(courseName,courseId);
                appRepository.insertGroup(group);
            }
    }



    public Group getGroupByGroupId(String group_id){
        return appRepository.getGroupByGroupId(group_id);
    }


    public LiveData<List<Chat>> getAllChatsIncludingGroups(String userId) {
        return appRepository.getAllChatsIncludingGroups(userId);
    }

    public LiveData<Group> getGroupByIdLive(String group_id){
        return appRepository.getGroupByIdLive(group_id);
    }


    public LiveData<String>getCourseIdByGroupId(String group_id){
        return appRepository.getCourseIdByGroupId(group_id);
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


    public LiveData<List<GroupMembership>> getGroupMembershipById(String group_membership_id){
        return appRepository.getGroupMembershipById(group_membership_id);
    }




    public LiveData<GroupMembership> getGroupMembershipByGroupIdAndMemberId(String group_membership_id, String member_id){
        return appRepository.getGroupMembershipByGroupIdAndMemberId(group_membership_id,member_id);
    }



    public LiveData<List<GroupMembership>> getGroupMembershipsByGroupId(String group_id){
        return appRepository.getGroupMembershipsByGroupId(group_id);
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


    public LiveData<Lesson> getLessonById(String lessonId) {
        return appRepository.getLessonById(lessonId);
    }


    public LiveData<List<Lesson>> getAllLessonsByModuleId(String moduleId) {
        return appRepository.getAllLessonsByModuleId(moduleId);
    }


    public LiveData<List<Lesson>> getModuleExamByModuleId(String moduleId) {
        return appRepository.getModuleExamByModuleId(moduleId);
    }


    public void setLastLessonAsExam(String moduleId) {
        appRepository.setLastLessonAsExam(moduleId);
    }


    public void insertLessonAndUpdateModule(Lesson lesson) {
            appRepository.insertLessonAndUpdateModule(lesson);
    }


    public void deleteLessonAndUpdateModule(Lesson lesson) {
            appRepository.deleteLessonAndUpdateModule(lesson);
    }



    public void finishExam(String studentId, String lessonId, String moduleId, String courseId, double grade) {
            // Set module grade
            appRepository.setModuleGrade(studentId, moduleId, grade);

            // Recalculate final grade
            appRepository.calculateFinalGrade(studentId, courseId);

            // Check and set completion date if all lessons are done
            appRepository.setCompletionDateIfAllLessonsCompleted(studentId, courseId);
    }


    public void unlockNextLesson(String studentId, String lessonId){
            appRepository.unlockNextLesson(studentId,lessonId);
    }

    public LiveData<List<Lesson>> getLessonsByModule(String moduleId){
        return appRepository.getLessonsByModule(moduleId);
    }

    public LiveData<Boolean> isLessonAlreadyInserted(String studentId, String lessonId){
        return appRepository.isLessonAlreadyInserted(studentId,lessonId);
    }

    // MentorCourseDao --------------------------------------------




    public LiveData<List<String>> getAllCourseMentors(String course_id){
        return appRepository.getAllCourseMentors(course_id);
    }

    public LiveData<List<String>> getAllMentorCourses(String mentor_id){
        return appRepository.getAllMentorCourses(mentor_id);
    }






    // MentorDao --------------------------------------------






    public void insertMentor (Mentor mentor){
            appRepository.insertMentor(mentor);
    }

    public void insertMentorList(List<Mentor> mentorList){
            appRepository.insertMentorList(mentorList);
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

    public Mentor getMentorById(String mentor_id){
        return appRepository.getMentorById(mentor_id);
    }

    public LiveData<Mentor> getMentorByIdLive(String mentor_id){
        return appRepository.getMentorByIdLive(mentor_id);
    }


    public List<Mentor> getUnsyncedMentors(){
        return appRepository.getUnsyncedMentors();
    }





    //MentorCourseDao

    public void insertMentorCourse(MentorCourse mentorCourse){
            appRepository.insertMentorCourse(mentorCourse);
    }

    public void updateMentorCourse(MentorCourse mentorCourse){
            appRepository.updateMentorCourse(mentorCourse);
    }

    public void deleteMentorCourse(MentorCourse mentorCourse){
            appRepository.deleteMentorCourse(mentorCourse);
    }


    public LiveData<Mentor> getMentorsByCourseId(String courseId) {
        return appRepository.getMentorsByCourseId(courseId);
    }


    public LiveData<Integer> getMentorCourseCount(String mentorId){
        return appRepository.getMentorCourseCount(mentorId);
    }

    public LiveData<List<String>> getCoursesForAMentor(String mentorId) {
        MutableLiveData<List<String>> courseIds = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<String> ids = appRepository.getCoursesForAMentor(mentorId);
            courseIds.postValue(ids);
        });
        return courseIds;
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

    public LiveData<List<Message>> getMessageById(String message_id){
        return appRepository.getMessageById(message_id);
    }

    public LiveData<List<Message>> getGroupMessagesByGroupId(String group_id){
        return appRepository.getGroupMessagesByGroupId(group_id);
    }

    public LiveData<List<Message>> getChatMessagesByChatId(String chat_id){
        return appRepository.getChatMessagesByChatId(chat_id);
    }

    public LiveData<List<Message>> getChatMessagesByMessageType(String message_type){
        return appRepository.getChatMessagesByMessageType(message_type);
    }


    public LiveData<List<Message>> searchMessagesByContent(String content){
        return appRepository.searchMessagesByContent(content);
    }


    public LiveData<Message> getLastMessageForChat(String chat_id){
        return appRepository.getLastMessageForChat(chat_id);
    }

    public LiveData<Message> getLastMessageForGroup(String groupId){
        return appRepository.getLastMessageForGroup(groupId);
    }


    public LiveData<List<Message>> getUnreadMessages(String chatId, String currentUserId) {
        return appRepository.getUnreadMessages(chatId, currentUserId);
    }

    public void markMessagesAsRead(String chatId, String currentUserId) {
        appRepository.getUnreadMessages(chatId, currentUserId).observeForever(unreadMessages -> {
            if (unreadMessages == null || unreadMessages.isEmpty()) return;

            Date now = new Date();
            for (Message message : unreadMessages) {
                message.setReadAt(now);
                message.setStatus(STATUS_READ);
                message.setIs_synced(false);
                appRepository.updateMessage(message);

                // Update Firebase
                FirebaseFirestore.getInstance().collection("Message")
                        .document(message.getMessage_id())
                        .update("status", STATUS_READ, "readAt", Converter.toFirestoreTimestamp(now));
            }
        });
    }




    public void markMessagesAsDelivered(String chatId, String currentUserId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Message> sentMessages = appRepository.getSentMessagesForChat(chatId, currentUserId);
            Date now = new Date();
            for (Message message : sentMessages) {
                message.setStatus(STATUS_DELIVERED);
                message.setDeliveredAt(now);
                message.setIs_synced(false);
                appRepository.updateMessage(message);

                // Update Firebase
                FirebaseFirestore.getInstance().collection("Message")
                        .document(message.getMessage_id())
                        .update("status", STATUS_DELIVERED, "deliveredAt", Converter.toFirestoreTimestamp(now));
            }
        });
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


    public LiveData<List<Module>> getModuleById (String module_id){
        return appRepository.getModuleById(module_id);
    }


    public LiveData<List<Module>> getAllCourseModules (String course_id){
        return appRepository.getAllCourseModules(course_id);
    }


    int getModuleDuration(String module_id){
        return appRepository.getModuleDuration(module_id);
    }


    public void updateModuleDuration(String module_id){
            appRepository.updateModuleDuration(module_id);
    }



    public LiveData<List<ModuleWithLessons>> getModulesWithLessons(String courseId){
        return appRepository.getModulesWithLessons(courseId);
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


    public LiveData<List<Notification>> getNotificationById(String notification_id){
        return appRepository.getNotificationById(notification_id);
    }



    public LiveData<List<Notification>> getAllStudentNotifications(String student_id){
        return appRepository.getAllStudentNotifications(student_id);
    }

    public LiveData<List<Student>> getStudentsByCourseIdLive(String courseId) {
        return appRepository.getStudentsByCourseIdLive(courseId);
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

    public void insertMentorReview(String student_id, String mentor_id, double rate, String comment) {
            Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
            insertReview(review);
    }


    public void insertCourseReview(String student_id, String course_id, double rate, String comment) {
            Review review = new Review(student_id, course_id, "Course", rate, comment);
            insertReview(review);
    }

    public LiveData<List<Review>> getAllReviews(){
        return appRepository.getAllReviews();
    }

    public LiveData<List<Review>> getReviewById(String review_id){
        return appRepository.getReviewById(review_id);
    }


    public LiveData<List<Review>> getReviewsForCourse(String course_id){
        return appRepository.getReviewsForCourse(course_id);
    }


    public LiveData<List<Review>> getReviewsForMentor(String mentor_id){
        return appRepository.getReviewsForMentor(mentor_id);
    }


    public void deleteAllCourseReviews(String course_id){
            appRepository.deleteAllCourseReviews(course_id);
    }


    public void deleteAllMentorReviews(String mentor_id){
        appRepository.deleteAllMentorReviews(mentor_id);
    }


    public void deleteReviewForCourse(String course_id, String review_id){
        appRepository.deleteReviewForCourse(course_id,review_id);
    }



    public void deleteReviewForMentor(String mentor_id, String review_id){
            appRepository.deleteReviewForMentor(mentor_id,review_id);
    }



    public LiveData<Integer> getReviewCountForCourse(String courseId) {
        return appRepository.getReviewCountForCourse(courseId);
    }


    public LiveData<Integer> getReviewCountForMentor(String mentorId){
        return appRepository.getReviewCountForMentor(mentorId);
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

        Notification notification = new Notification();
        notification.setNotification_id(UUID.randomUUID().toString());
        notification.setStudent_id(student.getId());
        notification.setTitle("Account Updated");
        notification.setContent("Your account information was updated successfully.");
        notification.setType("profile_update");
        notification.setTimestamp(new Date());
        notification.setLast_updated(new Date());
        notification.setIs_synced(false);


        appRepository.insertNotification(notification);

    }

    public void updateStudentPassword(String email, String newPassword){
            appRepository.updateStudentPassword(email,newPassword);
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

    public LiveData<Student> getStudentByIdLive (String student_id){
        return appRepository.getStudentByIdLive(student_id);
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


    public void updateCompletionStatus(String student_id, String lesson_id, boolean completion_status){
            appRepository.updateCompletionStatus(student_id,lesson_id,completion_status);
    }


    public LiveData<Boolean> getCompletionStatus(String student_id, String lesson_id){
        return appRepository.getCompletionStatus(student_id,lesson_id);
    }


    public void updateLessonCompletionStatus(String student_id, String lesson_id, boolean completion_status){
            appRepository.updateLessonCompletionStatus(student_id,lesson_id,completion_status);
    }

    public void updateLessonStatusAndProgress(String student_id, String lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, String course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }


    public void markLessonAsCompleted(String studentId, String lessonId, String courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                appRepository.markLessonAsCompleted(studentId, lessonId, courseId);
                lessonCompletionStatus.postValue(true); // Notify success
            } catch (Exception e) {
                lessonCompletionStatus.postValue(false); // Notify failure
            }
        });
    }


    public LiveData<Integer> getCompletedLessonsCount(String studentId){
        return appRepository.getCompletedLessonsCount(studentId);
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

    public LiveData<List<String>> getAllMentorStudents(String mentor_id){
        return appRepository.getAllMentorStudents(mentor_id);
    }

    public LiveData<List<String>> getAllStudentMentors(String student_id){
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

    public LiveData<List<Double>> getStudentModuleGrade(String student_id, String module_id){
        return appRepository.getStudentModuleGrade(student_id,module_id);
    }


    public void setModuleGrade(String student_id, String module_id, double module_grade){
            appRepository.setModuleGrade(student_id,module_id,module_grade);
    }


//AdDao

    public void insertAd(Ad ad){
            appRepository.insertAd(ad);
    }

    public void insertAdList(List<Ad> adList){
            appRepository.insertAdList(adList);
    }

    public void updateAd(Ad ad){
            appRepository.updateAd(ad);
    }

    public void deleteAd(Ad ad){
            appRepository.deleteAd(ad);
    }


    public LiveData<List<Ad>> getAllAds(){
        return appRepository.getAllAds();
    }


}
