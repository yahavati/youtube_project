package com.example.youtube_project.user;

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
    private List<OnUserDetailsChangeListener> listeners;

    public interface OnUserDetailsChangeListener {
        void onUserDetailsChanged(UserDetails userDetails);
    }

    private UserManager() {
        users = new HashMap<>();
        isLoggedIn = false;
        videos = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        if (!loggedIn) {
            notifyUserDetailsChanged(null);
        }
    }

    public boolean validateUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = username;
            notifyUserDetailsChanged(users.get(currentUser));
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

    public void setCurrentUser(String username, UserDetails userDetails) {
        currentUser = username;
        users.put(username, userDetails);
        notifyUserDetailsChanged(userDetails);
    }

    public void logout() {
        currentUser = null;
        setLoggedIn(false);
    }

    public List<VideoItem> getVideos() {
        return videos;
    }

    public void addVideo(VideoItem newVideo) {
        videos.add(newVideo);
    }

    public void deleteVideo(VideoItem deleteVideo) {
        videos.remove(deleteVideo);
    }

    public String getCurrentUserName() {
        return currentUser;
    }

    public void addOnUserDetailsChangeListener(OnUserDetailsChangeListener listener) {
        listeners.add(listener);
    }

    public void removeOnUserDetailsChangeListener(OnUserDetailsChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyUserDetailsChanged(UserDetails userDetails) {
        for (OnUserDetailsChangeListener listener : listeners) {
            listener.onUserDetailsChanged(userDetails);
        }
    }
}
