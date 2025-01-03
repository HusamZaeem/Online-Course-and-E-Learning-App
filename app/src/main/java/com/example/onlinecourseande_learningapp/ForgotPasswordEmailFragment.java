package com.example.onlinecourseande_learningapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.onlinecourseande_learningapp.databinding.FragmentForgotPasswordEmailBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordEmailFragment extends Fragment {

    private FragmentForgotPasswordEmailBinding binding;
    private FirebaseFirestore firestore;

    public ForgotPasswordEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordEmailBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();

        // Show Email or Phone number field based on radio selection
        binding.radioGroupMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEmail) {
                binding.etEmailForgotPassword.setVisibility(View.VISIBLE);
                binding.etPhoneForgotPassword.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioSms) {
                binding.etPhoneForgotPassword.setVisibility(View.VISIBLE);
                binding.etEmailForgotPassword.setVisibility(View.GONE);
            }
        });

        binding.btnForgotPasswordContinue.setOnClickListener(v -> {
            if (binding.radioEmail.isChecked()) {
                String email = binding.etEmailForgotPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkEmailAndSendOtp(email);
            } else if (binding.radioSms.isChecked()) {
                String phone = binding.etPhoneForgotPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkPhoneAndSendOtp(phone);
            } else {
                Toast.makeText(getContext(), "Please select a method (Email or SMS)", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    // Function to check if email exists in the Firestore collection and send OTP if found
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

    // Function to check if phone number exists in the Firestore collection and send OTP via SMS if found
    private void checkPhoneAndSendOtp(String phone) {
        firestore.collection("Student")
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Phone number exists, send OTP
                        sendOtpToPhone(phone);
                    } else {
                        Toast.makeText(getContext(), "No account registered for this phone number", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Function to send OTP to email
    private void sendOtpToEmail(String email) {
        showLoading();
        String otp = String.format("%06d", new Random().nextInt(1000000));

        new Thread(() -> {
            try {
                String url = "https://online-course-and-e-learning-app.onrender.com/send-otp";
                OkHttpClient client = new OkHttpClient();

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email);
                jsonBody.put("otp", otp);

                RequestBody body = RequestBody.create(
                        jsonBody.toString(), MediaType.parse("application/json")
                );

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    hideLoading();
                    if (response.isSuccessful()) {
                        requireActivity().runOnUiThread(() -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("otp", otp);
                            bundle.putString("maskedEmail", maskEmail(email));
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentVerifyCode, bundle);
                        });
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Failed to send OTP: " + errorBody, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (IOException | JSONException e) {
                hideLoading();
                requireActivity().runOnUiThread(() ->{
                            Toast.makeText(getContext(), "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            startRetryTimer();

                        }

                );
            }
        }).start();
    }

    // Function to send OTP to phone number via SMS using Firebase Phone Auth
    private void sendOtpToPhone(String phone) {
        showLoading();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60, // Timeout in seconds
                java.util.concurrent.TimeUnit.SECONDS,
                getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // No-op, handle verification completion if needed
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        hideLoading();
                        Toast.makeText(getContext(), "OTP sending failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        startRetryTimer();

                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Store the verificationId to verify the code later
                        hideLoading();
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phone);
                        bundle.putString("maskedPhone", maskPhone(phone));
                        bundle.putString("verificationId", verificationId);
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentVerifyCode, bundle);
                    }
                });
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) return email;

        StringBuilder maskedEmail = new StringBuilder();
        maskedEmail.append(email.charAt(0)).append("***");
        maskedEmail.append(email.substring(atIndex - 1));

        return maskedEmail.toString();
    }

    private String maskPhone(String phone) {


        // Mask everything except the last 4 digits
        StringBuilder maskedPhone = new StringBuilder();
        maskedPhone.append(phone.substring(0, phone.length() - 4));
        maskedPhone.append("****");  // Masked section
        maskedPhone.append(phone.substring(phone.length() - 4));

        return maskedPhone.toString();
    }

    private void showLoading() {
        requireActivity().runOnUiThread(() -> binding.loadingContainer.setVisibility(View.VISIBLE));
    }

    private void hideLoading() {
        requireActivity().runOnUiThread(() -> binding.loadingContainer.setVisibility(View.GONE));
    }


    private void startRetryTimer() {
        binding.btnForgotPasswordContinue.setEnabled(false);

        // Countdown timer for 60 seconds
        new android.os.CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update button text to show the remaining seconds
                binding.btnForgotPasswordContinue.setText("Retry again in " + (millisUntilFinished / 1000) + "s");
            }

            public void onFinish() {
                // Re-enable the button and reset its text
                binding.btnForgotPasswordContinue.setEnabled(true);
                binding.btnForgotPasswordContinue.setText("Send Code");
            }
        }.start();
    }

}
