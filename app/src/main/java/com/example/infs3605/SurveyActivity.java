package com.example.infs3605;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {
    public TextView question_first;
    public TextView question_second;
    public TextView question_third;
    public CheckBox yes1;
    public CheckBox yes2;
    public CheckBox yes3;
    public CheckBox no1;
    public CheckBox no2;
    public CheckBox no3;
    public CheckBox noAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        question_first = findViewById(R.id.question1);
        question_second = findViewById(R.id.question2);
        question_third = findViewById(R.id.question3);
        yes1 = (CheckBox) findViewById(R.id.yes1);
        yes2 = (CheckBox) findViewById(R.id.yes2);
        yes3 = (CheckBox) findViewById(R.id.yes3);
        no1 = (CheckBox) findViewById(R.id.no1);
        no2 = (CheckBox) findViewById(R.id.no2);
        no3 = (CheckBox) findViewById(R.id.no3);
        noAll = (CheckBox) findViewById(R.id.allbox);

        AllCheckListener allCheckListener=new AllCheckListener();
        noAll.setOnClickListener(allCheckListener);

        BoxCheckListener boxCheckListener=new BoxCheckListener();
        no1.setOnCheckedChangeListener(boxCheckListener);
        no2.setOnCheckedChangeListener(boxCheckListener);
        no3.setOnCheckedChangeListener(boxCheckListener);

    }

    class AllCheckListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            CheckBox all=(CheckBox)v;
            no1.setChecked(all.isChecked());
            no2.setChecked(all.isChecked());
            no3.setChecked(all.isChecked());
        }
    }
    class BoxCheckListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!isChecked){
                noAll.setChecked(isChecked);
            }
        }
    }
}



