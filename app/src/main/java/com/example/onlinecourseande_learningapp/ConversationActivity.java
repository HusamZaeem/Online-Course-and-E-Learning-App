package com.example.onlinecourseande_learningapp;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
                        attachmentUri = result.getData().getData();
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
                        Glide.with(this).load(attachmentUri).into(binding.ivAttachmentPreview);
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

        messageAdapter = new MessageAdapter(messageList, new ArrayList<>(), this, currentUserId, isGroupChat);
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



    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+ (API 34+)
            // Request READ_MEDIA_VISUAL_USER_SELECTED permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, 100);
            } else {
                openMediaPicker();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33)
            // Request READ_MEDIA_IMAGES and READ_MEDIA_VIDEO permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                }, 100);
            } else {
                openMediaPicker();
            }
        } else { // Android 12 and below
            // Request READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 100);
            } else {
                openMediaPicker();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Permission granted for selected photos.");
                openMediaPicker();  // Open media picker after permission is granted
            } else {
                Log.d("Permission", "Permission denied for selected photos.");
                Toast.makeText(this, "Permission denied, can't access photos", Toast.LENGTH_SHORT).show();
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

                    WriteBatch batch = firestore.batch(); // Initialize WriteBatch for batch updates

                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Message message = doc.toObject(Message.class);
                        if (message != null) {
                            // Handle updates for status
                            if (message.getStatus() == STATUS_DELIVERED) {
                                messageAdapter.updateMessageStatus(message.getMessage_id(), STATUS_DELIVERED);
                            } else if (message.getStatus() == STATUS_READ) {
                                messageAdapter.updateMessageStatus(message.getMessage_id(), STATUS_READ);
                            }
                        }
                    }

                    // Commit batch updates
                    batch.commit().addOnFailureListener(e -> {
                        Log.e("Firestore", "Error in batch commit: ", e);
                        Toast.makeText(ConversationActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    });
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
        message.setContent(content.isEmpty() ? "" : content);
        message.setTimestamp(now);
        message.setMessage_type(attachmentUri == null ? "text" : attachmentType);
        message.setStatus(STATUS_PENDING);  // Set as "pending"
        message.setIs_synced(false);
        message.setLast_updated(now);

        if (isGroupChat) {
            message.setGroup_id(groupId);
        }

        // Insert in Room DB first
        appViewModel.insertMessage(message);

        // Clear EditText after sending message
        binding.etMessage.setText("");
        binding.attachmentPreview.setVisibility(View.GONE);
        attachmentUri = null;
        attachmentType = null;

        // If there's an attachment, upload it first before syncing message
        if (attachmentUri != null) {
            uploadAttachment(attachmentUri, messageId, message);
        } else {
            syncMessageToFirebase(message);
        }
    }



    private void uploadAttachment(Uri uri, String messageId, Message message) {


        requestPermissionsIfNeeded(); // Requesting permissions

        // Determine file type and upload it to Firebase
        String fileExtension = getFileExtension(uri);
        attachmentType = getAttachmentType(fileExtension);

        // Upload the attachment to Firebase Storage
        StorageReference fileRef = storageRef.child("attachments/" + UUID.randomUUID().toString());
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                            // Create and save attachment to Room DB
                            Attachment attachment = new Attachment();
                            attachment.setAttachment_id(UUID.randomUUID().toString());
                            attachment.setMessage_id(messageId);
                            attachment.setMedia_url(downloadUrl.toString());
                            attachment.setType(attachmentType);
                            attachment.setIs_synced(false);
                            attachment.setLast_updated(new Date());

                            appViewModel.insertAttachment(attachment);
                            syncAttachmentToFirebase(attachment);

                            // Sync the message after uploading the attachment
                            message.setStatus(STATUS_SENT);
                            message.setIs_synced(true);
                            appViewModel.updateMessage(message);
                            syncMessageToFirebase(message);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show();
                        })
        );
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
        ActivityResultLauncher<Intent> pickFile = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        attachmentUri = result.getData().getData();

                        // Grant persistent URI permission for Android 14+
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            getContentResolver().takePersistableUriPermission(
                                    attachmentUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            );
                        }

                        attachmentType = getAttachmentType(getFileExtension(attachmentUri));
                        binding.attachmentPreview.setVisibility(View.VISIBLE);
                        Glide.with(this).load(attachmentUri).into(binding.ivAttachmentPreview);
                    }
                });


        binding.btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionsIfNeeded();
                openMediaPicker();
            }
        });
    }


    private void openMediaPicker() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        } else { // Android 13 and below
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
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
