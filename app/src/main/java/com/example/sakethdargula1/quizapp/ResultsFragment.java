package com.example.sakethdargula1.quizapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsFragment extends Fragment {
    private static final String ARG_CORRECT_ANSWER = "correctAnswer";
    private static final String ARG_USER_ANSWER = "userAnswer";

    private ArrayList<String> getCorrectAnswers;
    private ArrayList<String> getUserAnswers;

    private int score = 0;

    private OnScoreSendListener scoreSendListener;

    public ResultsFragment() {
    }

    public static ResultsFragment newInstance(ArrayList<Question> questions) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        ArrayList<String> correctAnswers = new ArrayList<>();
        ArrayList<String> userAnswers = new ArrayList<>();
        for(Question question: questions){
            correctAnswers.add(question.getCorrectAnswer());
            userAnswers.add(question.getUserAnswer());
        }
        args.putStringArrayList(ARG_CORRECT_ANSWER, correctAnswers);
        args.putStringArrayList(ARG_USER_ANSWER, userAnswers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getCorrectAnswers = getArguments().getStringArrayList(ARG_CORRECT_ANSWER);
            getUserAnswers = getArguments().getStringArrayList(ARG_USER_ANSWER);
        }
        getResults();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        TextView resultsView = (TextView)view.findViewById(R.id.textView_Results);
        resultsView.setText("You got a "+score+"/10!");
        return view;
    }

    public void getResults(){
        for(int x = 0;x<getCorrectAnswers.size();x++){
            if(getCorrectAnswers.get(x).equals(getUserAnswers.get(x))){
                score++;
            }
        }
        scoreSendListener.sendScore(score);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScoreSendListener) {
            scoreSendListener = (OnScoreSendListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        scoreSendListener = null;
    }

     public interface OnScoreSendListener{
        void sendScore(int score);
    }

}
