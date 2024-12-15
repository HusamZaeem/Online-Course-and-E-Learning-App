package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignUpBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;


public class SignUp extends AppCompatActivity {


    private boolean isPasswordVisible = false;

    ActivitySignUpBinding binding;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);




        // Add Eye Icon Click Listener
        binding.etPasswordSignUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2; // Index for drawableEnd
                if (event.getRawX() >= (binding.etPasswordSignUp.getRight() -
                        binding.etPasswordSignUp.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordSignUp);
                    return true;
                }
            }
            return false;
        });

        // Change colors on focus
        binding.etPasswordSignUp.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Change icon color
            binding.etPasswordSignUp.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Lock icon
            binding.etPasswordSignUp.getCompoundDrawables()[2].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Eye icon
        });


        binding.etEmailSignUp.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Update email icon color
            binding.etEmailSignUp.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        });












        binding.btnSignUpNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String email = binding.etEmailSignUp.getText().toString();
               String password = binding.etPasswordSignUp.getText().toString();

                if (isValidEmail(email) && isValidPassword(password)){

                    appViewModel.insertStudent(new Student(email,password));
                    Toast.makeText(getBaseContext(),"Registered Successfully",Toast.LENGTH_LONG).show();

                }



            }
        });












        binding.btnSignInHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });



    }







    private void togglePasswordVisibility(EditText passwordEditText) {
        int focusColor = getResources().getColor(R.color.et_bg_active);
        int defaultColor = getResources().getColor(R.color.et_bg_default);

        // Check if EditText is focused to determine the correct color
        int iconColor = passwordEditText.hasFocus() ? focusColor : defaultColor;

        if (isPasswordVisible) {
            // Hide Password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,  // Lock icon remains unchanged
                    0,
                    R.drawable.ic_eye_closed,   // Change eye icon to closed
                    0
            );
        } else {
            // Show Password
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,  // Lock icon remains unchanged
                    0,
                    R.drawable.ic_eye_open,    // Change eye icon to open
                    0
            );
        }

        // Apply consistent color to both icons
        passwordEditText.getCompoundDrawables()[0].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN); // Lock icon
        passwordEditText.getCompoundDrawables()[2].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN); // Eye icon

        passwordEditText.setSelection(passwordEditText.length()); // Move cursor to end
        isPasswordVisible = !isPasswordVisible;
    }




    private boolean isValidEmail(String email) {


        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            return true;
        }else {

            Toast.makeText(this,"Please enter a valid email address.",Toast.LENGTH_LONG).show();
            return false;
        }


    }


    private boolean isValidPassword(String password) {


        if (password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&    // At least one uppercase letter
                password.matches(".*[a-z].*") &&    // At least one lowercase letter
                password.matches(".*\\d.*") &&      // At least one digit
                password.matches(".*[@#$%^&+=!].*")){

            return true;
        }else {
            Toast.makeText(this,"Password must be at least 8 characters, include uppercase, lowercase, a digit, and a special character.",Toast.LENGTH_LONG).show();
            return false;
        }


    }



}