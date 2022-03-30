package com.example.infs3605.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SurveyItem implements Parcelable {
    private SurveyType surveyType;
    private String question;
    private String choiceAnswer;
    private List<String> multipleAnswers;

    // Firebase needs empty constructor for Data Transfer Objects
    public SurveyItem() {
    }

    public SurveyItem(SurveyType surveyType, String title, String choiceAnswer, List<String> multipleAnswers) {
        this.surveyType = surveyType;
        this.question = title;
        this.choiceAnswer = choiceAnswer;
        this.multipleAnswers = multipleAnswers;
    }

    public SurveyType getSurveyType() {
        return surveyType;
    }

    public String getQuestion() {
        return question;
    }

    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public List<String> getMultipleAnswers() {
        return multipleAnswers;
    }

    protected SurveyItem(Parcel in) {
        question = in.readString();
        choiceAnswer = in.readString();
        multipleAnswers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(choiceAnswer);
        dest.writeStringList(multipleAnswers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SurveyItem> CREATOR = new Creator<SurveyItem>() {
        @Override
        public SurveyItem createFromParcel(Parcel in) {
            return new SurveyItem(in);
        }

        @Override
        public SurveyItem[] newArray(int size) {
            return new SurveyItem[size];
        }
    };
}
