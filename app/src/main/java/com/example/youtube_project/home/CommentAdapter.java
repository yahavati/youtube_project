package com.example.youtube_project.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube_project.R;
import com.example.youtube_project.home.Comment;
import com.example.youtube_project.user.UserManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private OnCommentClickListener listener;
    private UserManager userManager;

    public CommentAdapter(List<Comment> comments, OnCommentClickListener listener) {
        this.comments = comments;
        this.listener = listener;
        userManager = UserManager.getInstance();
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
        ImageView userPhoto; // Added for user photo

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text_username);
            commentText = itemView.findViewById(R.id.text_comment);
            likeCount = itemView.findViewById(R.id.text_like_count);
            likeButton = itemView.findViewById(R.id.button_like);
            dislikeButton = itemView.findViewById(R.id.button_dislike);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
            userPhoto = itemView.findViewById(R.id.image_user_photo); // Initialize user photo ImageView
        }

        public void bind(Comment comment, OnCommentClickListener listener) {
            username.setText(comment.getUsername());
            commentText.setText(comment.getText());
            likeCount.setText(String.valueOf(comment.getLikeCount()));

            likeButton.setImageResource(comment.isLiked() ? R.drawable.like : R.drawable.like);
            dislikeButton.setImageResource(comment.isDisliked() ? R.drawable.dislike : R.drawable.dislike);

            // Set user photo
            if (comment.getUserPhoto() != null) {
                try {
                    Bitmap bitmap = getBitmapFromUri(comment.getUserPhoto());
                    bitmap = rotateImageIfRequired(bitmap, comment.getUserPhoto());
                    userPhoto.setImageBitmap(getCircularBitmap(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                    userPhoto.setImageResource(R.drawable.ic_circlr1); // Set a default image in case of error
                }
            } else {
                userPhoto.setImageResource(R.drawable.ic_circlr1); // Default user photo
            }

            likeButton.setOnClickListener(v -> listener.onLikeClicked(comment));
            dislikeButton.setOnClickListener(v -> listener.onDislikeClicked(comment));

            String currentUserName = UserManager.getInstance().getCurrentUserName();
            if (currentUserName != null && currentUserName.equals(comment.getUsername())) {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setOnClickListener(v -> listener.onEditClicked(comment));
                deleteButton.setOnClickListener(v -> listener.onDeleteClicked(comment));
            } else {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }

        private Bitmap getCircularBitmap(Bitmap bitmap) {
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);
            if (squaredBitmap != bitmap) {
                bitmap.recycle();
            }

            Bitmap circularBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(circularBitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return circularBitmap;
        }

        private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
            InputStream input = itemView.getContext().getContentResolver().openInputStream(selectedImage);
            ExifInterface ei;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ei = new ExifInterface(input);
            } else {
                ei = new ExifInterface(selectedImage.getPath());
            }

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }

        private Bitmap rotateImage(Bitmap img, int degree) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        }

        private Bitmap getBitmapFromUri(Uri uri) throws IOException {
            InputStream inputStream = itemView.getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        }
    }

    public interface OnCommentClickListener {
        void onLikeClicked(Comment comment);
        void onDislikeClicked(Comment comment);
        void onEditClicked(Comment comment);
        void onDeleteClicked(Comment comment);
    }
}
