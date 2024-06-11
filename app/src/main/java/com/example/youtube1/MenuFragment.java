package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Prevent touch events within the MenuFragment from propagating
        View menuContent = view.findViewById(R.id.menu_content);
        menuContent.setOnTouchListener((v, event) -> true);

        View transparentOverlay = view.findViewById(R.id.transparent_overlay);
        transparentOverlay.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Close the MenuFragment and show the HomeFragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
                return true;
            }
            return false;
        });

        return view;
    }
}
