package com.ethanchris.android.healthpet.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class GoogleFitViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public GoogleFitViewModel create(@NonNull Class aClass) {
        return new GoogleFitViewModel();
    }
}
