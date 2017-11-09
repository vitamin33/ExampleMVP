package com.mvp.example.messages.domain.usecase;


import android.support.annotation.NonNull;

import com.mvp.example.UseCase;
import com.mvp.example.data.source.TasksDataSource;
import com.mvp.example.data.source.TasksRepository;
import com.mvp.example.messages.domain.model.FriendlyMessage;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetMessages extends UseCase<GetMessages.RequestValues, GetMessages.ResponseValue> {

    private final TasksRepository mMessagesRepository;

    public GetMessages(TasksRepository repository) {
        mMessagesRepository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        if (requestValues.isForceUpdate()) {
            mMessagesRepository.refreshMessages();
        }

        mMessagesRepository.getMessages(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<FriendlyMessage> tasks) {

                getUseCaseCallback().onSuccess(new ResponseValue(tasks));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<FriendlyMessage> mTasks;

        public ResponseValue(@NonNull List<FriendlyMessage> tasks) {
            mTasks = checkNotNull(tasks, "tasks cannot be null!");
        }

        public List<FriendlyMessage> getTasks() {
            return mTasks;
        }
    }
}
