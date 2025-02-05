package com.example.onlinecourseande_learningapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.databinding.FragmentProfileBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AppViewModel appViewModel;
    private Student currentStudent;
    private boolean isEditing = false;

    // Launchers for gallery and camera.
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    // Keystore helper for password encryption/decryption.
    private KeystoreHelper keystoreHelper;

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

        // Initialize KeystoreHelper.
        keystoreHelper = new KeystoreHelper();
        try {
            keystoreHelper.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get student id from SharedPreferences.
        String studentId = getStudentIdFromSharedPreferences(requireContext());
        if (studentId == null) {
            Log.e("ProfileFragment", "Student id not found in SharedPreferences!");
            return;
        }

        // Observe the Room database for student info.
        appViewModel.getStudentByIdLive(studentId).observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                if (student != null) {
                    currentStudent = student;
                    populateStudentData(student);
                }
            }
        });

        // Edit/Save button: toggle between view and edit mode.
        binding.btnEditSave.setOnClickListener(v -> {
            if (!isEditing) {
                setFieldsEnabled(true);
                binding.btnEditSave.setText("Save");
                isEditing = true;
            } else {
                if (currentStudent != null) {
                    currentStudent.setFirst_name(binding.etFirstName.getText().toString().trim());
                    currentStudent.setLast_name(binding.etLastName.getText().toString().trim());
                    currentStudent.setEmail(binding.etEmail.getText().toString().trim());
                    currentStudent.setPhone(binding.etPhone.getText().toString().trim());

                    // Encrypt the password before saving.
                    String plainPassword = binding.etStudentPassword.getText().toString().trim();
                    try {
                        String encryptedPassword = keystoreHelper.encryptPassword(plainPassword);
                        currentStudent.setPassword(encryptedPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Mark unsynced and update last update date.
                    currentStudent.setIs_synced(false);
                    currentStudent.setLast_updated(new Date());
                    appViewModel.updateStudent(currentStudent);

                    setFieldsEnabled(false);
                    binding.btnEditSave.setText("Edit Personal Information");
                    isEditing = false;
                }
            }
        });

        // Photo selection via camera or gallery.
        binding.ivEditPhoto.setOnClickListener(v -> showPhotoOptionsDialog());
        initActivityResultLaunchers();
    }

    // Populate UI fields with student data.
    private void populateStudentData(Student student) {
        binding.etFirstName.setText(student.getFirst_name());
        binding.etLastName.setText(student.getLast_name());
        binding.etEmail.setText(student.getEmail());
        binding.etPhone.setText(student.getPhone());

        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            try {
                String decryptedPassword = keystoreHelper.decryptPassword(student.getPassword());
                binding.etStudentPassword.setText(decryptedPassword);
            } catch (Exception e) {
                e.printStackTrace();
                binding.etStudentPassword.setText("");
            }
        } else {
            binding.etStudentPassword.setText("");
        }

        if (student.getProfile_photo() != null && !student.getProfile_photo().isEmpty()) {
            ImageLoaderUtil.loadImageFromFirebaseStorage(requireContext(), student.getProfile_photo(), binding.ivProfileStudentPhoto);
        } else {
            binding.ivProfileStudentPhoto.setImageResource(R.drawable.head_icon);
        }
    }

    // Enable or disable edit fields.
    private void setFieldsEnabled(boolean enabled) {
        binding.etFirstName.setEnabled(enabled);
        binding.etLastName.setEnabled(enabled);
        binding.etEmail.setEnabled(enabled);
        binding.etStudentPassword.setEnabled(enabled);
        binding.etPhone.setEnabled(enabled);
    }

    // Show dialog to choose camera or gallery.
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

    // Initialize launchers for activity results.
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

    // Upload image to Firebase Storage and update the student record.
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

    // Save Bitmap to a file in cache and return its URI.
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
}
