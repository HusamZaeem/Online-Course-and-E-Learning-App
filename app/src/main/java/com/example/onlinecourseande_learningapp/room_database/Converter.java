package com.example.onlinecourseande_learningapp.room_database;

import androidx.room.TypeConverter;
import com.google.firebase.Timestamp;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    // Convert from Long (used by Room) to Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Convert from Date to Long (used by Room)
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    // For Firestore: Convert from Timestamp to Date
    public static Date fromFirestoreTimestamp(Timestamp timestamp) {
        return timestamp != null ? timestamp.toDate() : null;
    }

    // For Firestore: Convert from Date to Timestamp
    public static Timestamp toFirestoreTimestamp(Date date) {
        return date != null ? new Timestamp(date) : null;
    }



    // Convert from JSON String to Map<String, Boolean>
    @TypeConverter
    public static Map<String, Boolean> fromStringToTypingStatus(String value) {
        if (value == null) {
            return new HashMap<>();
        }
        Type mapType = new TypeToken<Map<String, Boolean>>() {}.getType();
        return new Gson().fromJson(value, mapType);
    }

    //  Convert from Map<String, Boolean> to JSON String
    @TypeConverter
    public static String fromTypingStatusToString(Map<String, Boolean> map) {
        return new Gson().toJson(map);
    }

}
