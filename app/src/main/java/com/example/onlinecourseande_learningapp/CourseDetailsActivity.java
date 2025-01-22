package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinecourseande_learningapp.databinding.ActivityCourseDetailsBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CourseDetailsActivity extends AppCompatActivity {

    ActivityCourseDetailsBinding binding;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        String courseId = getIntent().getStringExtra("course_id");
        setCourseDetails(courseId);


        CourseDetailsPagerAdapter adapter = new CourseDetailsPagerAdapter(this,courseId);
        binding.vbCourseDetails.setAdapter(adapter);
        binding.vbCourseDetails.setOffscreenPageLimit(3);


        new TabLayoutMediator(binding.courseDetailsTabLayout, binding.vbCourseDetails, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("About");
                    break;
                case 1:
                    tab.setText("Lessons");
                    break;
                case 2:
                    tab.setText("Reviews");
                    break;
            }
        }).attach();




    }


    public void setCourseDetails(String course_id){
        appViewModel.getCourseByIdLiveData(course_id).observe(this, course -> {
            String courseName = course.getCourse_name();
            String courseCategory = course.getCategory();
            String courseRating = String.valueOf(course.getCourse_rating());
            String courseActualPrice = String.format("$%.2f",course.getPrice());
            double courseDiscountPrice = course.getPrice() - (course.getPrice() * 0.4);
            String courseCurrentPrice = String.format("$%.2f", courseDiscountPrice);
            String courseStudentNo = String.valueOf(course.getStudents_count());
            String courseHours = String.valueOf(course.getHours());
            ImageLoaderUtil.loadImageFromFirebaseStorage(getApplicationContext(), course.getPhoto_url(), binding.ivCourseDetailsCoursePhoto);
            binding.tvCourseDetailsCourseName.setText(courseName);
            binding.tvCourseDetailsCourseCategory.setText(courseCategory);
            binding.tvCourseDetailsCourseRating.setText(courseRating);
            binding.tvCourseDetailsCourseCurrentPrice.setText(courseCurrentPrice);
            binding.tvCourseDetailsCourseActualPrice.setText(courseActualPrice);
            binding.tvCourseDetailsCourseStudentsNo.setText(courseStudentNo + " Students");
            binding.tvCourseDetailsCourseHours.setText(courseHours + " Hours");



            appViewModel.getReviewCountForCourse(course_id).observe(this, reviewCount -> {
                if (reviewCount != null){
                    binding.tvCourseDetailsCourseReviewsNo.setText("(" + reviewCount + " reviews)");
                }else binding.tvCourseDetailsCourseReviewsNo.setText("0 reviews");
            });
        });


    }
}