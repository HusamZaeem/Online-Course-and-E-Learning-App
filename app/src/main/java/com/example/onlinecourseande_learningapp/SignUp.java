package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignUpBinding;





public class SignUp extends AppCompatActivity {


    private boolean isPasswordVisible = false;

    ActivitySignUpBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        EditText passwordEditText = findViewById(R.id.etPassword);

        // Add Eye Icon Click Listener
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEnd = 2; // Right drawable
                if (event.getRawX() >= (passwordEditText.getRight() -
                        passwordEditText.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    togglePasswordVisibility(passwordEditText);
                    return true;
                }
            }
            return false;
        });

        // Change colors on focus
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? R.color.et_bg_active : R.color.et_bg_default;
            passwordEditText.getBackground().setColorFilter(
                    getResources().getColor(color), PorterDuff.Mode.SRC_IN);
        });













    }


    private void togglePasswordVisibility(EditText passwordEditText) {
        if (isPasswordVisible) {
            // Hide Password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_eye_open, 0, R.drawable.ic_eye_closed, 0);
        } else {
            // Show Password
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_eye_closed, 0, R.drawable.ic_eye_open, 0);
        }
        passwordEditText.setSelection(passwordEditText.length()); // Move cursor to end
        isPasswordVisible = !isPasswordVisible;
    }
}