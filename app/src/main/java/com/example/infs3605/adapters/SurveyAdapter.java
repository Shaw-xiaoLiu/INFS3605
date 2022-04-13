package com.example.infs3605.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.R;
import com.example.infs3605.databinding.ItemSurveyBinding;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.PlaceHolder> {

    private final Context context;
    @DrawableRes
    private final int[] surveyImages;
    @StringRes
    private final int[] surveyQuestions;
    private final ArrayList<Pair<Integer, Integer>> choices;
    @Nullable
    private final ArrayList<String> answers;
    private final OnAnswerCheckedListener onAnswerCheckedListener;

    public SurveyAdapter(Context context, @DrawableRes int[] surveyImages, @StringRes int[] surveyQuestions, ArrayList<Pair<Integer, Integer>> choices, @Nullable ArrayList<String> answers, OnAnswerCheckedListener onAnswerCheckedListener) {
        this.context = context;
        this.surveyImages = surveyImages;
        this.surveyQuestions = surveyQuestions;
        this.choices = choices;
        this.onAnswerCheckedListener = onAnswerCheckedListener;
        this.answers = answers;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceHolder(LayoutInflater.from(context).inflate(R.layout.item_survey, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        holder.binding.iv.setImageResource(surveyImages[position]);
        holder.binding.tvQuestion.setText(surveyQuestions[position]);

        Pair<Integer, Integer> choice = choices.get(position);
        holder.binding.rBtnQuestionFirst.setText(choice.first);
        holder.binding.rBtnQuestionSecond.setText(choice.second);

        if (answers != null) {
            if (answers.get(position).equals(context.getString(choice.first))) {
                holder.binding.rBtnQuestionFirst.setChecked(true);
            } else {
                holder.binding.rBtnQuestionSecond.setChecked(true);
            }
        }

        holder.binding.rGroupQuestion.setOnCheckedChangeListener((group, checkedId) -> {
            String checkedAnswer = ((MaterialRadioButton) holder.binding.getRoot().findViewById(checkedId)).getText().toString();
            onAnswerCheckedListener.onAnswerChecked(holder.getAdapterPosition(), checkedAnswer);
        });
    }

    @Override
    public int getItemCount() {
        return surveyQuestions.length;
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder {
        public ItemSurveyBinding binding;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSurveyBinding.bind(itemView);
        }
    }

    public interface OnAnswerCheckedListener {
        void onAnswerChecked(int position, String answer);
    }
}
