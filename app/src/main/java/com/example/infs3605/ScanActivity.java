package com.example.infs3605;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScanActivity extends AppCompatActivity{
    TextView code_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        code_result = (TextView) findViewById(R.id.code_result);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create IntentIntegrator
                IntentIntegrator intentIntegrator = new IntentIntegrator(ScanActivity.this);
                // start scanning
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast toast=Toast.makeText(ScanActivity.this, "cancel scanning...", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast=Toast.makeText(ScanActivity.this, "content:" + result.getContents(), Toast.LENGTH_LONG);
                toast.show();

                code_result.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}