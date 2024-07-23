package com.example.youtube_project;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.example.youtube_project.home.VideoDetailFragment;

public class CustomMediaController extends MediaController {
    private ImageButton fullScreenButton;
    private boolean isFullScreen = false;
    private VideoDetailFragment fragment;

    public CustomMediaController(Context context, VideoDetailFragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        fullScreenButton = new ImageButton(getContext());
        fullScreenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.rightMargin = 80; // Adjust as needed
        addView(fullScreenButton, params);

        fullScreenButton.setOnClickListener(v -> {
            isFullScreen = !isFullScreen;
            fragment.toggleFullScreen(isFullScreen);
            updateFullScreenButton();
        });
    }

    private void updateFullScreenButton() {
        if (isFullScreen) {
            fullScreenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
        } else {
            fullScreenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
        }
    }
}