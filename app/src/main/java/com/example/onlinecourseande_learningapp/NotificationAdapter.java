package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.entities.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOTIFICATION = 1;

    private List<Object> notifications;
    private Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
        notifications = new ArrayList<>();
    }

    public void submitList(List<Object> notificationList) {
        this.notifications = notificationList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return notifications.get(position) instanceof String ? TYPE_HEADER : TYPE_NOTIFICATION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            ((HeaderViewHolder) holder).bind((String) notifications.get(position));
        } else {
            ((NotificationViewHolder) holder).bind((Notification) notifications.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvNotificationHeader);
        }

        void bind(String header) {
            tvHeader.setText(header);
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivNotificationIcon;
        private TextView tvNotificationTitle;
        private TextView tvNotificationSubtitle;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationSubtitle = itemView.findViewById(R.id.tvNotificationSubtitle);
        }

        void bind(Notification notification) {
            tvNotificationTitle.setText(notification.getTitle());
            tvNotificationSubtitle.setText(notification.getContent());
            ivNotificationIcon.setImageResource(getNotificationIcon(notification.getType()));
        }

        private int getNotificationIcon(String type) {
            switch (type) {
                case "payment": return R.drawable.ic_payment;
                case "enrollment": return R.drawable.ic_enrollment_notification;
                case "new_course": return R.drawable.ic_new_course;
                case "profile_update": return R.drawable.ic_profile_update;
                default: return R.drawable.ic_default_notification;
            }
        }
    }
}
