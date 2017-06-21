package com.example.sakethdargula1.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_QUESTION = "question";
    private static final String ARG_IFBOOLEAN = "ifboolean";
    private static final String ARG_INDEX = "index";
    private static final String ARG_CATEGORY = "category";

    private boolean getArgIfBoolean;
    private ArrayList<String> getArgAnswers;
    private String getArgQuestion;
    private int getArgIndex;
    private String getArgCategory;

    private int imageRes;
    private int colorRef;
    private int textColorRef;

    private OnFragmentInteractionListener fragmentListener;

    public static QuestionFragment newInstance(Question question, int index) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ANSWERS, question.getAnswers());
        args.putString(ARG_QUESTION, question.getQuestion());
        args.putBoolean(ARG_IFBOOLEAN, question.isBool());
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_CATEGORY, question.getCategory());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArgIfBoolean = getArguments().getBoolean(ARG_IFBOOLEAN);
            getArgAnswers = getArguments().getStringArrayList(ARG_ANSWERS);
            getArgQuestion = getArguments().getString(ARG_QUESTION);
            getArgIndex = getArguments().getInt(ARG_INDEX);
            getArgCategory = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        getVisual();

        ImageButton backButton = (ImageButton)view.findViewById(R.id.imageButton_Back);
        backButton.setOnClickListener(this);

        TextView questionView = (TextView)view.findViewById(R.id.textView_Question);
        if (Build.VERSION.SDK_INT >= 24) {
            questionView.setText(Html.fromHtml(getArgQuestion,Html.FROM_HTML_MODE_LEGACY));
        }else{
            questionView.setText(Html.fromHtml(getArgQuestion));
        }
        Button button1 = (Button)view.findViewById(R.id.button_One);
        button1.setBackgroundColor(ContextCompat.getColor(getContext(),colorRef));
        button1.setTextColor(ContextCompat.getColor(getContext(),textColorRef));
        button1.setText(getArgAnswers.get(0));
        button1.setOnClickListener(this);

        Button button2 = (Button)view.findViewById(R.id.button_Two);
        button2.setBackgroundColor(ContextCompat.getColor(getContext(),colorRef));
        button2.setTextColor(ContextCompat.getColor(getContext(),textColorRef));
        button2.setText(getArgAnswers.get(1));
        button2.setOnClickListener(this);

        Button button3 = (Button)view.findViewById(R.id.button_Three);
        Button button4 = (Button)view.findViewById(R.id.button_four);

        if(!getArgIfBoolean){
            button3.setBackgroundColor(ContextCompat.getColor(getContext(),colorRef));
            button3.setTextColor(ContextCompat.getColor(getContext(),textColorRef));
            button3.setText(getArgAnswers.get(2));
            button3.setOnClickListener(this);

            button4.setBackgroundColor(ContextCompat.getColor(getContext(),colorRef));
            button4.setTextColor(ContextCompat.getColor(getContext(),textColorRef));
            button4.setText(getArgAnswers.get(3));
            button4.setOnClickListener(this);
        } else {
            ViewGroup layout = (ViewGroup)button3.getParent();
            layout.removeView(button3);
            layout.removeView(button4);
            ViewGroup parentLayout = (ViewGroup)layout.getParent();
            parentLayout.removeView(layout);
        }

        if(getArgIndex == 0) {
            ViewGroup layout = (ViewGroup)backButton.getParent();
            layout.removeView(backButton);
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(imageRes);

        return view;
    }

    public void getVisual(){
        if(getArgCategory.toLowerCase().contains("general knowledge")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorPrimary;
            imageRes = R.drawable.generalknowledge;
        }else if(getArgCategory.toLowerCase().contains("entertainment")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorRed;
            imageRes = R.drawable.entertainment;
        }else if(getArgCategory.toLowerCase().contains("science")){
            textColorRef = R.color.colorBlack;
            colorRef = R.color.colorYellow;
            imageRes = R.drawable.science;
        }else if(getArgCategory.toLowerCase().contains("sports")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorOrange;
            imageRes = R.drawable.sports;
        }else if(getArgCategory.toLowerCase().contains("mythology")) {
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorBlack;
            imageRes = R.drawable.mythology;
        }else if(getArgCategory.toLowerCase().contains("geography")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorGreen;
            imageRes = R.drawable.geography;
        }else if(getArgCategory.toLowerCase().contains("history")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorBlack;
            imageRes = R.drawable.history;
        }else if(getArgCategory.toLowerCase().contains("politics")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorPurple;
            imageRes = R.drawable.politics;
        }else if(getArgCategory.toLowerCase().contains("art")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorRed;
            imageRes = R.drawable.art;
        }else if(getArgCategory.toLowerCase().contains("celebrities")){
            textColorRef = R.color.colorBlack;
            colorRef = R.color.colorPink;
            imageRes = R.drawable.celebrity;
        }else if(getArgCategory.toLowerCase().contains("animals")){
            textColorRef = R.color.colorWhite;
            colorRef = R.color.colorBrown;
            imageRes = R.drawable.animal;
        }else if(getArgCategory.toLowerCase().contains("vehicles")){
            textColorRef = R.color.colorBlack;
            colorRef = R.color.colorYellow;
            imageRes = R.drawable.vehicle;
        }

    }

    @Override
    public void onClick(View view) {
        onButtonPressed(view);
    }

    public void onButtonPressed(View view) {
        if (fragmentListener != null) {
            fragmentListener.onButtonClicked(view);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentListener = (OnFragmentInteractionListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public interface OnFragmentInteractionListener {
             void onButtonClicked(View view);
    }
}
