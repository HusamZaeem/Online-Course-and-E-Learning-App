package com.example.onlinecourseande_learningapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.onlinecourseande_learningapp.room_database.AppDatabase;
import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncWorker extends Worker {

    private final FirebaseFirestore firestore;
    private AppRepository repository;


    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.firestore = FirebaseFirestore.getInstance();
        repository = AppRepository.getInstance((Application) context.getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {


        try {
            // Perform the synchronization
            syncRoomToFirestore();
            syncFirestoreToRoom();
            return Result.success();
        } catch (Exception e) {
            Log.e("SyncWorker", "Synchronization failed", e);
            return Result.failure();
        }
    }

    private void syncRoomToFirestore() {
        // Fetch the unsynced students from the local Room database
        List<Student> unsyncedStudents = repository.getUnsyncedStudents();

        for (Student student : unsyncedStudents) {
            // Create a map to store student data to upload to Firestore
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("student_id", student.getStudent_id());
            userMap.put("email", student.getEmail());
            userMap.put("password", student.getPassword());

            // Upload the student data to Firestore
            firestore.collection("students").document(student.getStudent_id())
                    .set(userMap)
                    .addOnSuccessListener(aVoid -> {
                        // Mark student as synced after successful upload
                        student.setIs_synced(true);
                        repository.updateStudent(student); // Update student in Room
                    })
                    .addOnFailureListener(e -> Log.e("SyncWorker", "Failed to sync student to Firestore", e));
        }
    }

    private void syncFirestoreToRoom() {
        // Fetch students from Firestore and update the local Room database
        firestore.collection("students")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Perform the operation in a background thread to avoid UI blocking
                    AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String uid = document.getString("student_id");
                            String email = document.getString("email");
                            String password = document.getString("password");

                            // Create a new student object from Firestore data
                            Student student = new Student(uid, email, password);
                            student.setIs_synced(true); // Mark the student as synced

                            // Insert the student into the local Room database
                            repository.insertStudent(student);
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e("SyncWorker", "Failed to fetch data from Firestore", e));
    }
}