package com.example.youtube_project.user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("password")
    private String password;

    @SerializedName("photo")
    private String photoUrl;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("videos")
    private List<String> videoIds;  // List of video IDs associated with the user

    @SerializedName("comments")
    private List<String> commentIds;  // List of comment IDs associated with the user

    // Constructor
    public User(String username, String displayName, String password, String photoUrl) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.photoUrl = photoUrl;
        this.createdAt = new Date();
        this.videoIds = new ArrayList<>();
        this.commentIds = new ArrayList<>();
    }

    // Getters and setters...

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(List<String> videoIds) {
        this.videoIds = videoIds;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }
}
