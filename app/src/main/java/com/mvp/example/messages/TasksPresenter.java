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

package com.mvp.example.messages;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.mvp.example.UseCase;
import com.mvp.example.UseCaseHandler;
import com.mvp.example.messages.domain.model.FriendlyMessage;
import com.mvp.example.messages.domain.usecase.AddMessage;
import com.mvp.example.messages.domain.usecase.GetMessages;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI , retrieves the data and updates the
 * UI as required.
 */
public class TasksPresenter implements TasksContract.Presenter {


    private final TasksContract.View mTasksView;
    private final GetMessages mGetTasks;

    private boolean mFirstLoad = true;

    private final UseCaseHandler mUseCaseHandler;

    public TasksPresenter(@NonNull UseCaseHandler useCaseHandler,
                          @NonNull TasksContract.View tasksView, @NonNull GetMessages getTasks,
                          @NonNull AddMessage completeTask) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");
        mGetTasks = checkNotNull(getTasks, "getTask cannot be null!");

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(true,false);
    }

    @Override
    public void loadMessages(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }

        GetMessages.RequestValues requestValue = new GetMessages.RequestValues(forceUpdate);

        mUseCaseHandler.execute(mGetTasks, requestValue,
                new UseCase.UseCaseCallback<GetMessages.ResponseValue>() {
                    @Override
                    public void onSuccess(GetMessages.ResponseValue response) {
                        List<FriendlyMessage> tasks = response.getTasks();
                        // The view may not be able to handle UI updates anymore
                        if (!mTasksView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mTasksView.setLoadingIndicator(false);
                        }

                        processTasks(tasks);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mTasksView.isActive()) {
                            return;
                        }
                        mTasksView.showLoadingTasksError();
                    }
                });
    }

    private void processTasks(List<FriendlyMessage> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTasksView.showTasks(tasks);
        }
    }

    private void processEmptyTasks() {
        mTasksView.showNoTasks();
    }

    @Override
    public void addNewMessage() {
        mTasksView.showAddTask();
    }

}
