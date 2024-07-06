package com.example.youtube_project.home;

public class VideoItem {
    private int videoResId;
    private String title;
    private int thumbnailResId;
    private String author;
    private String date;
    private String views;

    public VideoItem(int videoResId, String title, int thumbnailResId, String author, String date, String views) {
        this.videoResId = videoResId;
        this.title = title;
        this.thumbnailResId = thumbnailResId;
        this.author = author;
        this.date = date;
        this.views = views;
    }

    public int getVideoResId() {
        return videoResId;
    }

    public String getTitle() {
        return title;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getViews() {
        return views;
    }
}
