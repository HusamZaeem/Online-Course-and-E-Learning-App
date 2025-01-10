package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.onlinecourseande_learningapp.databinding.ActivitySignUpBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.PeriodicSyncWorker;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SignUp extends AppCompatActivity {


    private boolean isPasswordVisible = false;

    ActivitySignUpBinding binding;

    private AppViewModel appViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;  // Request code for Google sign-in


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


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


        binding.btnSignUpNewAccount.setOnClickListener(v -> {
            String email = binding.etEmailSignUp.getText().toString();
            String password = binding.etPasswordSignUp.getText().toString();
            String hashedPassword = PasswordHasher.hashPassword(password);

            if (isValidEmail(email) && isValidPassword(password)) {
                if (isNetworkAvailable()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        String uid = firebaseUser.getUid();
                                        saveUserToFirestore(uid, email, hashedPassword);
                                        syncData();
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    showErrorMessage("Sign-Up Failed: " + task.getException().getMessage());
                                }
                            });
                } else {
                    saveUserLocally(email, hashedPassword);
                    showErrorMessage("You are offline. User will be saved locally.");
                }
            } else {
                showErrorMessage("Invalid email or password");
            }

        });



        callbackManager = CallbackManager.Factory.create();

        // Setup Facebook Login button
        MaterialButton btnSignupFacebook = binding.btnSignupFacebookSignUp;

        btnSignupFacebook.setOnClickListener(v -> {
            // Trigger Facebook login
            LoginManager.getInstance().logInWithReadPermissions(SignUp.this,
                    Arrays.asList("email", "public_profile"));
        });

        // Handle the Facebook login result
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Facebook login success, now authenticate with Firebase
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignUp.this, "Facebook login canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignUp.this, "Facebook login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






        // Initialize Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google Sign-Up Button Click Listener
        binding.btnSignupGoogleSignUp.setOnClickListener(v -> {
            signInWithGoogle();
        });









        binding.btnSignInHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        });




    }


    // Handle Facebook Access Token and authenticate with Firebase
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User successfully logged in, create user in Firestore
                            String uid = user.getUid();
                            String email = user.getEmail();
                            saveUserToFirestore(uid, email);
                            saveUserLocally(uid,email);
                            syncData();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // Handle failure
                        Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Facebook login result
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Handle Google Sign-In result
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Toast.makeText(SignUp.this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String email = user.getEmail();
                            saveUserToFirestore(uid, email);
                            saveUserLocally(uid, email);
                            syncData();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void saveUserLocally(String email, String password) {
        String uid = UUID.randomUUID().toString(); // Generate a unique ID locally
        Student student = new Student(uid, email, password);
        student.setIs_synced(false);
        appViewModel.insertStudent(student); // Save to Room database
    }

    private void saveUserToFirestore(String uid, String email, String password) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("password", password);

        FirebaseFirestore.getInstance().collection("Student")
                .document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("SignUp", "User added to Firestore"))
                .addOnFailureListener(e -> Log.e("SignUp", "Error adding user to Firestore", e));
    }

    private void saveUserToFirestore(String uid, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);

        FirebaseFirestore.getInstance().collection("Student")
                .document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("SignUp", "User added to Firestore"))
                .addOnFailureListener(e -> Log.e("SignUp", "Error adding user to Firestore", e));
    }


    private void showErrorMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }




    private void syncData() {
        // Enqueue a one-time sync worker to sync data between Firestore and Room
        OneTimeWorkRequest syncWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSyncWorker.class)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED) // Sync only if there's internet
                        .build())
                .build();

        WorkManager.getInstance(getBaseContext()).enqueue(syncWorkRequest);
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