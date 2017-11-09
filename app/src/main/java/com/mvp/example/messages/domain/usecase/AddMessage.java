package com.mvp.example.messages.domain.usecase;

import android.support.annotation.NonNull;

import com.mvp.example.UseCase;
import com.mvp.example.data.source.TasksRepository;
import com.mvp.example.messages.domain.model.FriendlyMessage;

import static com.google.common.base.Preconditions.checkNotNull;


public class AddMessage extends UseCase<AddMessage.RequestValues, AddMessage.ResponseValue> {

    private final TasksRepository mRepository;

    public AddMessage(TasksRepository repository) {
        mRepository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mRepository.saveMessage(requestValues.getCompletedTask());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final FriendlyMessage mAddMessage;

        public RequestValues(@NonNull FriendlyMessage message) {
            mAddMessage = checkNotNull(message, "completedTask cannot be null!");
        }

        public FriendlyMessage getCompletedTask() {
            return mAddMessage;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }
}
