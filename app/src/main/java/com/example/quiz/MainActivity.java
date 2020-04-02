package com.example.quiz;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    public List<DumbQuestion> dumbQuestions;
    public List<DumbQuestion> getDumbQuestions() {
        return dumbQuestions;
    }
    private static MediaPlayer mp;
    private static SoundPlayer sp;
    public static MediaPlayer getMp() { return mp; }
    public static SoundPlayer getSp() { return sp; }
    private boolean ready = false;
    public boolean isReady() { return ready; }
    public String getMpUri() { return mpUri;    }
    public void setMpUri(String mpUri) { this.mpUri = mpUri; }
    private String mpUri = "normal";
    public String getNormalScore() { return normalScore; }
    public String getLunaticScore() { return lunaticScore; }
    private String normalScore;
    private String lunaticScore;
    public void setHomeScreen(HomeScreen homeScreen) { this.homeScreen = homeScreen; }
    private HomeScreen homeScreen;
    public String getDifficultyMainActivity() { return difficultyMainActivity; }
    public void setDifficultyMainActivity(String difficultyMainActivity) { this.difficultyMainActivity = difficultyMainActivity; }
    private String difficultyMainActivity = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        sp = new SoundPlayer(this);
        mp = MediaPlayer.create(this, R.raw.easybgm);
        mp.setLooping(true);
        setContentView(R.layout.activity_main);

        // manually creaetee fragment... figure out how to set navcontroller
//        FragmentManager fragMgr = getSupportFragmentManager();
//        FragmentTransaction fragTrans = fragMgr.beginTransaction();
//        homeScreen = new HomeScreen(); //my custom fragment
//        fragTrans.replace(android.R.id.content, homeScreen, "HOME_SCREEN_REAL");
//        fragTrans.addToBackStack(null);
//        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragTrans.commit();

//        val myNavHostFragment: NavHostFragment = my_nav_host_fragment as NavHostFragment
//        val inflater = myNavHostFragment.navController.navInflater
//        val graph = inflater.inflate(R.navigation.my_nav_graph)
//        myNavHostFragment.navController.graph = graph
//        Fragment navi = fragMgr.getPrimaryNavigationFragment();
//        fragTrans.setPrimaryNavigationFragment(navi);

        mQueue = Volley.newRequestQueue(this);
        getLifecycle().addObserver(new BackgroundServices());
        readFiles();
        jsonParse();
    }

    public void writeFile(String score, String difficulty) {
        Log.d("writeFile","Write File "+difficulty+score);
        try {
            if(difficulty == "normal" && Integer.parseInt(score) > Integer.parseInt(normalScore) || difficulty == "lunatic" && Integer.parseInt(score) > Integer.parseInt(lunaticScore)){
                Log.d("writeFile","Write File "+difficulty+score);
                FileOutputStream fileOutputStream = openFileOutput(difficulty+".txt", MODE_PRIVATE);
                fileOutputStream.write(score.getBytes());
                fileOutputStream.close();
                if(difficulty == "normal"){
                    normalScore = score;
                }
                if(difficulty == "lunatic"){
                    lunaticScore = score;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFiles() {
        try {
            Log.d("FileManager","Read Files");
            FileInputStream fileInputStream = openFileInput("normal.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
            Log.d("FileManager","Normal String Buffer "+stringBuffer.toString());
            if(stringBuffer.toString() != null){
                normalScore = stringBuffer.toString();
                Log.d("FileManager","inside normal if normalScore "+normalScore);
//                    homeScreen.updateScore();
            } else {
                normalScore = "0";
                Log.d("FileManager","inside normal else "+normalScore);
            }
            FileInputStream fileInputStream2 = openFileInput("lunatic.txt");
            InputStreamReader inputStreamReader2 = new InputStreamReader(fileInputStream2);
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
            StringBuffer stringBuffer2 = new StringBuffer();
            String lines2;
            Log.d("FileManager","Lunatic String Buffer "+stringBuffer2.toString());
            while ((lines2 = bufferedReader2.readLine()) != null) {
                stringBuffer2.append(lines2);
            }

            if(stringBuffer2.toString() != null) {
                lunaticScore = stringBuffer2.toString();
                Log.d("FileManager","Lunatic inside else" + lunaticScore);
            } else {
                lunaticScore = "0";
                Log.d("FileManager","Lunatic inside else" + lunaticScore);
            }
        } catch (Exception e){
            normalScore = "0";
            lunaticScore = "0";
            e.printStackTrace();
        }
    }

    private void jsonParse(){
        dumbQuestions = new ArrayList<DumbQuestion>();
        String url = "https://raw.githubusercontent.com/jonmpan/trivia-game/master/js/questions.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("questions");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject fullQuestion = jsonArray.getJSONObject(i);
                                // get question
                                String question = fullQuestion.getString("question");
//                                Log.d("jsonParse", "Question: "+question);
                                // gets answers
                                String[] answers = {"0","1","2","3"};
                                JSONArray answersArray = fullQuestion.getJSONArray("answers");
                                for(int j = 0; j < answersArray.length(); j++){
//                                    Log.d("jsonParse", "Answer: "+answersArray.getString(j));
                                    answers[j] = answersArray.getString(j);
                                }
                                // gets correct answer
                                Integer correctIndex = fullQuestion.getInt("correct");
                                String correctAnswer = answers[correctIndex];
//                                Log.d("jsonParse", "CorrectAnswer: "+correctAnswer);
                                DumbQuestion dq = new DumbQuestion(question, correctAnswer, answers);
                                dumbQuestions.add(dq);
                            }
                            for (int k = 0; k < dumbQuestions.size(); k++) {
                                DumbQuestion printDQ = dumbQuestions.get(k);
//                                Log.d("Dumb Questions", "Question: "+ printDQ.getQuestion());
                            }
                            Log.d("dumbQuestions Length", "length: "+dumbQuestions.size());
                            ready = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
