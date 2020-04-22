package com.jonmpan.quiz;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private List<Question> questions;
    private int numberCorrect;
    private int numberIncorrect;
    private int totalQuestions;
    private int score;
    private int lives;
    private String difficulty = NormalGame.getDifficulty();
    private Question currentQuestion;
    private List<DumbQuestion> dumbQuestions;
    private DumbQuestion dumbQuestion;
    private int lastRandom;
    private int range;

    public DumbQuestion getDumbQuestion() {
        return dumbQuestion;
    }


    private boolean gameOver;

    public Game(List<DumbQuestion> dqs) {
        numberCorrect = 0;
        numberIncorrect = 0;
        totalQuestions = 0;
        score = 0;
        lives = 3;
        currentQuestion = new Question(10);
        questions = new ArrayList<Question>();
        gameOver = false;
        dumbQuestions = dqs;
        Log.d("Game", "game: line 21 "+difficulty+" "+dumbQuestions.size());
        if(difficulty == "normal"){
            range = 25;
        } else {
            range = dumbQuestions.size();
        }
    }

    public void makeNewQuestion() {
        Log.d("Game", "makeNewQuestion: line 29");
        currentQuestion = new Question(10);
        totalQuestions++;
        questions.add(currentQuestion);
    }

    public void makeDumbQuestion() {
        Random r = new Random();
        int result = r.nextInt(range);
        if(r.nextBoolean()){
            dumbQuestion = DumbQuestion.generateDumbQuestion(difficulty);
        } else{
            if(result != lastRandom){
                lastRandom = result;
                dumbQuestion = dumbQuestions.get(result);
            } else {
                makeDumbQuestion();
            }
        }
    }

    public boolean checkAnswer(String submittedAnswer) {
        boolean isCorrect;
        if (dumbQuestion.getCorrectAnswer() == submittedAnswer) {
            numberCorrect++;
            isCorrect = true;
        } else {
            numberIncorrect++;
            isCorrect=false;
            lives--;
            if(lives == 0){
                gameOver = true;
            }
        }
        score = numberCorrect;
        return isCorrect;
    }

    public boolean checkDumbAnswer(String submittedAnswer) {
        boolean isCorrect;
        if (dumbQuestion.getCorrectAnswer() == submittedAnswer) {
            numberCorrect++;
            isCorrect = true;
        } else {
            numberIncorrect++;
            isCorrect=false;
            lives--;
            if(lives == 0){
                gameOver = true;
            }
        }
        score = numberCorrect;
        return isCorrect;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public void setNumberCorrect(int numberCorrect) {
        this.numberCorrect = numberCorrect;
    }

    public int getNumberIncorrect() {
        return numberIncorrect;
    }

    public void setNumberIncorrect(int numberIncorrect) {
        this.numberIncorrect = numberIncorrect;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
