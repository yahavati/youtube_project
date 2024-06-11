package com.example.youtube1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItem> videoList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(VideoItem video);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public VideoAdapter(List<VideoItem> videoList) {
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoItem video = videoList.get(position);
        holder.bind(video, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumbnail;
        private TextView videoTitle;
        private TextView videoDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoDetails = itemView.findViewById(R.id.video_details);
        }

        public void bind(final VideoItem video, final OnItemClickListener listener) {
            videoTitle.setText(video.getTitle());
            videoThumbnail.setImageResource(video.getThumbnailResId());

            String details = video.getAuthor() + " • " + video.getDate() + " • " + video.getViews();
            videoDetails.setText(details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(video);
                }
            });
        }

    }
}
