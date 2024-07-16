package com.example.youtube_project.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.youtube_project.R;
import com.example.youtube_project.user.UserManager;

public class YourVideos extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private LinearLayout videoContainer;
    private Button uploadButton;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_videos);

        videoContainer = findViewById(R.id.video_container);
        uploadButton = findViewById(R.id.upload_button);
        userManager = UserManager.getInstance();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Add the bottom navigation fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_navigation_container, new BottomNavigationFragment());
        transaction.commit();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            addVideoToContainer(videoUri);
            addVideoToHomeScreen(videoUri);
        }
    }

    private void addVideoToContainer(Uri videoUri) {
        View videoView = getLayoutInflater().inflate(R.layout.video_item_your_videos, videoContainer, false);
        VideoView video = videoView.findViewById(R.id.video_view);
        video.setVideoURI(videoUri);
        video.start();
        videoContainer.addView(videoView);
    }

    private void addVideoToHomeScreen(Uri videoUri) {
        // Logic to add the video to the HomeScreen
        // This might involve communicating with HomeScreenActivity and updating its UI
        // Here is a simplified example
        HomeScreenActivity homeScreenActivity = (HomeScreenActivity) getParent();
        //homeScreenActivity.addUploadedVideo(videoUri, userManager.getCurrentUser());
    }
}
