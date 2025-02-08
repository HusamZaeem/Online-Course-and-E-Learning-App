package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.onlinecourseande_learningapp.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Get intent data
        Intent intent = getIntent();
        boolean fromProfile = intent.getBooleanExtra("fromProfile", false);
        String email = intent.getStringExtra("email");

        if (fromProfile) {
            // Pass data to the NewPasswordFragment
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromProfile", true);
            bundle.putString("email", email);

            // Navigate directly to NewPasswordFragment
            navController.navigate(R.id.NewPasswordFragment, bundle);
        }






    }
}