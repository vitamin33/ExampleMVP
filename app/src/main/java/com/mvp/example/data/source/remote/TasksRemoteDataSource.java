/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mvp.example.data.source.remote;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mvp.example.data.source.TasksDataSource;
import com.mvp.example.messages.MainActivity;
import com.mvp.example.messages.MessagesAdapter;
import com.mvp.example.messages.domain.model.FriendlyMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class TasksRemoteDataSource implements TasksDataSource {

    private static final String TAG = TasksRemoteDataSource.class.getSimpleName();

    public static final String MESSAGES_CHILD = "messages";

    private static TasksRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, FriendlyMessage> TASKS_SERVICE_DATA;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.", "");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!", "");
    }

    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private TasksRemoteDataSource() {

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private static void addTask(String title, String description, String imageUrl) {
        FriendlyMessage newTask = new FriendlyMessage(title, description, imageUrl);
        TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    @Override
    public void getMessages(final @NonNull TasksDataSource.LoadTasksCallback callback) {

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);

        final Handler handler = new Handler(Looper.getMainLooper());

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Database snapshot: " + String.valueOf(dataSnapshot));


                final List<FriendlyMessage> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "message: " + String.valueOf(messageSnapshot));
                    FriendlyMessage friendlyMessage = messageSnapshot.getValue(FriendlyMessage.class);
                    if (friendlyMessage != null) {
                        friendlyMessage.setId(dataSnapshot.getKey());
                    }
                    messages.add(friendlyMessage);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTasksLoaded(messages);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailableError();
                    }
                });
            }
        });
    }

    @Override
    public void saveMessage(@NonNull FriendlyMessage task) {

        mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                .push().setValue(task);
    }



    @Override
    public void refreshMessages() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }
}
