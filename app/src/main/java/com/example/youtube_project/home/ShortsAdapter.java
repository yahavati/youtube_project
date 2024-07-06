package com.example.youtube_project.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.youtube_project.R;
// ShortsAdapter.java
public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    private Context context;
    private List<String> videoUrls;
    private List<Integer> likes;
    private List<Integer> dislikes;

    public ShortsAdapter(Context context, List<String> videoUrls) {
        this.context = context;
        this.videoUrls = videoUrls;
        this.likes = new ArrayList<>(Collections.nCopies(videoUrls.size(), 0)); // Initialize likes with 0
        this.dislikes = new ArrayList<>(Collections.nCopies(videoUrls.size(), 0)); // Initialize dislikes with 0
    }

    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position) {
        holder.bind(videoUrls.get(position), position);
    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }

    class ShortsViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageButton btnLike;
        TextView likeCount;
        ImageButton btnDislike;
        TextView dislikeCount;

        ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            btnLike = itemView.findViewById(R.id.btn_like);
            likeCount = itemView.findViewById(R.id.like_count);
            btnDislike = itemView.findViewById(R.id.btn_dislike);
            dislikeCount = itemView.findViewById(R.id.dislike_count);
        }

        void bind(String videoUrl, int position) {
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
            videoView.start();

            likeCount.setText(String.valueOf(likes.get(position)));
            dislikeCount.setText(String.valueOf(dislikes.get(position)));

            btnLike.setOnClickListener(v -> {
                likes.set(position, likes.get(position) + 1);
                likeCount.setText(String.valueOf(likes.get(position)));
            });

            btnDislike.setOnClickListener(v -> {
                dislikes.set(position, dislikes.get(position) + 1);
                dislikeCount.setText(String.valueOf(dislikes.get(position)));
            });
        }
    }
}

