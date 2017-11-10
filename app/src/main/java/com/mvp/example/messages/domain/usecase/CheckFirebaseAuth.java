package com.mvp.example.messages.domain.usecase;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mvp.example.UseCase;

public class CheckFirebaseAuth extends UseCase<CheckFirebaseAuth.RequestValues, CheckFirebaseAuth.ResponseValue> {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

    public CheckFirebaseAuth() { }

    @Override
    protected void executeUseCase(CheckFirebaseAuth.RequestValues requestValues) {
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity

            getUseCaseCallback().onSuccess(new CheckFirebaseAuth.ResponseValue(false, mUsername, mPhotoUrl));
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
            getUseCaseCallback().onSuccess(new CheckFirebaseAuth.ResponseValue(true, mUsername, mPhotoUrl));
        }
    }

    public static final class RequestValues implements UseCase.RequestValues {    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final String mUsername;
        private final String mPhotoUrl;
        private final boolean mSignedIn;


        public ResponseValue(boolean signedIn, String username, String photoUrl) {
            mUsername = username;
            mPhotoUrl = photoUrl;
            mSignedIn = signedIn;
        }

        public String getPhotoUrl() {
            return mPhotoUrl;
        }

        public String getUsername() {
            return mUsername;
        }
    }
}
