package com.example.infs3605.constants;

import android.util.Pair;

import com.example.infs3605.R;

import java.util.ArrayList;

public class Surveys {

    /**
     * Fill all 3 fields to add new question
     *
     * @see Surveys#surveyImages
     * @see Surveys#surveyQuestions
     * @see Surveys#getChoices()
     **/


    // survey images
    public static final int[] surveyImages = {
            R.drawable.survey_question_1,
            R.drawable.survey_question_2,
            R.drawable.survey_question_3
    };

    // survey questions
    public static final int[] surveyQuestions = {
            R.string.survey_question_1,
            R.string.survey_question_2,
            R.string.survey_question_3
    };

    // survey choices
    public static ArrayList<Pair<Integer, Integer>> getChoices() {
        // Yes or No choices for each question
        ArrayList<Pair<Integer, Integer>> choices = new ArrayList<>();
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));
        choices.add(new Pair<>(R.string.btn_yes, R.string.btn_no));
        return choices;
    }

}
