package com.jonmpan.quiz;

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

        questionType = rng.nextInt(4);
//        questionType = 0;
//        questionType = 1;
//        questionType = 2;
        Log.d("GenerateDumbQuestion", "questionType="+questionType);
        if(difficulty == "normal"){
            numCount = 3;
            Log.d("GenerateDumbQuestion", "numCount="+numCount);
        }
        else if (difficulty == "lunatic"){
            numCount = rng.nextInt(2)+3;
            Log.d("GenerateDumbQuestion", "numCount="+numCount);
            upperLimit = 100;
            ninePrefixLimit = 20;
        }
        operators = generateOperatorsArray(operators, numCount);
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
            // generate operators
            Log.d("GenerateDumbQuestion", "operators="+Utils.StringArrayToString(operators));
            // get correct answer
            tempCorrectAnswer = Integer.toString(correctArithmetic(numbers,operators,numCount));
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
                String [] tempTempAnswers = {"0","0","0","0","0","0"};
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
            if (difficulty == "normal") {
                numCount = 3;
            }
            tempCorrectAnswer = "⑨";
            // generate numbers
            for(int i=0; i<numCount; i++){
                if(i==numCount-1){
                    int tempAnswerWithoutLastNumber = correctArithmetic(numbers,operators,numCount-1);
                    int lastNumber = 0;
                    if(operators[numCount-2] == "+"){
                        lastNumber = 9-tempAnswerWithoutLastNumber;
                        if(lastNumber<0){
                            operators[numCount-2] = "-";
                            lastNumber = Math.abs(lastNumber);
                        }
                    } else if(operators[numCount-2] == "-") {
                        lastNumber = -9+tempAnswerWithoutLastNumber;
                        if(lastNumber<0){
                            operators[numCount-2] = "+";
                            lastNumber = Math.abs(lastNumber);
                        }
                    }
                    numbers[numCount-1] = lastNumber;
                    Log.d("GenerateDumbQuestion", "operators="+Utils.StringArrayToString(operators));
                    Log.d("GenerateDumbQuestion", "tempAnswerWithoutLastNumber="+tempAnswerWithoutLastNumber);
                    Log.d("GenerateDumbQuestion", "lastNumber="+lastNumber);
                    Log.d("GenerateDumbQuestion", "correctArithmetic="+correctArithmetic(numbers,operators,numCount));
                } else {
                    numbers[i] = generateNon9Number(upperLimit);
                }
            }
            Log.d("GenerateDumbQuestion", "numbers="+Utils.IntArrayToString(numbers));
            tempQuestion = generateTempQuestion(numbers,operators);
            Log.d("GenerateDumbQuestion", "tempQuestion="+tempQuestion);
            if(checkIfNumberHas9(numbers[numCount-1])){
                Log.d("GenerateDumbQuestion", "checkIfNumberHas9=true");
                Log.d("GenerateDumbQuestion", "______________________________");
                Log.d("GenerateDumbQuestion", "REDOING BECAUSE 9");
                return generateDumbQuestion(difficulty);
            } else {
                Log.d("GenerateDumbQuestion", "checkIfNumberHas9=false");
                // generate answers, then return dumb question
                String [] tempTempAnswers = {"0","0","0","0","0"};
                tempTempAnswers[0] = Integer.toString(9+rng.nextInt(15)+5);
                tempTempAnswers[1] = Integer.toString(9+rng.nextInt(4)+1);
                tempTempAnswers[2] = correctBadMath(numbers, operators);
                tempTempAnswers[3] = incorrectBadMath(numbers, operators);
                tempTempAnswers[4] = incorrectBadMath2(numbers, operators);
                tempTempAnswers = Utils.ShuffleArray(tempTempAnswers);
                tempAnswers[0] = tempTempAnswers[0];
                tempAnswers[1] = tempTempAnswers[1];
                tempAnswers[2] = tempTempAnswers[2];
                return new DumbQuestion(tempQuestion,tempCorrectAnswer,tempAnswers);
            }
        }

        else if(questionType == 2 || questionType == 3){
            String [] tempTempAnswers = {"0","0","0","0"};
            for(int i=0; i<numCount; i++){
                numbers[i] = generateNon9NonRepeatingNumber(numbers, upperLimit, i);
            }
            Log.d("GenerateDumbQuestion", "numbers="+Utils.IntArrayToString(numbers));
            tempCorrectAnswer = correctBadMath(numbers, operators);
            int tempCorrectArithmetic = correctArithmetic(numbers, operators, numCount);
            tempTempAnswers[0] = Integer.toString(tempCorrectArithmetic-rng.nextInt(15)-5);
            tempTempAnswers[1] = Integer.toString(tempCorrectArithmetic-rng.nextInt(4)-1);
            tempTempAnswers[2] = incorrectBadMath(numbers, operators);
            tempTempAnswers[3] = incorrectBadMath2(numbers, operators);
            tempTempAnswers = Utils.ShuffleArray(tempTempAnswers);
            tempAnswers[0] = tempTempAnswers[0];
            tempAnswers[1] = tempTempAnswers[1];
            tempAnswers[2] = tempTempAnswers[2];
            tempAnswers[rng.nextInt(3)] = tempCorrectAnswer;
            tempQuestion = generateTempQuestion(numbers, operators);
            Log.d("GenerateDumbQuestion", "tempCorrectArithmetic="+tempCorrectArithmetic);
            Log.d("GenerateDumbQuestion", "tempQuestion="+tempQuestion);
            Log.d("GenerateDumbQuestion", "tempCorrectAnswer="+tempCorrectAnswer);
            Log.d("GenerateDumbQuestion", "tempAnswers="+Utils.StringArrayToString(tempAnswers));
            if(tempCorrectArithmetic != 9){
                return new DumbQuestion(tempQuestion, tempCorrectAnswer, tempAnswers);
            } else {
                return generateDumbQuestion(difficulty);
            }
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

    public static String [] generateOperatorsArray(String [] operators, int numCount){
        for(int i = 0; i<numCount-1; i++){
            if(Math.random() <= 0.5){
                operators[i]="+";
            } else {
                operators[i]="-";
            }
        }
        return operators;
    }

    public static boolean checkIfNumberHas9(int number){
        String numberStringed = Integer.toString(number);
        if(numberStringed.contains("9")){
            return true;
        } else {
            return false;
        }
    }

    public static int correctArithmetic(Integer [] numbers, String [] operators, int numCount){
        int tempTempCorrectAnswer = numbers[0];
        for(int i = 0; i<numCount-1; i++){
            if(operators[i]=="+"){
                tempTempCorrectAnswer += numbers[i+1];
            } else if(operators[i]=="-") {
                tempTempCorrectAnswer -= numbers[i+1];
            }
        }
        return tempTempCorrectAnswer;
    }

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

    public static int generateNon9NonRepeatingNumber (Integer [] numbers, int bound, int index){
        Random rng = new Random();
        int temp = rng.nextInt(bound);
        String tempString = Integer.toString(temp);
        for(int i=0; i<index; i++){
            if(numbers[i] == temp){
                Log.d("GenerateDumbQuestion", "generateNon9NonRepeatingNumber RECURSION");
                return generateNon9Number(bound);
            }
        }
        if(!tempString.contains("9")){
            return temp;
        } else {
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
