package com.example.onlinecourseande_learningapp;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentVerifyCodeBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class VerifyCodeFragment extends Fragment {

    FragmentVerifyCodeBinding binding;
    private CountDownTimer countDownTimer;
    private boolean isResendEnabled = false;
    private String email;
    private String phone;
    private String maskedEmail;
    private String maskedPhone;
    private String currentOtp;

    public VerifyCodeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentVerifyCodeBinding.inflate(inflater,container,false);

        if (getArguments() != null) {
            email = getArguments().getString("email", "");
            phone = getArguments().getString("phone", "");
            maskedEmail = getArguments().getString("maskedEmail", "");
            maskedPhone = getArguments().getString("maskedPhone", "");
            currentOtp = getArguments().getString("otp", "");
        }

        if (email != null && !email.isEmpty()) {
            binding.tvForgotPasswordVerify.setText("Code has been sent to " + maskedEmail);
        } else if (phone != null && !phone.isEmpty()) {
            binding.tvForgotPasswordVerify.setText("Code has been sent to " + maskedPhone);
        }

        setupOtpEditTexts();
        startCountdownTimer();

        binding.btnVerify.setOnClickListener(v -> {
            String enteredCode = getOtpFromFields();
            if (enteredCode.isEmpty()) {
                Toast.makeText(getContext(), "Enter the code", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(enteredCode);
        });

        binding.tvResendCode.setOnClickListener(v -> {
            if (isResendEnabled) {
                resendOtp();
            }
        });

        return binding.getRoot();
    }

    private void setupOtpEditTexts() {
        binding.codeDigit1.addTextChangedListener(new OtpTextWatcher(binding.codeDigit1, binding.codeDigit2));
        binding.codeDigit2.addTextChangedListener(new OtpTextWatcher(binding.codeDigit2, binding.codeDigit3));
        binding.codeDigit3.addTextChangedListener(new OtpTextWatcher(binding.codeDigit3, binding.codeDigit4));
        binding.codeDigit4.addTextChangedListener(new OtpTextWatcher(binding.codeDigit4, binding.codeDigit5));
        binding.codeDigit5.addTextChangedListener(new OtpTextWatcher(binding.codeDigit5, binding.codeDigit6));
        binding.codeDigit6.addTextChangedListener(new OtpTextWatcher(binding.codeDigit6, null));
    }

    private void startCountdownTimer() {
        if (binding == null) {
            Log.e("startCountdownTimer", "Binding is null. Timer not started.");
            return;
        }

        binding.tvResendCode.setEnabled(false);
        isResendEnabled = false;

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = millisUntilFinished / 1000 + "s";
                String text = "Resend code in " + remainingTime;

                SpannableString spannableString = new SpannableString(text);
                int startIndex = text.indexOf(remainingTime);
                int endIndex = startIndex + remainingTime.length();

                // Check for null context to prevent crashes
                if (getContext() != null) {
                    spannableString.setSpan(
                            new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.btn_color)),
                            startIndex, endIndex,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                binding.tvResendCode.setText(spannableString);
            }

            @Override
            public void onFinish() {
                if (binding != null) {
                    binding.tvResendCode.setText("Resend code");
                    if (getContext() != null) {
                        binding.tvResendCode.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_color));
                    }
                    binding.tvResendCode.setTypeface(null, Typeface.BOLD);
                    binding.tvResendCode.setEnabled(true);
                    isResendEnabled = true;
                }
            }
        }.start();
    }

    private String getOtpFromFields() {
        return binding.codeDigit1.getText().toString().trim()
                + binding.codeDigit2.getText().toString().trim()
                + binding.codeDigit3.getText().toString().trim()
                + binding.codeDigit4.getText().toString().trim()
                + binding.codeDigit5.getText().toString().trim()
                + binding.codeDigit6.getText().toString().trim();
    }

    private void verifyOtp(String enteredCode) {
        if (enteredCode.equals(currentOtp)) {
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("phone", phone);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentNewPassword, bundle);
        } else {
            Toast.makeText(getContext(), "Incorrect code", Toast.LENGTH_SHORT).show();
        }
    }



    private void resendOtp() {
        currentOtp = String.format("%06d", new Random().nextInt(1000000));

        binding.tvResendCode.setText("Sending OTP...");
        binding.tvResendCode.setEnabled(false);

        if (email != null && !email.isEmpty()) {
            sendOtpToEmail(email);
        } else if (phone != null && !phone.isEmpty()) {
            sendOtpToPhone(phone);
        }

        if (email != null && !email.isEmpty()) {
            binding.tvForgotPasswordVerify.setText("A new code has been sent to " + maskedEmail);
        } else if (phone != null && !phone.isEmpty()) {
            binding.tvForgotPasswordVerify.setText("A new code has been sent to " + maskedPhone);
        }
    }

    private void sendOtpToEmail(String email) {
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
                    if (response.isSuccessful()) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "New OTP sent to "+maskedEmail, Toast.LENGTH_SHORT).show();
                            currentOtp = otp;
                        });
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Failed to send OTP: " + errorBody, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (IOException | JSONException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    // Function to send OTP to phone number via SMS using Firebase Phone Auth
    private void sendOtpToPhone(String phone) {
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
                        Toast.makeText(getContext(), "OTP sending failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Store the verificationId to verify the code later
                        Toast.makeText(getContext(), "New OTP sent to "+maskedPhone, Toast.LENGTH_SHORT).show();
                        currentOtp = verificationId;
                    }
                });
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }
}