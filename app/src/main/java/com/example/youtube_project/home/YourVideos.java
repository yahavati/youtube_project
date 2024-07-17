package com.example.youtube_project.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.youtube_project.R;
import com.example.youtube_project.user.UserManager;

import java.io.IOException;
import java.util.List;

public class YourVideos extends Fragment {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int TAKE_PHOTO_REQUEST = 3;

    private LinearLayout videoContainer;
    private Button uploadButton;
    private UserManager userManager;
    private VideoItem currentVideoItem;
    private ImageView thumbnailPreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_your_videos, container, false);

        videoContainer = view.findViewById(R.id.video_container);
        uploadButton = view.findViewById(R.id.upload_button);
        userManager = UserManager.getInstance();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        loadUploadedVideos();

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            addVideoToHomeScreen(videoUri);
            loadUploadedVideos(); // Reload the videos after adding a new one
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                thumbnailPreview.setImageBitmap(bitmap);
                thumbnailPreview.setVisibility(View.VISIBLE);
                currentVideoItem.setThumbnailUri(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                Uri imageUri = getImageUri(imageBitmap);
                thumbnailPreview.setImageBitmap(imageBitmap);
                thumbnailPreview.setVisibility(View.VISIBLE);
                currentVideoItem.setThumbnailUri(imageUri);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Thumbnail", null);
        return Uri.parse(path);
    }

    private void addVideoToHomeScreen(Uri videoUri) {
        userManager = UserManager.getInstance();
        VideoItem videoItem = new VideoItem(videoUri, "New Video", R.drawable.img7, userManager.getCurrentUserName(), "01 Sep 2024", "0 views");
        userManager.addVideo(videoItem);
        userManager.getCurrentUser().addVideo(videoItem);
    }

    private void loadUploadedVideos() {
        videoContainer.removeAllViews(); // Clear any existing video views

        List<VideoItem> videos = userManager.getCurrentUser().getVideos();
        for (VideoItem video : videos) {
            View videoView = getLayoutInflater().inflate(R.layout.video_item_your_videos, videoContainer, false);

            VideoView videoDisplay = videoView.findViewById(R.id.video_view);
            Button editButton = videoView.findViewById(R.id.edit_button);
            Button deleteButton = videoView.findViewById(R.id.delete_button);

            // Set the video URI and start the video
            videoDisplay.setVideoURI(video.getVideoResIdUri());
            videoDisplay.seekTo(1); // Display the first frame as a thumbnail

            // Handle edit button click
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(video);
                }
            });

            // Handle delete button click
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userManager.getCurrentUser().deleteVideo(video);
                    userManager.deleteVideo(video);
                    loadUploadedVideos(); // Reload the videos after deletion
                }
            });

            videoContainer.addView(videoView); // Add the video view to the container
        }
    }

    private void showEditDialog(VideoItem videoItem) {
        currentVideoItem = videoItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_video, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_text_title);
        EditText editDescription = dialogView.findViewById(R.id.edit_text_description);
        Button chooseThumbnailButton = dialogView.findViewById(R.id.button_choose_thumbnail);
        Button saveButton = dialogView.findViewById(R.id.button_save);
        thumbnailPreview = dialogView.findViewById(R.id.thumbnail_preview);

        editTitle.setText(videoItem.getTitle());
        editDescription.setText(videoItem.getDescription());

        chooseThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThumbnailOptions();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoItem.setTitle(editTitle.getText().toString());
                videoItem.setDescription(editDescription.getText().toString());
                loadUploadedVideos();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showThumbnailOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Thumbnail")
                .setItems(new CharSequence[]{"Choose from Gallery", "Take Photo"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Choose from Gallery
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                                break;
                            case 1: // Take Photo
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
