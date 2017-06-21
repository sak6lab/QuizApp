package com.example.sakethdargula1.quizapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SakethDargula1 on 5/12/17.
 */

public class QuizProgressReader {

    InputStream inputStream;

    public QuizProgressReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ArrayList<Question> getProgressedQuestions() throws JSONException, FileNotFoundException {
        String save = readSave();
        ArrayList<Question> questions = new ArrayList<>();
        JSONObject object = new JSONObject(save);
        JSONArray array = object.getJSONArray("Questions");
        for(int x=0; x<10; x++){
            JSONObject questionItem = array.getJSONObject(x);
            String question = questionItem.getString("question");
            String correctAnswer = questionItem.getString("correctAnswer");
            ArrayList<String> answers = new ArrayList<>();
            JSONArray answersArray = questionItem.getJSONArray("answers");
            Boolean isbool = questionItem.getBoolean("isbool");
            int max;
            if (isbool){
                max = 2;
            } else {
                max = 4;
            }
            for(int i = 0;i<max;i++){
                String answerItem = answersArray.getString(i);
                answers.add(answerItem);
            }

            String category = questionItem.getString("category");
            String userAnswer = questionItem.getString("userAnswer");
            int imageResID = questionItem.getInt("imageResID");
            Question questionObject = new Question(question, correctAnswer, answers, category, isbool, imageResID);
            questionObject.setUserAnswer(userAnswer);
            questions.add(questionObject);
        }
        return questions;
    }

    private String readSave() throws FileNotFoundException {
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        String string = "";
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            Log.d("Save",builder.toString());
            string = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return string;
    }

}

