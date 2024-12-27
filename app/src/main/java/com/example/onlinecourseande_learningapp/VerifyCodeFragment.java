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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentVerifyCodeBinding;

import java.io.IOException;
import java.util.Random;


public class VerifyCodeFragment extends Fragment {

    FragmentVerifyCodeBinding binding;
    private CountDownTimer countDownTimer;
    private boolean isResendEnabled = false;
    private String email;
    private String maskedEmail;
    private String currentOtp;

    public VerifyCodeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentVerifyCodeBinding.inflate(inflater,container,false);

        email = getArguments().getString("email");
        maskedEmail = getArguments().getString("maskedEmail");
        currentOtp = getArguments().getString("otp");

        binding.tvForgotPasswordVerify.setText("Code has been sent to " + maskedEmail);

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
        binding.codeDigit4.addTextChangedListener(new OtpTextWatcher(binding.codeDigit4, null));
    }

    private void startCountdownTimer() {
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
                binding.tvResendCode.setTypeface(null, Typeface.BOLD);
                binding.tvResendCode.setEnabled(true);
                isResendEnabled = true;
            }
        }.start();
    }

    private String getOtpFromFields() {
        return binding.codeDigit1.getText().toString().trim()
                + binding.codeDigit2.getText().toString().trim()
                + binding.codeDigit3.getText().toString().trim()
                + binding.codeDigit4.getText().toString().trim();
    }

    private void verifyOtp(String enteredCode) {
        if (enteredCode.equals(currentOtp)) {
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_fragmentNewPassword, bundle);
        } else {
            Toast.makeText(getContext(), "Incorrect code", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOtp() {
        currentOtp = String.format("%04d", new Random().nextInt(10000));

        new Thread(() -> {
            try {
                // Resend OTP email using SparkPost
                SparkPostSender.sendEmail(
                        email,
                        "Password Reset OTP - Resend",
                        "Dear User,\n\nYour new OTP for password reset is: " + currentOtp + "\n\nPlease do not share this code with anyone.\n\nBest Regards,\nOnline Course & E-Learning App"
                );

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "OTP resent successfully", Toast.LENGTH_SHORT).show();
                    startCountdownTimer();
                });
            } catch (IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to resend OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}