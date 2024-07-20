package com.example.youtube_project.home;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class VideoItem {
    private Uri videoResIdUri;
    private int videoResIdInt;
    private String title;
    private int thumbnailResId;
    private Uri thumbnailUri;
    private String author;
    private String date;
    private String views;
    private String description;
    private List<String> likes;
    private List<String> dislikes;

    private List<Comment> comments;

    private int likesAmount;
    private int disLikesAmount;

    public VideoItem(Uri videoResId, String title, int thumbnailResId, String author, String date, String views) {
        this.videoResIdUri = videoResId;
        this.title = title;
        this.thumbnailResId = thumbnailResId;
        this.author = author;
        this.date = date;
        this.views = views;
        this.description = null;
        this.thumbnailUri = null;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        likesAmount = 0;
        disLikesAmount = 0;
        this.comments = new ArrayList<>();
    }

    public VideoItem(int videoResId, String title, int thumbnailResId, String author, String date, String views) {
        this.videoResIdInt = videoResId;
        this.title = title;
        this.thumbnailResId = thumbnailResId;
        this.author = author;
        this.date = date;
        this.views = views;
        this.description = null;
        this.thumbnailUri = null;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        likesAmount = 0;
        disLikesAmount = 0;
        this.comments = new ArrayList<>();
    }

    public int getLikesAmount() {
        int num = likes.size();
        if (likesAmount != num){
            likesAmount = num;
        }
        return likesAmount;
    }

    public int getDisLikesAmount() {
        int num = dislikes.size();
        if (disLikesAmount != num){
            disLikesAmount = num;
        }
        return disLikesAmount;
    }

    public void addLike(String username) {
        if (likes.contains(username) && !dislikes.contains(username)) {
            likes.remove(username);
            likesAmount -=1;
        } else if (dislikes.contains(username) && !likes.contains(username)) {
            dislikes.remove(username);
            likes.add(username);
            disLikesAmount -=1;
            likesAmount +=1;
        } else {
            likes.add(username);
            likesAmount +=1;
        }
    }

    public void addDislike(String username) {
        if (likes.contains(username) && !dislikes.contains(username)) {
            likes.remove(username);
            dislikes.add(username);
            disLikesAmount +=1;
            likesAmount -=1;
        } else if (dislikes.contains(username) && !likes.contains(username)) {
            dislikes.remove(username);
            disLikesAmount -=1;
        } else {
            dislikes.add(username);
            disLikesAmount +=1;
        }
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public int getVideoResIdInt() {
        return videoResIdInt;
    }

    public Uri getVideoResIdUri() {
        return videoResIdUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public void setThumbnailResId(int thumbnailResId) {
        this.thumbnailResId = thumbnailResId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public boolean isUri() {
        return videoResIdUri != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addComment(Comment newComment){
        comments.add(newComment);
    }
    public void deleteComment(Comment rem_comment){
        if (comments.contains(rem_comment)){
            comments.remove(rem_comment);
        }
    }

    public List<Comment> getComments() {
        return comments;
    }
}
