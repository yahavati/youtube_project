package com.example.youtube_project.user;

import android.net.Uri;

import com.example.youtube_project.home.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class UserDetails {
    private String password;
    private String nickName;
    private Uri profilePhoto;
    private List<VideoItem> videos;

    public UserDetails(){
        videos = new ArrayList<>();
    }

    public List<VideoItem> getVideos() {
        return videos;
    }
    public void addVideo(VideoItem newVideo) {
        videos.add(newVideo);
    }
    public void deleteVideo(VideoItem deleteVideo){
        if (videos.contains(deleteVideo)) {
            videos.remove(deleteVideo);
        }
    }

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
