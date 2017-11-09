/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.mvp.example;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvp.example.data.source.TasksRepository;
import com.mvp.example.data.source.local.TasksLocalDataSource;
import com.mvp.example.data.source.remote.TasksRemoteDataSource;
import com.mvp.example.messages.domain.usecase.AddMessage;
import com.mvp.example.messages.domain.usecase.GetMessages;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations at compile time.
 */
public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }

    public static GetMessages provideGetMessages(@NonNull Context context) {
        return new GetMessages(provideTasksRepository(context));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

     public static AddMessage provideSaveTask(Context context) {
        return new AddMessage(Injection.provideTasksRepository(context));
    }
}
