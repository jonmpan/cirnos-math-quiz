package com.example.quiz;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class HomeScreen extends Fragment {
    Button btn_normal, btn_lunatic;
    TextView tv_loading;
    NormalGame normalGame = new NormalGame();
    private SoundPlayer sp;
    MediaPlayer mp;
    CountDownTimer timer = null;
    MainActivity mainActivity;
    String difficulty;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        sp = MainActivity.getSp();
        mp = MainActivity.getMp();
//        mp.start();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_screen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mainActivity = ((MainActivity)getActivity());
        difficulty = mainActivity.getDifficultyMainActivity();
//        HomeScreen myFragment = (HomeScreen) getFragmentManager().findFragmentById(R.id.homeScreen);
        HomeScreen myFragment = (HomeScreen) getChildFragmentManager().findFragmentByTag("HOME_SCREEN_REAL");
        if (myFragment != null) {
            Log.d("MyFragment", "Here i am");
            mainActivity.setHomeScreen(myFragment);
        } else {
            Log.d("MyFragment", "Inside Else");
        }
        super.onViewCreated(view, savedInstanceState);
        btn_normal = view.findViewById(R.id.btn_normal);
        btn_lunatic = view.findViewById(R.id.btn_lunatic);
        btn_normal.setVisibility(View.INVISIBLE);
        btn_lunatic.setVisibility(View.INVISIBLE);
        tv_loading = view.findViewById(R.id.tv_loading);

        view.findViewById(R.id.btn_home_to_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_homeScreen_to_scoreScreen);
            }
        });
        WebView webView = (WebView) view.findViewById(R.id.webView_homeScreen);
        if(difficulty == "normal"){
            webView.loadUrl("file:///android_asset/gifView.html");
        }
        if(difficulty == "lunatic"){
            webView.loadUrl("file:///android_asset/lunaticGifView.html");
        }

        timer = new CountDownTimer(10000, 1000) {
            private boolean gameReady = false;
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("timer","timer - "+millisUntilFinished);
                gameReady = mainActivity.isReady();
                if(gameReady){
                    btn_normal.setVisibility(View.VISIBLE);
                    btn_lunatic.setVisibility(View.VISIBLE);
                    tv_loading.setVisibility(View.INVISIBLE);
                    timer.cancel();
                }
            }
            @Override
            public void onFinish() {
                timer.cancel();
            }
        };
        timer.start();

        view.findViewById(R.id.btn_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmpUri = ((MainActivity)getActivity()).getMpUri();
                Log.d("tmpUri", "tmpUri: "+tmpUri);
                Bundle bundle = new Bundle();
                difficulty = "normal";
                mainActivity.setDifficultyMainActivity("normal");
                bundle.putString("difficulty", "normal");
                sp.playStartSound();
                try {
                    String filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.easybgm;
                    Log.d("filename", "filename: "+filename);
                    if(tmpUri != "normal"){
                        ((MainActivity)getActivity()).setMpUri("normal");
                        mp.reset();
                        mp.setLooping(true);
                        mp.setDataSource(getActivity(), Uri.parse(filename));
                        mp.prepare();
                        mp.start();
                    }
//                    mp.reset();
//                    String filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.easybgm;
//                    mp.setDataSource(getActivity(), Uri.parse(filename));
//                    mp.prepare();
//                    mp.start();
                    Log.d("TRY TO PLAY", "Line 44");
                } catch (Exception e){
                    e.printStackTrace();
                }
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_homeScreen_to_normalGame, bundle);
            }
        });

        view.findViewById(R.id.btn_lunatic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmpUri = ((MainActivity)getActivity()).getMpUri();
                Bundle bundle = new Bundle();
                difficulty = "lunatic";
                mainActivity.setDifficultyMainActivity("lunatic");
                bundle.putString("difficulty", "lunatic");
                sp.playSpellcardSound();
                try {
                    String filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.lunaticbgm;
                    if(tmpUri != "lunatic"){
                        ((MainActivity)getActivity()).setMpUri("lunatic");
                        mp.reset();
                        mp.setLooping(true);
                        mp.setDataSource(getActivity(), Uri.parse(filename));
                        mp.prepare();
                        mp.start();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_homeScreen_to_normalGame, bundle);
            }
        });
    }

//    public void updateScore(){
//        tv_normal_high_score.setText(((MainActivity)getActivity()).getNormalScore());
//        tv_normal_high_score.setVisibility(View.VISIBLE);
//        tv_lunatic_high_score.setText(((MainActivity)getActivity()).getLunaticScore());
//        tv_lunatic_high_score.setVisibility(View.VISIBLE);
//    }
}