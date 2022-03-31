package com.ethanchris.android.healthpet.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HabitGoalPopupViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public HabitGoalPopupViewModel create(@NonNull Class aClass) {
        return new HabitGoalPopupViewModel();
    }
}
