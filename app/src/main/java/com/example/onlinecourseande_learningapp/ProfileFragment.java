package com.example.onlinecourseande_learningapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.onlinecourseande_learningapp.databinding.FragmentProfileBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AppViewModel appViewModel;
    private Student currentStudent;
    private boolean isEditing = false;

    // Launchers for gallery and camera.
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Retrieve student id from SharedPreferences.
        String studentId = getStudentIdFromSharedPreferences(requireContext());
        if (studentId == null) {
            Log.e("ProfileFragment", "Student id not found in SharedPreferences!");
            return;
        }

        // Observe student data from Room database.
        appViewModel.getStudentByIdLive(studentId).observe(getViewLifecycleOwner(), student -> {
            if (student != null) {
                currentStudent = student;
                populateStudentData(student);
            }
        });

        // Disable all fields by default.
        setFieldsEnabled(false);

        // Toggle edit mode when "Edit Personal Information" button is clicked.
        binding.btnEditSave.setOnClickListener(v -> {
            if (!isEditing) {
                // Enable fields for editing.
                setFieldsEnabled(true);
                binding.btnEditSave.setText("Save");
                isEditing = true;
            } else {
                // Save changes and disable fields.
                if (currentStudent != null) {
                    currentStudent.setFirst_name(binding.etFirstName.getText().toString().trim());
                    currentStudent.setLast_name(binding.etLastName.getText().toString().trim());
                    currentStudent.setPhone(binding.etPhone.getText().toString().trim());

                    // Parse and set Date of Birth.
                    String dobString = binding.etDob.getText().toString().trim();
                    if (!dobString.isEmpty()) {
                        try {
                            Date dob = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dobString);
                            currentStudent.setDate_of_birth(dob);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // Mark as unsynced and update last_updated.
                    currentStudent.setIs_synced(false);
                    currentStudent.setLast_updated(new Date());
                    appViewModel.updateStudent(currentStudent);
                }
                setFieldsEnabled(false);
                binding.btnEditSave.setText("Edit Personal Information");
                isEditing = false;
            }
        });

        // Set Date of Birth field to open DatePickerDialog when clicked (only in edit mode).
        binding.etDob.setOnClickListener(v -> {
            if (isEditing) {
                showDatePickerDialog();
            }
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            // Prepare the intent to launch the ForgotPasswordActivity.
            Intent intent = new Intent(requireContext(), ForgotPasswordActivity.class);
            // Pass along any required data. For example, the student's email.
            String email = currentStudent != null ? currentStudent.getEmail() : "";
            intent.putExtra("email", email);
            // Flag to indicate that the reset was initiated from Profile.
            intent.putExtra("fromProfile", true);
            startActivity(intent);
        });



        // Logout button: clear only the student session (remove student_id) while preserving email/password if "remember me" is checked.
        binding.btnLogout.setOnClickListener(v -> {
            clearStudentSession(requireContext());
            Intent intent = new Intent(requireContext(), SignIn.class);
            // Clear the back stack.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Photo selection: allow updating the profile photo.
        binding.ivEditPhoto.setOnClickListener(v -> showPhotoOptionsDialog());
        initActivityResultLaunchers();
    }

    // Populate the UI fields with the student data.
    private void populateStudentData(Student student) {
        binding.etFirstName.setText(student.getFirst_name());
        binding.etLastName.setText(student.getLast_name());
        binding.etPhone.setText(student.getPhone());

        if (student.getDate_of_birth() != null) {
            String dobStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(student.getDate_of_birth());
            binding.etDob.setText(dobStr);
        } else {
            binding.etDob.setText("");
        }

        if (student.getProfile_photo() != null && !student.getProfile_photo().isEmpty()) {
            ImageLoaderUtil.loadImageFromFirebaseStorage(requireContext(), student.getProfile_photo(), binding.ivProfileStudentPhoto);
        } else {
            binding.ivProfileStudentPhoto.setImageResource(R.drawable.head_icon);
        }
    }

    // Enable or disable editing on fields.
    private void setFieldsEnabled(boolean enabled) {
        binding.etFirstName.setEnabled(enabled);
        binding.etLastName.setEnabled(enabled);
        binding.etPhone.setEnabled(enabled);
        binding.etDob.setEnabled(enabled);  // Only editable in edit mode.
    }

    // Display a DatePickerDialog for selecting Date of Birth.
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        if (currentStudent != null && currentStudent.getDate_of_birth() != null) {
            calendar.setTime(currentStudent.getDate_of_birth());
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String dobStr = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    binding.etDob.setText(dobStr);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Show a dialog to choose between Camera and Gallery for selecting a profile photo.
    private void showPhotoOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Photo")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                            cameraLauncher.launch(takePictureIntent);
                        }
                    } else {
                        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryLauncher.launch(pickPhotoIntent);
                    }
                });
        builder.create().show();
    }

    // Initialize ActivityResultLaunchers for Camera and Gallery.
    private void initActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            uploadImageToFirebase(imageUri);
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            Uri imageUri = getImageUriFromBitmap(requireContext(), imageBitmap);
                            if (imageUri != null) {
                                uploadImageToFirebase(imageUri);
                            }
                        }
                    }
                });
    }

    // Upload the image to Firebase Storage and update the student's profile photo.
    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String studentId = currentStudent.getStudent_id();
        StorageReference storageRef = storage.getReference()
                .child("student_photos/" + studentId + "_" + System.currentTimeMillis() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            if (currentStudent != null) {
                                currentStudent.setProfile_photo(downloadUri.toString());
                                currentStudent.setIs_synced(false);
                                currentStudent.setLast_updated(new Date());
                                appViewModel.updateStudent(currentStudent);
                                // Immediately update the UI with the new photo.
                                ImageLoaderUtil.loadImageFromFirebaseStorage(requireContext(), downloadUri.toString(), binding.ivProfileStudentPhoto);
                            }
                        })
                )
                .addOnFailureListener(e -> Log.e("ProfileFragment", "Image upload failed", e));
    }

    // Save Bitmap to a temporary file and return its URI.
    private Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        try {
            File cacheDir = context.getCacheDir();
            File imageFile = new File(cacheDir, "profile_photo_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve the student id from SharedPreferences.
    private String getStudentIdFromSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }


    private void clearStudentSession(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("student_id");
        editor.apply();
    }
}
