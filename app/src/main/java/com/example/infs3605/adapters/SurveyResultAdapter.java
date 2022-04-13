package com.example.infs3605.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.R;
import com.example.infs3605.databinding.ItemSurveyBinding;
import com.example.infs3605.dto.SurveyItem;
import com.example.infs3605.dto.SurveyType;

import java.util.ArrayList;

public class SurveyResultAdapter extends RecyclerView.Adapter<SurveyResultAdapter.PlaceHolder> {

    private final Context context;
    @DrawableRes
    private final int[] surveyImages;
    private final ArrayList<Pair<Integer, Integer>> choices;
    private final ArrayList<SurveyItem> surveyItems;

    public SurveyResultAdapter(Context context, int[] surveyImages, ArrayList<Pair<Integer, Integer>> choices, ArrayList<SurveyItem> surveyItems) {
        this.context = context;
        this.surveyImages = surveyImages;
        this.choices = choices;
        this.surveyItems = surveyItems;

        if (surveyItems.size() > surveyImages.length)
            throw new IllegalArgumentException(String.format("survey item count (%d) is greater than survey image count (%d).", surveyItems.size(), surveyImages.length));
    }


    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceHolder(LayoutInflater.from(context).inflate(R.layout.item_survey, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {

        SurveyItem item = surveyItems.get(position);
        holder.binding.iv.setImageResource(surveyImages[position]);
        holder.binding.tvQuestion.setText(item.getQuestion());

        Pair<Integer, Integer> choice = choices.get(position);
        holder.binding.rBtnQuestionFirst.setText(choice.first);
        holder.binding.rBtnQuestionSecond.setText(choice.second);

        if (item.getSurveyType() == SurveyType.MULTIPLE_CHOICE) {
            if (item.getChoiceAnswer().equals(context.getString(choice.first))) {
                holder.binding.rBtnQuestionFirst.setChecked(true);
            } else {
                holder.binding.rBtnQuestionSecond.setChecked(true);
            }
        }

        holder.binding.rBtnQuestionFirst.setEnabled(false);
        holder.binding.rBtnQuestionSecond.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return surveyItems.size();
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder {
        public ItemSurveyBinding binding;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSurveyBinding.bind(itemView);
        }
    }
}

