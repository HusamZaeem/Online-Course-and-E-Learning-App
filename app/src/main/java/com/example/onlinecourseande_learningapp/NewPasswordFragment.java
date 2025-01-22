package com.example.onlinecourseande_learningapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinecourseande_learningapp.databinding.FragmentNewPasswordBinding;
import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.PeriodicSyncWorker;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewPasswordFragment extends Fragment {

    private FragmentNewPasswordBinding binding;
    private FirebaseFirestore firestore;
    private AppRepository repository;
    private String email;
    private boolean isPasswordVisible = false;
    private AlertDialog dialog;

    public NewPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPasswordBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();
        repository = AppRepository.getInstance(requireActivity().getApplication());

        if (getArguments() != null) {
            email = getArguments().getString("email", "");
        }

        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Error: Missing email details.", Toast.LENGTH_SHORT).show();
            return binding.getRoot();
        }

        binding.btnResetPassword.setOnClickListener(v -> {
            String newPassword = binding.etPasswordNew1.getText().toString().trim();
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            String confirmPassword = binding.etPasswordNew2.getText().toString().trim();

            if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isValidPassword(newPassword)){

                resetPassword(hashedPassword);
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
            Drawable[] drawables = binding.etPasswordNew1.getCompoundDrawables();

            // Check and apply color filter to the start icon (index 0)
            if (drawables[0] != null) {
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }

            // Check and apply color filter to the end icon (index 2)
            if (drawables[2] != null) {
                drawables[2].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        });

        binding.etPasswordNew2.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? getResources().getColor(R.color.et_bg_active) : getResources().getColor(R.color.et_bg_default);
            Drawable[] drawables = binding.etPasswordNew2.getCompoundDrawables();

            // Check and apply color filter to the start icon (index 0)
            if (drawables[0] != null) {
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }

            // Check and apply color filter to the end icon (index 2)
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
        // Encrypt the new password before updating in local database
        try {
            KeystoreHelper keystoreHelper = new KeystoreHelper();
            keystoreHelper.generateKey();
            String encryptedPassword = keystoreHelper.encryptPassword(newPassword);

            // First update Firebase
            firestore.collection("Student")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String studentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            firestore.collection("Student").document(studentId)
                                    .update("password", newPassword) // Store the plain password in Firebase
                                    .addOnSuccessListener(unused -> {
                                        Log.d("ResetPassword", "Password updated successfully in Firebase.");


                                        repository.updateStudentPassword(email, encryptedPassword);

                                        // Show Dialog and Start Sync Worker
                                        enqueueSyncWorkAndShowDialog(newPassword);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ResetPassword", "Error updating password in Firebase: ", e);
                                        Toast.makeText(getContext(), "Error updating password in Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(getContext(), "No user found with the provided email.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Error fetching user: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Encryption error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void enqueueSyncWorkAndShowDialog(String newHashedPassword) {
        OneTimeWorkRequest syncWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSyncWorker.class).build();
        showCustomDialog();

        WorkManager.getInstance(requireContext()).enqueue(syncWorkRequest);
        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(syncWorkRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        new Handler().postDelayed(() -> {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                redirectToLogin();
                            }
                        }, 3000);
                    } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Sync failed. Password updated locally but may not be synced online.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), SignIn.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password_dialog, null);

        // Add rounded corner styling to the dialog
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