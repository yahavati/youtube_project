package com.example.youtube_project.home;

import com.example.youtube_project.user.User;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Comment {

    @SerializedName("_id")
    private String id;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    private String username;

    private String text;

    @SerializedName("createdAt")
    private Date date;

    private String likes;

    private String dislikes;

    private String userPhoto;  // Base64 encoded string

    private String userId;

    private String displayName;

    private String videoId;

    // Constructor
    public Comment(String id, String text, String userId, String displayName, String userPhoto, String likes, String dislikes, String videoId, Date date) {
        this.id = id;
        this.username = displayName; // Assuming displayName is the username displayed
        this.text = text;
        this.userId = userId;
        this.displayName = displayName;
        this.userPhoto = userPhoto;
        this.likes = likes;
        this.dislikes = dislikes;
        this.videoId = videoId;
        this.date = date;
    }
    public Comment() {}


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
