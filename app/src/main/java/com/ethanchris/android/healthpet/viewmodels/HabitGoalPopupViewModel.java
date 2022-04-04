package com.ethanchris.android.healthpet.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HabitGoalPopupViewModel extends ViewModel {
    MutableLiveData<String> habitName = new MutableLiveData<String>();
    MutableLiveData<Integer> numDays = new MutableLiveData<Integer>();

    HabitGoalPopupViewModel() {

    }

    public MutableLiveData<String> getHabitName() {
        return this.habitName;
    }

    public MutableLiveData<Integer> getNumDays() {
        return this.numDays;
    }
}
