package com.example.youtube1;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class VideoDetailFragment extends Fragment {

    private VideoView videoView;
    private TextView videoTitle;
    private TextView videoDetails;
    private RecyclerView recyclerView;
    private LinearLayout likeButton;
    private LinearLayout dislikeButton;
    private LinearLayout shareButton;
    private LinearLayout saveButton;
    private TextView likeCountText;
    private TextView dislikeCountText;
    private int likeCount = 0;
    private int dislikeCount = 0;

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
        likeCountText = view.findViewById(R.id.like_count);
        dislikeCountText = view.findViewById(R.id.dislike_count);

        // Retrieve video details from arguments
        Bundle args = getArguments();
        if (args != null) {
            int videoResId = args.getInt("VIDEO_RES_ID");
            String title = args.getString("VIDEO_TITLE");
            String author = args.getString("VIDEO_AUTHOR");
            String date = args.getString("VIDEO_DATE");
            String views = args.getString("VIDEO_VIEWS");

            // Set video and details
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

        // Initialize RecyclerView
        // Load related videos and set up RecyclerView adapter

        view.findViewById(R.id.comments_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentsFragment();
            }
        });

        // Set click listeners for buttons
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCount++;
                likeCountText.setText(String.valueOf(likeCount));
                likeButton.setBackgroundColor(Color.GREEN);
                dislikeButton.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeCount++;
                dislikeCountText.setText(String.valueOf(dislikeCount));
                dislikeButton.setBackgroundColor(Color.RED);
                likeButton.setBackgroundColor(Color.TRANSPARENT);
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

        return view;
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

    private void openCommentsFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CommentsFragment())
                .addToBackStack(null)
                .commit();
    }
}
