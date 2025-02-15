package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.AppRepository;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.ChatParticipant;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;

import java.util.ArrayList;
import java.util.List;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.NewChatViewHolder> implements Filterable {

    private List<ChatParticipant> originalList;
    private List<ChatParticipant> filteredList;
    private AppViewModel viewModel;
    private Context context;
    private String currentUserId;
    private String currentUserType;

    public NewChatAdapter(Context context, List<ChatParticipant> participants, AppViewModel viewModel, String currentUserId, String currentUserType) {
        this.context = context;
        this.viewModel = viewModel;
        this.currentUserId = currentUserId;
        this.currentUserType = currentUserType;
        this.originalList = participants;
        this.filteredList = new ArrayList<>(participants);
    }

    @NonNull
    @Override
    public NewChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_chat, parent, false);
        return new NewChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatViewHolder holder, int position) {
        ChatParticipant participant = filteredList.get(position);
        holder.bind(participant);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    String filterStr = constraint.toString().toLowerCase();
                    List<ChatParticipant> filtered = new ArrayList<>();
                    for (ChatParticipant participant : originalList) {
                        if (participant.getParticipantName().toLowerCase().contains(filterStr)) {
                            filtered.add(participant);
                        }
                    }
                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<ChatParticipant>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class NewChatViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfilePhoto;
        private TextView tvUserName;

        public NewChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePhoto = itemView.findViewById(R.id.iv_item_chat_student_photo);
            tvUserName = itemView.findViewById(R.id.tv_item_chat_user_name);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ChatParticipant participant = filteredList.get(position);
                    viewModel.getOrCreateIndividualChat(
                            currentUserId,
                            currentUserType,
                            participant.getParticipantId(),
                            participant.getParticipantType(),
                            new AppRepository.OnChatCreatedListener() {
                                @Override
                                public void onChatCreated(Chat chat) {
                                    Intent intent = new Intent(context, ConversationActivity.class);
                                    intent.putExtra("chat_id", chat.getChat_id());
                                    intent.putExtra("receiver_id", participant.getParticipantId());
                                    intent.putExtra("receiver_name", participant.getParticipantName());
                                    context.startActivity(intent);
                                }
                            }
                    );
                }
            });
        }

        public void bind(ChatParticipant participant) {
            tvUserName.setText(participant.getParticipantName());
            if (participant.getProfilePhotoUrl() != null && !participant.getProfilePhotoUrl().isEmpty()) {
                ImageLoaderUtil.loadImageFromFirebaseStorage(itemView.getContext(), participant.getProfilePhotoUrl(), ivProfilePhoto);
            } else {
                ivProfilePhoto.setImageResource(R.drawable.head_icon);
            }
        }
    }
}
