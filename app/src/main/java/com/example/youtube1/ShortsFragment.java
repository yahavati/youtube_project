package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class ShortsFragment extends Fragment {
    private ViewPager2 viewPager;
    private ShortsAdapter shortsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shorts, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager();
        return view;
    }

    private void setupViewPager() {
        List<String> videoUrls = new ArrayList<>();
        videoUrls.add("android.resource://" + getContext().getPackageName() + "/" + R.raw.videoapp1); // Add your video URL
        videoUrls.add("android.resource://" + getContext().getPackageName() + "/" + R.raw.videoapp2);

        shortsAdapter = new ShortsAdapter(getContext(), videoUrls);
        viewPager.setAdapter(shortsAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

    }
}
