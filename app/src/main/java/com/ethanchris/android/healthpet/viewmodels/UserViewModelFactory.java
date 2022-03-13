package com.ethanchris.android.healthpet.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public UserViewModel create(@NonNull Class aClass) {
        return new UserViewModel(FirebaseFirestore.getInstance());
    }
}
