package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignupLoginBinding;

public class SignupLogin extends AppCompatActivity {


    ActivitySignupLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_signup_login);



        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // smooth transition


            }
        });







    }
}