package com.example.onlinecourseande_learningapp;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class SyncWorkerScheduler {
    public static void schedulePeriodicSync(Context context) {
        // Define constraints: Only run when connected to the internet
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Schedule periodic work (e.g., every 15 minutes)
        PeriodicWorkRequest syncWorkRequest = new PeriodicWorkRequest.Builder(SyncWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        // Enqueue the work request
        WorkManager.getInstance(context).enqueue(syncWorkRequest);
    }
}