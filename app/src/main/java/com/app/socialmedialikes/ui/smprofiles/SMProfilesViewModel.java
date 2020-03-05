package com.app.socialmedialikes.ui.smprofiles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SMProfilesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SMProfilesViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}