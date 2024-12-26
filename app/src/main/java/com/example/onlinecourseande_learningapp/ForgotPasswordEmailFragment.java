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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;


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
        // Generate OTP
        String otp = String.valueOf((int) (Math.random() * 9000) + 1000);

        // Store OTP temporarily in Firestore
        firestore.collection("OtpVerification").document(email)
                .set(Collections.singletonMap("otp", otp))
                .addOnSuccessListener(unused -> {
                    // Redirect to FragmentVerifyCode
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("maskedEmail", maskEmail(email));
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentVerifyCode, bundle);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to send OTP", Toast.LENGTH_SHORT).show());
    }

    private String maskEmail(String email) {
        String[] parts = email.split("@");
        String prefix = parts[0];
        String domain = parts[1];
        return prefix.charAt(0) + "******@" + domain;
    }
}