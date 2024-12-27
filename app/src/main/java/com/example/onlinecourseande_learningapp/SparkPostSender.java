package com.example.onlinecourseande_learningapp;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SparkPostSender {

    private static final String API_KEY = "2d9f4eee90fcc8d953e1900ad286b08a4abed766";

    // Send an email using SparkPost
    public static void sendEmail(String to, String subject, String body) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Construct the request payload
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("content", new JSONObject()
                    .put("from", "from_onlinecoursesandelearning@gmail.com")
                    .put("subject", subject)
                    .put("text", body)
            );
            jsonBody.put("recipients", new JSONObject[] {
                    new JSONObject().put("address", to)
            });
        } catch (JSONException e) {
            throw new IOException("Error creating JSON object: " + e.getMessage(), e);
        }

        // Create the request body
        RequestBody bodyRequest = RequestBody.create(
                jsonBody.toString(), MediaType.parse("application/json")
        );

        // Make the request
        Request request = new Request.Builder()
                .url("https://api.sparkpost.com/api/v1/transmissions")
                .post(bodyRequest)
                .addHeader("Authorization", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Send the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("Email sent successfully: " + response.body().string());
        }
    }

    public static void main(String[] args) {
        try {
            sendEmail("recipient@example.com", "Test Subject", "This is a test email from SparkPost.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}