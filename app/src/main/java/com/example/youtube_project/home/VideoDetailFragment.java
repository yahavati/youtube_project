package com.example.youtube_project.home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.R;

import java.util.ArrayList;
import java.util.List;

public class VideoDetailFragment extends Fragment {

    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoDetails;
    private RecyclerView recyclerView;
    private LinearLayout likeButton;
    private LinearLayout dislikeButton;
    private LinearLayout shareButton;
    private LinearLayout saveButton;
    private LinearLayout commentsSection;
    private TextView likeCountText;
    private TextView dislikeCountText;

    private int likeCount = 0;
    private int dislikeCount = 0;
    private String currentVideoTitle;

    private static final String PREFS_NAME = "user_actions";
    private static final String PREF_LIKED_KEY = "liked_";
    private static final String PREF_DISLIKED_KEY = "disliked_";

    public VideoDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_detail, container, false);

        videoView = view.findViewById(R.id.video_view);
        videoTitle = view.findViewById(R.id.video_title);
        videoDetails = view.findViewById(R.id.video_details);
        recyclerView = view.findViewById(R.id.recycler_view);
        likeButton = view.findViewById(R.id.button_like);
        dislikeButton = view.findViewById(R.id.button_dislike);
        shareButton = view.findViewById(R.id.button_share);
        saveButton = view.findViewById(R.id.button_save);
        commentsSection = view.findViewById(R.id.comments_section); // Initialize comments section
        likeCountText = view.findViewById(R.id.like_count);
        dislikeCountText = view.findViewById(R.id.dislike_count);

        // Retrieve video details from arguments
        Bundle args = getArguments();
        if (args != null) {
            updateVideoDetails(args);
        }

        // Initialize RecyclerView
        List<VideoItem> videoList = new ArrayList<>();
        // Populate videoList with your video data
        videoList.add(new VideoItem(R.raw.videoapp1, "Music Video 1", R.drawable.img7, "Coldplay", "01 Jan 2022", "1000 views"));
        videoList.add(new VideoItem(R.raw.videoapp3, "Sport Video 1", R.drawable.img6, "Sport5", "02 Jan 2022", "2000 views"));
        videoList.add(new VideoItem(R.raw.videoapp2, "Music Video 2", R.drawable.img7, "Coldplay", "08 Jan 2023", "1000 views"));
        videoList.add(new VideoItem(R.raw.videoapp2, "Video 2", R.drawable.one, "Author 2", "02 Jan 2022", "2000 views"));
        videoList.add(new VideoItem(R.raw.videoapp1, "Video 1", R.drawable.two, "Author 1", "01 Jan 2022", "1000 views"));
        videoList.add(new VideoItem(R.raw.videoapp2, "Video 2", R.drawable.three, "Author 2", "02 Jan 2022", "2000 views"));
        // Add more videos as needed

        VideoAdapter videoAdapter = new VideoAdapter(videoList);
        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoItem video) {
                playVideo(video);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);

        // Set click listeners for buttons
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLike();
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDislike();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle share button click
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click
            }
        });

        commentsSection.setOnClickListener(new View.OnClickListener() { // Add click listener for comments section
            @Override
            public void onClick(View v) {
                openCommentsFragment();
            }
        });

        return view;
    }

    private void handleLike() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        boolean hasLiked = prefs.getBoolean(PREF_LIKED_KEY + currentVideoTitle, false);
        boolean hasDisliked = prefs.getBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false);

        SharedPreferences.Editor editor = prefs.edit();

        if (hasDisliked) {
            // Remove dislike
            editor.putBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false).apply();
            dislikeCount--;
        }

        if (hasLiked) {
            // Remove like
            editor.putBoolean(PREF_LIKED_KEY + currentVideoTitle, false).apply();
            likeCount--;
            likeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
            // Add like
            editor.putBoolean(PREF_LIKED_KEY + currentVideoTitle, true).apply();
            likeCount++;
            likeButton.setBackgroundColor(Color.GREEN);
        }

        likeCountText.setText(String.valueOf(likeCount));
        dislikeCountText.setText(String.valueOf(dislikeCount));
        updateButtons();
    }

    private void handleDislike() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        boolean hasLiked = prefs.getBoolean(PREF_LIKED_KEY + currentVideoTitle, false);
        boolean hasDisliked = prefs.getBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false);

        SharedPreferences.Editor editor = prefs.edit();

        if (hasLiked) {
            // Remove like
            editor.putBoolean(PREF_LIKED_KEY + currentVideoTitle, false).apply();
            likeCount--;
        }

        if (hasDisliked) {
            // Remove dislike
            editor.putBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false).apply();
            dislikeCount--;
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
            // Add dislike
            editor.putBoolean(PREF_DISLIKED_KEY + currentVideoTitle, true).apply();
            dislikeCount++;
            dislikeButton.setBackgroundColor(Color.RED);
        }

        likeCountText.setText(String.valueOf(likeCount));
        dislikeCountText.setText(String.valueOf(dislikeCount));
        updateButtons();
    }

    private void updateButtons() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        boolean hasLiked = prefs.getBoolean(PREF_LIKED_KEY + currentVideoTitle, false);
        boolean hasDisliked = prefs.getBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false);

        if (hasLiked) {
            likeButton.setBackgroundColor(Color.GREEN);
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        } else if (hasDisliked) {
            dislikeButton.setBackgroundColor(Color.RED);
            likeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
            likeButton.setBackgroundColor(Color.TRANSPARENT);
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        }
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

    private void playVideo(VideoItem video) {
        currentVideoTitle = video.getTitle();  // Use the title as the unique ID
        updateVideoDetails(video);

        // Reset counts for the new video
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        likeCount = prefs.getBoolean(PREF_LIKED_KEY + currentVideoTitle, false) ? 1 : 0;
        dislikeCount = prefs.getBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false) ? 1 : 0;

        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + video.getVideoResId();
        videoView.setVideoPath(videoPath);
        videoView.start();

        likeCountText.setText(String.valueOf(likeCount));
        dislikeCountText.setText(String.valueOf(dislikeCount));
        updateButtons();
    }

    private void updateVideoDetails(Bundle args) {
        int videoResId = args.getInt("VIDEO_RES_ID");
        String title = args.getString("VIDEO_TITLE");
        String author = args.getString("VIDEO_AUTHOR");
        String date = args.getString("VIDEO_DATE");
        String views = args.getString("VIDEO_VIEWS");

        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + videoResId;
        videoView.setVideoPath(videoPath);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        videoTitle.setText(title);
        videoDetails.setText(author + " • " + date + " • " + views);
    }

    private void updateVideoDetails(VideoItem video) {
        currentVideoTitle = video.getTitle();  // Use the title as the unique ID

        videoTitle.setText(video.getTitle());
        videoDetails.setText(video.getAuthor() + " • " + video.getDate() + " • " + video.getViews());
    }

    private void openCommentsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new CommentsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
