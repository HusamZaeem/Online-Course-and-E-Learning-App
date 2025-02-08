package com.example.onlinecourseande_learningapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentNewPasswordBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.PeriodicSyncWorker;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class NewPasswordFragment extends Fragment {

    private FragmentNewPasswordBinding binding;
    private AppViewModel appViewModel;
    private String email;
    private boolean isPasswordVisible = false;
    private AlertDialog dialog;
    // Flag to ensure the reset logic runs only once.
    private boolean passwordResetDone = false;
    private static final String TAG = "NewPasswordFragment";

    public NewPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPasswordBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Retrieve email from arguments.
        if (getArguments() != null) {
            email = getArguments().getString("email", "");
        }
        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Error: Missing email details.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return binding.getRoot();
        }

        binding.btnResetPassword.setOnClickListener(v -> {
            String newPassword = binding.etPasswordNew1.getText().toString().trim();
            String confirmPassword = binding.etPasswordNew2.getText().toString().trim();

            if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isValidPassword(newPassword)) {
                resetPassword(newPassword);
            }
        });

        // Add Eye Icon Click Listener for the first password field.
        binding.etPasswordNew1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2; // assuming the eye icon is at index 2
                if (event.getRawX() >= (binding.etPasswordNew1.getRight() -
                        binding.etPasswordNew1.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordNew1);
                    return true;
                }
            }
            return false;
        });

        // Add Eye Icon Click Listener for the second password field.
        binding.etPasswordNew2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2;
                if (event.getRawX() >= (binding.etPasswordNew2.getRight() -
                        binding.etPasswordNew2.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordNew2);
                    return true;
                }
            }
            return false;
        });

        // Set focus change listeners for the password fields to update icon colors.
        binding.etPasswordNew1.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            Drawable[] drawables = binding.etPasswordNew1.getCompoundDrawables();
            if (drawables[0] != null) {
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            if (drawables[2] != null) {
                drawables[2].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        });

        binding.etPasswordNew2.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            Drawable[] drawables = binding.etPasswordNew2.getCompoundDrawables();
            if (drawables[0] != null) {
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            if (drawables[2] != null) {
                drawables[2].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        });

        return binding.getRoot();
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        int focusColor = getResources().getColor(R.color.et_bg_active);
        int defaultColor = getResources().getColor(R.color.et_bg_default);
        int iconColor = passwordEditText.hasFocus() ? focusColor : defaultColor;

        if (isPasswordVisible) {
            // Hide password.
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,
                    0,
                    R.drawable.ic_eye_closed,
                    0
            );
        } else {
            // Show password.
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_lock_24,
                    0,
                    R.drawable.ic_eye_open,
                    0
            );
        }

        passwordEditText.getCompoundDrawables()[0].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        passwordEditText.getCompoundDrawables()[2].setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        passwordEditText.setSelection(passwordEditText.length());
        // Toggle the flag.
        isPasswordVisible = !isPasswordVisible;
    }

    private boolean isValidPassword(String password) {
        if (password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&    // at least one uppercase letter
                password.matches(".*[a-z].*") &&    // at least one lowercase letter
                password.matches(".*\\d.*") &&      // at least one digit
                password.matches(".*[@#$%^&+=!].*")) { // at least one special character
            return true;
        } else {
            Toast.makeText(getContext(),
                    "Password must be at least 8 characters, include uppercase, lowercase, a digit, and a special character.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void resetPassword(final String newPassword) {
        // Ensure this method runs only once.
        if (passwordResetDone) {
            Log.d(TAG, "resetPassword already executed; skipping.");
            return;
        }
        try {
            // Encrypt the new password.
            KeystoreHelper keystoreHelper = new KeystoreHelper();
            keystoreHelper.generateKey(); // generateKey() checks if the key already exists.
            final String encryptedPassword = keystoreHelper.encryptPassword(newPassword);
            Log.d(TAG, "Encrypted password: " + encryptedPassword);

            // Create a one-shot observer for the local database update.
            final Observer<Student> studentObserver = new Observer<Student>() {
                @Override
                public void onChanged(Student student) {
                    if (student != null && !passwordResetDone) {
                        passwordResetDone = true;  // Prevent repeated execution.
                        // Update the student record locally.
                        student.setPassword(encryptedPassword);
                        student.setIs_synced(false);
                        student.setLast_updated(new Date());
                        appViewModel.updateStudent(student);
                        Log.d(TAG, "Local update rows updated: ");
                        // Remove the observer.
                        appViewModel.getStudentByEmail(email).removeObserver(this);

                        // Now update Firebase Auth password.
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            firebaseUser.updatePassword(newPassword)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Firebase Auth password updated successfully.");
                                            // Now show success toast and enqueue sync.
                                            Toast.makeText(getContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                                            enqueueSyncWorkAndShowDialog(encryptedPassword);
                                        } else {
                                            Log.e(TAG, "Firebase Auth password update failed.", task.getException());
                                            Toast.makeText(getContext(), "Failed to update password on server.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.e(TAG, "No Firebase user is signed in.");
                            Toast.makeText(getContext(), "No Firebase user signed in.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
            // Observe the student LiveData (one-shot observer).
            appViewModel.getStudentByEmail(email).observe(getViewLifecycleOwner(), studentObserver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Encryption error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enqueueSyncWorkAndShowDialog(String newEncryptedPassword) {
        // Build and enqueue the sync work request.
        OneTimeWorkRequest syncWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSyncWorker.class).build();
        showCustomDialog();
        WorkManager.getInstance(requireContext()).enqueue(syncWorkRequest);

        // Create a timeout handler (e.g., 10 seconds).
        Handler timeoutHandler = new Handler();
        Runnable timeoutRunnable = () -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Sync operation timed out. Please try again later.", Toast.LENGTH_SHORT).show();
            }
            WorkManager.getInstance(requireContext()).cancelWorkById(syncWorkRequest.getId());
        };
        timeoutHandler.postDelayed(timeoutRunnable, 10000);

        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(syncWorkRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null) {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            timeoutHandler.removeCallbacks(timeoutRunnable);
                            new Handler().postDelayed(() -> {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                    redirectToLogin();
                                }
                            }, 3000);
                        } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                            timeoutHandler.removeCallbacks(timeoutRunnable);
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(getContext(), "Sync failed. Password updated locally but may not be synced online.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), SignIn.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password_dialog, null);
        // Apply rounded corner styling to the dialog.
        dialogView.setBackgroundResource(R.drawable.dialog_rounded_corners);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroyView();
    }
}
