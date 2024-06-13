package com.example.youtube1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class ShortsActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ShortsAdapter adapter;
    private List<String> videoUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);

        viewPager = findViewById(R.id.viewPager);

        // Sample video URLs
        videoUrls = new ArrayList<>();
        videoUrls.add("https://www.example.com/video1.mp4");
        videoUrls.add("https://www.example.com/video2.mp4");
        // Add more URLs as needed

        adapter = new ShortsAdapter(this, videoUrls);
        viewPager.setAdapter(adapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }
}
