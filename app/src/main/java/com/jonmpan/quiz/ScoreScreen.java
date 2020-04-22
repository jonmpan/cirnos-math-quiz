package com.jonmpan.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ScoreScreen extends Fragment {
    MainActivity mainActivity;
    Button btn_back_to_home;
    TextView tv_normal_score, tv_lunatic_score;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        mainActivity = ((MainActivity)getActivity());
        return inflater.inflate(R.layout.score_screen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_back_to_home = view.findViewById(R.id.btn_back_to_home);
        tv_normal_score = view.findViewById(R.id.tv_normal_score);
        tv_lunatic_score = view.findViewById(R.id.tv_lunatic_score);

        tv_normal_score.setText(mainActivity.getNormalScore()+"points");
        tv_lunatic_score.setText(mainActivity.getLunaticScore()+"points");

        btn_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ScoreScreen.this)
                        .navigate(R.id.action_scoreScreen_to_homeScreen);
            }
        });
    }
}
