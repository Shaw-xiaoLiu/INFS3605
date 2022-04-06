package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infs3605.databinding.ActivityMenuBinding;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    TextView name = (TextView)findViewById(R.id.menu_name);
    TextView id = (TextView)findViewById(R.id.menu_id);
    TextView addr = (TextView)findViewById(R.id.menu_address);
    TextView phone = (TextView)findViewById(R.id.menu_phone);
    EditText Ename = (EditText)findViewById(R.id.menu_edit_name);
    EditText Eid = (EditText)findViewById(R.id.menu_edit_id);
    EditText Eaddr = (EditText)findViewById(R.id.menu_edit_addr);
    EditText Ephone = (EditText)findViewById(R.id.menu_edit_phone);
    Button save = (Button)findViewById(R.id.edit_save);
    Button cancel = (Button)findViewById(R.id.edit_cancel);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set personal information
        InfoVisible();
        // set name
        name.setText("Ray");
        setContentView(name);
        // set id
        id.setText("654321");
        setContentView(id);
        // set addr
        addr.setText("Kingsford");
        setContentView(addr);
        // set phone
        phone.setText("123456");
        setContentView(phone);

        // set edit not Visible
        EditNotVisible();

        // binding the menu button
        binding.menuProfileBtn.setOnClickListener(v -> launchEditProfile());
        binding.menuQrBtn.setOnClickListener(v -> launchQRScan());
        binding.surveyBtn.setOnClickListener(v -> launchSurvey());

    }

    private void launchEditProfile() {
        // set edit Visible
        EditVisible();

        // set info invisible
        name.setVisibility(View.INVISIBLE);
        id.setVisibility(View.INVISIBLE);
        addr.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);

        save.setOnClickListener(v -> saveChange());
        cancel.setOnClickListener(v -> cancelChange());

        return;
    }

    private void saveChange() {
        // set edit Visible
        EditNotVisible();

        // set name
        name.setText(Ename.getText());
        setContentView(name);
        // set id
        id.setText(Eid.getText());
        setContentView(id);
        // set addr
        addr.setText(Eaddr.getText());
        setContentView(addr);
        // set phone
        phone.setText(Ephone.getText());
        setContentView(phone);


        // set info invisible
        InfoVisible();

    }

    private void cancelChange() {
        // set edit Visible
        EditNotVisible();
        InfoVisible();
    }

    private void EditNotVisible() {
        // set edit not Visible
        Ename.setVisibility(View.INVISIBLE);
        Eid.setVisibility(View.INVISIBLE);
        Eaddr.setVisibility(View.INVISIBLE);
        Ephone.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
    }

    private void EditVisible() {
        // set edit Visible
        Ename.setVisibility(View.VISIBLE);
        Eid.setVisibility(View.VISIBLE);
        Eaddr.setVisibility(View.VISIBLE);
        Ephone.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
    }

    private void InfoVisible() {
        // set info Visible
        name.setVisibility(View.VISIBLE);
        id.setVisibility(View.VISIBLE);
        addr.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
    }
    private void launchQRScan(){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchSurvey(){
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
        finish();
    }
}