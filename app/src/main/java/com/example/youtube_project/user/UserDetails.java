package com.example.youtube_project.user;

import android.net.Uri;

public class UserDetails {
    private String password;
    private String nickName;
    private Uri profilePhoto;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Uri profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
