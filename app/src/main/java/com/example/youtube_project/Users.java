package com.example.youtube_project;

import java.util.HashMap;

public class Users {
    private HashMap<String, String> userCredentials;

    public Users() {
        userCredentials = new HashMap<>();
//        // Prepopulate with some users for testing
//        userCredentials.put("testuser1", "password1");
//        userCredentials.put("testuser2", "password2");
    }

    public void addUser(String username, String password) {
        userCredentials.put(username, password);
    }

    public boolean validateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public boolean userExists(String username) {
        return userCredentials.containsKey(username);
    }
}
