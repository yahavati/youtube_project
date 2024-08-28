package com.example.youtube_project;

import android.util.Log;
import android.widget.Toast;

import com.example.youtube_project.user.UserManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiHandler {
    private static final String BASE_URL = "http://10.25.1.226:5000/api";
    private static final OkHttpClient httpClient;
    public static String jwtToken = "";

    private static final Gson gson = new Gson();



    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + jwtToken)
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .connectTimeout(120, TimeUnit.SECONDS) // Increase connect timeout
                .writeTimeout(120, TimeUnit.SECONDS) // Increase write timeout
                .readTimeout(120, TimeUnit.SECONDS) // Increase read timeout
                .build();

    }

    public static Response fetchRandomVideos() throws IOException {

        return sendGet("/videos/");
    }

    public static OkHttpClient getClient() {
        return httpClient;
    }

    public static void setJwtToken(String token) {
        jwtToken = token;
    }

    public static boolean isLoggedIn() {
        return jwtToken != null && !jwtToken.isEmpty();
    }

    public static void clearToken() {
        jwtToken = null;
    }

    private static Request buildRequest(String url, String method, RequestBody body) {
        Request.Builder builder = new Request.Builder().url(BASE_URL + url);
        switch (method.toUpperCase()) {
            case "POST":
                builder.post(body);
                break;
            case "PUT":
                builder.put(body);
                break;
            case "DELETE":
                builder.delete();
                break;
            default:
                builder.get();
                break;
        }
        return builder.build();
    }

    public static Response fetchCommentById(String commentId) throws IOException {
        return sendGet("/comments/" + commentId);
    }

    public static Response fetchToken(String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);

        Response response = sendPost("/tokens", payload);
        System.out.print("response: " + response.message());

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            Log.d("ApiHandler", "Response body: " + responseBody);
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            if (jsonResponse.has("token")) {
                setJwtToken(jsonResponse.get("token").getAsString());
            } else {
                Log.e("ApiHandler", "Token not found in response");
            }
        } else {
            Log.e("ApiHandler", "Failed to fetch token: " + response.message());
        }

        return response;
    }

    public static Response sendGet(String endpoint) throws IOException {
        ensureToken();
        Log.d("ApiHandler", "Sending GET request to: " + BASE_URL + endpoint);
        Log.d("ApiHandler", "Token being sent: " + jwtToken);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .build();

        return httpClient.newCall(request).execute();
    }

    public static Response sendPost(String endpoint, JsonObject payload) throws IOException {
        ensureToken();
        Log.d("ApiHandler", "Sending POST request to: " + BASE_URL + endpoint);
        Log.d("ApiHandler", "Token being sent: " + jwtToken);

        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        return httpClient.newCall(request).execute();
    }


    private static void ensureToken() throws IOException {
        if (jwtToken.isEmpty()) {
            UserManager userManager = UserManager.getInstance();
            if (userManager.isLoggedIn()) {
                String username = userManager.getCurrentUserName();
                fetchTokenWithoutRecursion(username);
            } else {
                Log.e("ApiHandler", "User not logged in, cannot fetch JWT token");
            }
        }
    }

    private static void fetchTokenWithoutRecursion(String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);

        Request request = new Request.Builder()
                .url(BASE_URL + "/tokens")
                .post(RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8")))
                .build();

        Response response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            Log.d("ApiHandler", "Response body: " + responseBody);
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            if (jsonResponse.has("token")) {
                setJwtToken(jsonResponse.get("token").getAsString());
            } else {
                Log.e("ApiHandler", "Token not found in response");
            }
        } else {
            Log.e("ApiHandler", "Failed to fetch token: " + response.message());
        }
    }


    private static void fetchJwtToken() throws IOException {
        // Only fetch token if it's not already set
        if (jwtToken.isEmpty()) {
            UserManager userManager = UserManager.getInstance();
            if (userManager.isLoggedIn()) {
                String username = userManager.getCurrentUserName();
                Response response = fetchToken(username);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                    if (jsonResponse.has("token")) {
                        setJwtToken(jsonResponse.get("token").getAsString()); // Store the JWT token
                    }
                } else {
                    Log.e("ApiHandler", "Failed to fetch JWT token");
                }
            } else {
                Log.e("ApiHandler", "User not logged in, cannot fetch JWT token");
            }
        }
    }
    public static Response sendPut(String endpoint, JsonObject payload) throws IOException {
        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = buildRequest(endpoint, "PUT", body);
        return httpClient.newCall(request).execute();
    }

    public static Response sendDelete(String endpoint) throws IOException {
        Request request = buildRequest(endpoint, "DELETE", null);
        return httpClient.newCall(request).execute();
    }

    public static Response sendMultipartPost(String endpoint, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody)
                .build();
        return httpClient.newCall(request).execute();
    }

    // Authentication Routes
    public static Response registerUser(JsonObject userInfo, File profilePhoto) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("username", userInfo.get("username").getAsString());
        builder.addFormDataPart("password", userInfo.get("password").getAsString());
        builder.addFormDataPart("nickname", userInfo.get("nickname").getAsString());

        if (profilePhoto != null) {
            builder.addFormDataPart("photo", profilePhoto.getName(),
                    RequestBody.create(MediaType.parse("image/*"), profilePhoto));
        }

        RequestBody body = builder.build();
        return sendMultipartPost("/auth/register", body);
    }

    public static Response loginUser(JsonObject credentials) throws IOException {
        return sendPost("/auth/login", credentials);
    }

    public static Response logoutUser() throws IOException {
        return sendPost("/auth/logout", new JsonObject());
    }

    public static Response getCurrentUser() throws IOException {
        return sendGet("/auth/me");
    }

    // Video Routes
    public static Response fetchAllVideos() throws IOException {
        return sendGet("/videos/");
    }

    public static Response fetchVideoById(String videoId) throws IOException {
        return sendGet("/videos/" + videoId);
    }

    public static Response incrementViews(String videoId) throws IOException {
        return sendPost("/videos/" + videoId + "/view", new JsonObject());
    }

    public static Response likeVideo(String videoId) throws IOException {
        return sendPost("/videos/" + videoId + "/like", new JsonObject());
    }

    public static Response dislikeVideo(String videoId) throws IOException {
        return sendPost("/videos/" + videoId + "/dislike", new JsonObject());
    }

    // Comment Routes
    public static Response addComment(String videoId, JsonObject commentData) throws IOException {
        return sendPost("/comments", commentData);
    }

    public static Response deleteComment(String commentId) throws IOException {
        return sendDelete("/comments/" + commentId);
    }

    public static Response updateComment(String commentId, JsonObject commentData) throws IOException {
        RequestBody body = RequestBody.create(commentData.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/comments/" + commentId)
                .put(body)
                .build();
        return httpClient.newCall(request).execute();
    }

    public static Response likeComment(String commentId) throws IOException {
        return sendPost("/comments/" + commentId + "/like", new JsonObject());
    }

    public static Response dislikeComment(String commentId) throws IOException {
        return sendPost("/comments/" + commentId + "/dislike", new JsonObject());
    }

    // Token Routes
    public static Response refreshToken(String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        return sendPost("/tokens", payload);
    }

    // User Routes
    public static Response createUser(JsonObject userInfo, File profilePhoto) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("username", userInfo.get("username").getAsString());
        builder.addFormDataPart("password", userInfo.get("password").getAsString());
        builder.addFormDataPart("nickname", userInfo.get("nickname").getAsString());

        if (profilePhoto != null) {
            builder.addFormDataPart("photo", profilePhoto.getName(),
                    RequestBody.create(MediaType.parse("image/*"), profilePhoto));
        }

        RequestBody body = builder.build();
        return sendMultipartPost("/users", body);
    }

    public static Response getUserById(String userId) throws IOException {
        return sendGet("/users/" + userId);
    }

    public static Response deleteUserById(String userId) throws IOException {
        return sendDelete("/users/" + userId);
    }

    public static Response getUserVideos(String userId) throws IOException {
        return sendGet("/users/" + userId + "/videos");
    }

    public static Response createUserVideo(String userId, File videoFile, JsonObject videoData) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("title", videoData.get("title").getAsString());
        builder.addFormDataPart("description", videoData.get("description").getAsString());

        if (videoFile != null) {
            builder.addFormDataPart("url", videoFile.getName(),
                    RequestBody.create(MediaType.parse("video/*"), videoFile));
        }

        RequestBody body = builder.build();
        return sendMultipartPost("/users/" + userId + "/videos", body);
    }

    public static Response getUserVideo(String userId, String videoId) throws IOException {
        return sendGet("/users/" + userId + "/videos/" + videoId);
    }

    public static Response deleteUserVideo(String userId, String videoId) throws IOException {
        return sendDelete("/users/" + userId + "/videos/" + videoId);
    }
}