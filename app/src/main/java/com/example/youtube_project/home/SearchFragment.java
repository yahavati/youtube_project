package com.example.youtube_project.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.R;
import com.example.youtube_project.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    private static final String TAG = "SearchFragment";
    private UserManager userManager;
    private List<VideoItem> listSearchVideos;
    private VideoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = UserManager.getInstance();
        listSearchVideos = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_search);
        adapter = new VideoAdapter(listSearchVideos);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText searchBox = view.findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ImageButton backButton = view.findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ((HomeScreenActivity) getActivity()).loadFragment(new HomeFragment());
                    ((HomeScreenActivity) getActivity()).showTopAndBottomFragments(true);
                }
            }
        });

        return view;
    }

    private void performSearch(String query) {
        listSearchVideos.clear();
        try {
            for (VideoItem video : userManager.getVideos()) {
                if (video.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        video.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                        (video.getDescription() != null && video.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                    listSearchVideos.add(video);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error during search: ", e);
        }
    }

    @Override
    public void onItemClick(VideoItem video) {
        VideoDetailFragment fragment = new VideoDetailFragment();
        fragment.setCurrentVideo(video);
        Bundle args = new Bundle();
        if (video.isUri()) {
            args.putParcelable("VIDEO_RES_URI", video.getVideoResIdUri());
            args.putBoolean("IS_URI", true);
        } else {
            args.putInt("VIDEO_RES_ID", video.getVideoResIdInt());
            args.putBoolean("IS_URI", false);
        }
        args.putString("VIDEO_TITLE", video.getTitle());
        args.putString("VIDEO_AUTHOR", video.getAuthor());
        args.putString("VIDEO_DATE", video.getDate());
        args.putString("VIDEO_VIEWS", video.getViews());
        args.putString("VIDEO_DESCRIPTION", video.getDescription());
        args.putStringArrayList("VIDEO_LIKES", new ArrayList<>(video.getLikes()));
        args.putStringArrayList("VIDEO_DISLIKES", new ArrayList<>(video.getDislikes()));
        fragment.setArguments(args);

        if (getActivity() != null) {
            ((HomeScreenActivity) getActivity()).loadFragment(fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            ((HomeScreenActivity) getActivity()).showTopAndBottomFragments(true);
        }
    }
}
