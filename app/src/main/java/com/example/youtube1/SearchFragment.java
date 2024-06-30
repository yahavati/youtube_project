package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the top and bottom fragments
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showTopAndBottomFragments(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to home fragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadFragment(new HomeFragment());
                    ((MainActivity) getActivity()).showTopAndBottomFragments(true);
                }
            }
        });

        // Handle microphone button click if needed

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show the top and bottom fragments again when search fragment is destroyed
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showTopAndBottomFragments(true);
        }
    }
}
