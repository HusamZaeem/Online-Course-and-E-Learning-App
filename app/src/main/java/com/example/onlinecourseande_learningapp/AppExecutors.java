package com.example.onlinecourseande_learningapp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {



    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        return new AppExecutors(Executors.newSingleThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }



}
