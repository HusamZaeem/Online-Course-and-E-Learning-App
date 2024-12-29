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

        if (getArguments() != null) {
            email = getArguments().getString("email", "");
            maskedEmail = getArguments().getString("maskedEmail", "");
            currentOtp = getArguments().getString("otp", "");
        }

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

        binding.tvResendCode.setText("Sending OTP...");
        binding.tvResendCode.setEnabled(false);

        new Thread(() -> {
            try {
                String url = "https://online-course-and-e-learning-app.onrender.com/send-otp"; 
                OkHttpClient client = new OkHttpClient();

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email);
                jsonBody.put("otp", currentOtp);

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
                            Toast.makeText(getContext(), "OTP resent successfully", Toast.LENGTH_SHORT).show();
                            startCountdownTimer();
                        });
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Failed to resend OTP: " + errorBody, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (IOException | JSONException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to resend OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
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