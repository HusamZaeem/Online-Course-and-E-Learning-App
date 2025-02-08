package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinecourseande_learningapp.databinding.FragmentNotificationBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private AppViewModel appViewModel;
    private NotificationAdapter adapter;
    private String studentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        studentId = getStudentIdFromSharedPreferences(getContext());

        binding.toolbarNotifications.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        adapter = new NotificationAdapter(getContext());
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotifications.setAdapter(adapter);

        appViewModel.getAllStudentNotifications(studentId).observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                adapter.submitList(groupNotificationsByDate(notifications));
            }
        });

        return binding.getRoot();
    }

    private String getStudentIdFromSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }

    private List<Object> groupNotificationsByDate(List<Notification> notifications) {
        List<Object> groupedList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault());

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();

        boolean todayAdded = false, yesterdayAdded = false;

        for (Notification notification : notifications) {
            String formattedDate;
            if (isSameDay(notification.getTimestamp(), today)) {
                formattedDate = "Today";
                if (!todayAdded) {
                    groupedList.add(formattedDate);
                    todayAdded = true;
                }
            } else if (isSameDay(notification.getTimestamp(), yesterday)) {
                formattedDate = "Yesterday";
                if (!yesterdayAdded) {
                    groupedList.add(formattedDate);
                    yesterdayAdded = true;
                }
            } else {
                formattedDate = sdf.format(notification.getTimestamp());
                if (!groupedList.contains(formattedDate)) {
                    groupedList.add(formattedDate);
                }
            }
            groupedList.add(notification);
        }
        return groupedList;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
