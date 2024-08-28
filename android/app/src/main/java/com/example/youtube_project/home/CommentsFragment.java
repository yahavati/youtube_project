package com.example.youtube_project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.ApiHandler;
import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.example.youtube_project.user.UserManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

public class CommentsFragment extends Fragment implements CommentAdapter.OnCommentClickListener {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private EditText editTextComment;
    private Button buttonAddComment;

    private Comment editingComment;
    private int editingPosition = -1;

    private VideoItem currentVideo;
    private static final String TAG = "CommentsFragment";

    public CommentsFragment(VideoItem video) {
        currentVideo = video;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonAddComment = view.findViewById(R.id.button_add_comment);

        // Fetch full comment objects from IDs asynchronously
        fetchCommentsFromIds(currentVideo.getCommentIds());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonAddComment.setOnClickListener(v -> {
            if (!isLoggedIn()) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }

            String commentText = editTextComment.getText().toString();
            if (!commentText.isEmpty()) {
                if (editingComment != null) {
                    editComment(editingComment, commentText);
                } else {
                    addNewComment(commentText);
                }
            }
        });

        return view;
    }

    private void fetchCommentsFromIds(List<String> commentIds) {
        new Thread(() -> {
            List<Comment> comments = new ArrayList<>();
            for (String id : commentIds) {
                Comment comment = fetchCommentById(id);
                if (comment != null) {
                    comments.add(comment);
                }
            }

            // Update the RecyclerView on the main thread
            getActivity().runOnUiThread(() -> {
                adapter = new CommentAdapter(comments, CommentsFragment.this);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private Comment fetchCommentById(String commentId) {
        try {
            Response response = ApiHandler.fetchCommentById(commentId);
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                if (jsonResponse != null) {
                    // Extracting fields from the JSON response
                    String id = jsonResponse.get("_id").getAsString();
                    String text = jsonResponse.get("text").getAsString();

                    JsonObject userObject = jsonResponse.getAsJsonObject("user");
                    String userId = userObject.get("_id").getAsString();
                    String displayName = userObject.get("displayName").getAsString();

                    String likes = jsonResponse.get("likes").getAsString();
                    String dislikes = jsonResponse.get("dislikes").getAsString();

                    // Create the Comment object
                    String videoId = jsonResponse.get("video").getAsString();
                    String createdAtString = jsonResponse.get("createdAt").getAsString();

                    // Convert createdAt to Date
                    Date createdAt = null;
                    try {
                        createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAtString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Comment comment = new Comment();

                    // Manually set each field
                    comment.setId(id);
                    comment.setText(text);
                    comment.setUsername(displayName);  // Assuming you want to set displayName as username
                    comment.setLikes(likes);
                    comment.setUser(UserManager.getInstance().getCurrentUser());
                    comment.setDislikes(dislikes);
                    comment.setVideoId(videoId);
                    comment.setDate(createdAt);

                    return comment;
                }
            } else {
                Log.e(TAG, "Failed to fetch comment by ID: " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching comment by ID", e);
        }
        return null;
    }


    private void addNewComment(String commentText) {
        new Thread(() -> {
            try {
                JsonObject commentData = new JsonObject();
                commentData.addProperty("user", UserManager.getInstance().getCurrentUser().getId());
                commentData.addProperty("text", commentText);
                commentData.addProperty("video", currentVideo.getId());

                Response response = ApiHandler.addComment(currentVideo.getId(), commentData);
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("data") && !jsonResponse.get("data").isJsonNull()) {
                        Comment newComment = new Gson().fromJson(jsonResponse.get("data"), Comment.class);
                        getActivity().runOnUiThread(() -> {
                            currentVideo.addComment(newComment.getId());  // Add comment ID to VideoItem
                            adapter.notifyItemInserted(currentVideo.getCommentIds().size() - 1);
                            editTextComment.setText("");
                        });
                    }
                } else {
                    Log.e(TAG, "Failed to add comment: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error adding comment", e);
            }
        }).start();
    }

    private void editComment(Comment comment, String newText) {
        new Thread(() -> {
            try {
                JsonObject commentData = new JsonObject();
                commentData.addProperty("text", newText);

                Response response = ApiHandler.updateComment(comment.getId(), commentData);
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        comment.setText(newText);
                        adapter.notifyItemChanged(editingPosition);
                        editingComment = null;
                        editingPosition = -1;
                        editTextComment.setText("");
                    });
                } else {
                    Log.e(TAG, "Failed to edit comment: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error editing comment", e);
            }
        }).start();
    }

    @Override
    public void onLikeClicked(Comment comment) {
        if (!isLoggedIn()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        new Thread(() -> {
            try {
                Response response = ApiHandler.likeComment(comment.getId());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                } else {
                    Log.e(TAG, "Failed to like comment: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error liking comment", e);
            }
        }).start();
    }

    @Override
    public void onDislikeClicked(Comment comment) {
        if (!isLoggedIn()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        new Thread(() -> {
            try {
                Response response = ApiHandler.dislikeComment(comment.getId());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                } else {
                    Log.e(TAG, "Failed to dislike comment: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error disliking comment", e);
            }
        }).start();
    }

    @Override
    public void onEditClicked(Comment comment) {
        if (getCurrentUsername().equals(comment.getUsername())) {
            editingComment = comment;
            editingPosition = currentVideo.getCommentIds().indexOf(comment.getId());
            editTextComment.setText(comment.getText());
            editTextComment.requestFocus();
        }
    }

    @Override
    public void onDeleteClicked(Comment comment) {
        if (getCurrentUsername().equals(comment.getUsername())) {
            deleteComment(comment);
        }
    }

    private void deleteComment(Comment comment) {
        new Thread(() -> {
            try {
                Response response = ApiHandler.deleteComment(comment.getId());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        int position = currentVideo.getCommentIds().indexOf(comment.getId());
                        if (position != -1) {
                            currentVideo.removeComment(comment.getId());  // Remove comment ID from VideoItem
                            adapter.notifyItemRemoved(position);
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to delete comment: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error deleting comment", e);
            }
        }).start();
    }

    private String getCurrentUsername() {
        return UserManager.getInstance().getCurrentUserName();
    }

    private boolean isLoggedIn() {
        return UserManager.getInstance().isLoggedIn();
    }
}
