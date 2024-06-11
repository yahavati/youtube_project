package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;

public class BottomNavigationFragment extends Fragment {

    public BottomNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        ImageButton homeButton = view.findViewById(R.id.button_home);
        ImageButton subscriptionsButton = view.findViewById(R.id.button_subscriptions);
        ImageButton youButton = view.findViewById(R.id.button_you);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new HomeFragment());
            }
        });

        subscriptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new SubscriptionsFragment());
            }
        });

        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(new YouFragment());
            }
        });

        return view;
    }
}
