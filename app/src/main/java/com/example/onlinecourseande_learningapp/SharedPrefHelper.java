package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPrefHelper {

    private static final String PREF_NAME = "app_preferences";
    private static final String RECENT_SEARCHES_KEY = "recent_searches";

    // Save recent searches to SharedPreferences
    public static void saveRecentSearches(Context context, List<String> recentSearches) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentSearches);
        editor.putString(RECENT_SEARCHES_KEY, json);
        editor.apply();
    }

    // Load recent searches from SharedPreferences
    public static List<String> loadRecentSearches(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(RECENT_SEARCHES_KEY, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Clear recent searches from SharedPreferences
    public static void clearRecentSearches(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(RECENT_SEARCHES_KEY);
        editor.apply();
    }
}
