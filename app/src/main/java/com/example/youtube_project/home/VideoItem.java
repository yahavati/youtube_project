package com.example.youtube_project.home;

import android.net.Uri;

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

    public VideoItem(Uri videoResId, String title, int thumbnailResId, String author, String date, String views) {
        this.videoResIdUri = videoResId;
        this.title = title;
        this.thumbnailResId = thumbnailResId;
        this.author = author;
        this.date = date;
        this.views = views;
        this.description = null;
        this.thumbnailUri = null;
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
}
