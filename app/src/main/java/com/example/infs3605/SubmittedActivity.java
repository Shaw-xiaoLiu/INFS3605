package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SubmittedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted);

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent intent = new Intent(SubmittedActivity.this, HomeActivity.class);
            intent.putExtra(HomeActivity.IS_SURVEY_ANSWERED, true);
            startActivity(intent);
            finish();
        });

    }
}