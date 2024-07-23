package com.example.youtube_project.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.youtube_project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItem> videoList;
    private OnItemClickListener onItemClickListener;

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(VideoItem video);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public VideoAdapter(Context context, List<VideoItem> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    public void setVideos(List<VideoItem> videos) {
        this.videoList = videos;

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoDetails = itemView.findViewById(R.id.video_details);
            this.context = context;
        }

        public void bind(final VideoItem video, final OnItemClickListener listener) {
            videoTitle.setText(video.getTitle());


// ...

            String thumbnailUrl = "http://192.168.221.1:5000/" + video.getVideoPath().replace("\\", "/");
            Log.d("thum_log", thumbnailUrl);
            Glide.with(context)
                    .load(thumbnailUrl)
                    .apply(new RequestOptions()
                            .frame(1000000) // Extract frame after 1 second
                            .centerCrop())
                    .into(videoThumbnail);

            Date date = video.getDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

// Format the date
            String formattedDate = dateFormat.format(date);


            String details = video.getAuthor() + " • " + formattedDate  + " • " + video.getViews();
            videoDetails.setText(details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(video);
                    }
                }
            });
        }
    }
}
