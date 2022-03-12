package com.ethanchris.android.healthpet.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public AuthViewModel create(@NonNull Class aClass) {
        return new AuthViewModel(FirebaseAuth.getInstance());
    }
}
