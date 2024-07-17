package com.example.youtube_project.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.youtube_project.R;
import com.example.youtube_project.activity.LoginActivity;
import com.example.youtube_project.home.HomeScreenActivity;
import com.example.youtube_project.user.UserDetails;
import com.example.youtube_project.user.UserManager;

public class YouFragment extends Fragment {

    private ImageView userIcon;
    private UserManager userManager;
    private Button yourVideos;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you, container, false);
        yourVideos = view.findViewById(R.id.your_videos_button);
        userIcon = view.findViewById(R.id.user_icon);
        userManager = UserManager.getInstance();
        UserDetails currUser = userManager.getCurrentUser();
        // userIcon.setImageURI(currUser.getProfilePhoto());

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
                if (userManager.isLoggedIn()){
                    ((HomeScreenActivity) getActivity()).loadFragment(new YourVideos());
                }
                else{
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        });

        return view;
    }
}
