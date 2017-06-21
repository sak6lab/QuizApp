package com.example.sakethdargula1.quizapp;

import java.util.ArrayList;

/**
 * Created by SakethDargula1 on 5/2/17.
 */

public class Question {
    private String question;
    private String correctAnswer;
    private String userAnswer;
    private ArrayList<String> answers;
    private String category;
    private boolean isBool;
    private int imageResID;

    public Question(String question, String correctAnswer, ArrayList<String> answers, String category, boolean isBool, int imageResID){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.category =category;
        this.isBool = isBool;
        this.imageResID = imageResID;
        this.userAnswer = "";
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getCategory() {
        return category;
    }

    public String getUserAnswer(){
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer){
        this.userAnswer = userAnswer;
    }

    public boolean isBool() {
        return isBool;
    }

    public int getImageResID() {
        return imageResID;
    }
}
