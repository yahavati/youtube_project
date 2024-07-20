package com.example.youtube_project.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.example.youtube_project.home.HomeScreenActivity;
import com.example.youtube_project.user.UserDetails;
import com.example.youtube_project.user.UserManager;

import java.io.IOException;
import java.io.InputStream;

public class YouFragment extends Fragment {

    private ImageView userIcon;
    private TextView userName;
    private UserManager userManager;
    private Button yourVideos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you, container, false);
        yourVideos = view.findViewById(R.id.your_videos_button);
        userIcon = view.findViewById(R.id.user_icon);
        userName = view.findViewById(R.id.user_name);
        userManager = UserManager.getInstance();
        UserDetails currUser = userManager.getCurrentUser();

        // Set the profile image and nickname if the user is logged in
        if (userManager.isLoggedIn()) {
            Uri photo = currUser.getProfilePhoto();
            if (photo != null) {
                try {
                    Bitmap bitmap = getBitmapFromUri(photo);
                    bitmap = rotateImageIfRequired(bitmap, photo);
                    userIcon.setImageBitmap(getCircularBitmap(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            userName.setText(currUser.getNickName());
        } else {
            userIcon.setImageResource(R.drawable.ic_circlr1); // Pink rounded photo
            userName.setText("Username");
        }

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

                if (userManager.isLoggedIn()) {
                    popupMenu.getMenu().findItem(R.id.action_login).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.action_logout).setVisible(true);
                } else {
                    popupMenu.getMenu().findItem(R.id.action_login).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.action_logout).setVisible(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_login) {
                            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(loginIntent);
                            return true;
                        } else if (item.getItemId() == R.id.action_logout) {
                            userManager.setLoggedIn(false);
                            userManager.logout();
                            Intent homeIntent = new Intent(getActivity(), HomeScreenActivity.class);
                            startActivity(homeIntent);
                            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        yourVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userManager.isLoggedIn()) {
                    ((HomeScreenActivity) getActivity()).loadFragment(new YourVideos());
                } else {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });

        return view;
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
        InputStream input = getActivity().getContentResolver().openInputStream(selectedImage);
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
        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return bitmap;
    }
}
