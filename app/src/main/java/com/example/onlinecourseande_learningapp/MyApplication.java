package com.example.onlinecourseande_learningapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Enable logging to help with debugging Facebook SDK
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }
}