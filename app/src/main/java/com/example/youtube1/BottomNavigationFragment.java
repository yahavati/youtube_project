package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BottomNavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        ImageButton buttonHome = view.findViewById(R.id.button_home);
        ImageButton buttonShorts = view.findViewById(R.id.button_shorts);
        ImageButton buttonSubscriptions = view.findViewById(R.id.button_subscriptions);
        ImageButton buttonYou = view.findViewById(R.id.button_you);

        // Set click listeners
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new HomeFragment());
            }
        });

        buttonShorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new ShortsFragment());
            }
        });

        buttonSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new SubscriptionsFragment());
            }
        });

        buttonYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new YouFragment());
            }
        });

        return view;
    }
}
