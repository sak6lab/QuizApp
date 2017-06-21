package com.example.sakethdargula1.quizapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener, ResultsFragment.OnScoreSendListener {

    //starting fragment shows main screen
    //if previous json file of last save is not found the app consults the api for new information
    //upon onclick of start button question fragments are loaded
    //when user clicks an answer it goes to next fragment and saves the current in the back stack
    //the back button will pop the back stack to the previous question
    //the answer selected will overwrite the answer previously selected
    //after finishing the 10th question a results fragment will calculate the final score and display it for 5 seconds before returning to the starting screen
    //the main activity receives the score and adds it to the total score that the use has
    //if the app stops the app will write the progress and save the index and score it in its preferences

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    final String URL = "https://opentdb.com/api.php?amount=10";
    QuestionThread thread;

    final String QUESTIONS_FILENAME = "questions.json";
    final String INDEX_KEY = "index_key";
    final String TOTAL_SCORE_KEY = "total_score_key";
    final String TOTAL_QUESTIONS_KEY = "total_questions_key";
    QuizProgressWriter writer;
    QuizProgressReader reader;


    ArrayList<Question> questions;
    int index = 0;
    int score = 0;
    int totalQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream inputStream = openFileInput(QUESTIONS_FILENAME);
            reader = new QuizProgressReader(inputStream);
            questions = reader.getProgressedQuestions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            thread = new QuestionThread();
            thread.execute(URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int getIndex = preferences.getInt(INDEX_KEY,-1);
        score = preferences.getInt(TOTAL_SCORE_KEY, 0);
        totalQuestions = preferences.getInt(TOTAL_QUESTIONS_KEY,0);
        Log.d("index", ""+getIndex);
        if(getIndex == -1){
            showStartScreen();
        } else {
            index = getIndex;
            if(getIndex == 0){
                showStartScreen();
            }else {
                for(int x = 0;x<index;x++){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    QuestionFragment fragment = QuestionFragment.newInstance(questions.get(x),x);
                    transaction.replace(R.id.mainConstraint, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                nextQuestion();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            OutputStreamWriter outputWriter = new OutputStreamWriter(openFileOutput(QUESTIONS_FILENAME, Context.MODE_PRIVATE));
            writer = new QuizProgressWriter(questions, outputWriter);
            writer.writeQuizProgress();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(INDEX_KEY, index);
        editor.putInt(TOTAL_SCORE_KEY, score);
        editor.putInt(TOTAL_QUESTIONS_KEY, totalQuestions);
        editor.apply();
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == R.id.imageButton_Back){
            getSupportFragmentManager().popBackStackImmediate();
            index--;
        } else if (view.getId() == R.id.button_start){
            nextQuestion();
        } else {
            Question question = questions.get(index);
            Button button = (Button)view;
            question.setUserAnswer(button.getText().toString());
            index += 1;
            nextQuestion();
        }
    }

    public void nextQuestion(){
        if(index<questions.size()){
            Question question = questions.get(index);
            Log.d("Answer", question.getCorrectAnswer());
            QuestionFragment fragment = QuestionFragment.newInstance(question,index);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.mainConstraint,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            showResults();
            mainThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    thread = new QuestionThread();
                    thread.execute(URL);
                    index=0;
                }
            }, 5000);
        }
    }

    public void showResults(){
        ResultsFragment fragment = ResultsFragment.newInstance(questions);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainConstraint, fragment);
        transaction.commit();
        totalQuestions+=10;
    }

    public void showStartScreen(){
        StartingFragment fragment = StartingFragment.newInstance(totalQuestions,score);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainConstraint, fragment);
        transaction.commit();
    }

    public ArrayList<Question> getQuestions(JSONObject object) {
        ArrayList<Question> arrayList = new ArrayList<>();
        try {
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < 10; i++) {
                JSONObject item = results.getJSONObject(i);
                String category = item.getString("category");
                String correctAnswer = item.getString("correct_answer");
                boolean isBool;
                ArrayList<String> answers = new ArrayList<>();
                if (item.getString("type").equals("boolean")) {
                    isBool = true;
                    answers.add("True");
                    answers.add("False");
                } else {
                    isBool = false;
                    JSONArray array = item.getJSONArray("incorrect_answers");
                    for (int x = 0; x < 3; x++) {
                        String answer = array.getString(x);
                        answers.add(answer);
                    }
                    answers.add(correctAnswer);
                    Collections.shuffle(answers,new Random());
                }
                String question = item.getString("question");
                arrayList.add(new Question(question, correctAnswer, answers, category, isBool, R.drawable.imagedefault));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    @Override
    public void sendScore(int score) {
        this.score += score;
    }


    private class QuestionThread extends AsyncTask<String, Integer, JSONObject>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            questions = getQuestions(jsonObject);
            showStartScreen();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                return new JSONObject(builder.toString());
            } catch (IOException | JSONException e){
                e.printStackTrace();
            }
            return null;
        }
    }

}


