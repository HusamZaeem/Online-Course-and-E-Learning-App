package com.example.onlinecourseande_learningapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.onlinecourseande_learningapp.databinding.ActivityMainBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.Converter;
import com.example.onlinecourseande_learningapp.room_database.entities.Chat;
import com.example.onlinecourseande_learningapp.room_database.entities.Course;
import com.example.onlinecourseande_learningapp.room_database.entities.Group;
import com.example.onlinecourseande_learningapp.room_database.entities.GroupMembership;
import com.example.onlinecourseande_learningapp.room_database.entities.Mentor;
import com.example.onlinecourseande_learningapp.room_database.entities.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private AppViewModel appViewModel;
    private FirebaseFirestore fs;
    private boolean groupSyncDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        fs = FirebaseFirestore.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new HomeFragment())
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_my_courses) {
                selectedFragment = new MyCoursesFragment();
            } else if (item.getItemId() == R.id.nav_inbox) {
                selectedFragment = new InboxFragment();
            } else if (item.getItemId() == R.id.nav_transactions) {
                selectedFragment = new TransactionsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, selectedFragment)
                        .commit();
            }

            return true;
        });

        if (!groupSyncDone) {
            syncGroupChatsInInbox();
        }
    }

    private void syncGroupChatsInInbox() {
        observeOnce(appViewModel.getAllCoursesDistinct(), this, courses -> {
            if (courses != null) {
                for (Course course : courses) {
                    syncGroupChatForCourse(course);
                }
                groupSyncDone = true;
                Log.d("MainActivity", "Group chat sync initiated for all courses");
            }
        });
    }

    private void syncGroupChatForCourse(Course course) {
        final String courseId = course.getCourse_id();
        observeOnce(appViewModel.getCourseGroupInfo(courseId), this, info -> {
            if (info == null) return;
            if (info.getGroup() == null) {
                Log.d("MainActivity", "No group for course " + courseId + ", creating group and chat");
                createGroupChatForCourse(course);
                // Re-check after a delay to let data propagate.
                recheckSyncForCourse(course.getCourse_id(), 2000);
            } else {
                Group group = info.getGroup();
                // Ensure chat exists for this group.
                observeOnce(appViewModel.getChatByGroupId(group.getGroup_id()), this, chat -> {
                    if (chat == null) {
                        Log.d("MainActivity", "No chat for group " + group.getGroup_id() + ", creating chat");
                        createChatForGroup(group.getGroup_id());
                        recheckSyncForCourse(course.getCourse_id(), 2000);
                    } else {
                        // Sync group memberships.
                        syncParticipantsForGroup(group, courseId);
                    }
                });
            }
        });
    }

    private void recheckSyncForCourse(String courseId, long delayMillis) {
        new android.os.Handler(getMainLooper()).postDelayed(() -> {
            observeOnce(appViewModel.getCourseByIdLiveData(courseId), this, course -> {
                if (course == null) {
                    Log.w("MainActivity", "Course not found for recheck: " + courseId);
                    return; // Stop recursion if course is missing
                }

                // Check if Group exists for this Course
                observeOnce(appViewModel.getCourseGroupInfo(courseId), this, info -> {
                    if (info == null) {
                        Log.w("MainActivity", "Course group info not found for: " + courseId);
                        return; // Stop recursion if no group info exists
                    }

                    if (info.getGroup() == null) {
                        Log.w("MainActivity", "Group does not exist for course: " + courseId);
                        syncGroupChatForCourse(course); // Create group and chat if missing
                        return;
                    }

                    String groupId = info.getGroup().getGroup_id();

                    // Check if Chat exists for this Group
                    observeOnce(appViewModel.getChatByGroupId(groupId), this, chat -> {
                        boolean chatExists = (chat != null);
                        Log.d("MainActivity", "Chat exists for group " + groupId + ": " + chatExists);

                        // Check if GroupMemberships exist
                        observeOnce(appViewModel.getGroupMembershipsByGroupId(groupId), this, memberships -> {
                            boolean membershipsExist = (memberships != null && !memberships.isEmpty());
                            Log.d("MainActivity", "Memberships exist for group " + groupId + ": " + membershipsExist);

                            if (!chatExists || !membershipsExist) {
                                Log.w("MainActivity", "Re-syncing missing data for group: " + groupId);
                                syncParticipantsForGroup(info.getGroup(), courseId);
                            } else {
                                Log.d("MainActivity", "No sync needed for course: " + courseId);
                            }
                        });
                    });
                });
            });
        }, delayMillis);
    }



    /**
     * Creates a new group for the course and then creates its chat.
     */
    private void createGroupChatForCourse(Course course) {
        String groupName = course.getCourse_name() + " Group";
        String courseId = course.getCourse_id();
        String groupId = generateUniqueGroupId(courseId);

        // Insert group locally.
        Group newGroup = new Group(groupId, groupName, courseId, true, new Date());
        appViewModel.insertGroup(newGroup);
        Log.d("MainActivity", "Group inserted: " + groupId);

        // Wait for group to appear in Room, then create chat.
        observeOnce(appViewModel.getGroupById(groupId), this, group -> {
            if (group != null) {
                createChatForGroup(groupId);
            } else {
                Log.e("MainActivity", "Group insertion not confirmed; chat not created for " + groupId);
            }
        });

        // Sync group to Firebase.
        Map<String, Object> groupData = new HashMap<>();
        groupData.put("group_name", groupName);
        groupData.put("course_id", courseId);
        groupData.put("last_updated", Converter.toFirestoreTimestamp(new Date()));
        fs.collection("Group").document(groupId)
                .set(groupData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "Group synced to Firebase: " + groupId))
                .addOnFailureListener(e -> Log.e("MainActivity", "Failed to sync group: " + groupId, e));
    }

    /**
     * Creates a chat for an existing group (locally and then syncs to Firebase).
     */
    private void createChatForGroup(String groupId) {
        Chat groupChat = new Chat();
        groupChat.setChat_id(groupId + "_chat");
        groupChat.setGroup_id(groupId);
        groupChat.setIs_group_chat(true);
        groupChat.setTimestamp(new Date());
        groupChat.setIs_synced(true);
        groupChat.setLast_updated(new Date());
        appViewModel.insertChat(groupChat);
        Log.d("MainActivity", "Chat created for group: " + groupId);

        Map<String, Object> chatData = new HashMap<>();
        chatData.put("group_id", groupId);
        chatData.put("is_group_chat", true);
        chatData.put("timestamp", Converter.toFirestoreTimestamp(new Date()));
        chatData.put("last_updated", Converter.toFirestoreTimestamp(new Date()));
        fs.collection("Chat").document(groupId + "_chat")
                .set(chatData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "Chat synced to Firebase for group: " + groupId))
                .addOnFailureListener(e -> Log.e("MainActivity", "Failed to sync chat for group: " + groupId, e));
    }

    /**
     * Syncs group memberships (for mentor and students) for a given group.
     * If the mentor or students data is missing, re-check after a delay.
     */
    private void syncParticipantsForGroup(Group group, String courseId) {
        observeOnce(appViewModel.getCourseGroupInfo(courseId), this, info -> {
            if (info == null) return;

            boolean recheckNeeded = false;

            if (info.getMentor() != null) {
                addParticipantToFirebaseGroup(group.getGroup_id(), info.getMentor().getId(), "Mentor");
            } else {
                recheckNeeded = true;
            }

            if (info.getStudents() != null && !info.getStudents().isEmpty()) {
                for (Student student : info.getStudents()) {
                    addParticipantToFirebaseGroup(group.getGroup_id(), student.getId(), "Student");
                }
            } else {
                recheckNeeded = true;
            }

            if (recheckNeeded) {
                // If mentor or students are not yet available, re-check after a delay.
                recheckSyncForCourse(courseId, 2000);

            }
        });
    }

    /**
     * Adds a participant to Firebase for a given group.
     * If the membership is not yet synced, it writes to Firebase and then inserts the membership into Room.
     */
    private void addParticipantToFirebaseGroup(String groupId, String participantId, String memberType) {
        String docId = groupId + "_" + participantId;
        observeOnce(appViewModel.getGroupMembershipByGroupIdAndMemberId(groupId, participantId), this, existingMembership -> {
            if (existingMembership != null && existingMembership.isIs_synced()) {
                Log.d("MainActivity", "Participant already synced, skipping: " + participantId);
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("group_id", groupId);
            data.put("member_id", participantId);
            data.put("member_type", memberType);
            data.put("last_updated", Converter.toFirestoreTimestamp(new Date()));

            fs.collection("GroupMembership").document(docId)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("MainActivity", "Participant added to Firebase: " + participantId);
                        GroupMembership newMembership = new GroupMembership(docId, groupId, participantId, memberType, true, new Date());
                        appViewModel.insertGroupMembership(newMembership);
                    })
                    .addOnFailureListener(e -> Log.e("MainActivity", "Failed to add participant: " + participantId, e));
        });
    }

    /**
     * Helper method: Observe a LiveData only once.
     */
    public static <T> void observeOnce(LiveData<T> liveData, LifecycleOwner owner, Observer<T> observer) {
        liveData.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                observer.onChanged(t);
                liveData.removeObserver(this);
            }
        });
    }



    private String generateUniqueGroupId(String courseId) {
        return "group_" + courseId;
    }

    public void loadSearchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SearchFragment())
                .addToBackStack(null)
                .commit();
    }
}
