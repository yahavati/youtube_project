package com.example.youtube_project.home;

import android.net.Uri;

public class Comment {
    private String username;
    private String text;
    private int likeCount;
    private boolean isLiked;
    private boolean isDisliked;
    private Uri userPhoto;

    public Comment(String username, String text, int likeCount, boolean isLiked, boolean isDisliked, Uri userPhoto) {
        this.username = username;
        this.text = text;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.userPhoto = userPhoto;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(Uri userPhotoUri) {
        this.userPhoto = userPhotoUri;
    }
    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }
}
