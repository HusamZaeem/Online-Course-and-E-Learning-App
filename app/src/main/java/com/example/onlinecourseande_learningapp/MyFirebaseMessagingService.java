package com.example.onlinecourseande_learningapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.MainActivity;
import com.example.onlinecourseande_learningapp.R;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.UUID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "Notification_Channel";

    private AppViewModel appViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        appViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AppViewModel.class);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            try {
                String studentId = remoteMessage.getData().get("student_id");
                String title = remoteMessage.getData().get("title");
                String type = remoteMessage.getData().get("type");
                String content = remoteMessage.getData().get("content");
                long timestamp = Long.parseLong(remoteMessage.getData().get("timestamp"));

                // Create and save the Notification entity
                Notification notification = new Notification(
                        UUID.randomUUID().toString(),
                        studentId,
                        title,
                        type,
                        content,
                        new Date(timestamp),
                        false,
                        new Date()
                );

                appViewModel.insertNotification(notification);

                // Show the notification in the system tray
                sendNotification(title, content);

            } catch (Exception e) {
                Log.e(TAG, "Error parsing notification: " + e.getMessage());
            }
        }
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_default_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());
    }
}