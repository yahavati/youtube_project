package com.example.youtube_project.home;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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

        profileViewModel.getProfilePhotoUrl().observe(getViewLifecycleOwner(), this::loadProfilePhoto);


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

    private void loadProfilePhoto(String base64String) {
        try {
            // Decode the Base64 string into a Bitmap
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Create a circular bitmap
            Bitmap circularBitmap = getCircularBitmap(decodedByte);

            // Set the circular bitmap to the ImageView
            buttonYou.setImageBitmap(circularBitmap);
        } catch (Exception e) {
            buttonYou.setImageResource(R.drawable.ic_circlr1);
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



}
