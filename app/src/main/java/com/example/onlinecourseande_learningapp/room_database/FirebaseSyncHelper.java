package com.example.onlinecourseande_learningapp.room_database;


import android.util.Log;

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
import com.example.onlinecourseande_learningapp.room_database.entities.Review;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentLesson;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentMentor;
import com.example.onlinecourseande_learningapp.room_database.entities.StudentModule;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class FirebaseSyncHelper {
    private static final String TAG = "FirebaseSyncHelper";
    private final FirebaseFirestore db;
    private final AppRepository appRepository;

    public FirebaseSyncHelper(AppRepository appRepository) {
        this.db = FirebaseFirestore.getInstance();
        this.appRepository = appRepository;
    }

    public void startSyncingMentors(Runnable onComplete) {
        db.collection("Mentor")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Mentor remoteMentor = dc.getDocument().toObject(Mentor.class);
                            remoteMentor.setMentor_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Mentor localMentor = appRepository.getMentorById(remoteMentor.getId());
                                handleSync(remoteMentor,localMentor,"Mentor");
                            });
                        }
                    }
                });
        onComplete.run();
    }

    public void startSyncingCourses(Runnable onComplete) {
        db.collection("Course")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Course remoteCourse = dc.getDocument().toObject(Course.class);
                            remoteCourse.setCourse_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Course localCourse = appRepository.getCourseById(remoteCourse.getId());
                                handleSync(remoteCourse,localCourse,"Course");
                            });
                        }
                    }
                });
        onComplete.run();
    }

    public void startSyncingStudents(Runnable onComplete) {
        db.collection("Student")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Student remoteStudent = dc.getDocument().toObject(Student.class);
                            remoteStudent.setStudent_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Student localStudent = appRepository.getStudentById(remoteStudent.getId());
                                handleSync(remoteStudent,localStudent,"Student");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingStudentModules(Runnable onComplete) {
        db.collection("StudentModule")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            StudentModule remoteStudentModule = dc.getDocument().toObject(StudentModule.class);
                            remoteStudentModule.setStudent_module_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                StudentModule localStudentModule = appRepository.getStudentModuleById(remoteStudentModule.getId());
                                handleSync(remoteStudentModule,localStudentModule,"StudentModule");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingStudentMentors(Runnable onComplete) {
        db.collection("StudentMentor")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            StudentMentor remoteStudentMentor = dc.getDocument().toObject(StudentMentor.class);
                            remoteStudentMentor.setStudent_mentor_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                StudentMentor localStudentMentor = appRepository.getStudentMentorById(remoteStudentMentor.getId());
                                handleSync(remoteStudentMentor,localStudentMentor,"StudentMentor");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingStudentLessons(Runnable onComplete) {
        db.collection("StudentLesson")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            StudentLesson remoteStudentLesson = dc.getDocument().toObject(StudentLesson.class);
                            remoteStudentLesson.setStudent_lesson_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                StudentLesson localStudentLesson = appRepository.getStudentLessonById(remoteStudentLesson.getId());
                                handleSync(remoteStudentLesson,localStudentLesson,"StudentLesson");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingReviews(Runnable onComplete) {
        db.collection("Review")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Review remoteReview = dc.getDocument().toObject(Review.class);
                            remoteReview.setReview_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Review localReview = appRepository.getReviewByReviewId(remoteReview.getId());
                                handleSync(remoteReview,localReview,"Review");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingNotifications(Runnable onComplete) {
        db.collection("Notification")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Notification remoteNotification = dc.getDocument().toObject(Notification.class);
                            remoteNotification.setNotification_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Notification localNotification = appRepository.getNotificationByNotificationId(remoteNotification.getId());
                                handleSync(remoteNotification,localNotification,"Notification");
                            });
                        }
                    }
                });
        onComplete.run();
    }

    public void startSyncingModules(Runnable onComplete) {
        db.collection("Module")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Module remoteModule = dc.getDocument().toObject(Module.class);
                            remoteModule.setModule_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Module localModule = appRepository.getModuleByModuleId(remoteModule.getId());
                                handleSync(remoteModule,localModule,"Module");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingMessages(Runnable onComplete) {
        db.collection("Message")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Message remoteMessage = dc.getDocument().toObject(Message.class);
                            remoteMessage.setMessage_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Message localMessage = appRepository.getMessageByMessageId(remoteMessage.getId());
                                handleSync(remoteMessage,localMessage,"Message");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingMentorCourses(Runnable onComplete) {
        db.collection("MentorCourse")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            MentorCourse remoteMentorCourse = dc.getDocument().toObject(MentorCourse.class);
                            remoteMentorCourse.setMentor_course_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                MentorCourse localMentorCourse = appRepository.getMentorCourseByMentorCourseId(remoteMentorCourse.getId());
                                handleSync(remoteMentorCourse,localMentorCourse,"MentorCourse");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingLessons(Runnable onComplete) {
        db.collection("Lesson")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Lesson remoteLesson = dc.getDocument().toObject(Lesson.class);
                            remoteLesson.setLesson_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Lesson localLesson = appRepository.getLessonByLessonId(remoteLesson.getId());
                                handleSync(remoteLesson,localLesson,"Lesson");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingGroupMemberships(Runnable onComplete) {
        db.collection("GroupMembership")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            GroupMembership remoteGroupMembership = dc.getDocument().toObject(GroupMembership.class);
                            remoteGroupMembership.setGroup_membership_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                GroupMembership localGroupMembership = appRepository.getGroupMembershipByGroupMembershipId(remoteGroupMembership.getId());
                                handleSync(remoteGroupMembership,localGroupMembership,"GroupMembership");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingGroups(Runnable onComplete) {
        db.collection("Group")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Group remoteGroup = dc.getDocument().toObject(Group.class);
                            remoteGroup.setGroup_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Group localGroup = appRepository.getGroupByCourseId(remoteGroup.getId());
                                handleSync(remoteGroup,localGroup,"Group");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingEnrollments(Runnable onComplete) {
        db.collection("Enrollment")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Enrollment remoteEnrollment = dc.getDocument().toObject(Enrollment.class);
                            remoteEnrollment.setEnrollment_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Enrollment localEnrollment = appRepository.getEnrollmentByEnrollmentId(remoteEnrollment.getId());
                                handleSync(remoteEnrollment,localEnrollment,"Enrollment");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingChats(Runnable onComplete) {
        db.collection("Chat")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Chat remoteChat = dc.getDocument().toObject(Chat.class);
                            remoteChat.setChat_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Chat localChat = appRepository.getChatById(remoteChat.getId());
                                handleSync(remoteChat,localChat,"Chat");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingCalls(Runnable onComplete) {
        db.collection("Call")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Call remoteCall = dc.getDocument().toObject(Call.class);
                            remoteCall.setCall_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Call localCall = appRepository.getCallById(remoteCall.getId());
                                handleSync(remoteCall,localCall,"Call");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingBookmarks(Runnable onComplete) {
        db.collection("Bookmark")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Bookmark remoteBookmark = dc.getDocument().toObject(Bookmark.class);
                            remoteBookmark.setBookmark_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Bookmark localBookmark = appRepository.getBookmarkByBookmarkId(remoteBookmark.getId());
                                handleSync(remoteBookmark, localBookmark,"Bookmark");
                            });

                        }
                    }
                });
        onComplete.run();
    }



    public void startSyncingAttachments(Runnable onComplete) {
        db.collection("Attachment")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Attachment remoteAttachment = dc.getDocument().toObject(Attachment.class);
                            remoteAttachment.setAttachment_id(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Attachment localAttachment = appRepository.getAttachmentByAttachmentId(remoteAttachment.getAttachment_id());
                                handleSync(remoteAttachment, localAttachment, "Attachment");
                            });
                        }
                    }
                });
        onComplete.run();
    }


    public void startSyncingAds(Runnable onComplete) {
        db.collection("Ad")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            Ad remoteAd = dc.getDocument().toObject(Ad.class);
                            remoteAd.setId(dc.getDocument().getId());
                            AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                                Ad localAd = appRepository.getAdById(remoteAd.getId());
                                handleSync(remoteAd, localAd, "Ad");
                            });
                        }
                    }
                });
        onComplete.run();
    }



    private <T extends Syncable> void handleSync(T remoteEntity, T localEntity, String collection) {
        String documentId = remoteEntity.getId();
        if (documentId == null || documentId.isEmpty()) {
            Log.w(TAG, "Firestore Document ID is missing for " + collection);
            return;
        }

        if (localEntity == null) {
            remoteEntity.setPrimaryKey(documentId);
            remoteEntity.insertInRepository(appRepository);
            Log.i(TAG, collection + " Inserted: " + documentId);
        } else {
            Date remoteLastUpdated = remoteEntity.getLast_updated();
            Date localLastUpdated = localEntity.getLast_updated();

            if (remoteLastUpdated == null && localLastUpdated == null) {
                Log.w(TAG, collection + " has no valid last_updated timestamps: " + documentId);
                return;
            }

            if (remoteLastUpdated != null && (localLastUpdated == null || remoteLastUpdated.compareTo(localLastUpdated) > 0)) {
                remoteEntity.setPrimaryKey(documentId);
                remoteEntity.updateInRepository(appRepository);
                Log.i(TAG, collection + " Updated locally from Firebase: " + documentId);
            } else if (localLastUpdated != null && (remoteLastUpdated == null || remoteLastUpdated.compareTo(localLastUpdated) < 0)) {
                Map<String, Object> localData = localEntity.toMap();
                localData.put("last_updated", Converter.toFirestoreTimestamp(localLastUpdated)); // Convert Date to Firestore Timestamp

                db.collection(collection).document(localEntity.getId())
                        .set(localData)
                        .addOnSuccessListener(aVoid -> Log.i(TAG, "Conflict resolved: Local data pushed to Firebase"))
                        .addOnFailureListener(error -> Log.e(TAG, "Failed to resolve conflict for local data: " + documentId, error));
            }
        }
    }








}