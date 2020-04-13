package com.example.quiz;

import android.util.Log;

import java.util.Random;

public class DumbQuestion {
    private String question;
    private String correctAnswer;
    private String [] answers;

    public DumbQuestion (String question, String correctAnswer, String [] answers){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public static DumbQuestion generateDumbQuestion (String difficulty){
        Log.d("GenerateDumbQuestion", "-----------------------------------");
        Log.d("GenerateDumbQuestion", "difficulty="+difficulty);
        Random rng = new Random();
        Integer upperLimit = 20;
        Integer ninePrefixLimit = 3;
        Integer[] numbers = {-1,-1,-1,-1,-1};
        String[] operators = {"","","",""};
        Integer numCount = null;
        Integer questionType; // 0 has a 9, 1 has no 9 but has ⑨ as the answer, 2 and 3 are just addition/subtraction
        String tempQuestion;
        String [] tempAnswers = {"0","0","0","⑨"};
        String tempCorrectAnswer;

//        questionType = rng.nextInt(4);
        questionType = 0;
//        questionType = 1;
        Log.d("GenerateDumbQuestion", "questionType="+questionType);
        if(difficulty == "normal"){
            numCount = rng.nextInt(2)+2;
            Log.d("GenerateDumbQuestion", "numCount="+numCount);
        }
        else if (difficulty == "lunatic"){
            numCount = rng.nextInt(2)+3;
            Log.d("GenerateDumbQuestion", "numCount="+numCount);
            upperLimit = 100;
            ninePrefixLimit = 20;
        }
        // questionType 0, 9 in the question
        if(questionType == 0){
            Integer tempTempCorrectAnswer = 0;
            Integer indexOf9;
            indexOf9 = rng.nextInt(numCount-1);
            Log.d("GenerateDumbQuestion", "indexOf9="+indexOf9);
            for(int i = 0; i<numCount; i++){
                numbers[i] = rng.nextInt(upperLimit)+1;
            }
            numbers[indexOf9] = Integer.parseInt(Integer.toString(rng.nextInt(ninePrefixLimit)+1) + "9");
            for(int i = 0; i<numCount-1; i++){
                if(Math.random() <= 0.5){
                    operators[i]="+";
                } else {
                    operators[i]="-";
                }
//                Log.d("GenerateDumbQuestion", "operators["+i+"]="+operators[i]);
            }
            tempTempCorrectAnswer = numbers[0];
            for(int i = 0; i<numCount-1; i++){
                if(operators[i]=="+"){
                    tempTempCorrectAnswer += numbers[i+1];
                } else if(operators[i]=="-") {
                    tempTempCorrectAnswer -= numbers[i+1];
                }
            }
            tempCorrectAnswer = Integer.toString(tempTempCorrectAnswer);
            Log.d("GenerateDumbQuestion", "tempCorrectAnswer="+tempCorrectAnswer);
//            if(rng.nextBoolean()){
//                tempCorrectAnswer = "9";
//            }
//            if(tempCorrectAnswer=="9"){
//                Log.d("GenerateDumbQuestion", "______________________________");
//                Log.d("GenerateDumbQuestion", "REDOING BECAUSE 9");
//                return generateDumbQuestion(difficulty);
//            } else {
//            }
                String [] tempTempAnswers = {"poop","0","0","0","0","0"};
                tempTempAnswers[0] = Integer.toString(tempTempCorrectAnswer+rng.nextInt(15)+5);
                tempTempAnswers[1] = Integer.toString(tempTempCorrectAnswer+rng.nextInt(4)+1);
                tempTempAnswers[2] = Integer.toString(tempTempCorrectAnswer-rng.nextInt(15)-5);
                tempTempAnswers[3] = Integer.toString(tempTempCorrectAnswer-rng.nextInt(4)-1);
                tempTempAnswers[4] = incorrectBadMath(numbers, operators);
                tempTempAnswers[5] = correctBadMath(numbers, operators);
                tempTempAnswers = Utils.ShuffleArray(tempTempAnswers);
                tempAnswers[0] = tempTempAnswers[0];
                tempAnswers[1] = tempTempAnswers[1];
                tempAnswers[2] = tempTempAnswers[2];
                tempAnswers[rng.nextInt(3)] = tempCorrectAnswer;
                Log.d("GenerateDumbQuestion","tempAnswers="+Utils.StringArrayToString(tempAnswers));
                tempQuestion=generateTempQuestion(numbers, operators);
                Log.d("GenerateDumbQuestion","tempQuestion="+tempQuestion);
                return new DumbQuestion(tempQuestion, tempCorrectAnswer, tempAnswers);
        }
        else if(questionType == 1){
            for(int i=0; i<numCount; i++){
                numbers[i] = generateNon9Number(upperLimit);
                Log.d("GenerateDumbQuestion", "numbers["+i+"]="+numbers[i]);
            }
        }
        else if(questionType == 2 || questionType == 3){

        }
        return null;
    }

//    public static DumbQuestion generateType1Question (int numCount, int upperLimit, String tempQuestion, String tempCorrectAnswer, Integer[] numbers, String[] operators, String [] tempAnswers){
//        for(int i=0; i<numCount; i++){
//            numbers[i] = generateNon9Number(upperLimit);
//            Log.d("GenerateDumbQuestion", "numbers["+i+"]="+numbers[i]);
//        }
//        return new DumbQuestion(tempQuestion, tempCorrectAnswer, tempAnswers);
//    }

    public static int generateNon9Number (int bound){
        Random rng = new Random();
        int temp = rng.nextInt(bound);
        String tempString = Integer.toString(temp);
//        if(rng.nextBoolean()){
//            tempString = "911";
//        }
        if(!tempString.contains("9")){
            return temp;
        } else {
//            Log.d("GenerateDumbQuestion", "RECURSION:"+temp);
            return generateNon9Number(bound);
        }
    }

    public static String generateTempQuestion(Integer [] numbers, String [] operators){
        String tempQuestion = Integer.toString(numbers[0]);
        for(int i=0; i<operators.length; i++){
            if(operators[i] == "+" || operators[i] == "-"){
                tempQuestion+=operators[i]+numbers[i+1];
            }
        }
        return tempQuestion;
    }

    public static String correctBadMath(Integer [] numbers, String [] operators) {
        String tempAnswer = Integer.toString(numbers[0]);
        for(int i=0; i<operators.length; i++){
            if(operators[i] == "+"){
                tempAnswer = tempAnswer+numbers[i+1];
            } else if(operators[i]=="-") {
                tempAnswer = numbers[i+1]+tempAnswer;
            }
        }
        Log.d("GenerateDumbQuestion", "correctBadMath="+tempAnswer);
        return tempAnswer;
    }

    public static String incorrectBadMath(Integer [] numbers, String [] operators) {
        String tempAnswer = Integer.toString(numbers[0]);
        for(int i=0; i<operators.length; i++){
            if(operators[i] == "+"){
                tempAnswer = numbers[i+1]+tempAnswer;
            } else if(operators[i]=="-") {
                tempAnswer = tempAnswer+numbers[i+1];
            }
        }
        Log.d("GenerateDumbQuestion", "incorrectBadMath="+tempAnswer);
        return tempAnswer;
    }

    public static String incorrectBadMath2(Integer [] numbers, String [] operators) {
        String tempAnswer = Integer.toString(numbers[0]);
        if(operators[0] == "+"){
            tempAnswer = tempAnswer+numbers[1];
        } else if(operators[0]=="-") {
            tempAnswer = numbers[1]+tempAnswer;
        }
        if(operators.length>1){
            for(int i=1; i<operators.length; i++){
                if(operators[i] == "+"){
                    tempAnswer = numbers[i+1]+tempAnswer;
                } else if(operators[i]=="-") {
                    tempAnswer = tempAnswer+numbers[i+1];
                }
            }
        }
        Log.d("GenerateDumbQuestion", "incorrectBadMath2="+tempAnswer);
        return tempAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
}
