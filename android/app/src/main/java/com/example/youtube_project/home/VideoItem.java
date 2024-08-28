package com.example.youtube_project.home;

import com.example.youtube_project.user.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoItem {
    @SerializedName("_id")
    private String id;

    private String title;
    private String description;
    private String url;
    private String thumbnail;

    @SerializedName("user")
    private User user;  // References the User object

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("comments")
    private List<String> commentIds;  // References comments by ID

    private int likes;
    private int dislikes;
    private int views;

    @SerializedName("likedBy")
    private List<String> likedBy;  // List of user IDs who liked the video

    @SerializedName("dislikedBy")
    private List<String> dislikedBy;  // List of user IDs who disliked the video

    @SerializedName("viewedBy")
    private List<String> viewedBy;  // List of user IDs who viewed the video

    public VideoItem(String title, String url, User user) {
        this.title = title;
        this.url = url;
        this.user = user;
        this.createdAt = new Date();
        this.likes = 0;
        this.dislikes = 0;
        this.views = 0;
        this.likedBy = new ArrayList<>();
        this.dislikedBy = new ArrayList<>();
        this.viewedBy = new ArrayList<>();
        this.commentIds = new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public List<String> getDislikedBy() {
        return dislikedBy;
    }

    public void setDislikedBy(List<String> dislikedBy) {
        this.dislikedBy = dislikedBy;
    }

    public List<String> getViewedBy() {
        return viewedBy;
    }

    public void setViewedBy(List<String> viewedBy) {
        this.viewedBy = viewedBy;
    }

    public void addLike(String userId) {
        if (!likedBy.contains(userId)) {
            likedBy.add(userId);
            dislikedBy.remove(userId);
            likes = likedBy.size();
            dislikes = dislikedBy.size();
        }
    }

    public void addDislike(String userId) {
        if (!dislikedBy.contains(userId)) {
            dislikedBy.add(userId);
            likedBy.remove(userId);
            likes = likedBy.size();
            dislikes = dislikedBy.size();
        }
    }

    public void addView(String userId) {
        if (!viewedBy.contains(userId)) {
            viewedBy.add(userId);
            views = viewedBy.size();
        }
    }

    public void addComment(String commentId) {
        commentIds.add(commentId);
    }

    public void removeComment(String commentId) {
        commentIds.remove(commentId);
    }
}
