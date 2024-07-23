package com.example.youtube_project.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.ApiHandler;
import com.example.youtube_project.Constants;
import com.example.youtube_project.CustomMediaController;
import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Response;

public class VideoDetailFragment extends Fragment {

    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoDetails;
    private TextView videoDescription;
    private RecyclerView recyclerView;
    private LinearLayout likeButton;
    private LinearLayout dislikeButton;
    private LinearLayout shareButton;
    private LinearLayout saveButton;
    private LinearLayout commentsSection;

    private ImageView fullscreenButton;
    private boolean isFullScreen = false;


    private TextView likeCountText;
    private TextView dislikeCountText;

    private int likeCount = 0;
    private int dislikeCount = 0;
    private String currentVideoTitle;
    private FrameLayout videoContainer;
    private ScrollView contentScroll;
    private FrameLayout rootLayout;
    private VideoItem currentVideo;

    private static final String TAG = "VideoDetailFragment";
    private Button viewAuthorVideosButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_detail, container, false);

        videoView = view.findViewById(R.id.video_view);
        videoTitle = view.findViewById(R.id.video_title);
        videoDetails = view.findViewById(R.id.video_details);
        videoDescription = view.findViewById(R.id.video_description);
        recyclerView = view.findViewById(R.id.recycler_view);
        likeButton = view.findViewById(R.id.button_like);
        dislikeButton = view.findViewById(R.id.button_dislike);
        shareButton = view.findViewById(R.id.button_share);
        saveButton = view.findViewById(R.id.button_save);
        commentsSection = view.findViewById(R.id.comments_section);
        likeCountText = view.findViewById(R.id.like_count);
        dislikeCountText = view.findViewById(R.id.dislike_count);
        rootLayout = view.findViewById(R.id.root_layout);
        videoContainer = view.findViewById(R.id.video_container);
        contentScroll = view.findViewById(R.id.content_scroll);

        viewAuthorVideosButton = view.findViewById(R.id.button_view_author_videos);

        if (currentVideo != null) {
            updateVideoDetails(currentVideo);
            initializeVideo(currentVideo);
        }

        loadVideos();

        likeButton.setOnClickListener(v -> {
            if (isLoggedIn()) {
                handleLike();
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        dislikeButton.setOnClickListener(v -> {
            if (isLoggedIn()) {
                handleDislike();
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        shareButton.setOnClickListener(v -> showShareDialog());

        saveButton.setOnClickListener(v -> {
            // Implement save functionality
        });

        commentsSection.setOnClickListener(v -> openCommentsFragment());
        viewAuthorVideosButton.setOnClickListener(v -> showAuthorVideosDialog(currentVideo.getAuthor()));

        return view;
    }

    public void toggleFullScreen(boolean fullScreen) {
        if (fullScreen) {
            // Save the current video position
            int currentPosition = videoView.getCurrentPosition();

            // Hide the system UI
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            // Set the video container to match the screen height
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int screenHeight = size.y;

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    screenHeight);
            videoContainer.setLayoutParams(layoutParams);

            // Hide the content scroll view
            contentScroll.setVisibility(View.GONE);

            // Resume the video from the saved position
            videoView.seekTo(currentPosition);
        } else {
            // Save the current video position
            int currentPosition = videoView.getCurrentPosition();

            // Show the system UI
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE);

            // Set the video container back to its original size
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(getContext(), 250));
            videoContainer.setLayoutParams(layoutParams);

            // Show the content scroll view
            contentScroll.setVisibility(View.VISIBLE);

            // Adjust the content scroll view's layout params
            FrameLayout.LayoutParams scrollViewParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            scrollViewParams.topMargin = dpToPx(getContext(), 250);
            contentScroll.setLayoutParams(scrollViewParams);

            // Resume the video from the saved position
            videoView.seekTo(currentPosition);
        }
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void showAuthorVideosDialog(String author) {
        new Thread(() -> {
            try {
                Response response = ApiHandler.fetchAllVideos();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray videoArray = jsonObject.getAsJsonArray("data");

                    List<VideoItem> videos = new ArrayList<>();
                    for (JsonElement element : videoArray) {
                        VideoItem video = new Gson().fromJson(element, VideoItem.class);
                        if (video.getAuthor().equals(author)) {
                            videos.add(video);
                        }
                    }

                    getActivity().runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_author_videos, null);
                        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view_author_videos);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        VideoAdapter videoAdapter = new VideoAdapter(getContext(), videos);
                        recyclerView.setAdapter(videoAdapter);

                        builder.setView(dialogView)
                                .setTitle("Videos by " + author)
                                .setPositiveButton("Close", (dialog, id) -> dialog.dismiss());
                        builder.create().show();
                    });
                } else {
                    Log.e(TAG, "Failed to fetch author's videos: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching author's videos", e);
            }
        }).start();
    }

    private void loadVideos() {
        new Thread(() -> {
            try {
                Response response = ApiHandler.fetchAllVideos();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray videoArray = jsonObject.getAsJsonArray("data");

                    List<VideoItem> videos = new ArrayList<>();
                    for (JsonElement element : videoArray) {
                        VideoItem video = new Gson().fromJson(element, VideoItem.class);
                        videos.add(video);
                    }

                    getActivity().runOnUiThread(() -> {
                        VideoAdapter videoAdapter = new VideoAdapter(getContext(), videos);
                        videoAdapter.setOnItemClickListener(this::playVideo);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(videoAdapter);
                    });
                } else {
                    Log.e(TAG, "Failed to fetch videos: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching videos", e);
            }
        }).start();
    }

    private void handleLike() {
        new Thread(() -> {
            try {
                Response response = ApiHandler.likeVideo(currentVideo.getId(), getCurrentUsername());
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("data") && !jsonResponse.get("data").isJsonNull()) {
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        if (data.has("likes") && !data.get("likes").isJsonNull()) {
                            currentVideo.setLikesAmount(data.get("likes").getAsJsonArray().size());
                        }
                        if (data.has("dislikes") && !data.get("dislikes").isJsonNull()) {
                            currentVideo.setDislikesAmount(data.get("dislikes").getAsJsonArray().size());
                        }
                        getActivity().runOnUiThread(() -> {
                            updateLikeDislikeCounts();
                            updateButtons();
                            Log.d(TAG, "Liked video successfully");
                        });
                    } else {
                        Log.e(TAG, "Response data is null or missing");
                    }
                } else {
                    Log.e(TAG, "Failed to like video: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error liking video", e);
            }
        }).start();
    }

    private void handleDislike() {
        new Thread(() -> {
            try {
                Response response = ApiHandler.dislikeVideo(currentVideo.getId(), getCurrentUsername());
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("data") && !jsonResponse.get("data").isJsonNull()) {
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        if (data.has("likes") && !data.get("likes").isJsonNull()) {
                            currentVideo.setLikesAmount(data.get("likes").getAsJsonArray().size());
                        }
                        if (data.has("dislikes") && !data.get("dislikes").isJsonNull()) {
                            currentVideo.setDislikesAmount(data.get("dislikes").getAsJsonArray().size());
                        }
                        getActivity().runOnUiThread(() -> {
                            updateLikeDislikeCounts();
                            updateButtons();
                            Log.d(TAG, "Disliked video successfully");
                        });
                    } else {
                        Log.e(TAG, "Response data is null or missing");
                    }
                } else {
                    Log.e(TAG, "Failed to dislike video: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error disliking video", e);
            }
        }).start();
    }

    private void updateLikeDislikeCounts() {
        likeCount = currentVideo.getLikesAmount();
        dislikeCount = currentVideo.getDislikesAmount();
        likeCountText.setText(String.valueOf(likeCount));
        dislikeCountText.setText(String.valueOf(dislikeCount));
    }

    private void updateButtons() {
        String currentUsername = getCurrentUsername();
        if (currentVideo.getLikes().contains(currentUsername)) {
            likeButton.setBackgroundColor(Color.GREEN);
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        } else if (currentVideo.getDislikes().contains(currentUsername)) {
            dislikeButton.setBackgroundColor(Color.RED);
            likeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
            likeButton.setBackgroundColor(Color.TRANSPARENT);
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private String getCurrentUsername() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.getString("username", "");
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.contains("username");
    }

    private void playVideo(VideoItem video) {
        currentVideoTitle = video.getTitle();
        currentVideo = video;
        updateVideoDetails(video);
        initializeVideo(video);
        incrementViewCount(video.getId());
    }

    private void incrementViewCount(String videoId) {
        new Thread(() -> {
            try {
                Response response = ApiHandler.incrementViews(videoId);
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("data") && !jsonResponse.get("data").isJsonNull()) {
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        if (data.has("views")) {
                            int newViewCount = data.get("views").getAsInt();
                            getActivity().runOnUiThread(() -> {
                                currentVideo.setViews(newViewCount);
                                updateVideoDetails(currentVideo);
                            });
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to increment view count: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error incrementing view count", e);
            }
        }).start();
    }

    private void initializeVideo(VideoItem video) {
        if (video != null && video.getVideoPath() != null) {
            videoView.setVideoURI(Uri.parse(Constants.URL + video.getVideoPath().replace("\\", "/")));
        }
        videoView.start();

        CustomMediaController mediaController = new CustomMediaController(getContext(), this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        updateLikeDislikeCounts();
        updateButtons();
    }

    private void updateVideoDetails(VideoItem video) {
        Date date = video.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        currentVideoTitle = video.getTitle();
        currentVideo = video;
        videoTitle.setText(video.getTitle());
        videoDetails.setText(video.getAuthor() + " • " + formattedDate + " • " + video.getViews() + " views");
        videoDescription.setText(video.getDescription());
    }

    private void openCommentsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new CommentsFragment(this.currentVideo));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showShareDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fake_share_menu, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        ImageView closeButton = dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.facebook_option).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sharing to Facebook", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.twitter_option).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sharing to Twitter", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.whatsapp_option).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sharing to WhatsApp", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.email_option).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sharing via Email", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    public void setCurrentVideo(VideoItem currentVideo) {
        this.currentVideo = currentVideo;
    }
}
