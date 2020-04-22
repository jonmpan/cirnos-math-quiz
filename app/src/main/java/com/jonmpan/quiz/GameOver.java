package com.jonmpan.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class GameOver extends Fragment {
    SoundPlayer sp;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        sp = ((MainActivity)getActivity()).getSp();
        return inflater.inflate(R.layout.game_over, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = (WebView) view.findViewById(R.id.webView_homeScreen);
        webView.loadUrl("file:///android_asset/gameOverGif.html");
        view.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.playStartSound();
                NavHostFragment.findNavController(GameOver.this)
                        .navigate(R.id.action_gameOver_to_homeScreen2);
            }
        });
    }
}
