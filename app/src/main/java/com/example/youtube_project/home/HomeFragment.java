package com.example.youtube_project.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.ApiHandler;
import com.example.youtube_project.ProfileViewModel;
import com.example.youtube_project.R;

import com.example.youtube_project.user.UserManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class HomeFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    private UserManager userManager;
    private VideoAdapter adapter;
    private RecyclerView recyclerView;
    private ProfileViewModel profileViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userManager = UserManager.getInstance();
        adapter = new VideoAdapter(getContext(), new ArrayList<>());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

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

        // Fetch videos from the server
        new LoadVideosTask().execute();

        return view;
    }

    private class LoadVideosTask extends AsyncTask<Void, Void, List<VideoItem>> {
        @Override
        protected List<VideoItem> doInBackground(Void... voids) {
            try {
                Response response = ApiHandler.fetchRandomVideos();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    JsonArray videoArray = jsonObject.getAsJsonArray("data");

                    List<VideoItem> videos = new ArrayList<>();

                    for (int i = 0; i < videoArray.size(); i++) {
                        JsonObject videoObject = videoArray.get(i).getAsJsonObject();
                        VideoItem video = new Gson().fromJson(videoObject, VideoItem.class);
                        videos.add(video);
                    }
                    return videos;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<VideoItem> videos) {
            if (videos != null) {
                // Sort the videos by views in descending order
                videos.sort((video1, video2) -> Integer.compare(video2.getViews(), video1.getViews()));

                // Update the adapter with the sorted list
                adapter.setVideos(videos);
            } else {
                // Handle the error
                System.err.println("Failed to fetch videos");
            }
        }
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
        fragment.setCurrentVideo(video);
        Bundle args = new Bundle();

        args.putString("VIDEO_TITLE", video.getTitle());
        args.putString("VIDEO_AUTHOR", video.getAuthor());
        args.putSerializable("VIDEO_DATE", video.getDate());
        args.putInt("VIDEO_VIEWS", video.getViews());

        args.putStringArrayList("VIDEO_LIKES", new ArrayList<>(video.getLikes()));
        args.putStringArrayList("VIDEO_DISLIKES", new ArrayList<>(video.getDislikes()));
        fragment.setArguments(args);

        if (getActivity() != null) {
            ((HomeScreenActivity) getActivity()).loadFragment(fragment);
        }
    }
}
