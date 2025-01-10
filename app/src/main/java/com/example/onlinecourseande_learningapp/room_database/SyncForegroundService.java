package com.example.onlinecourseande_learningapp.room_database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.onlinecourseande_learningapp.R;

public class SyncForegroundService extends Service {

    private SyncManager syncManager;

    @Override
    public void onCreate() {
        super.onCreate();
        syncManager = new SyncManager(this);

        // Create notification channel for API 26+
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "SYNC_CHANNEL",
                    "Foreground Sync",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the foreground service
        Notification notification = new NotificationCompat.Builder(this, "SYNC_CHANNEL")
                .setContentTitle("Data Sync in Progress")
                .setContentText("Synchronizing data with the server.")
                .setSmallIcon(R.drawable.baseline_cloud_upload_24)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);

        // Perform sync
        new Thread(() -> {
            syncManager.syncAllData();
            stopSelf(); // Stop service when done
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
