package com.example.youtube_project.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.youtube_project.R;

public class HomeScreenActivity extends AppCompatActivity {

    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        // Load the bottom navigation fragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.bottom_navigation_container, new BottomNavigationFragment());
            transaction.replace(R.id.fragment_container, new HomeFragment()); // Load HomeFragment by default
            transaction.replace(R.id.top_right_buttons_container, new TopRightButtonsFragment());
            transaction.commit();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // This will add the transaction to the back stack
        transaction.commit();

        // Add or remove settings button based on the fragment
        if (fragment instanceof YouFragment) {
            addSettingsButton();
        } else {
            removeSettingsButton();
        }
    }

    private void addSettingsButton() {
        if (settingsButton == null) {
            settingsButton = new ImageButton(this);
            settingsButton.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
            settingsButton.setBackground(null);
            settingsButton.setImageResource(R.drawable.setting);
            settingsButton.setContentDescription("Settings icon");
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new SettingsFragment());
                }
            });

            LinearLayout topRightButtonsContainer = findViewById(R.id.top_right_buttons_container);
            int searchButtonIndex = -1;
            for (int i = 0; i < topRightButtonsContainer.getChildCount(); i++) {
                if (topRightButtonsContainer.getChildAt(i).getId() == R.id.button_search) {
                    searchButtonIndex = i;
                    break;
                }
            }
            if (searchButtonIndex != -1) {
                topRightButtonsContainer.addView(settingsButton, searchButtonIndex + 1);
            } else {
                topRightButtonsContainer.addView(settingsButton);
            }
        }
    }

    private void removeSettingsButton() {
        if (settingsButton != null) {
            LinearLayout topRightButtonsContainer = findViewById(R.id.top_right_buttons_container);
            topRightButtonsContainer.removeView(settingsButton);
            settingsButton = null;
        }
    }

    public void showTopAndBottomFragments(boolean show) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (show) {
            transaction.show(getSupportFragmentManager().findFragmentById(R.id.top_right_buttons_container));
            transaction.show(getSupportFragmentManager().findFragmentById(R.id.bottom_navigation_container));
            findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
        } else {
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.top_right_buttons_container));
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.bottom_navigation_container));
            findViewById(R.id.app_bar_layout).setVisibility(View.GONE);
        }
        transaction.commit();
    }
//    public void addUploadedVideo(Uri videoUri, String username) {
//        LinearLayout homeVideoContainer = findViewById(R.id.home_video_container); // Assuming you have a container for videos
//        View videoView = getLayoutInflater().inflate(R.layout.video_item_home_screen, homeVideoContainer, false);
//        VideoView video = videoView.findViewById(R.id.video_view_home);
//        video.setVideoURI(videoUri);
//        video.start();
//
//        TextView videoDetails = videoView.findViewById(R.id.video_details);
//        videoDetails.setText("Uploaded by " + username + " | 0 views | Just now");
//
//        homeVideoContainer.addView(videoView);
//    }
}
