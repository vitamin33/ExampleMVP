package com.mvp.example.data.source;

import android.support.annotation.NonNull;

import com.mvp.example.model.FriendlyMessage;

/**
 * Created by vitalii_serbyn on 10/27/17.
 */

public class MessagesRepository implements MessagesDataSource {
    @Override
    public void getTasks(@NonNull LoadMessagesCallback callback) {

    }

    @Override
    public void saveTask(@NonNull FriendlyMessage task) {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
