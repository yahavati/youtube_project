package com.example.youtube_project.user;



import android.content.Context;

import com.example.youtube_project.R;
import com.example.youtube_project.home.VideoItem;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class UserManager {
    private static UserManager instance;
    private Map<String, UserDetails> users;
    private String currentUser;

    private List<VideoItem> videos;

    private boolean isLoggedIn;
    private UserManager() {
        users = new HashMap<>();
        isLoggedIn = false;
        videos = new ArrayList<>();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }



    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean validateUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = username;
            return true;
        }
        return false;
    }

    public void addUser(String username, UserDetails userDetails) {
        users.put(username, userDetails);
    }

    public UserDetails getCurrentUser() {
        return users.get(currentUser);
    }

    public void logout() {
        currentUser = null;
    }

    //we have to call makeVideos at the class that is defined in manifest ti run first
    //in order to make sure that these videos will be made, even when I uploaded a video first
    public List<VideoItem> makeVideos() {
        if (videos.isEmpty()){
            videos.add(new VideoItem( R.raw.videoapp1, "Music", R.drawable.img7, "coldplay", "01 Jan 2022", "1000 views"));
            videos.add(new VideoItem(R.raw.videoapp2, "Music", R.drawable.img2, "coldplay", "08 Jan 2023", "1000 views"));
            videos.add(new VideoItem( R.raw.videoapp3, "SPORT", R.drawable.img6, "sport5", "02 Jan 2024", "2000 views"));
            videos.add(new VideoItem( R.raw.videoapp4, "Toys", R.drawable.one, "Lian", "01 Jan 2020", "5 views"));
            videos.add(new VideoItem(R.raw.videoapp5, "Park", R.drawable.two, "Lian", "02 Jan 2021", "2000 views"));
            videos.add(new VideoItem(R.raw.videoapp6, "Window", R.drawable.three, "Yahav", "08 Jan 2023", "1000 views"));
            videos.add(new VideoItem( R.raw.videoapp7, "AirCon", R.drawable.img1, "Yahav", "01 Jan 2022", "30 views"));
            videos.add(new VideoItem(R.raw.videoapp8, "Light", R.drawable.img3, "Ido", "02 Jan 2016", "2000 views"));
            videos.add(new VideoItem(R.raw.videoapp9, "Cupboard", R.drawable.img4, "Ido", "01 Jan 2008", "500 views"));
            videos.add(new VideoItem(R.raw.videoapp10, "Books", R.drawable.img5, "Lian", "02 Jan 2018", "200 views"));
        }
        return videos;
    }
    public List<VideoItem> getVideos() {
        return videos;
    }
    public void addVideo(VideoItem newVideo) {
        videos.add(newVideo);
    }
    public void deleteVideo(VideoItem deleteVideo){
        if (videos.contains(deleteVideo)) {
            videos.remove(deleteVideo);
        }
    }

    public String getCurrentUserName(){
        return currentUser;
    }
}

