package com.example.youtube_project.home;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.youtube_project.Constants;
import com.example.youtube_project.ProfileViewModel;
import com.example.youtube_project.R;
import com.example.youtube_project.user.UserManager;
import com.example.youtube_project.ApiHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Response;



public class BottomNavigationFragment extends Fragment {
    private UserManager userManager;
    private ImageView buttonYou;
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);
        userManager = UserManager.getInstance();
        ImageButton buttonHome = view.findViewById(R.id.button_home);
        ImageButton buttonShorts = view.findViewById(R.id.button_shorts);
        ImageButton buttonSubscriptions = view.findViewById(R.id.button_subscriptions);
        buttonYou = view.findViewById(R.id.button_you);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        profileViewModel.getProfilePhotoUrl().observe(getViewLifecycleOwner(), this::updateProfilePhoto);
        // Fetch profile image if the user is logged in
        if (isLoggedIn(getContext())) {
            new FetchProfilePhotoTask().execute();
        }

        // Set click listeners
        buttonHome.setOnClickListener(v -> ((HomeScreenActivity) getActivity()).loadFragment(new HomeFragment()));
        buttonShorts.setOnClickListener(v -> ((HomeScreenActivity) getActivity()).loadFragment(new ShortsFragment()));
        buttonSubscriptions.setOnClickListener(v -> ((HomeScreenActivity) getActivity()).loadFragment(new SubscriptionsFragment()));
        buttonYou.setOnClickListener(v -> ((HomeScreenActivity) getActivity()).loadFragment(new YouFragment()));

        return view;
    }
    private static boolean isLoggedIn(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "") != "";
    }

    private void updateProfilePhoto(String photoUrl) {
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constants.URL + photoUrl.replace("\\", "/"))
                    .circleCrop()
                    .into(buttonYou);
        }
    }

    private class FetchProfilePhotoTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Assuming the server returns a JSON object with a field "profilePhotoUrl"
                Response response = ApiHandler.sendGet("/profile-photo");
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = new Gson().fromJson(response.body().string(), JsonObject.class);
                    return jsonResponse.get("profilePhotoUrl").getAsString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String photoUrl) {

            if (photoUrl != null) {
                Log.d("url_log", Constants.URL+photoUrl.replace("\\", "/"));

                // Use Glide to load the image into the ImageView
                Glide.with(BottomNavigationFragment.this)
                        .load(Constants.URL+photoUrl.replace("\\", "/"))
                        .circleCrop() // This applies circular cropping
                        .into(buttonYou);
            }
        }
    }
}
