package com.example.youtube1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
                // Handle opening comments section
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
}
