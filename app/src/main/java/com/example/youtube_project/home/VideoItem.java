package com.example.youtube_project.home;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoItem {
    @SerializedName("_id")
    private String id;

    private String title;
    private String author;
    private String description;
    private Date date;
    private int views;
    private String videoPath;
    private String thumbnailUri;
    private List<String> likes;
    private List<String> dislikes;
    private List<Comment> comments;
    private int likesAmount;
    private int dislikesAmount;

    public VideoItem(String title, String author, String videoPath) {
        this.title = title;
        this.author = author;
        this.videoPath = videoPath;
        this.date = new Date();
        this.views = 0;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.likesAmount = 0;
        this.dislikesAmount = 0;
    }

    // Getters and setters...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLikesAmount(int likesAmount) {
        this.likesAmount = likesAmount;
    }

    public int getLikesAmount() {
        return this.likesAmount;
    }

    public void setDislikesAmount(int dislikesAmount) {
        this.dislikesAmount = dislikesAmount;
    }

    public int getDislikesAmount() {
        return this.dislikesAmount;
    }

    public void addLike(String username) {
        if (!likes.contains(username)) {
            likes.add(username);
            dislikes.remove(username);
            likesAmount = likes.size();
            dislikesAmount = dislikes.size();
        }
    }

    public void addDislike(String username) {
        if (!dislikes.contains(username)) {
            dislikes.add(username);
            likes.remove(username);
            likesAmount = likes.size();
            dislikesAmount = dislikes.size();
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
