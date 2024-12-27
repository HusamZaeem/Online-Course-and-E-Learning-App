package com.example.onlinecourseande_learningapp;
import okhttp3.*;
public class EmailSender {

    private static final String API_URL = "https://api.sendgrid.com/v3/mail/send";
    private static final String API_KEY = "YOUR_SENDGRID_API_KEY";

    public static void sendEmail(String to, String subject, String body) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String jsonPayload = "{"
                + "\"personalizations\": [{\"to\": [{\"email\": \"" + to + "\"}]}],"
                + "\"from\": {\"email\": \"your-email@example.com\"},"
                + "\"subject\": \"" + subject + "\","
                + "\"content\": [{\"type\": \"text/plain\", \"value\": \"" + body + "\"}]"
                + "}";

        RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("Failed to send email: " + response.body().string());
        }
    }
}