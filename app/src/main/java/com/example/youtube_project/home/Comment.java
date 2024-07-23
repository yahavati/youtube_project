package com.example.youtube_project.home;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Comment {
    private String username;
    private String text;
    private Date date;

    private List<String> likes;

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    private List<String> dislikes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("_id")
    private String id;


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

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }

    private Uri userPhoto;

    public Comment(String username, String text) {
        this.username = username;
        this.text = text;
        this.date = new Date();
    }

    // Getters and setters for all fields
}