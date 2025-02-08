package com.example.onlinecourseande_learningapp.room_database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PeriodicSyncWorker extends Worker {

    private SyncManager syncManager;

    public PeriodicSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        syncManager = new SyncManager(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            syncManager.syncAllData();
            return Result.success();
        } catch (SQLiteConstraintException e) {
            // Log the error and return failure so it doesn't retry indefinitely.
            Log.e("PeriodicSyncWorker", "Constraint exception during sync", e);
            return Result.failure();
        } catch (Exception e) {
            return Result.retry();
        }
    }
}
