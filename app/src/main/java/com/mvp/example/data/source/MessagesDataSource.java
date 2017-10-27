package com.mvp.example.data.source;

import android.support.annotation.NonNull;

import com.mvp.example.model.FriendlyMessage;

import java.util.List;

/**
 * Created by vitalii_serbyn on 10/27/17.
 */

public interface MessagesDataSource {

    interface LoadMessagesCallback {

        void onTasksLoaded(List<FriendlyMessage> tasks);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadMessagesCallback callback);

    void saveTask(@NonNull FriendlyMessage task);

    void deleteTask(@NonNull String taskId);
}
