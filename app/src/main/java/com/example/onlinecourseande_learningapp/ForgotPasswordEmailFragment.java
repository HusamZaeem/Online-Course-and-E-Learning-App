package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentForgotPasswordEmailBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ForgotPasswordEmailFragment extends Fragment {

    FragmentForgotPasswordEmailBinding binding;
    private FirebaseFirestore firestore;


    public ForgotPasswordEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordEmailBinding.inflate(inflater,container,false);
        firestore = FirebaseFirestore.getInstance();

        binding.btnForgotPasswordContinue.setOnClickListener(v -> {
            String email = binding.etEmailForgotPassword.getText().toString().trim();
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailForgotPassword.setError("Enter a valid email");
                return;
            }
            checkEmailAndSendOtp(email);
        });

        return binding.getRoot();
    }

    private void checkEmailAndSendOtp(String email) {
        firestore.collection("Student")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Email exists, send OTP
                        sendOtpToEmail(email);
                    } else {
                        binding.etEmailForgotPassword.setError("Email not registered");
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendOtpToEmail(String email) {
        String otp = String.format("%04d", new Random().nextInt(10000));

        // Make an HTTP request to the Render server to send OTP
        new Thread(() -> {
            try {
                // Replace with your Render URL
                String url = "https://online-course-and-e-learning-app.onrender.com"; // URL of your deployed service
                OkHttpClient client = new OkHttpClient();

                // Create the request payload
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email);
                jsonBody.put("otp", otp);

                // Create the request body
                RequestBody body = RequestBody.create(
                        jsonBody.toString(), MediaType.parse("application/json")
                );

                // Make the POST request
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                // Send the request
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        // If OTP sent successfully, navigate to the next fragment
                        requireActivity().runOnUiThread(() -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("otp", otp); // Pass OTP to VerifyCodeFragment
                            bundle.putString("maskedEmail", maskEmail(email));
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentVerifyCode, bundle);
                        });
                    } else {
                        // Handle failure
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Failed to send OTP: " + response.message(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (IOException | JSONException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private String maskEmail(String email) {
        String[] parts = email.split("@");
        String prefix = parts[0];
        String domain = parts[1];
        return prefix.charAt(0) + "******@" + domain;
    }
}