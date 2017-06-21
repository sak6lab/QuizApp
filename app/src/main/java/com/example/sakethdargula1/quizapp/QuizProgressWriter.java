package com.example.sakethdargula1.quizapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by SakethDargula1 on 5/10/17.
 */

public class QuizProgressWriter {

    OutputStreamWriter writer;

    private ArrayList<Question> questions;
    public QuizProgressWriter(ArrayList<Question> questions,OutputStreamWriter writer){
        this.questions = questions;
        this.writer = writer;
    }

    public void writeQuizProgress() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Question question: questions){
            JSONObject item = new JSONObject();
            item.put("question", question.getQuestion());
            item.put("correctAnswer", question.getCorrectAnswer());

            JSONArray answers = new JSONArray();
            for(String answer: question.getAnswers()){
                answers.put(answer);
            }
            item.put("answers", answers);

            item.put("category", question.getCategory());
            item.put("userAnswer", question.getUserAnswer());
            item.put("isbool", question.isBool());
            item.put("imageResID", question.getImageResID());

            jsonArray.put(item);
        }
        jsonObject.put("Questions",jsonArray);
        try{
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
