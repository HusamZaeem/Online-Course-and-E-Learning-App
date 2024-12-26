package com.example.onlinecourseande_learningapp;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentVerifyCodeBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;


public class VerifyCodeFragment extends Fragment {

    FragmentVerifyCodeBinding binding;
    private FirebaseFirestore firestore;
    private CountDownTimer countDownTimer;
    private boolean isResendEnabled = false;
    private String email;
    private String maskedEmail;

    public VerifyCodeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentVerifyCodeBinding.inflate(inflater,container,false);
        firestore = FirebaseFirestore.getInstance();

        email = getArguments().getString("email");
        maskedEmail = getArguments().getString("maskedEmail");

        binding.tvForgotPasswordVerify.setText("Code has been sent to " + maskedEmail);

        binding.btnVerify.setOnClickListener(v -> {
            String enteredCode = getOtpFromFields();
            if (enteredCode.isEmpty()) {
                Toast.makeText(getContext(), "Enter the code", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(enteredCode);
        });

        binding.tvResendCode.setOnClickListener(v -> resendOtp());

        return binding.getRoot();
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOtpEditTexts();
        startCountdownTimer();

        // Resend code on click
        binding.tvResendCode.setOnClickListener(v -> {
            if (isResendEnabled) {
                resendOtp();
            }
        });

        // Handle Verify Button click
        binding.btnVerify.setOnClickListener(v -> {
            String otp = getOtpFromFields();
            if (otp.length() == 4) {
                // Validate the OTP
                verifyOtp(otp);
            } else {
                Toast.makeText(requireContext(), "Please enter the complete OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupOtpEditTexts() {
        // Setup each EditText to move focus to the next one
        binding.codeDigit1.addTextChangedListener(new OtpTextWatcher(binding.codeDigit1, binding.codeDigit2));
        binding.codeDigit2.addTextChangedListener(new OtpTextWatcher(binding.codeDigit2, binding.codeDigit3));
        binding.codeDigit3.addTextChangedListener(new OtpTextWatcher(binding.codeDigit3, binding.codeDigit4));
        binding.codeDigit4.addTextChangedListener(new OtpTextWatcher(binding.codeDigit4, null));
    }

    private void startCountdownTimer() {
        binding.tvResendCode.setEnabled(false);
        isResendEnabled = false;

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = String.valueOf(millisUntilFinished / 1000) + "s";
                String text = "Resend code in " + remainingTime;

                SpannableString spannableString = new SpannableString(text);
                int startIndex = text.indexOf(remainingTime); // Find where the number starts
                int endIndex = startIndex + remainingTime.length();

                // Make the number blue
                spannableString.setSpan(
                        new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.btn_color)),
                        startIndex, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                binding.tvResendCode.setText(spannableString);
            }

            @Override
            public void onFinish() {
                binding.tvResendCode.setText("Resend code");
                binding.tvResendCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.btn_color));
                binding.tvResendCode.setTypeface(null, Typeface.BOLD); // Bold text
                binding.tvResendCode.setEnabled(true);
                isResendEnabled = true;
            }
        }.start();
    }

    private String getOtpFromFields() {
        // Concatenate all the OTP digits
        return binding.codeDigit1.getText().toString().trim()
                + binding.codeDigit2.getText().toString().trim()
                + binding.codeDigit3.getText().toString().trim()
                + binding.codeDigit4.getText().toString().trim();
    }

    private void verifyOtp(String enteredCode) {
        firestore.collection("OtpVerification").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String correctOtp = documentSnapshot.getString("otp");
                    if (enteredCode.equals(correctOtp)) {
                        // OTP verified, navigate to FragmentNewPassword
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentNewPassword, bundle);
                    } else {
                        Toast.makeText(getContext(), "Incorrect code", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void resendOtp() {
        // Regenerate and resend OTP
        String otp = String.valueOf((int) (Math.random() * 9000) + 1000);
        firestore.collection("OtpVerification").document(email)
                .set(Collections.singletonMap("otp", otp))
                .addOnSuccessListener(unused -> Toast.makeText(getContext(), "OTP resent", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to resend OTP", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}