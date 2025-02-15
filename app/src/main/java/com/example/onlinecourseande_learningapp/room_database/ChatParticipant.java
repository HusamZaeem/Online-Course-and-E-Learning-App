package com.example.onlinecourseande_learningapp.room_database;

public class ChatParticipant {
    private String participantId;
    private String participantName;
    private String participantType; // "Student" or "Mentor"
    private String profilePhotoUrl;

    public ChatParticipant(String participantId, String participantName, String participantType, String profilePhotoUrl) {
        this.participantId = participantId;
        this.participantName = participantName;
        this.participantType = participantType;
        this.profilePhotoUrl = profilePhotoUrl;
    }


    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }

    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }

    public String getParticipantType() { return participantType; }
    public void setParticipantType(String participantType) { this.participantType = participantType; }

    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }
}