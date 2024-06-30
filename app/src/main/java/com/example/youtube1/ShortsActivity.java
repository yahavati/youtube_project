package com.example.youtube1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class ShortsActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ShortsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);

        viewPager = findViewById(R.id.viewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        List<String> videoUrls = new ArrayList<>();
        videoUrls.add("android.resource://" + getPackageName() + "/" + R.raw.videoapp1); // Add your video URL

        adapter = new ShortsAdapter(this, videoUrls);
        viewPager.setAdapter(adapter);
    }
}

