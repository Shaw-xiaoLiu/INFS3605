package com.example.infs3605;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                    .setTitle("Declaration")
                    .setMessage("Your privacy is safe")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent1 = new Intent(WelcomeActivity.this, SurveyActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    })

                    .setNegativeButton("I do not agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(WelcomeActivity.this, "Sorry, we can not acquire your data if you do not acknowledge withe this declaration.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();
            alertDialog2.show();
        });
    }
}