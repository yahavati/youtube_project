package com.example.youtube_project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
        fetchUserData();

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
                Response response = ApiHandler.sendGet("/user/profile");

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseReader(new StringReader(responseBody)).getAsJsonObject();

                    if (jsonResponse.get("success").getAsBoolean()) {
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        String nickname = data.get("username").getAsString();
                        String profilePhotoUrl = data.get("profilePhotoUrl").getAsString();

                        getActivity().runOnUiThread(() -> {
                            userName.setText(nickname);
                            loadProfilePhoto(profilePhotoUrl);
                            Log.d(TAG, "User data fetched successfully");

                            profileViewModel.setProfilePhotoUrl(profilePhotoUrl);
                        });
                    } else {
                        Log.e(TAG, "Failed to fetch user data: success is false");
                        getActivity().runOnUiThread(() -> {
                            setDefaultUserInfo();

                        });
                    }
                } else {
                    Log.e(TAG, "Failed to fetch user data: " + response.code());
                    getActivity().runOnUiThread(() -> {
                        setDefaultUserInfo();

                    });
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

    private void loadProfilePhoto(String photoUrl) {
        Glide.with(this)
                .load(Constants.URL + photoUrl.replace("\\", "/"))
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_circlr1)
                .error(R.drawable.ic_circlr1)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Failed to load profile photo", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "Profile photo loaded successfully");
                        reloadBottomNavigationFragment();
                        return false;
                    }
                })
                .into(userIcon);

        Log.d(TAG, "http://192.168.221.1:5000/" + photoUrl.replace("\\", "/"));
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
                performLogout();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void performLogout() {
        new Thread(() -> {

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

        }).start();
    }
}
