package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignInBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    private ActivitySignInBinding binding;

    private AppViewModel appViewModel;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();


        // Add Eye Icon Click Listener
        binding.etPasswordSignIn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2; // Index for drawableEnd
                if (event.getRawX() >= (binding.etPasswordSignIn.getRight() -
                        binding.etPasswordSignIn.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordSignIn);
                    return true;
                }
            }
            return false;
        });

        // Change colors on focus
        binding.etPasswordSignIn.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Change icon color
            binding.etPasswordSignIn.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Lock icon
            binding.etPasswordSignIn.getCompoundDrawables()[2].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Eye icon
        });


        binding.etEmailSignIn.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Update email icon color
            binding.etEmailSignIn.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        });





        binding.btnSignUpNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        });







        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.etEmailSignIn.getText().toString();
                String password = binding.etPasswordSignIn.getText().toString();

                if (isValidEmail(email) && isValidPassword(password)) {
                    if (isNetworkAvailable()) {
                        // User is online, sign in normally with Firebase
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        showErrorMessage("Sign-In Failed: " + task.getException().getMessage());
                                    }
                                });
                    } else {
                        // User is offline, observe the LiveData for student
                        appViewModel.getStudentByEmail(email).observe(SignIn.this, student -> {
                            if (student != null && PasswordHasher.verifyPassword(password, student.getPassword())) {
                                // User logged in successfully with local data
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                finish();
                            } else {
                                showErrorMessage("No matching user found offline.");
                            }
                        });
                    }
                } else {
                    showErrorMessage("Invalid email or password");
                }

            }
        });








    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }




    private void togglePasswordVisibility(EditText passwordEditText) {
        int focusColor = getResources().getColor(R.color.et_bg_active);
        int defaultColor = getResources().getColor(R.color.et_bg_default);


        int iconColor = passwordEditText.hasFocus() ? focusColor : defaultColor;

        if (isPasswordVisible) {
            // Hide Password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,
                    0,
                    R.drawable.ic_eye_closed,   // Change eye icon to closed
                    0
            );
        } else {
            // Show Password
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,
                    0,
                    R.drawable.ic_eye_open,    // Change eye icon to open
                    0
            );
        }

        // Apply consistent color to both icons
        passwordEditText.getCompoundDrawables()[0].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        passwordEditText.getCompoundDrawables()[2].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);

        passwordEditText.setSelection(passwordEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }
}