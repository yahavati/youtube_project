package com.example.youtube_project.user;



import java.util.HashMap;

import java.util.Map;
public class UserManager {
    private static UserManager instance;
    private Map<String, UserDetails> users;
    private String currentUser;

    private UserManager() {
        users = new HashMap<>();

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

    public String getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
