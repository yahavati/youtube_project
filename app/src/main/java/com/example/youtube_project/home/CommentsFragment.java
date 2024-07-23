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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.ApiHandler;
import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;

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

        adapter = new CommentAdapter(currentVideo.getComments(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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

    private void addNewComment(String commentText) {
        new Thread(() -> {
            try {
                Log.d("video_data", currentVideo.getId() + ", " + getCurrentUsername() +", " + commentText);
                Response response = ApiHandler.addComment(currentVideo.getId(), getCurrentUsername(), commentText);
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("data") && !jsonResponse.get("data").isJsonNull()) {
                        Comment newComment = new Gson().fromJson(jsonResponse.get("data"), Comment.class);
                        getActivity().runOnUiThread(() -> {
                            currentVideo.addComment(newComment);
                            adapter.notifyItemInserted(currentVideo.getComments().size() - 1);
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
                Response response = ApiHandler.editComment(currentVideo.getId(), comment.getId(), newText);
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
                Response response = ApiHandler.likeComment(currentVideo.getId(), comment.getId(), getCurrentUsername());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        // Update the comment object and notify the adapter
                        // You might need to parse the response to get the updated comment data
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
                Response response = ApiHandler.dislikeComment(currentVideo.getId(), comment.getId(), getCurrentUsername());
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
    public void onEditClicked(Comment comment) {
        if (getCurrentUsername().equals(comment.getUsername())) {
            editingComment = comment;
            editingPosition = currentVideo.getComments().indexOf(comment);
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
                Response response = ApiHandler.deleteComment(currentVideo.getId(), comment.getId());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        int position = currentVideo.getComments().indexOf(comment);
                        if (position != -1) {
                            currentVideo.getComments().remove(position);
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
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.getString("username", "");
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.contains("username");
    }
}