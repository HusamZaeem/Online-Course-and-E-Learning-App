package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.onlinecourseande_learningapp.AppExecutors;
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
import com.example.onlinecourseande_learningapp.room_database.entities.CourseWithProgress;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Lesson;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.MentorCourse;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.example.onlinecourseande_learningapp.room_database.entities.MessageWithAttachments;
import com.example.onlinecourseande_learningapp.room_database.entities.Module;
import com.example.onlinecourseande_learningapp.room_database.entities.ModuleWithLessons;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public AdDao adDao;


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
        adDao = db.adDao();
    }

    public static synchronized AppRepository getInstance(Application application) {
        if (instance == null) {
            instance = new AppRepository(application);
        }
        return instance;
    }





    /**
     * Returns LiveData containing a list of ChatParticipant objects for all students (and mentors)
     * enrolled in courses where the specified student is enrolled.
     */
    public LiveData<List<ChatParticipant>> getChatParticipants(final String studentId) {
        final MutableLiveData<List<ChatParticipant>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ChatParticipant> participants = new ArrayList<>();
            Set<String> processedKeys = new HashSet<>();

            // Get courses that the student is enrolled in.
            List<Enrollment> myEnrollments = enrollmentDao.getEnrollmentsForStudentList(studentId);
            Set<String> courseIds = new HashSet<>();
            for (Enrollment enrollment : myEnrollments) {
                courseIds.add(enrollment.getCourse_id());
            }

            // For each course, get all other enrolled students.
            for (String courseId : courseIds) {
                List<Enrollment> courseEnrollments = enrollmentDao.getEnrollmentsForCourse(courseId);
                for (Enrollment enrollment : courseEnrollments) {
                    if (enrollment.getStudent_id().equals(studentId)) continue;
                    Student student = studentDao.getStudentById(enrollment.getStudent_id());
                    if (student != null) {
                        String key = student.getStudent_id() + "_Student";
                        if (!processedKeys.contains(key)) {
                            processedKeys.add(key);
                            String fullName = student.getFirst_name() + " " + student.getLast_name();
                            ChatParticipant cp = new ChatParticipant(
                                    student.getStudent_id(),
                                    fullName,
                                    "Student",
                                    student.getProfile_photo()
                            );
                            participants.add(cp);
                        }
                    }
                }
                // For each course, get the mentors.
                List<String> mentorIds = mentorCourseDao.getAllCourseMentorsList(courseId);
                for (String mentorId : mentorIds) {
                    Mentor mentor = mentorDao.getMentorById(mentorId);
                    if (mentor != null) {
                        String key = mentor.getMentor_id() + "_Mentor";
                        if (!processedKeys.contains(key)) {
                            processedKeys.add(key);
                            String fullName = mentor.getMentor_fName() + " " + mentor.getMentor_lName();
                            ChatParticipant cp = new ChatParticipant(
                                    mentor.getMentor_id(),
                                    fullName,
                                    "Mentor",
                                    mentor.getMentor_photo()
                            );
                            participants.add(cp);
                        }
                    }
                }
            }
            liveData.postValue(participants);
        });
        return liveData;
    }


    public void getOrCreateIndividualChat(String senderId, String senderType, String receiverId, String receiverType, OnChatCreatedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Chat existingChat = chatDao.findChatBetweenUsers(senderId, receiverId);
            if (existingChat == null) {
                // Create new chat
                Chat newChat = new Chat();
                newChat.setChat_id(UUID.randomUUID().toString());
                newChat.setSender_id(senderId);
                newChat.setSender_type(senderType);
                newChat.setReceiver_id(receiverId);
                newChat.setReceiver_type(receiverType);
                newChat.setTimestamp(new Date());
                newChat.setIs_synced(false);
                newChat.setLast_updated(new Date());
                chatDao.insertChat(newChat);
                listener.onChatCreated(newChat);
            } else {
                listener.onChatCreated(existingChat);
            }
        });
    }

    public interface OnChatCreatedListener {
        void onChatCreated(Chat chat);
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




    public List<Attachment> getStudentAttachmentsInAChat(String student_id, String chat_id){
        return attachmentDao.getStudentAttachmentsInAChat(student_id,chat_id);
    }



    public Attachment getAttachmentByAttachmentId(String attachment_id){
        return attachmentDao.getAttachmentByAttachmentId(attachment_id);
    }

    public LiveData<Attachment> getAttachmentById(String attachmentId){
        return attachmentDao.getAttachmentById(attachmentId);
    }


    public LiveData<Attachment> getAttachmentByMessageId(String message_id){
        return attachmentDao.getAttachmentByMessageId(message_id);
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


    public LiveData<List<Bookmark>> getBookmarkById (String bookmark_id){
        return bookmarkDao.getBookmarkById(bookmark_id);
    }


    public LiveData<List<Bookmark>> getAllStudentBookmarks (String student_id){
        return bookmarkDao.getAllStudentBookmarks(student_id);
    }



    public Bookmark getBookmarkByBookmarkId(String bookmark_id){
        return bookmarkDao.getBookmarkByBookmarkId(bookmark_id);
    }

    public LiveData<List<Course>> getBookmarkedCourses(String student_id){
        return bookmarkDao.getBookmarkedCourses(student_id);
    }

    public Bookmark getBookmarkForCourseAndStudent(String courseId, String studentId){
        return bookmarkDao.getBookmarkForCourseAndStudent(courseId,studentId);
    }

    public void removeBookmark(String courseId, String studentId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Bookmark bookmark = bookmarkDao.getBookmarkForCourseAndStudent(courseId, studentId);
            if (bookmark != null) {
                bookmarkDao.deleteBookmark(bookmark);
            }
        });
    }

    public void isCourseBookmarked(String courseId, String studentId, AppRepository.Callback<Boolean> callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean isBookmarked = bookmarkDao.isCourseBookmarked(courseId, studentId);
            // Post the result to the callback on the main thread
            new Handler(Looper.getMainLooper()).post(() -> callback.onResult(isBookmarked));
        });
    }

    public interface Callback<T> {
        void onResult(T result);
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


    public Call getCallById (String call_id){
        return callDao.getCallById(call_id);
    }




    public LiveData<List<Call>> getAllStudentCalls (String student_id){
        return callDao.getAllStudentCalls(student_id);
    }



    public LiveData<List<Call>> getAllStudentCallsForAChat (String student_id, String chat_id){
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


    public Chat getChatById(String chat_id){
        return chatDao.getChatById(chat_id);
    }



    public LiveData<List<Chat>> getAllStudentChats(String student_id){
        return chatDao.getAllStudentChats(student_id);
    }



    public LiveData<List<Chat>> getAllChatsIncludingGroups(String userId) {
        MutableLiveData<List<Chat>> liveData = new MutableLiveData<>();

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Chat> personalChats = chatDao.getAllChatsList().stream()
                    // Add null checks for sender_id and receiver_id
                    .filter(chat -> (chat.getSender_id() != null && chat.getSender_id().equals(userId)) ||
                            (chat.getReceiver_id() != null && chat.getReceiver_id().equals(userId)))
                    .collect(Collectors.toList());

            List<Group> groups = groupDao.getAllGroupsList();
            for (Group group : groups) {
                List<String> participants = getGroupParticipants(group.getGroup_id()); // Get members
                if (participants.contains(userId)) {
                    Chat groupChat = new Chat();
                    groupChat.setChat_id(group.getGroup_id());
                    groupChat.setIs_group_chat(true);
                    groupChat.setGroup_id(group.getGroup_id());
                    groupChat.setTimestamp(group.getLast_updated());
                    personalChats.add(groupChat);
                }
            }

            liveData.postValue(personalChats);
        });

        return liveData;
    }



    public List<String> getGroupParticipants(String groupId) {
        return groupDao.getGroupParticipants(groupId);
    }


    public LiveData<Chat>getChatByGroupId(String group_id){
        return chatDao.getChatByGroupId(group_id);
    }


    // CourseDao --------------------------------------------




    public void insertCourse (Course course){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insertCourse(course);
        });
    }

    public void insertCourseList(List<Course> courseList){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insertCourseList(courseList);
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

    public Course getCourseById (String course_id){
        return courseDao.getCourseById(course_id);
    }

    public LiveData<List<Course>> getCoursesByIds(List<String> courseIds){
        return courseDao.getCoursesByIds(courseIds);
    }

    public LiveData<Course> getCourseByIdLiveData(String course_id){
        return courseDao.getCourseByIdLiveData(course_id);
    }


    public Course getCoursesByCategory (String category){
        return courseDao.getCoursesByCategory(category);
    }

    public List<Course> getAllCoursesList (){
        return courseDao.getAllCoursesList();
    }


    public List<Course> getUnsyncedCourses(){
        return courseDao.getUnsyncedCourses();
    }


    public LiveData<List<CourseWithProgress>> getOngoingCourses(String studentId) {
        MutableLiveData<List<CourseWithProgress>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CourseWithProgress> ongoingCourses = new ArrayList<>();
            // Fetch enrollments for the student (synchronously)
            List<Enrollment> enrollments = enrollmentDao.getEnrollmentsForStudentList(studentId);
            if (enrollments != null) {
                for (Enrollment enrollment : enrollments) {
                    // Use the synchronous DAO method to get modules with lessons for this course
                    List<ModuleWithLessons> modules = moduleDao.getModulesWithLessonsSync(enrollment.getCourse_id());
                    if (modules != null) {
                        int totalLessons = modules.stream()
                                .mapToInt(m -> m.getLessons() != null ? m.getLessons().size() : 0)
                                .sum();
                        List<String> lessonIds = new ArrayList<>();
                        for (ModuleWithLessons m : modules) {
                            if (m.getLessons() != null) {
                                for (Lesson lesson : m.getLessons()) {
                                    lessonIds.add(lesson.getLesson_id());
                                }
                            }
                        }
                        // Count completed lessons using the list of lesson IDs
                        int completedLessons = studentLessonDao.countCompletedLessons(studentId, lessonIds);
                        // Only add if the course is still ongoing (not 100% complete)
                        if (totalLessons > 0 && completedLessons < totalLessons) {
                            Course course = courseDao.getCourseByIdSync(enrollment.getCourse_id());
                            ongoingCourses.add(new CourseWithProgress(course, enrollment, completedLessons, totalLessons));
                        }
                    }
                }
            }
            liveData.postValue(ongoingCourses);
        });
        return liveData;
    }

    public LiveData<List<CourseWithProgress>> getCompletedCourses(String studentId) {
        MutableLiveData<List<CourseWithProgress>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<CourseWithProgress> completedCourses = new ArrayList<>();
            List<Enrollment> enrollments = enrollmentDao.getCompletedEnrollmentsList(studentId);
            Log.d("CompletedCourses", "Found " + (enrollments != null ? enrollments.size() : 0) + " completed enrollments");
            if (enrollments != null) {
                for (Enrollment enrollment : enrollments) {
                    List<ModuleWithLessons> modules = moduleDao.getModulesWithLessonsSync(enrollment.getCourse_id());
                    if (modules != null) {
                        int totalLessons = modules.stream()
                                .mapToInt(m -> m.getLessons() != null ? m.getLessons().size() : 0)
                                .sum();
                        List<String> lessonIds = new ArrayList<>();
                        for (ModuleWithLessons m : modules) {
                            if (m.getLessons() != null) {
                                for (Lesson lesson : m.getLessons()) {
                                    lessonIds.add(lesson.getLesson_id());
                                }
                            }
                        }
                        int completedLessons = studentLessonDao.countCompletedLessons(studentId, lessonIds);
                        // Only add if the course is 100% complete
                        if (totalLessons > 0 && completedLessons >= totalLessons) {
                            Course course = courseDao.getCourseByIdSync(enrollment.getCourse_id());
                            if (course != null) {
                                Log.d("CompletedCourses", "Loaded course: " + course.getCourse_name());
                            } else {
                                Log.d("CompletedCourses", "Course not found for ID: " + enrollment.getCourse_id());
                            }

                            Log.d("CompletedCourses", "Course ID: " + enrollment.getCourse_id() +
                                    " | Total Lessons: " + totalLessons +
                                    " | Completed Lessons: " + completedLessons);

                            completedCourses.add(new CourseWithProgress(course, enrollment, completedLessons, totalLessons));
                        }
                    }
                }
            }
            liveData.postValue(completedCourses);
        });
        return liveData;
    }






    public LiveData<Integer> getTotalLessons(String courseId){
        return courseDao.getTotalLessons(courseId);
    }


    // EnrollmentDao --------------------------------------------



    public void updateEnrollmentProgress(String studentId, String courseId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Get all modules and their lessons for the course synchronously
            List<ModuleWithLessons> modules = moduleDao.getModulesWithLessonsSync(courseId);
            if (modules != null) {
                int totalLessons = modules.stream()
                        .mapToInt(m -> m.getLessons() != null ? m.getLessons().size() : 0)
                        .sum();
                List<String> lessonIds = new ArrayList<>();
                for (ModuleWithLessons m : modules) {
                    if (m.getLessons() != null) {
                        for (Lesson lesson : m.getLessons()) {
                            lessonIds.add(lesson.getLesson_id());
                        }
                    }
                }
                int completedLessons = studentLessonDao.countCompletedLessons(studentId, lessonIds);
                if (totalLessons > 0) {
                    int progressPercentage = (int) ((completedLessons / (float) totalLessons) * 100);
                    enrollmentDao.updateProgress(studentId, courseId, progressPercentage);
                }
            }
        });
    }






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

    public LiveData<Enrollment> getEnrollmentById(String enrollment_id){
        return enrollmentDao.getEnrollmentById(enrollment_id);
    }


    public Enrollment getStudentEnrollmentInCourse(String student_id, String course_id){
        return enrollmentDao.getStudentEnrollmentInCourse(student_id,course_id);
    }


    public int getTotalLessonsForCourse(String course_id){
        return enrollmentDao.getTotalLessonsForCourse(course_id);
    }


    public int getCompletedLessonsForStudentInCourse(String student_id, String course_id){
        return enrollmentDao.getCompletedLessonsForStudentInCourse(student_id,course_id);
    }






    public LiveData<List<Enrollment>> getEnrollmentsForStudent(String student_id){
        return enrollmentDao.getEnrollmentsForStudent(student_id);
    }




    public void calculateFinalGrade(String student_id, String course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.calculateFinalGrade(student_id,course_id);
        });
    }



    public void setCompletionDateIfAllLessonsCompleted(String student_id, String course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            enrollmentDao.setCompletionDateIfAllLessonsCompleted(student_id,course_id);
        });
    }








    // Enroll in a free course
    public void enrollInFreeCourse(String enrollment_id, String student_id, String course_id) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            enrollmentDao.enrollInFreeCourse(enrollment_id, student_id, course_id);
        });
    }

    public LiveData<Enrollment> checkEnrollment(String student_id, String course_id) {
        return enrollmentDao.checkEnrollment(student_id, course_id);
    }


    public Enrollment getEnrollmentByEnrollmentId(String enrollment_id){
        return enrollmentDao.getEnrollmentByEnrollmentId(enrollment_id);
    }


    public LiveData<List<Enrollment>> checkEnrollmentInMentorCourses(String studentId, List<String> courseIds){
        return enrollmentDao.checkEnrollmentInMentorCourses(studentId,courseIds);
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

    public LiveData<List<Group>> getGroupById(String group_id){
        return groupDao.getGroupById(group_id);
    }

    public LiveData<List<Group>> getGroupByGroupName(String group_name){
        return groupDao.getGroupByGroupName(group_name);
    }



    public Group getGroupByCourseId(String courseId) {
        return groupDao.getGroupByCourseId(courseId);
    }


    public LiveData<Group> getGroupByCourseIdLiveData(String courseId) {
        return groupDao.getGroupByCourseIdLiveData(courseId);
    }



    public void createGroupForCourse(String courseId, String courseName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (groupDao.getGroupByCourseId(courseId) == null){
                Group group = new Group(courseName,courseId);
                groupDao.insertGroup(group);
            }

        });
    }




    public Group getGroupByGroupId(String group_id){

        return groupDao.getGroupByGroupId(group_id);

    }

    public LiveData<Group> getGroupByIdLive(String group_id){
        return groupDao.getGroupByIdLive(group_id);
    }


    public LiveData<String>getCourseIdByGroupId(String group_id){
        return groupDao.getCourseIdByGroupId(group_id);
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


    public LiveData<List<GroupMembership>> getGroupMembershipById(String group_membership_id){
        return groupMembershipDao.getGroupMembershipById(group_membership_id);
    }






    public GroupMembership getGroupMembershipByGroupMembershipId(String group_membership_id){
        return groupMembershipDao.getGroupMembershipByGroupMembershipId(group_membership_id);
    }



    public LiveData<GroupMembership> getGroupMembershipByGroupIdAndMemberId(String group_membership_id, String member_id){
        return groupMembershipDao.getGroupMembershipByGroupIdAndMemberId(group_membership_id,member_id);
    }


    public LiveData<List<GroupMembership>> getGroupMembershipsByGroupId(String group_id){
        return groupMembershipDao.getGroupMembershipsByGroupId(group_id);
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


    public LiveData<Lesson> getLessonById(String lessonId) {
        return lessonDao.getLessonById(lessonId);
    }


    public LiveData<List<Lesson>> getAllLessonsByModuleId(String moduleId) {
        return lessonDao.getAllLessonsByModuleId(moduleId);
    }


    public LiveData<List<Lesson>> getModuleExamByModuleId(String moduleId) {
        return lessonDao.getModuleExamByModuleId(moduleId);
    }


    public void setLastLessonAsExam(String moduleId) {
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



    public void finishExam(String studentId, String lessonId, String moduleId, String courseId, double grade) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Set module grade
            studentModuleDao.setModuleGrade(studentId, moduleId, grade);

            // Recalculate final grade
            enrollmentDao.calculateFinalGrade(studentId, courseId);

            // Check and set completion date if all lessons are done
            enrollmentDao.setCompletionDateIfAllLessonsCompleted(studentId, courseId);
        });
    }


    public void unlockNextLesson(String studentId, String lessonId){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.unlockNextLesson(studentId,lessonId);
        });
    }

    public Lesson getLessonByLessonId(String lesson_id){
        return lessonDao.getLessonByLessonId(lesson_id);
    }


    public LiveData<List<Lesson>> getLessonsByModule(String moduleId){
        return lessonDao.getLessonsByModule(moduleId);
    }


    public LiveData<Boolean> isLessonAlreadyInserted(String studentId, String lessonId){
        return studentLessonDao.isLessonAlreadyInserted(studentId,lessonId);
    }

    // MentorCourseDao --------------------------------------------


    public void insertMentorCourse(MentorCourse mentorCourse){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorCourseDao.insertMentorCourse(mentorCourse);
        });
    }

    public void updateMentorCourse(MentorCourse mentorCourse){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorCourseDao.updateMentorCourse(mentorCourse);
        });
    }

    public void deleteMentorCourse(MentorCourse mentorCourse){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorCourseDao.deleteMentorCourse(mentorCourse);
        });
    }

    public LiveData<List<String>> getAllCourseMentors(String course_id){
        return mentorCourseDao.getAllCourseMentors(course_id);
    }

    public LiveData<List<String>> getAllMentorCourses(String mentor_id){
        return mentorCourseDao.getAllMentorCourses(mentor_id);
    }



    public MentorCourse getMentorCourseByMentorCourseId(String mentor_course_id){
        return mentorCourseDao.getMentorCourseByMentorCourseId(mentor_course_id);
    }

    public LiveData<Mentor> getMentorsByCourseId(String courseId) {
        return mentorCourseDao.getMentorsByCourseId(courseId);
    }

    public LiveData<Integer> getMentorCourseCount(String mentorId){
        return mentorCourseDao.getMentorCourseCount(mentorId);
    }


    public List<String> getCoursesForAMentor(String mentorId){
        return mentorCourseDao.getCoursesForAMentor(mentorId);
    }

    // MentorDao --------------------------------------------






    public void insertMentor (Mentor mentor){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.insertMentor(mentor);
        });
    }

    public void insertMentorList(List<Mentor> mentorList){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mentorDao.insertMentorList(mentorList);
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

    public Mentor getMentorById(String mentor_id){
        return mentorDao.getMentorById(mentor_id);
    }

    public LiveData<Mentor> getMentorByIdLive(String mentor_id){
        return mentorDao.getMentorByIdLive(mentor_id);
    }


    public List<Mentor> getUnsyncedMentors(){
        return mentorDao.getUnsyncedMentors();
    }





    public LiveData<Mentor> getMentorByCourseId(String courseId) {
        MutableLiveData<Mentor> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Get the mentor_id for the course
            String mentorId = mentorCourseDao.getMentorIdByCourseId(courseId);
            // Fetch the mentor using the mentor_id
            Mentor mentor = mentorDao.getMentorById(mentorId);
            liveData.postValue(mentor);
        });
        return liveData;
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

    public LiveData<List<Message>> getMessageById(String message_id){
        return messageDao.getMessageById(message_id);
    }

    public LiveData<List<Message>> getGroupMessagesByGroupId(String group_id){
        return messageDao.getGroupMessagesByGroupId(group_id);
    }

    public LiveData<List<Message>> getChatMessagesByChatId(String chat_id){
        return messageDao.getChatMessagesByChatId(chat_id);
    }

    public LiveData<List<Message>> getChatMessagesByMessageType(String message_type){
        return messageDao.getChatMessagesByMessageType(message_type);
    }


    public LiveData<List<Message>> searchMessagesByContent(String content){
        return messageDao.searchMessagesByContent(content);
    }



    public Message getMessageByMessageId(String message_id){
        return messageDao.getMessageByMessageId(message_id);
    }


    public LiveData<Message> getLastMessageForChat(String chat_id){
        return messageDao.getLastMessageForChat(chat_id);
    }


    public LiveData<Message> getLastMessageForGroup(String groupId){
        return messageDao.getLastMessageForGroup(groupId);
    }


    public LiveData<List<Message>> getUnreadMessages(String chatId, String currentUserId) {
        return messageDao.getUnreadMessages(chatId, currentUserId);
    }

    public List<Message> getSentMessagesForChat(String chatId, String currentUserId) {
        return messageDao.getSentMessagesForChat(chatId, currentUserId);
    }


    public LiveData<List<MessageWithAttachments>> getMessagesWithAttachments(String chatId){
        return messageDao.getMessagesWithAttachments(chatId);
    }


    public void updateMessageStatus(String messageId, int newStatus, Date lastUpdated){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateMessageStatus(messageId,newStatus,lastUpdated);
        });
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


    public LiveData<List<Module>> getModuleById (String module_id){
        return moduleDao.getModuleById(module_id);
    }


    public LiveData<List<Module>> getAllCourseModules (String course_id){
        return moduleDao.getAllCourseModules(course_id);
    }


    public int getModuleDuration(String module_id){
        return moduleDao.getModuleDuration(module_id);
    }


    public void updateModuleDuration(String module_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            moduleDao.updateModuleDuration(module_id);
        });
    }



    public Module getModuleByModuleId(String module_id){
        return moduleDao.getModuleByModuleId(module_id);
    }


    public LiveData<List<ModuleWithLessons>> getModulesWithLessons(String courseId){
        return moduleDao.getModulesWithLessons(courseId);
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


    public LiveData<List<Notification>> getNotificationById(String notification_id){
        return notificationDao.getNotificationById(notification_id);
    }



    public LiveData<List<Notification>> getAllStudentNotifications(String student_id){
        return notificationDao.getAllStudentNotifications(student_id);
    }


    public Notification getNotificationByNotificationId(String notification_id){
        return notificationDao.getNotificationByNotificationId(notification_id);
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

    public void insertMentorReview(String student_id, String mentor_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, mentor_id, "Mentor", rate, comment);
            insertReview(review);
        });
    }


    public void insertCourseReview(String student_id, String course_id, double rate, String comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Review review = new Review(student_id, course_id, "Course", rate, comment);
            insertReview(review);
        });
    }

    public LiveData<List<Review>> getAllReviews(){
        return reviewDao.getAllReviews();
    }

    public LiveData<List<Review>> getReviewById(String review_id){
        return reviewDao.getReviewById(review_id);
    }


    public LiveData<List<Review>> getReviewsForCourse(String course_id){
        return reviewDao.getReviewsForCourse(course_id);
    }


    public LiveData<List<Review>> getReviewsForMentor(String mentor_id){
        return reviewDao.getReviewsForMentor(mentor_id);
    }


    public void deleteAllCourseReviews(String course_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllCourseReviews(course_id);
        });
    }


    public void deleteAllMentorReviews(String mentor_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteAllMentorReviews(mentor_id);
        });
    }


    public void deleteReviewForCourse(String course_id, String review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForCourse(course_id,review_id);
        });
    }



    public void deleteReviewForMentor(String mentor_id, String review_id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reviewDao.deleteReviewForMentor(mentor_id,review_id);
        });
    }



    public Review getReviewByReviewId(String review_id){
        return reviewDao.getReviewByReviewId(review_id);
    }

    public LiveData<Integer> getReviewCountForCourse(String courseId) {
        return reviewDao.getReviewCountForCourse(courseId);
    }


    public LiveData<Integer> getReviewCountForMentor(String mentorId){
        return reviewDao.getReviewCountForMentor(mentorId);
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

    public void updateStudentPassword(String email, String newPassword){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentDao.updateStudentPassword(email,newPassword);
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

    public Student getStudentById (String student_id){
        return studentDao.getStudentById(student_id);
    }

    public LiveData<Student> getStudentByIdLive (String student_id){
        return studentDao.getStudentByIdLive(student_id);
    }

    public List<Student> getUnsyncedStudents(){
        return studentDao.getUnsyncedStudents();
    }



    public LiveData<Student> getStudentByEmail (String email){
        return studentDao.getStudentByEmail(email);
    }


//    public List<Student> getStudentsByCourseId(String courseId) {
//        // Fetch all student IDs for a given course
//        List<String> studentIds = enrollmentDao.getStudentIdsByCourseId(courseId);
//
//        // Use student IDs to get the students from the Student entity
//        return studentDao.getStudentsByIds(studentIds);
//    }


    public LiveData<List<Student>> getStudentsByCourseIdLive(String courseId) {
        MutableLiveData<List<Student>> liveData = new MutableLiveData<>();
        CompletableFuture.runAsync(() -> {
            List<String> studentIds = enrollmentDao.getStudentIdsByCourseId(courseId);
            List<Student> students = studentDao.getStudentsByIds(studentIds);
            liveData.postValue(students);
        });
        return liveData;
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


    public void updateCompletionStatus(String student_id, String lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateCompletionStatus(student_id,lesson_id,completion_status);
        });
    }


    public LiveData<Boolean> getCompletionStatus(String student_id, String lesson_id){
        return studentLessonDao.getCompletionStatus(student_id,lesson_id);
    }


    public void updateLessonCompletionStatus(String student_id, String lesson_id, boolean completion_status){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentLessonDao.updateLessonCompletionStatus(student_id,lesson_id,completion_status);
        });
    }

    public void updateLessonStatusAndProgress(String student_id, String lesson_id, boolean completion_status, EnrollmentDao enrollmentDao, String course_id) {
        updateLessonCompletionStatus(student_id, lesson_id, completion_status);
        enrollmentDao.updateEnrollmentProgress(student_id, course_id);

    }


    public void markLessonAsCompleted(String studentId, String lessonId, String courseId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            studentLessonDao.updateLessonStatusAndProgress(studentId, lessonId, true, enrollmentDao, courseId);
        });
    }


    public StudentLesson getStudentLessonById(String student_lesson_id){
        return studentLessonDao.getStudentLessonById(student_lesson_id);
    }

    public LiveData<Integer> getCompletedLessonsCount(String studentId){
        return studentLessonDao.getCompletedLessonsCount(studentId);
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

    public LiveData<List<String>> getAllMentorStudents(String mentor_id){
        return studentMentorDao.getAllMentorStudents(mentor_id);
    }

    public LiveData<List<String>> getAllStudentMentors(String student_id){
        return studentMentorDao.getAllStudentMentors(student_id);
    }



    public StudentMentor getStudentMentorById(String student_mentor_id){
        return studentMentorDao.getStudentMentorById(student_mentor_id);
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

    public LiveData<List<Double>> getStudentModuleGrade(String student_id, String module_id){
        return studentModuleDao.getStudentModuleGrade(student_id,module_id);
    }


    public void setModuleGrade(String student_id, String module_id, double module_grade){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            studentModuleDao.setModuleGrade(student_id,module_id,module_grade);
        });
    }

    public StudentModule getStudentModuleById(String student_module_id){
        return studentModuleDao.getStudentModuleById(student_module_id);
    }


    //AdDao

    public void insertAd(Ad ad){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            adDao.insertAd(ad);
        });
    }

    public void insertAdList(List<Ad> adList){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            adDao.insertAdList(adList);
        });
    }

    public void updateAd(Ad ad){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            adDao.updateAd(ad);
        });
    }

    public void deleteAd(Ad ad){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            adDao.deleteAd(ad);
        });
    }


    public LiveData<List<Ad>> getAllAds(){
        return adDao.getAllAds();
    }


    public Ad getAdById(String ad_id){
        return adDao.getAdById(ad_id);
    }



    //getUnsyncedData



    public List<StudentModule> getUnsyncedStudentModule(){
        return studentModuleDao.getUnsyncedStudentModule();
    }


    public List<StudentMentor> getUnsyncedStudentMentor(){
        return studentMentorDao.getUnsyncedStudentMentor();
    }

    public List<StudentLesson> getUnsyncedStudentLesson(){
        return studentLessonDao.getUnsyncedStudentLesson();
    }

    public List<Review> getUnsyncedReview(){
        return reviewDao.getUnsyncedReview();
    }

    public List<Notification> getUnsyncedNotification(){
        return notificationDao.getUnsyncedNotification();
    }

    public List<Module> getUnsyncedModule(){
        return moduleDao.getUnsyncedModule();
    }

    public List<Message> getUnsyncedMessage(){
        return messageDao.getUnsyncedMessage();
    }


    public List<MentorCourse> getUnsyncedMentorCourse(){
        return mentorCourseDao.getUnsyncedMentorCourse();
    }


    public List<Lesson> getUnsyncedLesson(){
        return lessonDao.getUnsyncedLesson();
    }

    public List<GroupMembership> getUnsyncedGroupMembership(){
        return groupMembershipDao.getUnsyncedGroupMembership();
    }


    public List<Group> getUnsyncedGroup(){
        return groupDao.getUnsyncedGroup();
    }


    public List<Enrollment> getUnsyncedEnrollment(){
        return enrollmentDao.getUnsyncedEnrollment();
    }


    public List<Chat> getUnsyncedChat(){
        return chatDao.getUnsyncedChat();
    }


    public List<Call> getUnsyncedCall(){
        return callDao.getUnsyncedCall();
    }


    public List<Bookmark> getUnsyncedBookmark(){
        return bookmarkDao.getUnsyncedBookmark();
    }


    public List<Attachment> getUnsyncedAttachment(){
        return attachmentDao.getUnsyncedAttachment();
    }


    public List<Ad> getUnsyncedAd(){
        return adDao.getUnsyncedAd();
    }

}
