package com.example.onlinecourseande_learningapp;




import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;



public class CourseDetailsPagerAdapter extends FragmentStateAdapter {
    private final String courseId;

    public CourseDetailsPagerAdapter(@NonNull FragmentActivity fragmentActivity, String courseId) {
        super(fragmentActivity);
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AboutCourseFragment.newInstance(courseId);
            case 1:
                return LessonsFragment.newInstance(courseId);
            case 2:
                return ReviewsFragment.newInstance(courseId, "Course");
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

