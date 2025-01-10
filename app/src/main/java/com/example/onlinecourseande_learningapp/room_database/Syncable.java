package com.example.onlinecourseande_learningapp.room_database;

import java.util.Date;
import java.util.Map;

public interface Syncable {
    void markAsSynced();
    void updateInRepository(AppRepository repository);
    void insertInRepository(AppRepository repository);
    Date getLast_updated();
    String getId();
    void setPrimaryKey(String documentId);
    public Map<String, Object> toMap();
}
