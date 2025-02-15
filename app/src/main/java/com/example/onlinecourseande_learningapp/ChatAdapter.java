package com.example.onlinecourseande_learningapp;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private AppViewModel appViewModel;
    private String currentUserId;
    private Map<String, String> lastMessages = new HashMap<>();
    private Map<String, Integer> unreadMessagesCount = new HashMap<>();
    private LifecycleOwner lifecycleOwner;

    public ChatAdapter(List<Chat> chats, AppViewModel appViewModel, String currentUserId, LifecycleOwner lifecycleOwner) {
        this.chatList = chats;
        this.appViewModel = appViewModel;
        this.currentUserId = currentUserId;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void updateChats(List<Chat> chats) {
        this.chatList = chats;
        notifyDataSetChanged();
    }

    public void updateLastMessage(String chatId, String lastMessageContent) {
        lastMessages.put(chatId, lastMessageContent);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        final String[] userId = new String[1];
        final String[] userType = new String[1];


        if (chat.isIs_group_chat()) {
            // **Handling Group Chat**
            appViewModel.getGroupByIdLive(chat.getGroup_id()).observe(lifecycleOwner, group -> {
                if (group != null) {
                    holder.tvUserName.setText(group.getGroup_name());
                    appViewModel.getCourseIdByGroupId(group.getGroup_id())
                            .observe(lifecycleOwner, courseId -> {
                                if (courseId != null) {
                                    appViewModel.getCourseByIdLiveData(courseId)
                                            .observe(lifecycleOwner, course -> {
                                                if (course != null && course.getPhoto_url() != null) {
                                                    ImageLoaderUtil.loadImageFromFirebaseStorage(holder.itemView.getContext(), course.getPhoto_url(), holder.ivStudentPhoto);
                                                } else {
                                                    holder.ivStudentPhoto.setImageResource(R.drawable.head_icon);
                                                }
                                            });
                                } else {
                                    holder.ivStudentPhoto.setImageResource(R.drawable.head_icon);
                                }
                            });
                } else {
                    holder.tvUserName.setText("Group Chat");
                    holder.ivStudentPhoto.setImageResource(R.drawable.head_icon);
                }
            });
        } else {
            // **Handling One-to-One Chat**

            if (chat.getSender_id().equalsIgnoreCase(currentUserId)) {
                userId[0] = chat.getReceiver_id();
                userType[0] = chat.getReceiver_type();
            } else {
                userId[0] = chat.getSender_id();
                userType[0] = chat.getSender_type();
            }
            bindUserNameAndPhoto(holder.tvUserName, holder.ivStudentPhoto, userId[0], userType[0]);
        }

        // **Handle Last Message Display**
        String lastMessageContent = lastMessages.get(chat.getChat_id());
        holder.tvLastMessage.setText((lastMessageContent != null) ? lastMessageContent : "No messages yet");
        holder.tvLastMessageTime.setText(formatTime(chat.getTimestamp()));

        // **Handle Unread Message Count**
        int unreadCount = unreadMessagesCount.getOrDefault(chat.getChat_id(), 0);
        if (unreadCount > 0) {
            holder.cvNewMessages.setVisibility(View.VISIBLE);
            holder.tvNewMessagesNumber.setText(String.valueOf(unreadCount));
        } else {
            holder.cvNewMessages.setVisibility(View.GONE);
        }

        // **Click Listener to Open Conversation**
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ConversationActivity.class);
            intent.putExtra("chat_id", chat.getChat_id());
            intent.putExtra("is_group_chat", false); // Set false as it is not a group chat
            intent.putExtra("target_user_id", userId[0]);
            intent.putExtra("target_user_type", userType[0]);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStudentPhoto;
        TextView tvUserName, tvLastMessage, tvLastMessageTime, tvNewMessagesNumber;
        CardView cvNewMessages;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStudentPhoto = itemView.findViewById(R.id.iv_item_chat_student_photo);
            tvUserName = itemView.findViewById(R.id.tv_item_chat_user_name);
            tvLastMessage = itemView.findViewById(R.id.tv_item_chat_last_message);
            tvLastMessageTime = itemView.findViewById(R.id.tv_item_chat_last_message_time);
            tvNewMessagesNumber = itemView.findViewById(R.id.tv_item_chat_new_messages_number);
            cvNewMessages = itemView.findViewById(R.id.cv_new_messages);
        }
    }

    /**
     * Binds the user name and photo into the provided views. This method uses LiveData observers with the given lifecycleOwner
     * to asynchronously update the views when the data becomes available.
     */
    private void bindUserNameAndPhoto(TextView tvUserName, ImageView ivPhoto, String userId, String userType) {
        if ("Mentor".equalsIgnoreCase(userType)) {
            appViewModel.getMentorByIdLive(userId).observe(lifecycleOwner, mentor -> {
                if (mentor != null) {
                    tvUserName.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
                    // Load photo using your image loader
                    ImageLoaderUtil.loadImageFromFirebaseStorage(tvUserName.getContext(), mentor.getMentor_photo(), ivPhoto);
                } else {
                    tvUserName.setText("Unknown Mentor");
                }
            });
        } else {
            appViewModel.getStudentByIdLive(userId).observe(lifecycleOwner, student -> {
                if (student != null) {
                    tvUserName.setText(student.getFirst_name() + " " + student.getLast_name());
                    // Load photo using your image loader
                    ImageLoaderUtil.loadImageFromFirebaseStorage(tvUserName.getContext(), student.getProfile_photo(), ivPhoto);
                } else {
                    tvUserName.setText("Unknown Student");
                }
            });
        }
    }

    private String formatTime(Date timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }

    // Update unread message count externally
    public void updateUnreadMessagesCount(String chatId, int count) {
        unreadMessagesCount.put(chatId, count);
        notifyDataSetChanged();
    }
}
