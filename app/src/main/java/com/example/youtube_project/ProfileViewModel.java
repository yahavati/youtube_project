package com.example.youtube_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> profilePhotoUrl = new MutableLiveData<>();

    public void setProfilePhotoUrl(String url) {
        profilePhotoUrl.setValue(url);
    }

    public LiveData<String> getProfilePhotoUrl() {
        return profilePhotoUrl;
    }
}