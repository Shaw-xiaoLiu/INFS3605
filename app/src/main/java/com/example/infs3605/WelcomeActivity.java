package com.example.infs3605;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView welcome = findViewById(R.id.welcome);
        TextView location= findViewById(R.id.location);
        Intent intent =getIntent();
        String msg=intent.getStringExtra("location");
        location.setText(msg);
        findViewById(R.id.turn).setOnClickListener(view -> {
            Intent intent1 = new Intent(WelcomeActivity.this, SurveyActivity.class);
            startActivity(intent1);
            finish();
        });
    }
}