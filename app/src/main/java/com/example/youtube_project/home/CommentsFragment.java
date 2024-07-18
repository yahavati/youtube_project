package com.example.youtube_project.home;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.example.youtube_project.user.UserManager;

public class CommentsFragment extends Fragment implements CommentAdapter.OnCommentClickListener {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private EditText editTextComment;
    private Button buttonAddComment;

    private Comment editingComment;
    private int editingPosition = -1;

    private UserManager userManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonAddComment = view.findViewById(R.id.button_add_comment);

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        userManager = UserManager.getInstance();

        buttonAddComment.setOnClickListener(v -> {
            if (!userManager.isLoggedIn()) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }

            String commentText = editTextComment.getText().toString();
            if (!commentText.isEmpty()) {
                if (editingComment != null) {
                    editingComment.setText(commentText);
                    adapter.notifyItemChanged(editingPosition);
                    editingComment = null;
                    editingPosition = -1;
                } else {
                    Comment newComment = new Comment(userManager.getCurrentUser().getNickName(), commentText, 0, false, false);
                    commentList.add(newComment);
                    adapter.notifyItemInserted(commentList.size() - 1);
                }
                editTextComment.setText("");
            }
        });

        return view;
    }

    @Override
    public void onLikeClicked(Comment comment) {
        userManager = UserManager.getInstance();
        if (!userManager.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (comment.isDisliked()) {
            comment.setDisliked(false);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }

        if (comment.isLiked()) {
            comment.setLiked(false);
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            comment.setLiked(true);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDislikeClicked(Comment comment) {
        userManager = UserManager.getInstance();
        if (!userManager.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (comment.isLiked()) {
            comment.setLiked(false);
            comment.setLikeCount(comment.getLikeCount() - 1);
        }

        if (comment.isDisliked()) {
            comment.setDisliked(false);
        } else {
            comment.setDisliked(true);
            comment.setLikeCount(comment.getLikeCount() - 1);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClicked(Comment comment) {
        editingComment = comment;
        editingPosition = commentList.indexOf(comment);
        editTextComment.setText(comment.getText());
        editTextComment.requestFocus();
    }

    @Override
    public void onDeleteClicked(Comment comment) {
        int position = commentList.indexOf(comment);
        if (position != -1) {
            commentList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}
