package com.example.sakethdargula1.quizapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class StartingFragment extends Fragment implements View.OnClickListener{

    private QuestionFragment.OnFragmentInteractionListener startListener;

    private static final String ARG_TOTAl_QUESTIONS = "total_questions";
    private static final String ARG_TOTAL_SCORE = "total_score";

    private int getTotalQuestions;
    private int getTotalScore;

    public static StartingFragment newInstance(int totalQuestions, int totalScore) {
        StartingFragment fragment = new StartingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOTAl_QUESTIONS, totalQuestions);
        args.putInt(ARG_TOTAL_SCORE,totalScore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTotalQuestions = getArguments().getInt(ARG_TOTAl_QUESTIONS);
        getTotalScore = getArguments().getInt(ARG_TOTAL_SCORE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starting, container, false);
        TextView totalQuestionsView = (TextView)view.findViewById(R.id.textView_TotalQuestions);
        totalQuestionsView.setText("Total Questions: "+getTotalScore+"/"+getTotalQuestions);
        Button startButton = (Button)view.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionFragment.OnFragmentInteractionListener) {
            startListener = (QuestionFragment.OnFragmentInteractionListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        startListener = null;
    }

    @Override
    public void onClick(View view) {
        if(startListener != null){
            startListener.onButtonClicked(view);
        }
    }
}
