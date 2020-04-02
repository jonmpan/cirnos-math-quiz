package com.example.quiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;

import java.util.List;

public class NormalGame extends Fragment {
    private int secondsRemaining;
    private static String difficulty;
    private RequestQueue mQueue;
    private List<DumbQuestion> dumbQuestions;
    SoundPlayer sp;
    MainActivity mainActivity;

    Button btn_start, btn_answer0, btn_answer1, btn_answer2, btn_answer3;
    TextView tv_score, tv_timer, tv_lives, tv_questions;
    ProgressBar progress_timer;
    Game g;
    CountDownTimer timer;
    private Object NormalGame;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        sp = mainActivity.getSp();
        mainActivity = ((MainActivity)getActivity());
        difficulty = getArguments().getString("difficulty");
        Log.d("difficulty", "difficulty: "+difficulty);
        dumbQuestions = mainActivity.getDumbQuestions();
        Log.d("dumbquestions in game", "length "+dumbQuestions.size());
        g = new Game(dumbQuestions);
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.normal_game, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VideoView video = view.findViewById(R.id.videoView);
        // loads the webview with the gif
        if(difficulty == "normal"){
            video.setVisibility(View.INVISIBLE);
            WebView webView = (WebView) view.findViewById(R.id.webView);
            webView.loadUrl("file:///android_asset/gifView.html");
        } else {
//            video.setVisibility(View.VISIBLE);
//            video.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
//                    + R.raw.cirnomathclass));
//            video.start();
//
            WebView webView = (WebView) view.findViewById(R.id.webView);
            webView.loadUrl("file:///android_asset/lunaticGifView.html");
        }

        btn_answer0 = view.findViewById(R.id.btn_answer0);
        btn_answer1 = view.findViewById(R.id.btn_answer1);
        btn_answer2 = view.findViewById(R.id.btn_answer2);
        btn_answer3 = view.findViewById(R.id.btn_answer3);

        tv_timer = view.findViewById(R.id.tv_timer);
        tv_lives = view.findViewById(R.id.tv_lives);
        tv_score = view.findViewById(R.id.tv_score);
        tv_questions = view.findViewById(R.id.tv_questions);

        progress_timer = view.findViewById(R.id.progress_timer);

        tv_timer.setText("9");
        tv_lives.setText(Integer.toString(g.getLives()));
        nextTurn();

        final View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button buttonClicked = (Button) v;
//                int answerSelected = Integer.parseInt(buttonClicked.getText().toString());
                String answerSelected = buttonClicked.getText().toString();
                boolean correct = g.checkAnswer(answerSelected);
//                Toast.makeText(MainActivity.this, "answerSelected = "+answerSelected, Toast.LENGTH_SHORT).show();
                if(correct){
                    tv_score.setText(Integer.toString(g.getScore()));
                    btn_answer0.setVisibility(View.VISIBLE);
                    btn_answer1.setVisibility(View.VISIBLE);
                    btn_answer2.setVisibility(View.VISIBLE);
                    btn_answer3.setVisibility(View.VISIBLE);
                    sp.playCorrectSound();
                    timer.cancel();
                    nextTurn();
                } else {
                    tv_score.setText(Integer.toString(g.getScore()));
                    buttonClicked.setVisibility(View.INVISIBLE);
                    tv_lives.setText(Integer.toString(g.getLives()));
                    boolean gameOver = g.isGameOver();
                    if(gameOver){
                        timer.cancel();
                        Log.d("GameOver", "onClick: "+g.getScore()+difficulty);
                        mainActivity.writeFile(Integer.toString(g.getScore()), difficulty);
//                        Toast.makeText(NormalGame.this, "GameOver", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(NormalGame.this)
                                .navigate(R.id.action_normalGame_to_gameOver);
                        sp.playOrinSound();
                    } else {
                        sp.playIncorrectSound();
                    }
                }
            }
        };
        btn_answer0.setOnClickListener(answerButtonClickListener);
        btn_answer1.setOnClickListener(answerButtonClickListener);
        btn_answer2.setOnClickListener(answerButtonClickListener);
        btn_answer3.setOnClickListener(answerButtonClickListener);
    }

    private void nextTurn(){
        Log.d("Next", "nextTurn: Here I am");
//        g.makeNewQuestion();
//        Question currentQuestion = g.getCurrentQuestion();
        g.makeDumbQuestion();
        DumbQuestion currentQuestion = g.getDumbQuestion();
        String [] answers = currentQuestion.getAnswers();

        btn_answer0.setText(answers[0]);
        btn_answer1.setText(answers[1]);
        btn_answer2.setText(answers[2]);
        btn_answer3.setText(answers[3]);
        btn_answer0.setEnabled(true);
        btn_answer1.setEnabled(true);
        btn_answer2.setEnabled(true);
        btn_answer3.setEnabled(true);
        secondsRemaining = 10;

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining--;
                tv_timer.setText(Integer.toString(secondsRemaining));
                progress_timer.setProgress(10 - secondsRemaining);
            }
            @Override
            public void onFinish() {
//                g.checkAnswer(-999999999);
                g.checkDumbAnswer("afdlkjalskdjflaksjd");
                tv_score.setText(Integer.toString(g.getScore()));
                tv_lives.setText(Integer.toString(g.getLives()));
                boolean gameOver = g.isGameOver();
                if(gameOver){
                    sp.playOrinSound();
                    timer.cancel();
                    mainActivity.writeFile(Integer.toString(g.getScore()), difficulty);
                    NavHostFragment.findNavController(NormalGame.this)
                            .navigate(R.id.action_normalGame_to_gameOver);
                } else {
                    btn_answer0.setVisibility(View.VISIBLE);
                    btn_answer1.setVisibility(View.VISIBLE);
                    btn_answer2.setVisibility(View.VISIBLE);
                    btn_answer3.setVisibility(View.VISIBLE);
                    sp.playIncorrectSound();
                    nextTurn();
                }
            }
        };
        timer.start();
        tv_questions.setText(currentQuestion.getQuestion());
    };

    public static String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
