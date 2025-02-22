package com.example.onlinecourseande_learningapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    private AppViewModel appViewModel;
    private LifecycleOwner lifecycleOwner;

    public MessageAdapter(List<Message> messages, List<Attachment> attachments, Context context,
                          String currentUserId, boolean isGroupChat, AppViewModel appViewModel,
                          LifecycleOwner lifecycleOwner) {
        this.messages = messages != null ? messages : new ArrayList<>();
        this.attachments = attachments != null ? attachments : new ArrayList<>();
        this.context = context;
        this.currentUserId = currentUserId;
        this.isGroupChat = isGroupChat;
        this.appViewModel = appViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getSender_id().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
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


        appViewModel.getAttachmentByMessageId(message.getMessage_id()).observe(lifecycleOwner, attachment -> {
            if (holder instanceof SentMessageHolder) {
                ((SentMessageHolder) holder).bind(message, attachment);
            } else {
                ((ReceivedMessageHolder) holder).bind(message, attachment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessagesWithAttachments(List<Message> newMessages, List<Attachment> newAttachments) {
        this.messages = newMessages != null ? newMessages : new ArrayList<>();
        this.attachments = newAttachments != null ? newAttachments : new ArrayList<>();
        notifyDataSetChanged();
    }



    public void updateMessageStatus(String messageId, int newStatus) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessage_id().equals(messageId)) {
                messages.get(i).setStatus(newStatus);
                messages.get(i).setLast_updated(new Date());
                notifyItemChanged(i);
                break;
            }
        }
    }

    // ViewHolder for sent messages.
    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivMessageStatus, ivAttachment;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageSent);
            tvTime = itemView.findViewById(R.id.tvTimeSent);
            ivMessageStatus = itemView.findViewById(R.id.ivMessageStatus);
            ivAttachment = itemView.findViewById(R.id.ivAttachmentSent);
        }

        // In SentMessageHolder
        void bind(Message message, Attachment attachment) {
            tvMessage.setText(message.getContent());
            tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.getTimestamp()));

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

            handleAttachment(attachment, ivAttachment); // Handle the single attachment
        }
    }

        class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView tvMessage, tvTime, tvSenderName;
            ImageView ivAttachment;

            ReceivedMessageHolder(@NonNull View itemView) {
                super(itemView);
                tvMessage = itemView.findViewById(R.id.tvMessageReceived);
                tvTime = itemView.findViewById(R.id.tvTimeReceived);
                tvSenderName = itemView.findViewById(R.id.tvSenderName);
                ivAttachment = itemView.findViewById(R.id.ivAttachmentReceived);
            }


            void bind(Message message, Attachment attachment) {
                tvMessage.setText(message.getContent());
                tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.getTimestamp()));

                // Fetch sender details (whether it's group or one-to-one chat)
                if ("Mentor".equalsIgnoreCase(message.getSender_type())) {
                    appViewModel.getMentorByIdLive(message.getSender_id()).observe(lifecycleOwner, mentor -> {
                        if (mentor != null) {
                            tvSenderName.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
                        } else {
                            tvSenderName.setText("Unknown Mentor");
                        }
                        tvSenderName.setVisibility(View.VISIBLE); // Set visibility after name is set
                    });
                } else {
                    appViewModel.getStudentByIdLive(message.getSender_id()).observe(lifecycleOwner, student -> {
                        if (student != null) {
                            tvSenderName.setText(student.getFirst_name() + " " + student.getLast_name());
                        } else {
                            tvSenderName.setText("Unknown Student");
                        }
                        tvSenderName.setVisibility(View.VISIBLE); // Set visibility after name is set
                    });
                }

                handleAttachment(attachment, ivAttachment); // Handle the single attachment
            }
        }


        private void handleAttachment(Attachment attachment, ImageView ivAttachment) {
            if (attachment == null) {
                ivAttachment.setVisibility(View.GONE);
                return;
            }

            ivAttachment.setVisibility(View.VISIBLE);

            if ("image".equalsIgnoreCase(attachment.getType())) {
                Glide.with(context)
                        .load(attachment.getMedia_url())
                        .into(ivAttachment);
            } else if ("video".equalsIgnoreCase(attachment.getType())) {
                Glide.with(context)
                        .load(attachment.getMedia_url())
                        .thumbnail(0.1f)  // Thumbnail for video loading speed
                        .into(ivAttachment);
            } else {
                ivAttachment.setImageResource(R.drawable.ic_file_placeholder); // Default file placeholder
            }

            // Set up an onClickListener to download the attachment when clicked (if needed)
            ivAttachment.setOnClickListener(v -> downloadAttachment(attachment.getMedia_url(), attachment.getType()));
        }



        // Helper method to update the messages list.
    public void updateMessages(List<Message> newMessages) {
        Collections.sort(newMessages, (m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void downloadAttachment(String url, String type) {
        new AlertDialog.Builder(context)
                .setTitle("Download Attachment")
                .setMessage("Do you want to download this attachment?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    String extension = type.equalsIgnoreCase("image") ? ".jpg" :
                            (type.equalsIgnoreCase("video") ? ".mp4" :
                                    (type.equalsIgnoreCase("pdf") ? ".pdf" : ".file"));

                    String fileName = UUID.randomUUID().toString() + extension;
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(extension));

                    Uri fileUri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        fileUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    } else {
                        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(downloadDir, fileName);
                        fileUri = Uri.fromFile(file);
                    }

                    if (fileUri != null) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationUri(fileUri);

                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        if (downloadManager != null) {
                            long downloadId = downloadManager.enqueue(request);

                            // BroadcastReceiver to handle download completion
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context ctx, Intent intent) {
                                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                    if (id == downloadId) {
                                        Uri downloadedUri = downloadManager.getUriForDownloadedFile(downloadId);
                                        if (downloadedUri != null) {
                                            Intent openIntent = new Intent(Intent.ACTION_VIEW);
                                            openIntent.setDataAndType(downloadedUri, getMimeType(extension));
                                            openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            //  Check if there's an app that can handle the file
                                            if (openIntent.resolveActivity(context.getPackageManager()) != null) {
                                                context.startActivity(openIntent);
                                            } else {
                                                Toast.makeText(context, "No app found to open this file.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                                        context.unregisterReceiver(this);
                                    }
                                }
                            };

                            // Register the BroadcastReceiver
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                context.registerReceiver(onComplete,
                                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                                        Context.RECEIVER_NOT_EXPORTED);
                            } else {
                                context.registerReceiver(onComplete,
                                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                            }

                            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    // Helper function to get MIME type based on file extension
    private String getMimeType(String extension) {
        switch (extension.toLowerCase(Locale.ROOT)) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".bmp":
                return "image/bmp";
            case ".mp4":
                return "video/mp4";
            case ".avi":
                return "video/x-msvideo";
            case ".mov":
                return "video/quicktime";
            case ".pdf":
                return "application/pdf";
            case ".doc":
            case ".docx":
                return "application/msword";
            case ".xls":
            case ".xlsx":
                return "application/vnd.ms-excel";
            case ".ppt":
            case ".pptx":
                return "application/vnd.ms-powerpoint";
            case ".txt":
                return "text/plain";
            case ".zip":
                return "application/zip";
            case ".rar":
                return "application/vnd.rar";
            case ".mp3":
                return "audio/mpeg";
            case ".wav":
                return "audio/wav";
            case ".ogg":
                return "audio/ogg";
            default:
                return "*/*";
        }
    }



}
