package com.example.onlinecourseande_learningapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class InboxPagerAdapter extends FragmentStateAdapter {
    public InboxPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Position 0: ChatListFragment, Position 1: CallHistoryFragment
        if (position == 0) {
            return new ChatListFragment();
        } else {
            return new CallHistoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
