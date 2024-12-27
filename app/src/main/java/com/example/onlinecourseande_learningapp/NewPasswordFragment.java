package com.example.onlinecourseande_learningapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentNewPasswordBinding;
import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewPasswordFragment extends Fragment {

    private FragmentNewPasswordBinding binding;
    private FirebaseFirestore firestore;
    private AppRepository repository;
    private String email;
    private boolean isPasswordVisible = false;

    public NewPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPasswordBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();
        repository = AppRepository.getInstance(requireActivity().getApplication());

        email = getArguments().getString("email");

        binding.btnResetPassword.setOnClickListener(v -> {
            String newPassword = binding.etPasswordNew1.getText().toString().trim();
            String confirmPassword = binding.etPasswordNew2.getText().toString().trim();

            if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isValidPassword(newPassword)){
                String newHashedPassword = PasswordHasher.hashPassword(newPassword);
                resetPassword(newHashedPassword);
            }

        });

        // Add Eye Icon Click Listener
        binding.etPasswordNew1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2; // Index for drawableEnd
                if (event.getRawX() >= (binding.etPasswordNew1.getRight() -
                        binding.etPasswordNew1.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordNew1);
                    return true;
                }
            }
            return false;
        });

        binding.etPasswordNew2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndIndex = 2; // Index for drawableEnd
                if (event.getRawX() >= (binding.etPasswordNew2.getRight() -
                        binding.etPasswordNew2.getCompoundDrawables()[drawableEndIndex].getBounds().width())) {
                    togglePasswordVisibility(binding.etPasswordNew2);
                    return true;
                }
            }
            return false;
        });

        binding.etPasswordNew1.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Change icon color
            binding.etPasswordNew1.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Lock icon
            binding.etPasswordNew1.getCompoundDrawables()[2].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Eye icon
        });

        binding.etPasswordNew2.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            // Change icon color
            binding.etPasswordNew2.getCompoundDrawables()[0].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Lock icon
            binding.etPasswordNew2.getCompoundDrawables()[2].setColorFilter(color, PorterDuff.Mode.SRC_IN); // Eye icon
        });






        return binding.getRoot();
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





    private boolean isValidPassword(String password) {


        if (password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&    // At least one uppercase letter
                password.matches(".*[a-z].*") &&    // At least one lowercase letter
                password.matches(".*\\d.*") &&      // At least one digit
                password.matches(".*[@#$%^&+=!].*")){

            return true;
        }else {
            Toast.makeText(getContext(),"Password must be at least 8 characters, include uppercase, lowercase, a digit, and a special character.",Toast.LENGTH_LONG).show();
            return false;
        }


    }

    private void resetPassword(String newPassword) {
        // Update password in Firebase
        firestore.collection("Student")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String studentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        firestore.collection("Student").document(studentId)
                                .update("password", newPassword)
                                .addOnSuccessListener(unused -> {
                                    // Update Room database
                                    repository.updateStudentPassword(email, newPassword);

                                    // Sync changes
                                    enqueueSyncWorkAndShowDialog();
                                    Toast.makeText(getContext(), "Password reset successful", Toast.LENGTH_SHORT).show();


                                    startActivity(new Intent(getContext(), SignIn.class));
                                    getActivity().finish();

                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void enqueueSyncWorkAndShowDialog() {
        // Create WorkRequest
        OneTimeWorkRequest syncWorkRequest = new OneTimeWorkRequest.Builder(SyncWorker.class).build();

        // Show Custom AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Record the start time of the dialog
        long startTime = System.currentTimeMillis();

        // Observe WorkManager State
        WorkManager.getInstance(requireContext()).enqueue(syncWorkRequest);
        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(syncWorkRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        // Calculate elapsed time
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        long delay = Math.max(0, 3000 - elapsedTime); // Ensure at least 3 seconds

                        // Delay dismissal and redirection
                        dialogView.postDelayed(() -> {
                            dialog.dismiss(); // Dismiss the loading dialog
                            startActivity(new Intent(getContext(), SignIn.class)); // Navigate to SignIn
                        }, delay);
                    } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Sync failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}