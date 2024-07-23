package com.example.youtube_project.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.youtube_project.Constants;
import com.example.youtube_project.R;
import com.example.youtube_project.ApiHandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class YourVideos extends Fragment {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int TAKE_PHOTO_REQUEST = 3;

    private LinearLayout videoContainer;
    private Button uploadButton;
    private ApiHandler apiHandler;
    private VideoItem currentVideoItem;
    private ImageView thumbnailPreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_your_videos, container, false);

        videoContainer = view.findViewById(R.id.video_container);
        uploadButton = view.findViewById(R.id.upload_button);
        apiHandler = new ApiHandler();

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
                currentVideoItem.setThumbnailUri(imageUri.toString());
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
                currentVideoItem.setThumbnailUri(imageUri.toString());
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Thumbnail", null);
        return Uri.parse(path);
    }

    private void addVideoToHomeScreen(Uri videoUri) {
        if (videoUri != null) {
            new AddVideoTask().execute(videoUri);
        } else {
            Toast.makeText(getContext(), "No video selected", Toast.LENGTH_SHORT).show();
        }
    }
    private Uri uriToFileProviderUri(Uri uri) throws IOException {
        if (uri == null) {
            throw new IOException("Input URI is null");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new IOException("Failed to open input stream");
            }

            File videoFile = new File(getContext().getCacheDir(), "temp_video_" + System.currentTimeMillis() + ".mp4");
            outputStream = new FileOutputStream(videoFile);

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            return FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", videoFile);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("uriToFileProviderUri", "Error closing input stream", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("uriToFileProviderUri", "Error closing output stream", e);
                }
            }
        }
    }


    private void loadUploadedVideos() {
        new LoadVideosTask().execute();
    }

    private class AddVideoTask extends AsyncTask<Uri, Void, Boolean> {
        private String errorMessage = "";

        @Override
        protected Boolean doInBackground(Uri... params) {
            if (params.length == 0 || params[0] == null) {
                errorMessage = "No video URI provided";
                return false;
            }

            Uri videoUri = params[0];
            try {
                Uri fileProviderUri = uriToFileProviderUri(videoUri);

                // Get the file name from the original Uri
                String fileName = getFileName(videoUri);

                RequestBody videoBody = new RequestBody() {
                    @Override public MediaType contentType() {
                        return MediaType.parse("video/*");
                    }

                    @Override public long contentLength() {
                        return -1; // We don't know the length in advance
                    }

                    @Override public void writeTo(BufferedSink sink) throws IOException {
                        try (InputStream inputStream = getContext().getContentResolver().openInputStream(fileProviderUri)) {
                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = inputStream.read(buffer)) != -1) {
                                sink.write(buffer, 0, read);
                            }
                        }
                    }
                };

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("video", fileName, videoBody)
                        .addFormDataPart("title", "New Video")
                        .addFormDataPart("author", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "guest"))
                        .addFormDataPart("date", new Date().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(Constants.URL+"api/videos")
                        .post(requestBody)
                        .build();

                Response response = ApiHandler.getClient().newCall(request).execute();
                if (!response.isSuccessful()) {
                    errorMessage = "Server error: " + response.code() + " " + response.message();
                    Log.e("AddVideoTask", errorMessage + "\n" + response.body().string());
                    return false;
                }
                return true;
            } catch (IOException e) {
                errorMessage = "IO Exception: " + e.getMessage();
                Log.e("AddVideoTask", errorMessage, e);
                return false;
            } catch (Exception e) {
                errorMessage = "Unexpected error: " + e.getMessage();
                Log.e("AddVideoTask", errorMessage, e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                loadUploadedVideos();
                Toast.makeText(getContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to upload video: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private class UpdateVideoTask extends AsyncTask<Void, Void, Boolean> {
        private final String videoId;
        private final String newTitle;
        private final String newDescription;
        private final Uri thumbnailUri;

        UpdateVideoTask(String videoId, String newTitle, String newDescription, Uri thumbnailUri) {
            this.videoId = videoId;
            this.newTitle = newTitle;
            this.newDescription = newDescription;
            this.thumbnailUri = thumbnailUri;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", newTitle)
                        .addFormDataPart("description", newDescription);

                if (thumbnailUri != null) {
                    File thumbnailFile = new File(getRealPathFromURI(thumbnailUri));
                    RequestBody thumbnailBody = RequestBody.create(MediaType.parse("image/*"), thumbnailFile);
                    multipartBuilder.addFormDataPart("thumbnail", thumbnailFile.getName(), thumbnailBody);
                }

                RequestBody requestBody = multipartBuilder.build();

                Request request = new Request.Builder()
                        .url("http://192.168.221.1:5000/api/videos/" + videoId)
                        .put(requestBody)
                        .build();

                Response response = ApiHandler.getClient().newCall(request).execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getContext(), "Video updated successfully", Toast.LENGTH_SHORT).show();
                loadUploadedVideos();
            } else {
                Toast.makeText(getContext(), "Failed to update video", Toast.LENGTH_SHORT).show();
            }
        }

        private String getRealPathFromURI(Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }
    private class DeleteVideoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String videoId = params[0];
            try {
                Request request = new Request.Builder()
                        .url(Constants.URL+"api/videos/" + videoId)
                        .delete()
                        .build();

                Response response = ApiHandler.getClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    return "success";
                } else {
                    return "Server error: " + response.code() + " " + response.message() + "\n" + response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Network error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Video deleted successfully", Toast.LENGTH_SHORT).show();
                loadUploadedVideos();
            } else {
                Toast.makeText(getContext(), "Failed to delete video: " + result, Toast.LENGTH_LONG).show();
                Log.e("DeleteVideoTask", "Error: " + result);
            }
        }
    }
    private class LoadVideosTask extends AsyncTask<Void, Void, List<VideoItem>> {
        @Override
        protected List<VideoItem> doInBackground(Void... voids) {
            try {
                Response response = ApiHandler.fetchAllVideos();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("LoadVideosTask", "Response body: " + responseBody);
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    JsonArray videoArray = jsonObject.getAsJsonArray("data");

                    List<VideoItem> videos = new ArrayList<>();

                    for (int i = 0; i < videoArray.size(); i++) {
                        JsonObject videoObject = videoArray.get(i).getAsJsonObject();
                        Log.d("LoadVideosTask", "Video object: " + videoObject.toString());
                        VideoItem video = new Gson().fromJson(videoObject, VideoItem.class);
                        Log.d("video_checker", "Video ID: " + video.getId() + ", Title: " + video.getTitle());
                        String username = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "");
                        if(video != null && video.getAuthor().equals(username))  {
                            videos.add(video);
                        }
                    }
                    return videos;
                } else {
                    Log.e("LoadVideosTask", "Unsuccessful response: " + response.code() + " " + response.message());
                    return null;
                }
            } catch (IOException e) {
                Log.e("LoadVideosTask", "Error loading videos", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<VideoItem> videos) {
            if (videos != null) {
                videoContainer.removeAllViews(); // Clear any existing video views
                for (VideoItem video : videos) {
                    View videoView = getLayoutInflater().inflate(R.layout.video_item_your_videos, videoContainer, false);

                    VideoView videoDisplay = videoView.findViewById(R.id.video_view);
                    Button editButton = videoView.findViewById(R.id.edit_button);
                    Button deleteButton = videoView.findViewById(R.id.delete_button);

                    // Set the video URI and start the video
                    videoDisplay.setVideoURI(Uri.parse(Constants.URL+video.getVideoPath().replace("\\", "/")));
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
                            String videoId = video.getId();
                            Log.d("DeleteVideo", "Attempting to delete video with ID: " + videoId);
                            if (videoId != null && !videoId.isEmpty()) {
                                new DeleteVideoTask().execute(videoId);
                            } else {
                                Toast.makeText(getContext(), "Invalid video ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    videoContainer.addView(videoView); // Add the video view to the container
                }
            }
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


        chooseThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThumbnailOptions();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        // In showEditDialog
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = editTitle.getText().toString();
                String newDescription = editDescription.getText().toString();
                Uri thumbnailUri = null;
                if(currentVideoItem.getThumbnailUri() != null) {
                    thumbnailUri = Uri.parse(currentVideoItem.getThumbnailUri());
                }

                new UpdateVideoTask(videoItem.getId(), newTitle, newDescription, thumbnailUri).execute();
                dialog.dismiss();
            }
        });


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
