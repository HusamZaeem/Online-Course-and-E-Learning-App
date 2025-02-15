package com.example.onlinecourseande_learningapp.room_database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;



public class SyncManager {

    private final AppDatabase roomDatabase;
    private final AppRepository appRepository;
    private final FirebaseSyncHelper firebaseSyncHelper;
    private final FirebaseFirestore fs;
    private final ExecutorService firebaseExecutor;

    public SyncManager(Context context) {
        this.roomDatabase = AppDatabase.getDatabase(context);
        this.appRepository = AppRepository.getInstance((Application) context.getApplicationContext());
        this.firebaseSyncHelper = new FirebaseSyncHelper(appRepository);
        this.fs = FirebaseFirestore.getInstance();
        this.firebaseExecutor = Executors.newFixedThreadPool(4); // Separate executor for Firebase operations
    }

    public void syncAllData() {
        CompletableFuture.runAsync(() -> {
            // Start listening for Firebase updates
            startListeningForFirebaseUpdates()
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedStudents, "Student"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedMentors, "Mentor"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedChat, "Chat"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedStudentModule, "StudentModule"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedStudentMentor, "StudentMentor"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedStudentLesson, "StudentLesson"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedMentorCourse, "MentorCourse"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedGroupMembership, "GroupMembership"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedCall, "Call"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedReview, "Review"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedNotification, "Notification"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedAttachment, "Attachment"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedGroup, "Group"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedEnrollment, "Enrollment"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedMessage, "Message"))
                    .thenCompose(aVoid -> syncEntity(appRepository::getUnsyncedBookmark, "Bookmark"))
                    .thenRun(() -> Log.d("SyncManager", "All data synced successfully"))
                    .exceptionally(e -> {
                        Log.e("SyncManager", "Error during sync", e);
                        return null;
                    });
        });
    }

    private CompletableFuture<Void> syncEntity(Supplier<List<? extends Syncable>> fetchUnsynced, String collection) {
        return CompletableFuture.runAsync(() -> {
            List<? extends Syncable> unsyncedEntities = fetchUnsynced.get();

            if (unsyncedEntities == null || unsyncedEntities.isEmpty()) {
                Log.d("SyncManager", "No unsynced entities found in collection: " + collection);
                return;
            }

            for (Syncable entity : unsyncedEntities) {
                String documentId = entity.getId();
                if (documentId == null || documentId.isEmpty()) {
                    Log.e("SyncManager", "Invalid ID for entity in collection: " + collection);
                    continue;
                }

                Map<String, Object> firebaseData = new HashMap<>();
                Map<String, Object> entityData = entity.toMap();

                if (entityData == null) {
                    Log.e("SyncManager", "Failed to convert entity to map for collection: " + collection);
                    continue;
                }

                // Convert Date fields to Firestore Timestamps
                for (Map.Entry<String, Object> entry : entityData.entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        firebaseData.put(entry.getKey(), Converter.toFirestoreTimestamp((Date) entry.getValue()));
                    } else {
                        firebaseData.put(entry.getKey(), entry.getValue());
                    }
                }

                CompletableFuture.runAsync(() -> {
                    fs.collection(collection).document(documentId)
                            .set(firebaseData)
                            .addOnSuccessListener(aVoid -> {
                                entity.markAsSynced();
                                entity.updateInRepository(appRepository);
                                Log.d("SyncManager", "Successfully synced entity in collection: " + collection);
                            })
                            .addOnFailureListener(e -> Log.e("SyncError", "Failed to sync entity in collection: " + collection, e));
                }, firebaseExecutor);
            }
        });
    }


    private CompletableFuture<Void> startListeningForFirebaseUpdates() {
        return CompletableFuture.runAsync(() -> {
            firebaseSyncHelper.startSyncingStudents(() -> {
                firebaseSyncHelper.startSyncingMentors(() -> {
                    firebaseSyncHelper.startSyncingCourses(() -> {
                        firebaseSyncHelper.startSyncingModules(() -> {
                            firebaseSyncHelper.startSyncingLessons(() -> {
                                firebaseSyncHelper.startSyncingChats(() -> {
                                    firebaseSyncHelper.startSyncingStudentModules(() -> {
                                        firebaseSyncHelper.startSyncingStudentMentors(() -> {
                                            firebaseSyncHelper.startSyncingStudentLessons(() -> {
                                                firebaseSyncHelper.startSyncingMentorCourses(() -> {
                                                    firebaseSyncHelper.startSyncingGroups(() -> {
                                                        firebaseSyncHelper.startSyncingGroupMemberships(() -> {
                                                            firebaseSyncHelper.startSyncingCalls(() -> {
                                                                firebaseSyncHelper.startSyncingReviews(() -> {
                                                                    firebaseSyncHelper.startSyncingNotifications(() -> {
                                                                        firebaseSyncHelper.startSyncingEnrollments(() -> {
                                                                            firebaseSyncHelper.startSyncingMessages(() -> {
                                                                                firebaseSyncHelper.startSyncingAttachments(() -> {
                                                                                    firebaseSyncHelper.startSyncingBookmarks(() -> {
                                                                                        firebaseSyncHelper.startSyncingAds(() -> {

                                                                                        });
                                                                                    });
                                                                                });
                                                                            });
                                                                        });
                                                                    });
                                                                });
                                                            });
                                                        });
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });

    }
}