package com.example.youtube_project.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.youtube_project.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private OnCommentClickListener listener;

    public CommentAdapter(List<Comment> comments, OnCommentClickListener listener) {
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment, listener);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView commentText;
        TextView likeCount;
        ImageButton likeButton;
        ImageButton dislikeButton;
        ImageButton editButton;
        ImageButton deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text_username);
            commentText = itemView.findViewById(R.id.text_comment);
            likeCount = itemView.findViewById(R.id.text_like_count);
            likeButton = itemView.findViewById(R.id.button_like);
            dislikeButton = itemView.findViewById(R.id.button_dislike);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Comment comment, OnCommentClickListener listener) {
            username.setText(comment.getUsername());
            commentText.setText(comment.getText());
            likeCount.setText(String.valueOf(comment.getLikeCount()));

            likeButton.setImageResource(comment.isLiked() ? R.drawable.like : R.drawable.like);
            dislikeButton.setImageResource(comment.isDisliked() ? R.drawable.dislike : R.drawable.dislike);

            likeButton.setOnClickListener(v -> listener.onLikeClicked(comment));
            dislikeButton.setOnClickListener(v -> listener.onDislikeClicked(comment));
            editButton.setOnClickListener(v -> listener.onEditClicked(comment));
            deleteButton.setOnClickListener(v -> listener.onDeleteClicked(comment));
        }
    }

    public interface OnCommentClickListener {
        void onLikeClicked(Comment comment);
        void onDislikeClicked(Comment comment);
        void onEditClicked(Comment comment);
        void onDeleteClicked(Comment comment);
    }
}
