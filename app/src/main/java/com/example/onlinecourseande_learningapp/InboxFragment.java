package com.example.onlinecourseande_learningapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.ChatParticipant;
import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class InboxFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private InboxPagerAdapter pagerAdapter;
    private FloatingActionButton fab;
    private AppViewModel viewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        // Initialize UI components
        tabLayout = view.findViewById(R.id.tabLayoutChat);
        viewPager = view.findViewById(R.id.vb_chat);
        fab = view.findViewById(R.id.fab);

        // Set up ViewPager with Tabs
        pagerAdapter = new InboxPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Chats" : "Calls");
        }).attach();



        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        // Handle floating button click
        fab.setOnClickListener(v -> showNewChatDialog());


        return view;
    }






    /**
     * Displays a dialog to start a new chat by selecting a participant.
     */
    private void showNewChatDialog() {
        String currentStudentId = getStudentIdFromSharedPreferences();
        String currentUserType = "Student";

        viewModel.getChatParticipants(currentStudentId).observe(getViewLifecycleOwner(), chatParticipants -> {
            if (chatParticipants == null || chatParticipants.isEmpty()) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("No participants found.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                setupChatDialog(chatParticipants, currentStudentId, currentUserType);
            }
        });
    }

    /**
     * Sets up the new chat selection dialog.
     */
    private void setupChatDialog(List<ChatParticipant> chatParticipants, String currentStudentId, String currentUserType) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.item_new_chat_dialog, null);
        EditText etSearch = dialogView.findViewById(R.id.etSearch);
        RecyclerView rvNewChat = dialogView.findViewById(R.id.rvNewChat);

        rvNewChat.setLayoutManager(new LinearLayoutManager(requireContext()));

        NewChatAdapter adapter = new NewChatAdapter(requireContext(), chatParticipants, viewModel, currentStudentId, currentUserType);
        rvNewChat.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Select Participant")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }


    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);
    }
}
