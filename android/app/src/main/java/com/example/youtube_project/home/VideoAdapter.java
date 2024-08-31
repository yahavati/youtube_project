package com.example.youtube_project.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
        private void tryLoadingBase64(Context context, String url, ImageView videoThumbnail) {
            try {
                byte[] decodedBytes = Base64.decode(url, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                if (decodedBitmap != null) {
                    Glide.with(context)
                            .load(decodedBitmap)
                            .apply(new RequestOptions()
                                    .frame(1000000)
                                    .centerCrop())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    loadPlaceholder(context, videoThumbnail);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(videoThumbnail);
                } else {
                    loadPlaceholder(context, videoThumbnail);
                }
            } catch (Exception ex) {
                Log.e("ThumbnailLoader", "Error decoding base64 image", ex);
                loadPlaceholder(context, videoThumbnail);
            }
        }

        private void loadPlaceholder(Context context, ImageView videoThumbnail) {
            try {
                Glide.with(context)
                        .load(R.drawable.ic_circlr1) // Replace with your default thumbnail drawable
                        .into(videoThumbnail);
            } catch (Exception e) {
                Log.e("ThumbnailLoader", "Error loading placeholder image", e);
                // If even loading the placeholder fails, we'll just leave the ImageView as is
            }
        }


        public void bind(final VideoItem video, final OnItemClickListener listener) {
            videoTitle.setText(video.getTitle());


// ..
            Glide.with(context)
                    .load(video.getUrl())
                    .apply(new RequestOptions()
                            .frame(1000000) // Extract frame after 1 second
                            .centerCrop())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // If loading fails, try decoding the URL as base64
                            tryLoadingBase64(context, video.getUrl(), videoThumbnail);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false; // Return false if you want Glide to handle setting the resource on the target
                        }
                    })
                    .into(videoThumbnail);

            Date date = video.getCreatedAt();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

// Format the date
            String formattedDate = dateFormat.format(date);


            String details = formattedDate  + " • " + video.getViews();
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
