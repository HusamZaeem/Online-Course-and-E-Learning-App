package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncWorker extends Worker {

    private final FirebaseFirestore firestore;
    private final AppViewModel appViewModel;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams, AppViewModel appViewModel) {
        super(context, workerParams);
        this.firestore = FirebaseFirestore.getInstance();
        this.appViewModel = appViewModel;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            syncRoomToFirestore();
            syncFirestoreToRoom();
            return Result.success();
        } catch (Exception e) {
            Log.e("SyncWorker", "Synchronization failed", e);
            return Result.failure();
        }
    }

    private void syncRoomToFirestore() {
        appViewModel.getUnsyncedStudents().observeForever(students -> {
            for (Student student : students) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("student_id", student.getStudent_id());
                userMap.put("email", student.getEmail());
                userMap.put("password", student.getPassword());

                firestore.collection("students").document(student.getStudent_id())
                        .set(userMap)
                        .addOnSuccessListener(aVoid -> {
                            student.setIs_synced(true); // Mark as synced in local DB
                            appViewModel.updateStudent(student);
                        })
                        .addOnFailureListener(e -> Log.e("SyncWorker", "Failed to sync student to Firestore", e));
            }
        });
    }

    private void syncFirestoreToRoom() {
        firestore.collection("students")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Student> studentsFromFirestore = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String uid = document.getString("student_id");
                        String email = document.getString("email");
                        String password = document.getString("password");
                        Student student = new Student(uid, email, password);
                        student.setIs_synced(true); // Mark as synced in Room
                        studentsFromFirestore.add(student);
                    }
                    appViewModel.insertStudents(studentsFromFirestore);
                })
                .addOnFailureListener(e -> Log.e("SyncWorker", "Failed to fetch data from Firestore", e));
    }
}
