package com.ethanchris.android.healthpet.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeScreenViewModel extends ViewModel {
    private MutableLiveData title;

    public HomeScreenViewModel(MutableLiveData title) {
        this.title = title;
    }
}
