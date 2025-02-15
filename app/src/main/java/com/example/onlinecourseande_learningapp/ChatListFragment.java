package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private AppViewModel appViewModel;
    private String currentUserId; // Could be student_id or mentor_id
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        currentUserId = getUserIdFromSharedPreferences();

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(new ArrayList<>(), appViewModel, currentUserId, getViewLifecycleOwner());
        recyclerView.setAdapter(chatAdapter);

        loadChats();

        return view;
    }



    private void registerFirebaseChatListener() {
        firestore.collection("Chat")
                .whereArrayContains("participants", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Chat chat = dc.getDocument().toObject(Chat.class);
                        if (chat != null) {

                            if (chat.isIs_group_chat()) {
                                // Verify group exists in local DB
                                Group group = appViewModel.getGroupByGroupId(chat.getGroup_id());
                                if (group == null) {
                                    firestore.collection("Group")
                                            .document(chat.getGroup_id())
                                            .get()
                                            .addOnSuccessListener(groupDoc -> {
                                                if (groupDoc.exists()) {
                                                    Group newGroup = groupDoc.toObject(Group.class);
                                                    if (newGroup != null) {
                                                        appViewModel.insertGroup(newGroup);
                                                    }
                                                }
                                            });
                                }

                                // **Ensure user is a valid member before inserting**
                                firestore.collection("GroupMembership")
                                        .whereEqualTo("group_id", chat.getGroup_id())
                                        .whereEqualTo("member_id", currentUserId)
                                        .get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            if (!querySnapshot.isEmpty()) {
                                                if (appViewModel.getChatById(chat.getChat_id()) == null) {
                                                    appViewModel.insertChat(chat);
                                                } else {
                                                    appViewModel.updateChat(chat);
                                                }
                                            }
                                        });

                            } else { // One-to-one chat
                                if (appViewModel.getChatById(chat.getChat_id()) == null) {
                                    appViewModel.insertChat(chat);
                                } else {
                                    appViewModel.updateChat(chat);
                                }
                            }
                        }
                    }
                });
    }


    private void loadChats() {
        appViewModel.getAllChatsIncludingGroups(currentUserId).observe(getViewLifecycleOwner(), chats -> {
            chatAdapter.updateChats(chats);

            for (Chat chat : chats) {
                if (chat.isIs_group_chat()) {
                    appViewModel.getLastMessageForGroup(chat.getGroup_id()).observe(getViewLifecycleOwner(), lastMessage -> {
                        if (lastMessage != null) {
                            chatAdapter.updateLastMessage(chat.getChat_id(), lastMessage.getContent());
                        }
                    });
                } else {
                    appViewModel.getLastMessageForChat(chat.getChat_id()).observe(getViewLifecycleOwner(), lastMessage -> {
                        if (lastMessage != null) {
                            chatAdapter.updateLastMessage(chat.getChat_id(), lastMessage.getContent());
                        }
                    });
                }

                appViewModel.getUnreadMessages(chat.getChat_id(), currentUserId)
                        .observe(getViewLifecycleOwner(), unreadMessages -> {
                            int unreadCount = unreadMessages.size();
                            chatAdapter.updateUnreadMessagesCount(chat.getChat_id(), unreadCount);
                        });
            }
        });
    }


    private String getUserIdFromSharedPreferences() {
        SharedPreferences sp = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sp.getString("student_id", null);  // or "mentor_id", based on how you're setting it
    }
}
