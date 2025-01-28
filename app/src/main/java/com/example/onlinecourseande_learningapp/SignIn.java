package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignInBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    private ActivitySignInBinding binding;

    private AppViewModel appViewModel;
    private FirebaseAuth firebaseAuth;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();



        checkRememberedUser();


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


        binding.tvForgotPassSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));

            }
        });


        binding.btnSignUpNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        });







        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.etEmailSignIn.getText().toString();
            String password = binding.etPasswordSignIn.getText().toString();

            if (isValidEmail(email) && isValidPassword(password)) {
                if (isNetworkAvailable()) {
                    // User is online, sign in with Firebase
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = user != null ? user.getUid() : null;
                                    if (userId != null) {
                                        Log.d("SignIn", "Student ID set: " + userId);
                                        saveStudentIdToSharedPreferences(userId);
                                        saveCredentials(email, password);
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                        finish();
                                    }else {
                                        showErrorMessage("Failed to retrieve student ID.");
                                    }
                                } else {
                                    showErrorMessage("Sign-In Failed: " + task.getException().getMessage());
                                }
                            });
                } else {
                    // User is offline, observe the LiveData for student
                    appViewModel.getStudentByEmail(email).observe(SignIn.this, student -> {
                        if (student != null) {
                            // Decrypt password using KeystoreHelper instance
                            try {
                                KeystoreHelper keystoreHelper = new KeystoreHelper();  // Create instance
                                String decryptedPassword = keystoreHelper.decryptPassword(student.getPassword());
                                if (PasswordHasher.verifyPassword(password, decryptedPassword)) {
                                    // User logged in successfully with local data
                                    saveStudentIdToSharedPreferences(student.getStudent_id());
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                } else {
                                    showErrorMessage("No matching user found offline.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                showErrorMessage("Failed to decrypt password.");
                            }
                        } else {
                            showErrorMessage("No matching user found offline.");
                        }
                    });
                }
            } else {
                showErrorMessage("Invalid email or password");
            }
        });










    }

    private void checkRememberedUser() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedEmail = prefs.getString(KEY_EMAIL, null);

        if (savedEmail != null) {
            binding.etEmailSignIn.setText(savedEmail);

            // Retrieve encrypted password from SharedPreferences
            String encryptedPassword = prefs.getString(KEY_PASSWORD, null);
            if (encryptedPassword != null) {
                try {
                    KeystoreHelper keystoreHelper = new KeystoreHelper();
                    String decryptedPassword = keystoreHelper.decryptPassword(encryptedPassword);  // Decrypt password
                    binding.etPasswordSignIn.setText(decryptedPassword);

                    // Attempt login using the decrypted password
                    firebaseAuth.signInWithEmailAndPassword(savedEmail, decryptedPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    saveStudentIdToSharedPreferences(firebaseAuth.getCurrentUser().getUid());
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }
                            });
                } catch (Exception e) {
                    showErrorMessage("Failed to decrypt password.");
                }
            }
        }
    }


    private void saveCredentials(String email, String password) {
        if (binding.cbRememberMeSignIn.isChecked()) {
            try {
                KeystoreHelper keystoreHelper = new KeystoreHelper();
                String encryptedPassword = keystoreHelper.encryptPassword(password);  // Encrypt password before saving

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_PASSWORD, encryptedPassword);
                editor.apply();
            } catch (Exception e) {
                showErrorMessage("Error saving credentials.");
            }
        }
    }



    private void saveStudentIdToSharedPreferences(String studentId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("student_id", studentId);
        editor.apply();
        Log.d("SignIn", "Student ID saved to SharedPreferences: " + studentId);
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