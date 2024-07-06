package com.example.youtube_project.home;

public class Comment {
    private String username;
    private String text;
    private int likeCount;

    public Comment(String username, String text, int likeCount) {
        this.username = username;
        this.text = text;
        this.likeCount = likeCount;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
