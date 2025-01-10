package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncManager {
    private final AppDatabase roomDatabase;
    private final AppRepository appRepository;
    private final FirebaseSyncHelper firebaseSyncHelper;
    private final FirebaseFirestore fs;

    public SyncManager(Context context) {
        this.roomDatabase = AppDatabase.getDatabase(context);
        this.appRepository = AppRepository.getInstance((Application) context.getApplicationContext());
        this.firebaseSyncHelper = new FirebaseSyncHelper(appRepository);
        this.fs = FirebaseFirestore.getInstance();
    }

    public void syncAllData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // First, start listening for Firebase updates
            startListeningForFirebaseUpdates(() -> {
                // Once listening is complete, start syncing each entity one by one
                syncEntity(appRepository::getUnsyncedStudents, "Student", () -> syncEntity(appRepository::getUnsyncedMentors, "Mentor", () -> syncEntity(appRepository::getUnsyncedCourses, "Course", () -> syncEntity(appRepository::getUnsyncedModule, "Module", () -> syncEntity(appRepository::getUnsyncedLesson, "Lesson", () -> syncEntity(appRepository::getUnsyncedChat, "Chat", () ->  syncEntity(appRepository::getUnsyncedStudentModule, "StudentModule", () -> syncEntity(appRepository::getUnsyncedStudentMentor, "StudentMentor", () -> syncEntity(appRepository::getUnsyncedStudentLesson, "StudentLesson", () -> syncEntity(appRepository::getUnsyncedMentorCourse, "MentorCourse", () -> syncEntity(appRepository::getUnsyncedGroupMembership, "GroupMembership", () ->  syncEntity(appRepository::getUnsyncedCall, "Call", () -> syncEntity(appRepository::getUnsyncedReview, "Review", () -> syncEntity(appRepository::getUnsyncedNotification, "Notification", () -> syncEntity(appRepository::getUnsyncedAttachment, "Attachment", () -> syncEntity(appRepository::getUnsyncedGroup, "Group", () -> syncEntity(appRepository::getUnsyncedEnrollment, "Enrollment", () -> syncEntity(appRepository::getUnsyncedMessage, "Message", () -> syncEntity(appRepository::getUnsyncedBookmark, "Bookmark", () -> syncEntity(appRepository::getUnsyncedAd, "Ad", () -> {
                    // Finally, all syncing is done
                }))))))))))))))))))));
            });
        });
    }


    // Modified syncEntity method to accept a SyncCallback
    public <T extends Syncable> void syncEntity(Supplier<List<T>> fetchUnsynced, String collection, SyncCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<T> unsyncedEntities = fetchUnsynced.get();

            if (unsyncedEntities == null || unsyncedEntities.isEmpty()) {
                Log.d("SyncManager", "No unsynced entities found in collection: " + collection);
                callback.onSyncComplete();
                return;
            }

            for (T entity : unsyncedEntities) {
                String documentId = entity.getId();
                if (documentId == null || documentId.isEmpty()) {
                    Log.e("SyncManager", "Invalid ID for entity in collection: " + collection);
                    continue; // Skip this entity
                }

                Map<String, Object> firebaseData = new HashMap<>();
                Map<String, Object> entityData = entity.toMap();

                if (entityData == null) {
                    Log.e("SyncManager", "Failed to convert entity to map for collection: " + collection);
                    continue; // Skip this entity
                }

                // Process the map to convert Date fields to Firestore Timestamps
                for (Map.Entry<String, Object> entry : entityData.entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        firebaseData.put(entry.getKey(), Converter.toFirestoreTimestamp((Date) entry.getValue()));
                    } else {
                        firebaseData.put(entry.getKey(), entry.getValue());
                    }
                }

                fs.collection(collection).document(documentId)
                        .set(firebaseData)
                        .addOnSuccessListener(aVoid -> {
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                entity.markAsSynced();
                                entity.updateInRepository(appRepository);
                                Log.d("SyncManager", "Successfully synced entity in collection: " + collection);
                                callback.onSyncComplete();
                            });
                        })
                        .addOnFailureListener(e -> {
                            Log.e("SyncError", "Failed to sync entity in collection: " + collection, e);
                            callback.onSyncComplete();
                        });
            }
        });
    }


    private void startListeningForFirebaseUpdates(Runnable onListeningComplete) {
        firebaseSyncHelper.startSyncingStudents(() -> firebaseSyncHelper.startSyncingMentors(() -> firebaseSyncHelper.startSyncingCourses(() -> firebaseSyncHelper.startSyncingModules(() -> firebaseSyncHelper.startSyncingLessons(() -> firebaseSyncHelper.startSyncingChats(() -> firebaseSyncHelper.startSyncingStudentModules(() -> firebaseSyncHelper.startSyncingStudentMentors(() -> firebaseSyncHelper.startSyncingStudentLessons(() -> firebaseSyncHelper.startSyncingMentorCourses(() -> firebaseSyncHelper.startSyncingGroupMemberships(() -> firebaseSyncHelper.startSyncingCalls(() -> firebaseSyncHelper.startSyncingReviews(() -> firebaseSyncHelper.startSyncingNotifications(() -> firebaseSyncHelper.startSyncingAttachments(() -> firebaseSyncHelper.startSyncingGroups(() -> firebaseSyncHelper.startSyncingEnrollments(() -> firebaseSyncHelper.startSyncingMessages(() ->  firebaseSyncHelper.startSyncingBookmarks(() -> firebaseSyncHelper.startSyncingAds(() -> {
            // Once everything is synced, we call the onListeningComplete callback
            onListeningComplete.run();
        }))))))))))))))))))));
    }


    public interface SyncCallback {
        void onSyncComplete();
    }
}
