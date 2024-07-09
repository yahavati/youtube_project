package com.example.youtube_project.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.youtube_project.R;

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
        overlayContainer.setOnTouchListener((v, event) -> {
            // Detect clicks outside the MenuFragment
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Close the MenuFragment and show the HomeFragment
                closeMenuFragment();
                return true;
            }
            return false;
        });

        return view;
    }

    private void openMenuFragment() {
        MenuFragment menuFragment = new MenuFragment();
        if (getParentFragmentManager() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
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
        if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
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
            ((HomeScreenActivity) getActivity()).loadFragment(fragment);
        }
    }

    private List<VideoItem> getVideos() {
        List<VideoItem> videos = new ArrayList<>();
        videos.add(new VideoItem(R.raw.videoapp1, "Music", R.drawable.img7, "coldplay", "01 Jan 2022", "1000 views"));
        videos.add(new VideoItem(R.raw.videoapp3, "SPORT", R.drawable.img6, "sport5", "02 Jan 2022", "2000 views"));
        videos.add(new VideoItem(R.raw.videoapp2, "Music", R.drawable.img7, "coldplay", "08 Jan 2023", "1000 views"));
        videos.add(new VideoItem(R.raw.videoapp2, "Video 2", R.drawable.one, "Author 2", "02 Jan 2022", "2000 views"));
        videos.add(new VideoItem(R.raw.videoapp1, "Video 1", R.drawable.two, "Author 1", "01 Jan 2022", "1000 views"));
        videos.add(new VideoItem(R.raw.videoapp2, "Video 2", R.drawable.three, "Author 2", "02 Jan 2022", "2000 views"));
        // Add more videos as needed
        return videos;
    }
}
