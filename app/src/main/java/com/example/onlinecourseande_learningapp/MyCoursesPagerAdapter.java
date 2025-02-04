package com.example.onlinecourseande_learningapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyCoursesPagerAdapter extends FragmentStateAdapter {
    private final OngoingFragment ongoingFragment;
    private final CompletedFragment completedFragment;

    public MyCoursesPagerAdapter(@NonNull Fragment fragment, String studentId) {
        super(fragment);
        ongoingFragment = OngoingFragment.newInstance(studentId);
        completedFragment = CompletedFragment.newInstance(studentId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? ongoingFragment : completedFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void filterCourses(String query) {
        if (ongoingFragment != null) {
            ongoingFragment.filterCourses(query);
        }
        if (completedFragment != null) {
            completedFragment.filterCourses(query);
        }
    }
}
