package com.example.youtube_project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.youtube_project.Constants;
import com.example.youtube_project.ProfileViewModel;
import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.example.youtube_project.ApiHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube_project.user.UserManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.Response;

public class YouFragment extends Fragment {

    private ImageView userIcon;
    private TextView userName;
    private Button yourVideos;
    private static final String TAG = "YouFragment";
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you, container, false);
        yourVideos = view.findViewById(R.id.your_videos_button);
        userIcon = view.findViewById(R.id.user_icon);
        userName = view.findViewById(R.id.user_name);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        fetchUserData(); // Aligning with /auth/me endpoint

        userIcon.setOnClickListener(v -> showPopupMenu());

        yourVideos.setOnClickListener(v -> {
            if (ApiHandler.isLoggedIn()) {
                ((HomeScreenActivity) getActivity()).loadFragment(new YourVideos());
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        return view;
    }

    private void fetchUserData() {
        new Thread(() -> {
            try {
                // Step 1: Fetch the token
                String username = UserManager.getInstance().getCurrentUserName(); // Replace with the actual username if dynamic
                Response tokenResponse = ApiHandler.fetchToken(username);

                if (tokenResponse.isSuccessful()) {
                    // Step 2: Use the token to fetch user data
                    Response response = ApiHandler.getCurrentUser();

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                            JsonObject data = jsonResponse.getAsJsonObject();
                            String nickname = data.get("username").getAsString();
                            String profilePhotoUrl = data.get("photo").getAsString();

                            getActivity().runOnUiThread(() -> {
                                userName.setText(nickname);
                                loadProfilePhoto(profilePhotoUrl);
                                Log.d(TAG, "User data fetched successfully");

                                profileViewModel.setProfilePhotoUrl(profilePhotoUrl);
                            });

                    } else {
                        Log.e(TAG, "Failed to fetch user data: " + response.code());
                        getActivity().runOnUiThread(() -> {
                            setDefaultUserInfo();
                        });
                    }
                } else {
                    Log.e(TAG, "Failed to fetch token: " + tokenResponse.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error fetching user data", e);
                getActivity().runOnUiThread(() -> {
                    setDefaultUserInfo();
                });
            }
        }).start();
    }


    private void loadProfilePhoto(String base64String) {
        try {
            // Decode the Base64 string into a Bitmap
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Create a circular bitmap
            Bitmap circularBitmap = getCircularBitmap(decodedByte);

            // Set the circular bitmap to the ImageView
            userIcon.setImageBitmap(circularBitmap);
        } catch (Exception e) {
            Log.e(TAG, "Failed to decode Base64 image", e);
            userIcon.setImageResource(R.drawable.ic_circlr1);
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int minEdge = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(minEdge, minEdge, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rect = new RectF(0, 0, minEdge, minEdge);
        canvas.drawOval(rect, paint);

        Path path = new Path();
        path.addOval(rect, Path.Direction.CCW);
        canvas.clipPath(path);

        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;
    }

    private void reloadBottomNavigationFragment() {
        if (getActivity() instanceof HomeScreenActivity) {
            HomeScreenActivity activity = (HomeScreenActivity) getActivity();
            activity.runOnUiThread(() -> {
                BottomNavigationFragment bottomNavFragment = new BottomNavigationFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottom_navigation_container, bottomNavFragment)
                        .commit();
            });
        }
    }

    private void setDefaultUserInfo() {
        userIcon.setImageResource(R.drawable.ic_circlr1);
        userName.setText("Username");
        Log.d(TAG, "Default user info set");
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), userIcon);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

        if (ApiHandler.isLoggedIn()) {
            popupMenu.getMenu().findItem(R.id.action_login).setVisible(false);
            popupMenu.getMenu().findItem(R.id.action_logout).setVisible(true);
        } else {
            popupMenu.getMenu().findItem(R.id.action_login).setVisible(true);
            popupMenu.getMenu().findItem(R.id.action_logout).setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_login) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                return true;
            } else if (item.getItemId() == R.id.action_logout) {
                performLogout(); // Align with /auth/logout endpoint
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void performLogout() {
        new Thread(() -> {
            try {
                Response response = ApiHandler.sendPost("/auth/logout", new JsonObject()); // Align with /auth/logout endpoint

                if (response.isSuccessful()) {
                    ApiHandler.clearToken();
                    getActivity().runOnUiThread(() -> {
                        SharedPreferences sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.remove("username");
                        edit.apply();
                        Intent homeIntent = new Intent(getActivity(), HomeScreenActivity.class);
                        startActivity(homeIntent);
                        Log.d(TAG, "Logged out successfully");
                    });
                } else {
                    Log.e(TAG, "Logout failed: " + response.code());
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Logout failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error during logout", e);
            }
        }).start();
    }
}
