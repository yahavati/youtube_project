package com.example.youtube1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TopRightButtonsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_right_buttons, container, false);

        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SearchFragment and hide top and bottom fragments
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).loadFragment(new SearchFragment());
                    ((MainActivity) getActivity()).showTopAndBottomFragments(false);
                }
            }
        });



        return view;
    }
}
