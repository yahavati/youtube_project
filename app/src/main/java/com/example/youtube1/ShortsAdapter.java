package com.example.youtube1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ShortsAdapter.java
public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    private List<String> videoUrls;
    private Context context;

    public ShortsAdapter(Context context, List<String> videoUrls) {
        this.context = context;
        this.videoUrls = videoUrls;
    }

    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position) {
        holder.bind(videoUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }

    static class ShortsViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bind(String videoUrl) {
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.start();
        }
    }
}
