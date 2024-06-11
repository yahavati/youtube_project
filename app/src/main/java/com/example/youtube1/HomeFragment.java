package com.example.youtube1;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        List<VideoItem> videos = getVideos();
        VideoAdapter adapter = new VideoAdapter(videos);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton infoButton = view.findViewById(R.id.button_info);
        infoButton.setOnClickListener(v -> openMenuFragment());

        // Set touch listener on overlay_container to detect outside clicks
        View overlayContainer = view.findViewById(R.id.overlay_container);
        overlayContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Detect clicks outside the MenuFragment
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Close the MenuFragment and show the HomeFragment
                    closeMenuFragment();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void openMenuFragment() {
        MenuFragment menuFragment = new MenuFragment();
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
            transaction.replace(R.id.overlay_container, menuFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View overlayContainer = getView().findViewById(R.id.overlay_container);
            if (overlayContainer != null) {
                overlayContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void closeMenuFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
            View overlayContainer = getView().findViewById(R.id.overlay_container);
            if (overlayContainer != null) {
                overlayContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(VideoItem video) {
        VideoDetailFragment fragment = new VideoDetailFragment();

        Bundle args = new Bundle();
        args.putInt("VIDEO_RES_ID", video.getVideoResId());
        args.putString("VIDEO_TITLE", video.getTitle());
        args.putString("VIDEO_AUTHOR", video.getAuthor());
        args.putString("VIDEO_DATE", video.getDate());
        args.putString("VIDEO_VIEWS", video.getViews());
        fragment.setArguments(args);

        if (getActivity() != null) {
            ((MainActivity) getActivity()).loadFragment(fragment);
        }
    }

    private List<VideoItem> getVideos() {
        List<VideoItem> videos = new ArrayList<>();
        videos.add(new VideoItem(R.raw.sample_video1, "Video 1", R.drawable.one, "Author 1", "01 Jan 2022", "1000 views"));
        videos.add(new VideoItem(R.raw.sample_video2, "Video 2", R.drawable.two, "Author 2", "02 Jan 2022", "2000 views"));
        videos.add(new VideoItem(R.raw.sample_video1, "Video 3", R.drawable.three, "Author 3", "03 Jan 2022", "3000 views"));
        videos.add(new VideoItem(R.raw.sample_video2, "Video 4", R.drawable.one, "Author 4", "04 Jan 2022", "4000 views"));
        videos.add(new VideoItem(R.raw.sample_video1, "Video 5", R.drawable.two, "Author 5", "05 Jan 2022", "5000 views"));
        return videos;
    }
}
