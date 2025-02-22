package com.example.onlinecourseande_learningapp;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlinecourseande_learningapp.databinding.ActivityConversationBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Attachment;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Message;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ConversationActivity extends AppCompatActivity {

    private ActivityConversationBinding binding;
    private String chatId;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String currentUserId, currentUserType;
    private AppViewModel appViewModel;
    private Uri attachmentUri;
    private String attachmentType;
    private Handler typingHandler = new Handler();
    private Runnable typingTimeoutRunnable;
    private String pendingPickerType = null;

    // Status constants
    private static final int STATUS_PENDING = -1;
    private static final int STATUS_SENT = 0;
    public static final int STATUS_DELIVERED = 1;
    public static final int STATUS_READ = 2;


    private ActivityResultLauncher<Intent> pickFileLauncher;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        pickFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            attachmentUri = result.getData().getData();
                        } else {
                            return;
                        }
                        // Persist permission if needed (for Android 14+)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        attachmentUri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (SecurityException e) {
                                Log.e("ConversationActivity", "Failed to take persistable URI permission", e);
                            }
                        }
                        attachmentType = getAttachmentType(getFileExtension(attachmentUri));
                        binding.attachmentPreview.setVisibility(View.VISIBLE);
                        if ("image".equalsIgnoreCase(attachmentType)) {
                            Glide.with(this).load(attachmentUri).into(binding.ivAttachmentPreview);
                        } else if ("video".equalsIgnoreCase(attachmentType)) {
                            Glide.with(this).load(attachmentUri).thumbnail(0.1f).into(binding.ivAttachmentPreview);
                        } else { // file
                            binding.ivAttachmentPreview.setImageResource(R.drawable.ic_file_placeholder);
                        }
                        binding.btnRemoveAttachment.setVisibility(View.VISIBLE);
                    }
                }
        );


        String targetUserId = getIntent().getStringExtra("target_user_id");
        String targetUserType = getIntent().getStringExtra("target_user_type");


        chatId = getIntent().getStringExtra("chat_id");
        String groupId = getIntent().getStringExtra("group_id");

        currentUserId = getStudentIdFromSharedPreferences();
        currentUserType = "Student";


        boolean isGroupChat = groupId != null && !groupId.isEmpty();


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        messageAdapter = new MessageAdapter(messageList, new ArrayList<>(), this, currentUserId, isGroupChat, appViewModel, this);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(messageAdapter);

        // Observe messages for either a chat or a group
        if (isGroupChat) {
            appViewModel.getGroupMessagesByGroupId(groupId).observe(this, messages -> {
                messageAdapter.updateMessages(messages);
                if (messages.size() > 0) {
                    binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
                }

            });
            bindUserNameAndPhoto(null, null, true, groupId);
        } else {
            appViewModel.getChatMessagesByChatId(chatId).observe(this, messages -> {
                messageAdapter.updateMessages(messages);
                if (messages.size() > 0) {
                    binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
                }

            });


            bindUserNameAndPhoto(targetUserId, targetUserType, false, null);
        }






        registerFirebaseMessageListener(isGroupChat, groupId);
        setupAttachmentPicker();

        binding.btnSend.setOnClickListener(v -> sendMessage(isGroupChat, groupId));

        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTypingStatus(true);
                if (typingTimeoutRunnable != null) {
                    typingHandler.removeCallbacks(typingTimeoutRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                typingTimeoutRunnable = () -> updateTypingStatus(false);
                typingHandler.postDelayed(typingTimeoutRunnable, 1500);
            }
        });

        binding.btnRemoveAttachment.setOnClickListener(v -> clearAttachment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        appViewModel.markMessagesAsRead(chatId, currentUserId);
        appViewModel.markMessagesAsDelivered(chatId, currentUserId);
    }

    private String getStudentIdFromSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sp.getString("student_id", null);
    }




    private void bindUserNameAndPhoto(String userId, String userType, boolean isGroupChat, String groupId) {
        if (isGroupChat) {
            // Handle Group Chat Name and Photo
            appViewModel.getGroupByIdLive(groupId).observe(this, group -> {
                if (group != null) {
                    binding.tvReceiverName.setText(group.getGroup_name());
                    appViewModel.getCourseIdByGroupId(groupId).observe(this, courseId -> {
                        if (courseId != null) {
                            appViewModel.getCourseByIdLiveData(courseId).observe(this, course -> {
                                if (course != null && course.getPhoto_url() != null) {
                                    ImageLoaderUtil.loadImageFromFirebaseStorage(binding.getRoot().getContext(), course.getPhoto_url(), binding.ivReceiverProfile);
                                } else {
                                    binding.ivReceiverProfile.setImageResource(R.drawable.head_icon);
                                }
                            });
                        }
                    });
                } else {
                    binding.tvReceiverName.setText("Group Chat");
                    binding.ivReceiverProfile.setImageResource(R.drawable.head_icon);
                }
            });
        } else {
            // Handle One-on-One Chat Name and Photo
            String targetUserId = userId;
            String targetUserType = userType;

            if ("Mentor".equalsIgnoreCase(targetUserType)) {
                appViewModel.getMentorByIdLive(targetUserId).observe(this, mentor -> {
                    if (mentor != null) {
                        binding.tvReceiverName.setText(mentor.getMentor_fName() + " " + mentor.getMentor_lName());
                        ImageLoaderUtil.loadImageFromFirebaseStorage(binding.getRoot().getContext(), mentor.getMentor_photo(), binding.ivReceiverProfile);
                    } else {
                        binding.tvReceiverName.setText("Unknown Mentor");
                        binding.ivReceiverProfile.setImageResource(R.drawable.head_icon);
                    }
                });
            } else {
                appViewModel.getStudentByIdLive(targetUserId).observe(this, student -> {
                    if (student != null) {
                        binding.tvReceiverName.setText(student.getFirst_name() + " " + student.getLast_name());
                        ImageLoaderUtil.loadImageFromFirebaseStorage(binding.getRoot().getContext(), student.getProfile_photo(), binding.ivReceiverProfile);
                    } else {
                        binding.tvReceiverName.setText("Unknown Student");
                        binding.ivReceiverProfile.setImageResource(R.drawable.head_icon);
                    }
                });
            }
        }
    }



    private boolean hasMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Request permissions without directly opening the picker.
    private void requestMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, 100);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            }, 100);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }


    private void checkAndOpenMediaPicker(String pickerType) {
        if (hasMediaPermissions()) {
            openMediaPicker(pickerType);
        } else {
            pendingPickerType = pickerType;
            requestMediaPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (granted && pendingPickerType != null) {
                openMediaPicker(pendingPickerType);
                pendingPickerType = null;
            } else {
                // Permission denied, show dialog to guide the user to the settings
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("To send media files, please allow storage access in settings.")
                        .setPositiveButton("Open Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }
    }




    private void registerFirebaseMessageListener(boolean isGroupChat, String groupId) {
        CollectionReference messageCollection = firestore.collection("Message");

        Query query = isGroupChat ?
                messageCollection.whereEqualTo("group_id", groupId) :
                messageCollection.whereEqualTo("chat_id", chatId);

        query.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    List<Message> messages = new ArrayList<>();
                    List<String> messageIds = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Message message = doc.toObject(Message.class);
                        if (message != null) {
                            messages.add(message);
                            messageIds.add(message.getMessage_id());
                            appViewModel.insertMessage(message); // Save message to RoomDB

                            //  Update the UI in real-time
                            messageAdapter.updateMessageStatus(message.getMessage_id(), message.getStatus());

                            //  Sync status updates to RoomDB to keep it consistent
                            appViewModel.updateMessageStatus(message.getMessage_id(), message.getStatus(), new Date());
                        }
                    }

                    fetchAttachmentsForMessages(messageIds, messages);
                });
    }


    // Fetch attachments related to messages
    private void fetchAttachmentsForMessages(List<String> messageIds, List<Message> messages) {
        firestore.collection("Attachment")
                .whereIn("message_id", messageIds)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    List<Attachment> attachments = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Attachment attachment = doc.toObject(Attachment.class);
                        if (attachment != null) {
                            attachments.add(attachment);
                            appViewModel.insertAttachment(attachment); // Save attachment to RoomDB
                        }
                    }

                    // Update adapter with messages and attachments
                    messageAdapter.updateMessagesWithAttachments(messages, attachments);
                });
    }






    private void sendMessage(boolean isGroupChat, String groupId) {
        String content = binding.etMessage.getText().toString().trim();
        if (content.isEmpty() && attachmentUri == null) return;

        String messageId = UUID.randomUUID().toString();
        Date now = new Date();

        Message message = new Message();
        message.setMessage_id(messageId);
        message.setChat_id(chatId);
        message.setSender_id(currentUserId);
        message.setSender_type(currentUserType);
        message.setContent(content);
        message.setTimestamp(now);
        message.setMessage_type(attachmentUri == null ? "text" : attachmentType);
        message.setStatus(STATUS_SENT);
        message.setIs_synced(false);
        message.setLast_updated(now);

        if (isGroupChat) {
            message.setGroup_id(groupId);
        }

        appViewModel.insertMessage(message);

        // Instantly update UI so message appears immediately
        messageAdapter.updateMessages(Collections.singletonList(message));

        Uri tempAttachmentUri = attachmentUri;
        binding.etMessage.setText("");
        binding.attachmentPreview.setVisibility(View.GONE);
        clearAttachment();

        if (tempAttachmentUri != null) {
            uploadAttachment(tempAttachmentUri, messageId, message);
        } else {
            syncMessageToFirebase(message);
        }
    }





    private void uploadAttachment(Uri uri, String messageId, Message message) {
        String fileExtension = getFileExtension(uri);
        attachmentType = getAttachmentType(fileExtension);

        StorageReference fileRef = storageRef.child("attachments/" + UUID.randomUUID().toString());
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    Attachment attachment = new Attachment();
                    attachment.setAttachment_id(UUID.randomUUID().toString());
                    attachment.setMessage_id(messageId);
                    attachment.setMedia_url(downloadUrl.toString());
                    attachment.setType(attachmentType);
                    attachment.setIs_synced(false);
                    attachment.setLast_updated(new Date());

                    appViewModel.insertAttachment(attachment);
                    syncAttachmentToFirebase(attachment);

                    message.setStatus(STATUS_SENT);
                    message.setIs_synced(true);
                    appViewModel.updateMessage(message);
                    syncMessageToFirebase(message);
                })
        ).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show();
        });
    }




    private String getFileExtension(Uri uri) {
        String extension = "";
        if (uri.getScheme().equals("content")) {
            String mimeType = getContentResolver().getType(uri);
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        } else if (uri.getScheme().equals("file")) {
            extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        }
        return extension != null ? extension : "file";
    }

    private String getAttachmentType(String extension) {
        if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif")) {
            return "image";
        } else if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("avi")) {
            return "video";
        } else {
            return "file"; // Default for unknown file types
        }
    }


    private void syncMessageToFirebase(Message message) {
        firestore.collection("Message").document(message.getMessage_id())
                .set(message.toMap())
                .addOnSuccessListener(aVoid -> {
                    message.setIs_synced(true);
                    appViewModel.updateMessage(message);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to sync message: " + e.getMessage());
                });
    }

    private void syncAttachmentToFirebase(Attachment attachment) {
        firestore.collection("Attachment").document(attachment.getAttachment_id())
                .set(attachment.toMap())
                .addOnSuccessListener(aVoid -> {
                    attachment.setIs_synced(true);
                    appViewModel.updateAttachment(attachment); // Update local database after successful sync
                });
    }

    private void setupAttachmentPicker() {
        binding.btnAttach.setOnClickListener(v -> {
            new AlertDialog.Builder(ConversationActivity.this)
                    .setTitle("Attach")
                    .setMessage("Choose attachment type")
                    .setPositiveButton("File", (dialog, which) -> checkAndOpenMediaPicker("file"))
                    .setNegativeButton("Photo/Video", (dialog, which) -> checkAndOpenMediaPicker("media"))
                    .show();
        });
    }


    private void openMediaPicker(String pickerType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if ("file".equals(pickerType)) {
            intent.setType("*/*");
        } else if ("media".equals(pickerType)) {
            intent.setType("*/*");
            String[] mimeTypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        pickFileLauncher.launch(intent);
    }



    private void clearAttachment() {
        attachmentUri = null;
        attachmentType = null;
        binding.attachmentPreview.setVisibility(View.GONE);
    }

    private void updateTypingStatus(boolean isTyping) {
        firestore.collection("Chat").document(chatId)
                .update("typingStatus." + currentUserId, isTyping);
    }
}
