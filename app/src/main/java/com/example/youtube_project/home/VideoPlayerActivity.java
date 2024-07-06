package com.example.youtube_project.home;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.youtube_project.R;
public class VideoPlayerActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.video_view);
        TextView titleTextView = findViewById(R.id.video_title);
        TextView authorTextView = findViewById(R.id.video_author);
        TextView dateTextView = findViewById(R.id.video_date);
        TextView viewsTextView = findViewById(R.id.video_views);

        int videoResId = getIntent().getIntExtra("VIDEO_RES_ID", -1);
        String title = getIntent().getStringExtra("VIDEO_TITLE");
        String author = getIntent().getStringExtra("VIDEO_AUTHOR");
        String date = getIntent().getStringExtra("VIDEO_DATE");
        String views = getIntent().getStringExtra("VIDEO_VIEWS");

        if (videoResId != -1) {
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
            videoView.setVideoURI(videoUri);

            // Initialize MediaController
            mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }

        titleTextView.setText(title);
        authorTextView.setText(author);
        dateTextView.setText(date);
        viewsTextView.setText(views);

        gestureDetector = new GestureDetector(this, new GestureListener());

        View contentView = findViewById(android.R.id.content);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY > SWIPE_THRESHOLD && Math.abs(distanceX) < SWIPE_THRESHOLD) {
                finish();
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) < Math.abs(diffY)) {
                    if (diffY > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        finish();
                        return true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Full screen in landscape
            getSupportActionBar().hide();
            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Default view in portrait
            getSupportActionBar().show();
            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = (int) (200 * getResources().getDisplayMetrics().density);
            videoView.setLayoutParams(params);
        }
    }
}
