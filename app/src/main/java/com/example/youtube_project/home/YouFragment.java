package com.example.youtube_project.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.youtube_project.R;
import com.example.youtube_project.user.UserDetails;
import com.example.youtube_project.user.UserManager;

public class YouFragment extends Fragment {
    private ImageView userIcon;
    private UserManager userManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_you, container, false);
        userIcon = view.findViewById(R.id.user_icon);
        userManager = UserManager.getInstance();
        UserDetails currUser = userManager.getCurrentUser();
      //  userIcon.setImageURI(currUser.getProfilePhoto());
        return view;
    }
}
