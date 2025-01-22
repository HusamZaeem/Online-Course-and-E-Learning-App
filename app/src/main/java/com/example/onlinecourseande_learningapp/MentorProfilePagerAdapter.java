package com.example.onlinecourseande_learningapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MentorProfilePagerAdapter extends FragmentStateAdapter {

    private final String mentorId;

    public MentorProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity, String mentorId) {
        super(fragmentActivity);
        this.mentorId = mentorId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                MentorCoursesFragment mentorCoursesFragment = new MentorCoursesFragment();
                Bundle args = new Bundle();
                args.putString("mentor_id", mentorId);
                mentorCoursesFragment.setArguments(args);
                return mentorCoursesFragment;
            }
            case 1:
                return ReviewsFragment.newInstance(mentorId, "Mentor");
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
