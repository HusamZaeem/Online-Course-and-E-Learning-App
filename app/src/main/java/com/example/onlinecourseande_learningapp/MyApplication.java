package com.example.onlinecourseande_learningapp;

import android.app.Application;


import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.onlinecourseande_learningapp.room_database.PeriodicSyncWorker;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance());
        firebaseAppCheck.setTokenAutoRefreshEnabled(false);


        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Enable logging to help with debugging Facebook SDK
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        // Immediate sync on launch
        scheduleImmediateSync();

        // Periodic sync every 12 hours
        schedulePeriodicSync();

    }


    private void scheduleImmediateSync() {
        OneTimeWorkRequest immediateSyncWorkRequest = new OneTimeWorkRequest.Builder(
                PeriodicSyncWorker.class
        ).build();

        WorkManager.getInstance(this).enqueueUniqueWork(
                "ImmediateSyncWork",
                ExistingWorkPolicy.KEEP, // Avoid duplicate immediate work
                immediateSyncWorkRequest
        );
    }

    private void schedulePeriodicSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicSyncWorkRequest = new PeriodicWorkRequest.Builder(
                PeriodicSyncWorker.class,
                12, TimeUnit.HOURS
        ).setConstraints(constraints).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "PeriodicSyncWork",
                ExistingPeriodicWorkPolicy.KEEP, // Avoid duplicate periodic work
                periodicSyncWorkRequest
        );
    }


}