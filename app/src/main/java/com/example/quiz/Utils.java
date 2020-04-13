package com.example.quiz;

import android.util.Log;

import java.util.Random;

class Utils {
    public static String StringArrayToString (String [] arr){
        String arrayStringed = arr[0];
        for (int i=1; i<arr.length; i++){
            arrayStringed += ","+arr[i];
        }
        return arrayStringed;
    }
    public static String IntArrayToString (int [] arr){
        String arrayStringed = Integer.toString(arr[0]);
        for (int i=1; i<arr.length; i++){
            arrayStringed += ","+arr[i];
        }
        return arrayStringed;
    }
    public static String [] ShuffleArray(String [] answerArray){
        int index;
        String temp;
        Random randomNumberGenerator = new Random();
        String[] array = answerArray;
        Log.d("Question", "line 44");
        for (int i = array.length - 1; i > 0; i--){
            index = randomNumberGenerator.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}
