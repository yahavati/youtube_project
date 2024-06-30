package com.example.youtube1;

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

public class CommentsFragment extends Fragment implements CommentAdapter.OnCommentClickListener {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private EditText editTextComment;
    private Button buttonAddComment;

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

        buttonAddComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString();
            if (!commentText.isEmpty()) {
                Comment newComment = new Comment("Username", commentText, 0);
                commentList.add(newComment);
                adapter.notifyItemInserted(commentList.size() - 1);
                editTextComment.setText("");
            }
        });

        return view;
    }

    @Override
    public void onLikeClicked(Comment comment) {
        comment.setLikeCount(comment.getLikeCount() + 1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDislikeClicked(Comment comment) {
        comment.setLikeCount(comment.getLikeCount() - 1);
        adapter.notifyDataSetChanged();
    }
}
