package com.example.youtube_project.home;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.R;
import com.example.youtube_project.user.UserManager;

import java.util.List;

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
    private TextView likeCountText;
    private TextView dislikeCountText;

    private int likeCount = 0;
    private int dislikeCount = 0;
    private String currentVideoTitle;

    private static final String PREFS_NAME = "user_actions";
    private static final String PREF_LIKED_KEY = "liked_";
    private static final String PREF_DISLIKED_KEY = "disliked_";

    private UserManager userManager;

    public VideoDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_detail, container, false);
        userManager = UserManager.getInstance();
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

        // Retrieve video details from arguments
        Bundle args = getArguments();
        if (args != null) {
            updateVideoDetails(args);
        }

        List<VideoItem> videoList = userManager.getVideos();
        VideoAdapter videoAdapter = new VideoAdapter(videoList);
        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoItem video) {
                playVideo(video);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);

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
                // Inflate the custom layout
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.fake_share_menu, null);

                // Create the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);

                final AlertDialog dialog = builder.create();
                dialog.show();

                // Handle click event on the close button
                ImageView closeButton = dialogView.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Handle click events on fake share options
                dialogView.findViewById(R.id.facebook_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Fake action, just dismiss dialog
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.twitter_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Fake action, just dismiss dialog
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.whatsapp_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Fake action, just dismiss dialog
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.email_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Fake action, just dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click
            }
        });

        commentsSection.setOnClickListener(new View.OnClickListener() {
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
            editor.putBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false).apply();
            dislikeCount--;
        }

        if (hasLiked) {
            editor.putBoolean(PREF_LIKED_KEY + currentVideoTitle, false).apply();
            likeCount--;
            likeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
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
            editor.putBoolean(PREF_LIKED_KEY + currentVideoTitle, false).apply();
            likeCount--;
        }

        if (hasDisliked) {
            editor.putBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false).apply();
            dislikeCount--;
            dislikeButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
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

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        likeCount = prefs.getBoolean(PREF_LIKED_KEY + currentVideoTitle, false) ? 1 : 0;
        dislikeCount = prefs.getBoolean(PREF_DISLIKED_KEY + currentVideoTitle, false) ? 1 : 0;

        if (video.isUri()) {
            videoView.setVideoURI(video.getVideoResIdUri());
        } else {
            String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + video.getVideoResIdInt();
            videoView.setVideoPath(videoPath);
        }
        videoView.start();

        likeCountText.setText(String.valueOf(likeCount));
        dislikeCountText.setText(String.valueOf(dislikeCount));
        updateButtons();
    }

    private void updateVideoDetails(Bundle args) {
        boolean isUri = args.getBoolean("IS_URI");
        String title = args.getString("VIDEO_TITLE");
        String author = args.getString("VIDEO_AUTHOR");
        String date = args.getString("VIDEO_DATE");
        String views = args.getString("VIDEO_VIEWS");
        String description = args.getString("VIDEO_DESCRIPTION");

        if (isUri) {
            Uri videoUri = args.getParcelable("VIDEO_RES_URI");
            videoView.setVideoURI(videoUri);
        } else {
            int videoResId = args.getInt("VIDEO_RES_ID");
            String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + videoResId;
            videoView.setVideoPath(videoPath);
        }

        videoTitle.setText(title);
        videoDetails.setText(author + " • " + date + " • " + views);

        if (description != null && !description.isEmpty()) {
            videoDescription.setText(description);
            videoDescription.setVisibility(View.VISIBLE);
        } else {
            videoDescription.setVisibility(View.GONE);
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Handle error
                return true;
            }
        });
    }

    private void updateVideoDetails(VideoItem video) {
        currentVideoTitle = video.getTitle();  // Use the title as the unique ID

        videoTitle.setText(video.getTitle());
        videoDetails.setText(video.getAuthor() + " • " + video.getDate() + " • " + video.getViews());

        if (video.getDescription() != null) {
            videoDescription.setText(video.getDescription());
            videoDescription.setVisibility(View.VISIBLE);
        } else {
            videoDescription.setVisibility(View.GONE);
        }
    }

    private void openCommentsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new CommentsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
