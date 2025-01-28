package com.example.onlinecourseande_learningapp;


import android.os.Handler;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    private static final Handler handler = new Handler();
    private static final MutableLiveData<Long> currentTime = new MutableLiveData<>(System.currentTimeMillis());

    static {
        // Periodically update the current time
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTime.setValue(System.currentTimeMillis());
                handler.postDelayed(this, 60000); // Update every minute
            }
        }, 60000);
    }

    public static LiveData<Long> getCurrentTimeLive() {
        return currentTime;
    }

    public static String getRelativeTime(long timestamp, long currentTime) {
        // Get the timezone from the system
        TimeZone timeZone = TimeZone.getDefault();

        // Adjust the timestamp to the user's timezone
        long adjustedTimestamp = timestamp + timeZone.getOffset(timestamp);

        long diff = currentTime - adjustedTimestamp;

        if (diff < 60 * 1000) { // Less than a minute
            return "Just now";
        } else if (diff < 60 * 60 * 1000) { // Less than an hour
            return (diff / (60 * 1000)) + " minutes ago";
        } else if (diff < 24 * 60 * 60 * 1000) { // Less than a day
            return (diff / (60 * 60 * 1000)) + " hours ago";
        } else if (diff < 7 * 24 * 60 * 60 * 1000) { // Less than a week
            return (diff / (24 * 60 * 60 * 1000)) + " days ago";
        } else if (diff < 30L * 24 * 60 * 60 * 1000) { // Less than a month
            return (diff / (7 * 24 * 60 * 60 * 1000)) + " weeks ago";
        } else if (diff < 365L * 24 * 60 * 60 * 1000) { // Less than a year
            return (diff / (30L * 24 * 60 * 60 * 1000)) + " months ago";
        } else {
            return (diff / (365L * 24 * 60 * 60 * 1000)) + " years ago";
        }
    }

    public static String formatTimestamp(long timestamp) {
        // Use a simple date format with timezone support
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
