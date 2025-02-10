package com.example.onlinecourseande_learningapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Enrollment;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CourseCertificateFragment extends Fragment {
    private static final int STORAGE_PERMISSION_REQUEST = 100;
    private FrameLayout flItemCertificate;
    private Button btnDownloadCertificate;
    private View certificateView;
    private AppViewModel appViewModel;
    private Enrollment currentEnrollment;
    private Course currentCourse;
    private Mentor currentMentor;

    public CourseCertificateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_certificate, container, false);
        flItemCertificate = rootView.findViewById(R.id.fl_item_certificate);
        btnDownloadCertificate = rootView.findViewById(R.id.btnDownloadCertificate);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        certificateView = inflater.inflate(R.layout.item_certificate, flItemCertificate, false);
        flItemCertificate.addView(certificateView);

        requestStoragePermission(); // Request permissions if needed

        Bundle args = getArguments();
        if (args != null && args.containsKey("enrollment_id")) {
            String enrollmentId = args.getString("enrollment_id");
            appViewModel.getEnrollmentById(enrollmentId).observe(getViewLifecycleOwner(), enrollment -> {
                if (enrollment != null) {
                    currentEnrollment = enrollment;
                    if (currentEnrollment.getCertificate_url() == null || currentEnrollment.getCertificate_url().isEmpty()) {
                        String certificateId = generateCertificateId();
                        currentEnrollment.setCertificate_url(certificateId);
                        if (currentEnrollment.getCompletion_date() == null) {
                            currentEnrollment.setCompletion_date(new Date());
                        }
                        appViewModel.updateEnrollment(currentEnrollment);
                    }

                    appViewModel.getCourseByIdLiveData(currentEnrollment.getCourse_id()).observe(getViewLifecycleOwner(), course -> {
                        if (course != null) {
                            currentCourse = course;
                            appViewModel.getMentorsByCourseId(currentCourse.getCourse_id()).observe(getViewLifecycleOwner(), mentor -> {
                                currentMentor = mentor;
                                populateCertificate();
                            });
                        }
                    });
                }
            });
        }

        btnDownloadCertificate.setOnClickListener(v -> {
            if (hasStoragePermission()) {
                downloadCertificateAsPdf();
            } else {
                requestStoragePermission();
            }
        });

        return rootView;
    }

    private void populateCertificate() {
        TextView tvStudentName = certificateView.findViewById(R.id.tv_student_name);
        TextView tvCourseName = certificateView.findViewById(R.id.tv_course_name);
        TextView tvCertificateDate = certificateView.findViewById(R.id.tv_certificate_date);
        TextView tvCertificateId = certificateView.findViewById(R.id.tv_certificate_id);
        TextView tvMentorName = certificateView.findViewById(R.id.tv_mentor_name);

        String student_id = getStudentIdFromSharedPreferences();
        appViewModel.getStudentByIdLive(student_id).observe(getViewLifecycleOwner(), student -> {
            tvStudentName.setText(student.getFirst_name() + " " + student.getLast_name());
        });

        tvCourseName.setText(currentCourse != null ? currentCourse.getCourse_name() : "Course Name");
        tvCertificateDate.setText(formatDate(currentEnrollment.getCompletion_date()));
        tvCertificateId.setText("Id : " + currentEnrollment.getCertificate_url());
        tvMentorName.setText(currentMentor != null ? currentMentor.getMentor_fName() + " " + currentMentor.getMentor_lName() : "Mentor Name");
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return "Issued on " + sdf.format(date);
    }

    private String generateCertificateId() {
        Random random = new Random();
        int num = random.nextInt(900000000) + 100000000;
        return "SK" + num;
    }

    private void downloadCertificateAsPdf() {
        certificateView.post(() -> {
            PdfDocument document = new PdfDocument();
            int width = certificateView.getWidth();
            int height = certificateView.getHeight();

            if (width == 0 || height == 0) {
                Toast.makeText(getContext(), "Error: Certificate view not fully rendered", Toast.LENGTH_SHORT).show();
                return;
            }

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            certificateView.draw(canvas);
            document.finishPage(page);

            try {
                String fileName = "certificate_" + System.currentTimeMillis() + ".pdf";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = getContext().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (uri == null) throw new IOException("Failed to create MediaStore entry");

                    try (OutputStream fos = getContext().getContentResolver().openOutputStream(uri)) {
                        document.writeTo(fos);
                    }
                } else {
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(downloadsDir, fileName);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        document.writeTo(fos);
                    }
                }

                document.close();
                Toast.makeText(getContext(), "Certificate saved to Downloads", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error saving certificate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasStoragePermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!hasStoragePermission()) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
            }
        }
    }
}
