package com.example.youtube_project;

import com.example.youtube_project.home.VideoItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;

public class ApiHandler {
    private static final String BASE_URL = "http://192.168.221.1:5000/api";
    private static final OkHttpClient httpClient;
    public static String jwtToken = null;

    private static final Gson gson = new Gson();

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static OkHttpClient getClient() {
        return httpClient;
    }
    public static void setJwtToken(String token) {
        jwtToken = token;
    }

    public static boolean isLoggedIn() {
        return jwtToken != null;
    }

    public static void clearToken() {
        jwtToken = null;
    }

    private static Request.Builder addAuthHeader(Request.Builder builder) {
        if (jwtToken != null) {
            builder.addHeader("Authorization", "Bearer " + jwtToken);
        }
        return builder;
    }

    public static VideoItem fetchVideoById(String videoId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/videos")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            JsonArray videoArray = jsonObject.getAsJsonArray("data");

            for (JsonElement element : videoArray) {
                JsonObject videoObject = element.getAsJsonObject();
                String id = videoObject.get("_id").getAsString();
                if (id.equals(videoId)) {
                    return gson.fromJson(videoObject, VideoItem.class);
                }
            }
        }

        return null; // Return null if no video with the given ID is found
    }

    private static Request buildRequest(String url, String method, RequestBody body) {
        Request.Builder builder = new Request.Builder().url(BASE_URL + url);
        builder = addAuthHeader(builder);
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

    public static Response sendGet(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build();

        request = addAuthHeader(request.newBuilder()).build();
        return httpClient.newCall(request).execute();
    }




    public static Response sendPost(String endpoint, JsonObject payload) throws IOException {
        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = buildRequest(endpoint, "POST", body);
        Response response  =  httpClient.newCall(request).execute();


        return response;


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
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody);
        builder = addAuthHeader(builder);
        Request request = builder.build();
        return httpClient.newCall(request).execute();
    }

    public static Response postFile(String url, File file, String fileFieldName, JsonObject json) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart(fileFieldName, file.getName(), RequestBody.create(file, MediaType.get("image/jpeg")));

        for (String key : json.keySet()) {
            builder.addFormDataPart(key, json.get(key).getAsString());
        }

        RequestBody body = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + url)
                .post(body);
        requestBuilder = addAuthHeader(requestBuilder);
        Request request = requestBuilder.build();
        return httpClient.newCall(request).execute();
    }

    public static Response fetchAllVideos() throws IOException {
        return sendGet("/videos");
    }

    public static Response uploadVideo(RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL+"/videos")
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }

    public static Response uploadVideo(JsonObject payload) throws IOException {
        return sendPost("/videos", payload);
    }
    public static Response likeVideo(String videoId, String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        return sendPost("/videos/" + videoId + "/like", payload);
    }

    

    public static Response dislikeVideo(String videoId, String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        return sendPost("/videos/" + videoId + "/dislike", payload);
    }

    public static Response addComment(String videoId, String username, String commentText) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        payload.addProperty("text", commentText);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                payload.toString()
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/videos/" + videoId + "/comments")
                .post(body)
                .build();

        request = addAuthHeader(request.newBuilder()).build();
        return httpClient.newCall(request).execute();
    }

    public static Response incrementViews(String videoId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/videos/" + videoId + "/view")
                .get()
                .build();
        return httpClient.newCall(request).execute();
    }
    public static Response likeComment(String videoId, String commentId, String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        return sendPost("/videos/" + videoId + "/comments/" + commentId + "/like", payload);
    }

    public static Response dislikeComment(String videoId, String commentId, String username) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", username);
        return sendPost("/videos/" + videoId + "/comments/" + commentId + "/dislike", payload);
    }

    public static Response editComment(String videoId, String commentId, String newText) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("text", newText)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.109.1:5000/api/videos/" + videoId + "/comments/" + commentId)
                .put(body)
                .build();
        return httpClient.newCall(request).execute();
    }

    public static Response deleteComment(String videoId, String commentId) throws IOException {
        Request request = new Request.Builder()
                .url("http://192.168.109.1:5000/api/videos/" + videoId + "/comments/" + commentId)
                .delete()
                .build();
        return httpClient.newCall(request).execute();
    }

    public static Response fetchRandomVideos() throws IOException {
        return sendGet("/allVideos");
    }
}
