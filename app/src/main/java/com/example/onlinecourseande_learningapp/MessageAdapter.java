package com.example.onlinecourseande_learningapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    // Status codes as defined in your Message entity:
    private static final int STATUS_SENT = 0;
    private static final int STATUS_DELIVERED = 1;
    private static final int STATUS_READ = 2;

    private List<Message> messages;
    private List<Attachment> attachments;
    private Context context;
    private String currentUserId;
    private boolean isGroupChat;

    public MessageAdapter(List<Message> messages, List<Attachment> attachments, Context context, String currentUserId, boolean isGroupChat) {
        this.messages = messages;
        this.attachments = attachments;
        this.context = context;
        this.currentUserId = currentUserId;
        this.isGroupChat = isGroupChat;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getSender_id().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        Attachment attachment = getAttachmentByMessageId(message.getMessage_id());

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).bind(message, attachment);
        } else {
            ((ReceivedMessageHolder) holder).bind(message, attachment);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }



    private Attachment getAttachmentByMessageId(String messageId) {
        for (Attachment attachment : attachments) {
            if (attachment.getMessage_id().equals(messageId)) {
                return attachment;
            }
        }
        return null;
    }



    // ViewHolder for sent messages.
    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivMessageStatus, ivAttachment;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageSent);
            tvTime = itemView.findViewById(R.id.tvTimeSent);
            ivMessageStatus = itemView.findViewById(R.id.ivMessageStatus);
            ivAttachment = itemView.findViewById(R.id.ivAttachmentSent);
        }

        public void bind(Message message, Attachment attachment) {
            tvMessage.setText(message.getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            tvTime.setText(sdf.format(message.getTimestamp()));

            // Set the status icon based on the message status.
            switch (message.getStatus()) {
                case STATUS_SENT:
                    ivMessageStatus.setImageResource(R.drawable.sent_checkmark);
                    break;
                case STATUS_DELIVERED:
                    ivMessageStatus.setImageResource(R.drawable.delivered_checkmarks);
                    break;
                case STATUS_READ:
                    ivMessageStatus.setImageResource(R.drawable.read_checkmarks);
                    break;
                default:
                    ivMessageStatus.setImageResource(R.drawable.sent_checkmark);
                    break;
            }


            if (attachment != null) {
                ivAttachment.setVisibility(View.VISIBLE);
                Glide.with(context).load(attachment.getMedia_url()).into(ivAttachment);
                ivAttachment.setOnClickListener(v -> downloadAttachment(attachment.getMedia_url(), attachment.getType()));
            } else {
                ivAttachment.setVisibility(View.GONE);
            }



        }
    }

    // ViewHolder for received messages.
    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvSenderName;
        ImageView ivAttachment;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageReceived);
            tvTime = itemView.findViewById(R.id.tvTimeReceived);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            ivAttachment = itemView.findViewById(R.id.ivAttachmentReceived);
        }

        public void bind(Message message, Attachment attachment) {
            tvMessage.setText(message.getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            tvTime.setText(sdf.format(message.getTimestamp()));


            if (isGroupChat) {
                tvSenderName.setVisibility(View.VISIBLE);
                tvSenderName.setText(message.getSender_type());
            } else {
                tvSenderName.setVisibility(View.GONE);
            }

            if (attachment != null) {
                ivAttachment.setVisibility(View.VISIBLE);
                Glide.with(context).load(attachment.getMedia_url()).into(ivAttachment);
                ivAttachment.setOnClickListener(v -> downloadAttachment(attachment.getMedia_url(), attachment.getType()));
            } else {
                ivAttachment.setVisibility(View.GONE);
            }
        }
    }

    // Helper method to update the messages list.
    public void updateMessages(List<Message> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    // Update a specific message's status and refresh the adapter.
    public void updateMessageStatus(String messageId, int newStatus) {
        for (Message message : messages) {
            if (message.getMessage_id().equals(messageId)) {
                message.setStatus(newStatus);
                message.setLast_updated(new Date());
                notifyDataSetChanged();
                break;
            }
        }
    }




    private void downloadAttachment(String url, String type) {
        String extension = type.equals("image") ? ".jpg" : (type.equals("video") ? ".mp4" : ".file");
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, UUID.randomUUID().toString() + extension);
        values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(extension));

        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fileUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        } else {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadDir, UUID.randomUUID().toString() + extension);
            fileUri = Uri.fromFile(file);
        }

        if (fileUri != null) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(fileUri);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Helper function to get MIME type
    private String getMimeType(String extension) {
        switch (extension) {
            case ".jpg": return "image/jpeg";
            case ".png": return "image/png";
            case ".mp4": return "video/mp4";
            default: return "application/octet-stream";
        }
    }


}
